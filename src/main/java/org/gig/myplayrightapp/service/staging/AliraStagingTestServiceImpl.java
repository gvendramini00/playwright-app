package org.gig.myplayrightapp.service.staging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gig.myplayrightapp.exception.PlayerSearchException;
import org.gig.myplayrightapp.util.AliraStagingLoginUtil;
import org.gig.myplayrightapp.util.AliraTestUtils;
import org.gig.myplayrightapp.util.PlaywrightUtil;
import org.gig.myplayrightapp.util.ScreenshotUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static org.gig.myplayrightapp.enums.AliraVariables.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AliraStagingTestServiceImpl implements AliraStagingTestService {

    private final AliraStagingLoginUtil aliraStagingLoginUtil;
    private final AliraTestUtils aliraTestUtils;
    private final ScreenshotUtil screenshotUtil;
    private final PlaywrightUtil playwrightUtil;

    @Value("${alira.staging.username}")
    private String stagingUsername;

    @Override
    public String testCase001LoginTest() {
        try {
            return playwrightUtil.withPage(page -> {
                log.info("Starting staging login test for user: {}", stagingUsername);
                aliraStagingLoginUtil.login(page);
                screenshotUtil.takeScreenshot(page, SCREENSHOT_STAGING_PATH, "staging_testCase001");
                log.info("Staging login successful for user: {}", stagingUsername);
                return String.format("OK — Staging user %s logged in successfully.", stagingUsername);
            });
        } catch (Exception e) {
            log.error("Staging login failed", e);
            return "KO — Error at staging login: " + e.getMessage();
        }
    }

    @Override
    public String testCase002NavigatePlayerProfile() {
        try {
            return playwrightUtil.withPage(page -> {
                aliraStagingLoginUtil.login(page);
                try {
                    aliraTestUtils.navigateToPlayerProfile(page, ALIRA_PLAYER.getValue());
                } catch (PlayerSearchException e) {
                    String screenshotPath = screenshotUtil.takeScreenshot(page, SCREENSHOT_STAGING_PATH, "staging_testCase002_failed");
                    log.warn("Staging player profile navigation error. Screenshot: {}", screenshotPath);
                    return "KO — " + e.getMessage() + ". Screenshot: " + screenshotPath;
                }
                screenshotUtil.takeScreenshot(page, SCREENSHOT_STAGING_PATH, "staging_testCase002");
                log.info("Staging player profile loaded for player: {}", ALIRA_PLAYER.getValue());
                return "OK — Staging Player Profile loaded correctly!";
            });
        } catch (Exception e) {
            log.error("Staging Player Profile navigation failed", e);
            return "KO — Error at staging Player Profile navigation: " + e.getMessage();
        }
    }
}
