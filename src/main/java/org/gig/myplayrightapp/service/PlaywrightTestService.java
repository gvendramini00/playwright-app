package org.gig.myplayrightapp.service;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.HarContentPolicy;
import com.microsoft.playwright.options.HarMode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class PlaywrightTestService {

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

        try (Playwright playwright = Playwright.create()) {
            Files.createDirectories(Paths.get("screenshots"));

            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                    .setHeadless(false)
            );

            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            // 1. Navigate & open manual registration form
            page.navigate("https://casinogranmadridonline.pre.tecnalis.com/");
            page.getByText("REGÍSTRATE").click();
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("REGISTRO MANUAL (~12 horas)")).click();

            // 2. Fill personal info
            page.locator("#name").fill("test");
            page.locator("#middlename").fill("testtest");
            page.locator("#surname").fill("testesttest");
            page.getByText("Hombre").click();
            page.locator("#day").selectOption("21");
            page.locator("#month").selectOption("0"); // January is 0
            page.locator("#year").selectOption("1990");
            page.locator("#nationalId").fill("X0638235P");
            page.locator("#c19oldfalse").check();
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Continuar")).click();

            // 3. Fill contact info
            page.locator("#e_mail").fill("testmee@gmail.com");
            page.locator("#re_mail").fill("testmee@gmail.com");
            page.locator("#phoneInput").fill("633863059");
            page.locator("#address").fill("carrer copernic 80");
            page.locator("#state").selectOption("277");
            //page.locator("#country").click(); // Optional interaction
            page.locator("#tax_state").selectOption("12");
            page.locator("#city_select").selectOption("ABELEDA");
            page.locator("#zipCode_select").selectOption("27513");
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Seguir")).click();

            // 4. Fill account credentials
            Locator userField = page.locator("#user");
            userField.waitFor(new Locator.WaitForOptions().setTimeout(10000));
            userField.fill("testmenoww");

            page.locator("#pwdField").fill("Password1");
            page.locator("#re_password").fill("Password1");
            page.locator("#securityResponse").fill("barcelona");
            page.locator("input[name=\"c18old\"]").check();
            page.locator("#subscription1").check();
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Empezar")).click();

            // 6. Screenshot final state
            page.waitForTimeout(1500);
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("screenshots/testCase004.png")));

            browser.close();
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
