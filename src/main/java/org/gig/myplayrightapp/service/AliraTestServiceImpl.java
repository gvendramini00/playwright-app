package org.gig.myplayrightapp.service;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gig.myplayrightapp.util.AliraLoginUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.gig.myplayrightapp.enums.AliraVariables.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AliraTestServiceImpl implements AliraTestService {

    private final AliraLoginUtil aliraLoginUtil;

    @Value("${alira.username}")
    private String aliraUsername;

    @Value("${alira.password}")
    private String aliraPassword;

    @Value("${alira.login-url}")
    private String aliraLoginUrl;

    @Value("${playwright.headless:true}")
    private boolean headless;

    @Override
    public String testCase001LoginTest() {
        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(headless))) {

            Files.createDirectories(Paths.get(SCREENSHOT_PATH.getValue()));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            log.info("Starting login test for user: {}", aliraUsername);
            page.navigate(aliraLoginUrl);
            page.getByPlaceholder("User name").fill(aliraUsername);
            page.getByPlaceholder("Password").fill(aliraPassword);
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Sign in")).click();
            page.waitForSelector("text=Alira Dashboard", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.ATTACHED).setTimeout(5000));
            page.waitForLoadState(LoadState.NETWORKIDLE);
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(SCREENSHOT_PATH.getValue() + "testCase001.png")).setFullPage(true));

            log.info("Login successful for user: {}", aliraUsername);
            return String.format("✅ User %s logged in successfully.", aliraUsername);

        } catch (Exception e) {
            log.error("Login failed", e);
            return "❌ Error at login: " + e.getMessage();
        }
    }

    @Override
    public String testCase002NavigatePlayerProfile() {
        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(headless))) {
            Files.createDirectories(Paths.get(SCREENSHOT_PATH.getValue()));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            aliraLoginUtil.login(page);

            page.locator("#playerSearcherText").click();
            page.locator("#playerSearcherText").fill(ALIRA_PLAYER.getValue());
            page.locator("#playerSearcherBtn").click();
            page.waitForSelector("text=Player Profile", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.ATTACHED).setTimeout(5000));
            page.waitForLoadState(LoadState.NETWORKIDLE);
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(SCREENSHOT_PATH.getValue() + "testCase002.png")).setFullPage(true));

            log.info("Player profile successful for player: {}", ALIRA_PLAYER.getValue());
            return "✅ Player Profile loaded correctly!";
        } catch (Exception e) {
            log.error("Player Profile", e);
            return "❌ Error at redirection to Player Profile: " + e.getMessage();
        }
    }
}
