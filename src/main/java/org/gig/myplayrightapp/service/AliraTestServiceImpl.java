package org.gig.myplayrightapp.service;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gig.myplayrightapp.enums.AliraVariables;
import org.gig.myplayrightapp.util.AliraLoginUtil;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.gig.myplayrightapp.enums.AliraVariables.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AliraTestServiceImpl implements AliraTestService {

    @Override
    public String testCase001LoginTest() {
        try (Playwright playwright = Playwright.create(); Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false))) {

            Files.createDirectories(Paths.get(SCREENSHOT_PATH.getValue()));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            String userName = AliraVariables.USER_NAME.getValue();
            String userPassword = AliraVariables.USER_PASSWORD.getValue();
            String loginUrl = AliraVariables.LOGIN_URL.getValue();
            String screenshotPath = SCREENSHOT_PATH.getValue();

            log.info("Starting login test for user: {}", userName);
            page.navigate(loginUrl);
            page.getByPlaceholder("User name").fill(userName);
            page.getByPlaceholder("Password").fill(userPassword);
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Sign in")).click();
            page.waitForSelector("text=Alira Dashboard", new Page.WaitForSelectorOptions().setState(WaitForSelectorState.ATTACHED).setTimeout(5000));
            page.waitForLoadState(LoadState.NETWORKIDLE);
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(screenshotPath + "testCase001.png")).setFullPage(true));

            log.info("Login successful for user: {}", userName);
            return String.format("✅ User %s logged in successfully.", userName);

        } catch (Exception e) {
            log.error("Login failed", e);
            return "❌ Error at login: " + e.getMessage();
        }
    }

    @Override
    public String testCase002NavigatePlayerProfile() {
        try (Playwright playwright = Playwright.create(); Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false))) {
            Files.createDirectories(Paths.get(SCREENSHOT_PATH.getValue()));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            AliraLoginUtil.login(page);

            page.locator("#playerSearcherText").click();
            page.locator("#playerSearcherText").fill(ALIRA_PLAYER.getValue());
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("")).click();
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
