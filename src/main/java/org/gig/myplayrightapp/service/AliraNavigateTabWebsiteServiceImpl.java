package org.gig.myplayrightapp.service;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gig.myplayrightapp.util.AliraLoginUtil;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.gig.myplayrightapp.enums.AliraVariables.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AliraNavigateTabWebsiteServiceImpl implements AliraNavigateTabWebsiteService{
    @Override
    public String testCase009NavigateTabWebsiteCMSTest() {
        try (Playwright playwright = Playwright.create(); Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false))) {
            Files.createDirectories(Paths.get(SCREENSHOT_PATH.getValue()));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            AliraLoginUtil.login(page);

            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(WEBSITE_TAB.getValue())).click();
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("CMS")).click();

            String targetUrl = BASE_URL.getValue() + "cms.aml#elf_l1_Lw";
            page.waitForURL("**/" + "cms.aml#elf_l1_Lw");

            if (page.url().equals(targetUrl)) {
                log.info("Success - Navigation to tab WEBSITE -> CMS");

                page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(SCREENSHOT_PATH.getValue() + "testCase009.png")));

                return "✅ CMS tab loaded correctly! URL: " + page.url();
            } else {
                return ERR_NAV_FAILED.getValue() + targetUrl + ERR_NAV_BUT_GOT.getValue() + page.url();
            }

        } catch (Exception e) {
            log.error(LOG_NAV_ERROR.getValue(), e);
            return ERR_NAV_GENERAL.getValue() + e.getMessage();
        }
    }

    @Override
    public String testCase010NavigateTabWebsiteConfigurationCMSAccessTest() {
        try (Playwright playwright = Playwright.create(); Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false))) {
            Files.createDirectories(Paths.get(SCREENSHOT_PATH.getValue()));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            AliraLoginUtil.login(page);

            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(WEBSITE_TAB.getValue())).click();
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(CONFIGURATION_TAB.getValue())).click();
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("CMS Access")).click();

            String targetUrl = BASE_URL.getValue() + "CMSAccess.aml?id=845";
            page.waitForURL("**/" + "CMSAccess.aml?id=845");

            if (page.url().equals(targetUrl)) {
                log.info("Success - Navigation to tab WEBSITE -> Configuration -> CMS Access");

                page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(SCREENSHOT_PATH.getValue() + "testCase010.png")));

                return "✅ CMS Access tab loaded correctly! URL: " + page.url();
            } else {
                return ERR_NAV_FAILED.getValue() + targetUrl + ERR_NAV_BUT_GOT.getValue() + page.url();
            }

        } catch (Exception e) {
            log.error(LOG_NAV_ERROR.getValue(), e);
            return ERR_NAV_GENERAL.getValue() + e.getMessage();
        }
    }

    @Override
    public String testCase011NavigateTabWebsiteConfigurationConstantsTest() {
        try (Playwright playwright = Playwright.create(); Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false))) {
            Files.createDirectories(Paths.get(SCREENSHOT_PATH.getValue()));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            AliraLoginUtil.login(page);

            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(WEBSITE_TAB.getValue())).click();
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(CONFIGURATION_TAB.getValue())).click();
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Constants")).click();

            String targetUrl = BASE_URL.getValue() + "CMSConstants.aml?id=847";
            page.waitForURL("**/" + "CMSConstants.aml?id=847");

            if (page.url().equals(targetUrl)) {
                log.info("Success - Navigation to tab WEBSITE -> Configuration -> Constants");

                page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(SCREENSHOT_PATH.getValue() + "testCase011.png")));

                return "✅ Constants tab loaded correctly! URL: " + page.url();
            } else {
                return ERR_NAV_FAILED.getValue() + targetUrl + ERR_NAV_BUT_GOT.getValue() + page.url();
            }

        } catch (Exception e) {
            log.error(LOG_NAV_ERROR.getValue(), e);
            return ERR_NAV_GENERAL.getValue() + e.getMessage();
        }
    }
}
