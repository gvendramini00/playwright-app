package org.gig.myplayrightapp.service;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gig.myplayrightapp.util.AliraLoginUtil;
import org.gig.myplayrightapp.util.PlaywrightUtil;
import org.gig.myplayrightapp.util.ScreenshotUtil;
import org.springframework.stereotype.Service;

import static org.gig.myplayrightapp.enums.AliraVariables.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AliraNavigateTabWebsiteServiceImpl implements AliraNavigateTabWebsiteService {

    private final AliraLoginUtil aliraLoginUtil;
    private final ScreenshotUtil screenshotUtil;
    private final PlaywrightUtil playwrightUtil;

    @Override
    public String testCase009NavigateTabWebsiteCMSTest() {
        try {
            return playwrightUtil.withPage(page -> {
                aliraLoginUtil.login(page);
                page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(WEBSITE_TAB.getValue())).click();
                page.waitForLoadState(LoadState.DOMCONTENTLOADED);
                page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("CMS")).click();
                String targetUrl = BASE_URL.getValue() + "cms.aml#elf_l1_Lw";
                page.waitForURL("**/" + "cms.aml#elf_l1_Lw");
                if (page.url().contains(targetUrl) || targetUrl.contains(page.url())) {
                    log.info("Success - Navigation to tab WEBSITE -> CMS");
                    screenshotUtil.takeScreenshot(page, SCREENSHOT_ALIRA_PATH, "testCase009");
                    return "OK — CMS tab loaded correctly! URL: " + page.url();
                }
                return ERR_NAV_FAILED.getValue() + targetUrl + ERR_NAV_BUT_GOT.getValue() + page.url();
            });
        } catch (Exception e) {
            log.error(LOG_NAV_ERROR.getValue(), e);
            return ERR_NAV_GENERAL.getValue() + e.getMessage();
        }
    }

    @Override
    public String testCase010NavigateTabWebsiteConfigurationCMSAccessTest() {
        try {
            return playwrightUtil.withPage(page -> {
                aliraLoginUtil.login(page);
                page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(WEBSITE_TAB.getValue())).click();
                page.waitForLoadState(LoadState.DOMCONTENTLOADED);
                page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(CONFIGURATION_TAB.getValue())).click();
                page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("CMS Access")).click();
                String targetUrl = BASE_URL.getValue() + "CMSAccess.aml?id=845";
                page.waitForURL("**/" + "CMSAccess.aml?id=845");
                if (page.url().contains(targetUrl) || targetUrl.contains(page.url())) {
                    log.info("Success - Navigation to tab WEBSITE -> Configuration -> CMS Access");
                    screenshotUtil.takeScreenshot(page, SCREENSHOT_ALIRA_PATH, "testCase010");
                    return "OK — CMS Access tab loaded correctly! URL: " + page.url();
                }
                return ERR_NAV_FAILED.getValue() + targetUrl + ERR_NAV_BUT_GOT.getValue() + page.url();
            });
        } catch (Exception e) {
            log.error(LOG_NAV_ERROR.getValue(), e);
            return ERR_NAV_GENERAL.getValue() + e.getMessage();
        }
    }

    @Override
    public String testCase011NavigateTabWebsiteConfigurationConstantsTest() {
        try {
            return playwrightUtil.withPage(page -> {
                aliraLoginUtil.login(page);
                page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(WEBSITE_TAB.getValue())).click();
                page.waitForLoadState(LoadState.DOMCONTENTLOADED);
                page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(CONFIGURATION_TAB.getValue())).click();
                page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Constants")).click();
                String targetUrl = BASE_URL.getValue() + "CMSConstants.aml?id=847";
                page.waitForURL("**/" + "CMSConstants.aml?id=847");
                if (page.url().contains(targetUrl) || targetUrl.contains(page.url())) {
                    log.info("Success - Navigation to tab WEBSITE -> Configuration -> Constants");
                    screenshotUtil.takeScreenshot(page, SCREENSHOT_ALIRA_PATH, "testCase011");
                    return "OK — Constants tab loaded correctly! URL: " + page.url();
                }
                return ERR_NAV_FAILED.getValue() + targetUrl + ERR_NAV_BUT_GOT.getValue() + page.url();
            });
        } catch (Exception e) {
            log.error(LOG_NAV_ERROR.getValue(), e);
            return ERR_NAV_GENERAL.getValue() + e.getMessage();
        }
    }
}
