package org.gig.myplayrightapp.service.staging;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gig.myplayrightapp.exception.PlayerSearchException;
import org.gig.myplayrightapp.util.AliraLoginUtil;
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

    private final AliraLoginUtil aliraStagingLoginUtil;
    private final AliraTestUtils aliraTestUtils;
    private final ScreenshotUtil screenshotUtil;
    private final PlaywrightUtil playwrightUtil;

    @Value("${alira.staging.username}")
    private String stagingUsername;

    @Value("${alira.staging.base-url}")
    private String stagingBaseUrl;

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

    @Override
    public String testCase003CmsWebsiteEditBanner() {
        try {
            return playwrightUtil.withPage(page -> {
                log.info("Starting staging CMS Website Banner Edit test for user: {}", stagingUsername);
                aliraStagingLoginUtil.login(page);

                try {
                    page.navigate(stagingBaseUrl + "index.aml");
                    page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(STAGING_WEBSITE_TAB.getValue())).click();
                    page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(LINK_CMS.getValue())).click();

                    Locator bannersTab = page.getByRole(AriaRole.TAB, new Page.GetByRoleOptions().setName(STAGING_BANNERS_TAB.getValue()));
                    page.waitForTimeout(2000);
                    bannersTab.click();

                    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Nothing Selected")).click();
                    page.getByRole(AriaRole.LISTBOX).getByRole(AriaRole.OPTION, new Locator.GetByRoleOptions().setName(STAGING_BANNER_HOME_LANDSCAPE.getValue())).click();
                    page.waitForTimeout(2000);

                    page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(STAGING_BANNER_LANDSCAPE_ORIGINAL.getValue())).click();
                    page.waitForTimeout(2000);

                    page.getByLabel("Name", new Page.GetByLabelOptions().setExact(true)).click();
                    page.getByLabel("Name", new Page.GetByLabelOptions().setExact(true)).fill(STAGING_BANNER_LANDSCAPE_EDIT_NAME.getValue());
                    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save")).click();

                    page.getByLabel("Name", new Page.GetByLabelOptions().setExact(true)).click();
                    page.getByLabel("Name", new Page.GetByLabelOptions().setExact(true)).fill(STAGING_BANNER_LANDSCAPE_RESTORED_NAME.getValue());
                    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save")).click();
                    page.waitForTimeout(1000);
                    page.getByText(STAGING_SUCCESS_SAVED.getValue()).click();

                } catch (Exception e) {
                    String screenshotPath = screenshotUtil.takeScreenshot(page, SCREENSHOT_STAGING_PATH, "staging_testCase003_failed");
                    log.error("CMS Website Banner Edit failed. Screenshot: {}", screenshotPath, e);
                    return "KO — CMS Website Banner Edit failed: " + e.getMessage() + ". Screenshot: " + screenshotPath;
                }

                String screenshotPath = screenshotUtil.takeScreenshot(page, SCREENSHOT_STAGING_PATH, "staging_testCase003");
                log.info("Staging CMS Website Banner Edit successful. Screenshot: {}", screenshotPath);
                return "OK — CMS Website Banner edited and restored successfully. Screenshot: " + screenshotPath;
            });
        } catch (Exception e) {
            log.error("Unexpected error in testCase003CmsWebsiteEditBanner", e);
            return "KO — Unexpected error in CMS Website Banner Edit: " + e.getMessage();
        }
    }
}
