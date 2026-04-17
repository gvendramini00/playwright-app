package org.gig.myplayrightapp.service.pre.alira;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gig.myplayrightapp.util.AliraLoginUtil;
import org.gig.myplayrightapp.util.PlaywrightUtil;
import org.gig.myplayrightapp.util.ScreenshotUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static org.gig.myplayrightapp.enums.AliraVariables.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AliraTestServiceImpl implements AliraTestService {

    private final AliraLoginUtil aliraLoginUtil;
    private final ScreenshotUtil screenshotUtil;
    private final PlaywrightUtil playwrightUtil;

    @Value("${alira.username}")
    private String aliraUsername;

    @Value("${alira.password}")
    private String aliraPassword;

    @Value("${alira.login-url}")
    private String aliraLoginUrl;

    @Override
    public String testCase001LoginTest() {
        try {
            return playwrightUtil.withPage(page -> {
                log.info("Starting login test for user: {}", aliraUsername);
                page.navigate(aliraLoginUrl);
                page.getByPlaceholder(PLACEHOLDER_USERNAME.getValue()).fill(aliraUsername);
                page.getByPlaceholder(PLACEHOLDER_PASSWORD.getValue()).fill(aliraPassword);
                page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(LINK_SIGN_IN.getValue())).click();
                page.waitForSelector(TEXT_ALIRA_DASHBOARD_SELECTOR.getValue(), new Page.WaitForSelectorOptions().setState(WaitForSelectorState.ATTACHED).setTimeout(5000));
                page.waitForLoadState(LoadState.NETWORKIDLE);
                screenshotUtil.takeScreenshot(page, SCREENSHOT_ALIRA_PATH, "testCase001");
                log.info("Login successful for user: {}", aliraUsername);
                return String.format("OK — User %s logged in successfully.", aliraUsername);
            });
        } catch (Exception e) {
            log.error("Login failed", e);
            return "KO — Error at login: " + e.getMessage();
        }
    }

    @Override
    public String testCase002NavigatePlayerProfile() {
        try {
            return playwrightUtil.withPage(page -> {
                aliraLoginUtil.login(page);
                page.locator("#playerSearcherText").click();
                page.locator("#playerSearcherText").fill(ALIRA_PLAYER.getValue());
                page.locator("#playerSearcherBtn").click();
                page.waitForSelector("text=" + TEXT_PLAYER_PROFILE.getValue(), new Page.WaitForSelectorOptions().setState(WaitForSelectorState.ATTACHED).setTimeout(5000));
                page.waitForLoadState(LoadState.NETWORKIDLE);
                screenshotUtil.takeScreenshot(page, SCREENSHOT_ALIRA_PATH, "testCase002");
                log.info("Player profile successful for player: {}", ALIRA_PLAYER.getValue());
                return "OK — Player Profile loaded correctly!";
            });
        } catch (Exception e) {
            log.error("Player Profile", e);
            return "KO — Error at redirection to Player Profile: " + e.getMessage();
        }
    }
}
