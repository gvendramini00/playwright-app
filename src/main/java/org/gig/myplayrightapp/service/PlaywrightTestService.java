package org.gig.myplayrightapp.service;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.HarContentPolicy;
import com.microsoft.playwright.options.HarMode;
import lombok.extern.slf4j.Slf4j;
import org.gig.myplayrightapp.dto.InsertPlayerDTO;
import org.gig.myplayrightapp.util.RegistrationDataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class PlaywrightTestService {

    @Autowired
    private PlayerService playerService;

    public String testCase001RunPreProdAccessTest() {
        log.info("✅ TestCase001 executed: Go to preprod site");
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
            Page page = browser.newPage();
            page.navigate("https://casinogranmadridonline.pre.tecnalis.com/");
            String title = page.title();
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("screenshots/testCase001.png")));
            browser.close();
            return "✅ Site loaded. Title: " + title;
        } catch (Exception e) {
            return "❌ Error loading site: " + e.getMessage();
        }
    }

    public String testCase002RunRegisterButtonTest() {
        log.info("✅ TestCase002 executed: Open Register modal and validate registration heading");

        try (Playwright playwright = Playwright.create()) {
            Files.createDirectories(Paths.get("screenshots"));

            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
            BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                    .setRecordHarPath(Paths.get("browser/output.har"))
                    .setRecordHarContent(HarContentPolicy.EMBED)
                    .setRecordHarMode(HarMode.MINIMAL));

            Page page = context.newPage();

            page.offConsoleMessage(consoleMessage -> log.error(consoleMessage.toString()));

            // Navegar a una página para capturar las solicitudes
            page.navigate("https://casinogranmadridonline.pre.tecnalis.com/");

            // 1. Click "REGÍSTRATE"
            page.getByText("REGÍSTRATE").click();

            // 2. Wait for modal heading
            Locator heading = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Regístrate ahora."));
            heading.waitFor(new Locator.WaitForOptions().setTimeout(5000));

            // Wait a bit to let modal animation/render finish
            page.waitForTimeout(1000);

            // Now take the screenshot
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("screenshots/testCase002.png")));

            // 4. Validate heading
            boolean headingVisible = heading.isVisible();

            // 5. Close browser
            context.close();
            browser.close();

            // 6. Return result
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
            Files.createDirectories(Paths.get("screenshots"));

            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                    .setHeadless(false)
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

            // Wait for iframe
            Locator iframeLocator = page.locator("#XpressID-iframe");
            iframeLocator.waitFor(new Locator.WaitForOptions().setTimeout(10000));
            FrameLocator iframe = page.frameLocator("#XpressID-iframe");

            // Click "Start"
            Locator startButton = iframe.getByRole(AriaRole.BUTTON, new FrameLocator.GetByRoleOptions().setName("Start"));
            startButton.waitFor(new Locator.WaitForOptions().setTimeout(15000));
            startButton.click();
            log.info("✅ Clicked 'Start' inside iframe");

            // ✅ Wait for camera validation text
            Locator capturePrompt = iframe.getByText("Fit the FRONT of the document");
            capturePrompt.waitFor(new Locator.WaitForOptions().setTimeout(15000));
            log.info("✅ Camera and document capture prompt visible");

            // Screenshot
            page.waitForTimeout(1500);
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("screenshots/testCase003.png")));

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
            Files.createDirectories(Paths.get("screenshots"));

            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            // 1. Navigate & open manual registration form
            page.navigate("https://casinogranmadridonline.pre.tecnalis.com/");
            page.getByText("REGÍSTRATE").click();
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("REGISTRO MANUAL (~12 horas)")).click();

            // 2. Fill personal info
            page.locator("#name").fill(dto.firstName());
            page.locator("#middlename").fill(dto.middleName());
            page.locator("#surname").fill(dto.lastName());
            page.getByText("Hombre").click();
            page.locator("#day").selectOption(String.valueOf(dto.birthDate().getDayOfMonth()));
            page.locator("#month").selectOption(String.valueOf(dto.birthDate().getMonthValue() - 1)); // 0-indexed
            page.locator("#year").selectOption(String.valueOf(dto.birthDate().getYear()));
            page.locator("#nationalId").fill(dto.nationalId());
            page.locator("#c19oldfalse").check();
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Continuar")).click();

            // 3. Fill contact info
            page.locator("#e_mail").fill(dto.email());
            page.locator("#re_mail").fill(dto.email());
            page.locator("#phoneInput").fill(dto.phone());
            page.locator("#address").fill(dto.address());
            page.locator("#state").selectOption(String.valueOf(dto.state()));
            page.locator("#tax_state").selectOption(String.valueOf(dto.taxState()));
            page.locator("#city_select").selectOption(dto.city());
            page.locator("#zipCode_select").selectOption(dto.zipCode());
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Seguir")).click();

            // 4. Fill account credentials
            Locator userField = page.locator("#user");
            userField.waitFor(new Locator.WaitForOptions().setTimeout(10000));
            userField.fill(dto.alias());
            page.locator("#pwdField").fill(dto.password());
            page.locator("#re_password").fill(dto.password());

            // Security question and response
            page.locator("#securityQuestion").selectOption(dto.securityQuestion());
            page.locator("#securityResponse").fill(dto.securityResponse());

            // Accept checkboxes
            page.locator("input[name=\"c18old\"]").check();
            page.locator("#subscription1").check();

            String beforeSubmitUrl = page.url();
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Empezar"))
                    .click(new Locator.ClickOptions().setTimeout(10000));

            page.waitForURL(url -> !url.equals(beforeSubmitUrl), new Page.WaitForURLOptions().setTimeout(5000));

            // 5. Check for visible errors
            Locator errorLocator = page.locator(".error, .text-danger, .invalid-feedback");
            if (errorLocator.count() > 0) {
                for (int i = 0; i < errorLocator.count(); i++) {
                    if (errorLocator.nth(i).isVisible()) {
                        String errorText = errorLocator.nth(i).innerText();
                        String failPath = "screenshots/testCase004_failed_" + System.currentTimeMillis() + ".png";
                        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(failPath)));
                        throw new RuntimeException("❌ Registration failed: " + errorText);
                    }
                }
            }

            // 6. Validate the page advanced
            if (page.url().equals(beforeSubmitUrl)) {
                String failPath = "screenshots/testCase004_failed_no_redirect_" + System.currentTimeMillis() + ".png";
                page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(failPath)));
                throw new RuntimeException("❌ Registration failed: Page did not proceed after submitting.");
            }

            // 7. Screenshot success
            String screenshotPath = "screenshots/testCase004_" + System.currentTimeMillis() + ".png";
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(screenshotPath)));

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
            Files.createDirectories(Paths.get("screenshots"));

            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                    .setHeadless(false)
            );

            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            // Step 1: Open registration form
            page.navigate("https://casinogranmadridonline.pre.tecnalis.com/");
            page.getByText("REGÍSTRATE").click();
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("REGISTRO MANUAL (~12 horas)")).click();

            // Step 2: Try to continue without filling the name
            page.locator("#surname").click(); // force blur from #name
            String currentURL = page.url(); // capture current step URL

            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Continuar")).click();
            page.waitForTimeout(1000); // allow time for error to show

            String afterClickURL = page.url();

            // Screenshot regardless of result
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("screenshots/testCase005.png")));

            // Check if page did not advance
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

}
