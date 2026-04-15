package org.gig.myplayrightapp.service;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.HarContentPolicy;
import com.microsoft.playwright.options.HarMode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gig.myplayrightapp.dto.InsertPlayerDTO;
import org.gig.myplayrightapp.util.RegistrationDataUtils;
import org.gig.myplayrightapp.util.ScreenshotUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.gig.myplayrightapp.enums.AliraVariables.SCREENSHOT_CGM_PATH;

@Slf4j
@Service
@RequiredArgsConstructor
public class CasinoGranMadridTestService {

    private final PlayerService playerService;
    private final ScreenshotUtil screenshotUtil;

    @Value("${playwright.headless:true}")
    private boolean headless;

    public String testCase001RunPreProdAccessTest() {
        log.info("✅ TestCase001 executed: Go to preprod site");
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(headless));
            Page page = browser.newPage();
            page.navigate("https://casinogranmadridonline.pre.tecnalis.com/");
            String title = page.title();
            screenshotUtil.takeScreenshot(page, SCREENSHOT_CGM_PATH, "testCase001");
            browser.close();
            return "✅ Site loaded. Title: " + title;
        } catch (Exception e) {
            return "❌ Error loading site: " + e.getMessage();
        }
    }

    public String testCase002RunRegisterButtonTest() {
        log.info("✅ TestCase002 executed: Open Register modal and validate registration heading");

        try (Playwright playwright = Playwright.create()) {

            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(headless));
            BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                    .setRecordHarPath(Paths.get("browser/output.har"))
                    .setRecordHarContent(HarContentPolicy.EMBED)
                    .setRecordHarMode(HarMode.MINIMAL));

            Page page = context.newPage();

            page.offConsoleMessage(consoleMessage -> log.error(consoleMessage.toString()));
            page.navigate("https://casinogranmadridonline.pre.tecnalis.com/");
            page.getByText("REGÍSTRATE").click();

            Locator heading = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Regístrate ahora."));
            heading.waitFor(new Locator.WaitForOptions().setTimeout(5000));

            page.waitForTimeout(1000);

            screenshotUtil.takeScreenshot(page, SCREENSHOT_CGM_PATH, "testCase002");

            boolean headingVisible = heading.isVisible();

            context.close();
            browser.close();

            return headingVisible
                    ? "✅ Registration modal loaded successfully: heading found"
                    : "❌ Registration modal did not appear";

        } catch (Exception e) {
            log.error("❌ Error in TestCase002", e);
            return "❌ TestCase002 failed: " + e.getMessage();
        }
    }

    public String testCase003RunFastRegisterVeridasTest() {
        log.info("✅ TestCase003 executed: Fast Register via Veridas — stop at camera activation");

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                    .setHeadless(headless)
                    .setArgs(Arrays.asList(
                            "--use-fake-ui-for-media-stream",
                            "--use-fake-device-for-media-stream"
                    ))
            );

            BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                    .setPermissions(List.of("camera"))
                    .setViewportSize(1280, 720)
                    .setBaseURL("https://casinogranmadridonline.pre.tecnalis.com/")
            );

            context.grantPermissions(
                    List.of("camera"),
                    new BrowserContext.GrantPermissionsOptions()
                            .setOrigin("https://casinogranmadridonline.pre.tecnalis.com/")
            );

            Page page = context.newPage();
            page.navigate("https://casinogranmadridonline.pre.tecnalis.com/");
            page.getByText("REGÍSTRATE").click();
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("REGISTRO RÁPIDO (~30 segs, só")).click();

            Locator iframeLocator = page.locator("#XpressID-iframe");
            iframeLocator.waitFor(new Locator.WaitForOptions().setTimeout(10000));
            FrameLocator iframe = page.frameLocator("#XpressID-iframe");

            Locator startButton = iframe.getByRole(AriaRole.BUTTON, new FrameLocator.GetByRoleOptions().setName("Start"));
            startButton.waitFor(new Locator.WaitForOptions().setTimeout(15000));
            startButton.click();
            log.info("✅ Clicked 'Start' inside iframe");

            Locator capturePrompt = iframe.getByText("Fit the FRONT of the document");
            capturePrompt.waitFor(new Locator.WaitForOptions().setTimeout(15000));
            log.info("✅ Camera and document capture prompt visible");

            page.waitForTimeout(1500);
            screenshotUtil.takeScreenshot(page, SCREENSHOT_CGM_PATH, "testCase003");

            browser.close();
            return "✅ Veridas Fast Register opened and camera capture screen visible.";

        } catch (Exception e) {
            log.error("❌ Error in TestCase003", e);
            return "❌ TestCase003 failed: " + e.getMessage();
        }
    }

    public String testCase004RunManualRegisterTest() {
        log.info("✅ TestCase004 executed: Manual Registration (non-Veridas)");

        InsertPlayerDTO dto = RegistrationDataUtils.generateUniqueInsertPlayerDTO(playerService);

        log.info("✅ Unique player to be inserted: {}", dto.email());

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(headless));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            page.navigate("https://casinogranmadridonline.pre.tecnalis.com/");
            page.getByText("REGÍSTRATE").click();
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("REGISTRO MANUAL (~12 horas)")).click();

            page.locator("#name").fill(dto.firstName());
            page.locator("#middlename").fill(dto.middleName());
            page.locator("#surname").fill(dto.lastName());
            page.getByText("Hombre").click();
            page.locator("#day").selectOption(String.valueOf(dto.birthDate().getDayOfMonth()));
            page.locator("#month").selectOption(String.valueOf(dto.birthDate().getMonthValue()));
            page.locator("#year").selectOption(String.valueOf(dto.birthDate().getYear()));
            page.locator("#nationalId").fill(dto.nationalId());
            page.locator("#c19oldfalse").check();
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Continuar")).click();

            page.locator("#e_mail").fill(dto.email());
            page.locator("#re_mail").fill(dto.email());
            page.locator("#phoneInput").fill(dto.phone());
            page.locator("#address").fill(dto.address());
            page.locator("#state").selectOption(String.valueOf(dto.state()));
            page.locator("#tax_state").selectOption(String.valueOf(dto.taxState()));
            page.locator("#city_select").selectOption(dto.city());
            page.locator("#zipCode_select").selectOption(dto.zipCode());
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Seguir")).click();

            Locator userField = page.locator("#user");
            userField.waitFor(new Locator.WaitForOptions().setTimeout(10000));
            userField.fill(dto.alias());
            page.locator("#pwdField").fill(dto.password());
            page.locator("#re_password").fill(dto.password());

            page.locator("#securityQuestion").selectOption(dto.securityQuestion());
            page.locator("#securityResponse").fill(dto.securityResponse());

            page.locator("input[name=\"c18old\"]").check();
            page.locator("#subscription1").check();

            String beforeSubmitUrl = page.url();
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Empezar"))
                    .click(new Locator.ClickOptions().setTimeout(10000));

            page.waitForURL(url -> !url.equals(beforeSubmitUrl), new Page.WaitForURLOptions().setTimeout(10000));

            Locator errorLocator = page.locator(".error, .text-danger, .invalid-feedback");
            if (errorLocator.count() > 0) {
                for (int i = 0; i < errorLocator.count(); i++) {
                    if (errorLocator.nth(i).isVisible()) {
                        String errorText = errorLocator.nth(i).innerText();
                        screenshotUtil.takeScreenshot(page, SCREENSHOT_CGM_PATH, "testCase004_failed");
                        throw new RuntimeException("❌ Registration failed: " + errorText);
                    }
                }
            }

            if (page.url().equals(beforeSubmitUrl)) {
                screenshotUtil.takeScreenshot(page, SCREENSHOT_CGM_PATH, "testCase004_failed_no_redirect");
                throw new RuntimeException("❌ Registration failed: Page did not proceed after submitting.");
            }

            String screenshotPath = screenshotUtil.takeScreenshot(page, SCREENSHOT_CGM_PATH, "testCase004");

            browser.close();
            RegistrationDataUtils.logGeneratedUser(dto.email(), dto.nationalId(), dto.alias(), screenshotPath);
            log.info("✅ Manual registration succeeded for user: {}", dto.alias());
            return "✅ Manual registration completed successfully.";

        } catch (Exception e) {
            log.error("❌ Error in TestCase004", e);
            return "❌ TestCase004 failed: " + e.getMessage();
        }
    }

    public String testCase005RunInvalidNameRegistrationTest() {
        log.info("✅ TestCase005 executed: Registration with missing name");

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(headless));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            page.navigate("https://casinogranmadridonline.pre.tecnalis.com/");
            page.getByText("REGÍSTRATE").click();
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("REGISTRO MANUAL (~12 horas)")).click();

            page.locator("#surname").click();
            String currentURL = page.url();

            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Continuar")).click();
            page.waitForTimeout(1000);

            String afterClickURL = page.url();

            screenshotUtil.takeScreenshot(page, SCREENSHOT_CGM_PATH, "testCase005");


            if (currentURL.equals(afterClickURL)) {
                log.info("✅ Page did not advance. Validation likely triggered.");
                return "✅ Validation succeeded: Page remained on step 1 (missing name)";
            } else {
                log.warn("❌ Page advanced despite missing name!");
                return "❌ Validation failed: Page moved to next step with invalid input";
            }

        } catch (Exception e) {
            log.error("❌ Error in TestCase005", e);
            return "❌ TestCase005 failed: " + e.getMessage();
        }
    }

    public String testCase006RunDuplicatedRegisterTest() {
        log.info("🧪 TestCase006: Manual Registration with duplicated user data");

        Optional<InsertPlayerDTO> maybeExistingPlayer = playerService.findAnyExistingPlayer();
        if (maybeExistingPlayer.isEmpty()) {
            return "⚠️ TestCase006 skipped: No existing player found to duplicate.";
        }

        InsertPlayerDTO dto = maybeExistingPlayer.get();

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(headless));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            page.navigate("https://casinogranmadridonline.pre.tecnalis.com/");
            page.getByText("REGÍSTRATE").click();
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("REGISTRO MANUAL (~12 horas)")).click();

            page.locator("#name").fill(dto.firstName());
            page.locator("#middlename").fill(dto.middleName());
            page.locator("#surname").fill(dto.lastName());
            page.getByText("Hombre").click();
            page.locator("#day").selectOption(String.valueOf(dto.birthDate().getDayOfMonth()));
            page.locator("#month").selectOption(String.valueOf(dto.birthDate().getMonthValue() - 1));
            page.locator("#year").selectOption(String.valueOf(dto.birthDate().getYear()));
            page.locator("#nationalId").fill(dto.nationalId());
            page.locator("#c19oldfalse").check();

            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Continuar")).click();

            page.waitForTimeout(3000);

            Locator duplicateModal = page.locator(".error, .text-danger, .invalid-feedback, .ng-binding, .form-errors");

            for (int i = 0; i < duplicateModal.count(); i++) {
                if (duplicateModal.nth(i).isVisible()) {
                    String text = duplicateModal.nth(i).innerText().trim();
                    if (text.contains("DNI/NIE ya está en uso")) {
                        String screenshotPath = screenshotUtil.takeScreenshot(page, SCREENSHOT_CGM_PATH, "testCase006_duplicate_dni");

                        log.info("✅ Duplicate DNI/NIE detected for user '{}' (DNI: {}). Screenshot saved: {}", dto.alias(), dto.nationalId(), screenshotPath);
                        browser.close();
                        return "✅ TestCase006 passed: Duplicate warning shown.";
                    }
                }
            }

            String failPath = screenshotUtil.takeScreenshot(page, SCREENSHOT_CGM_PATH, "testCase006_no_modal");
            log.warn("❌ Expected modal did not appear. Screenshot: {}", failPath);

            browser.close();
            return "❌ TestCase006 failed: Duplicate check did not trigger expected modal.";

        } catch (Exception e) {
            log.error("❌ Error in TestCase006", e);
            return "❌ TestCase006 failed: " + e.getMessage();
        }
    }
}
