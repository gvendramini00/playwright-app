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

    @Override
    public String testCase003NavigateTabGamesRoomsTest() {
        try (Playwright playwright = Playwright.create(); Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false))) {
            Files.createDirectories(Paths.get(SCREENSHOT_PATH.getValue()));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            AliraLoginUtil.login(page);

            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(GAME_TAB.getValue())).click();
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Rooms").setExact(true)).click();

            String targetUrl = BASE_URL.getValue() + "rooms.aml";
            page.waitForURL("**/" + "rooms.aml");

            if (page.url().equals(targetUrl)) {
                log.info("Success - Navigation to tab GAMES -> ROOMS");

                page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(SCREENSHOT_PATH.getValue() + "testCase003.png")));

                return "✅ Rooms tab loaded correctly! URL: " + page.url();
            } else {
                return ERR_NAV_FAILED.getValue() + targetUrl + ERR_NAV_BUT_GOT.getValue() + page.url();
            }

        } catch (Exception e) {
            log.error(LOG_NAV_ERROR.getValue(), e);
            return ERR_NAV_GENERAL.getValue() + e.getMessage();
        }
    }

    @Override
    public String testCase004NavigateTabGamesProcessRoomsTest() {
        try (Playwright playwright = Playwright.create(); Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false))) {
            Files.createDirectories(Paths.get(SCREENSHOT_PATH.getValue()));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            AliraLoginUtil.login(page);

            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(GAME_TAB.getValue())).click();
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Process Rooms")).click();

            String targetUrl = BASE_URL.getValue() + "processRooms.aml";
            page.waitForURL("**/" + "processRooms.aml");

            if (page.url().equals(targetUrl)) {
                log.info("Success - Navigation to tab GAMES -> PROCESS ROOMS");

                page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(SCREENSHOT_PATH.getValue() + "testCase004.png")));

                return "✅ Rooms tab loaded correctly! URL: " + page.url();
            } else {
                return ERR_NAV_FAILED.getValue() + targetUrl + ERR_NAV_BUT_GOT.getValue() + page.url();
            }

        } catch (Exception e) {
            log.error(LOG_NAV_ERROR.getValue(), e);
            return ERR_NAV_GENERAL.getValue() + e.getMessage();
        }
    }

    @Override
    public String testCase005NavigateTabGamesLobbyRoomsTest() {
        try (Playwright playwright = Playwright.create(); Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false))) {
            Files.createDirectories(Paths.get(SCREENSHOT_PATH.getValue()));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            AliraLoginUtil.login(page);

            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(GAME_TAB.getValue())).click();
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Lobby Rooms")).click();

            String targetUrl = BASE_URL.getValue() + "lobby.aml";
            page.waitForURL("**/" + "lobby.aml");

            if (page.url().equals(targetUrl)) {
                log.info("Success - Navigation to tab GAMES -> LOBBY ROOMS");

                page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(SCREENSHOT_PATH.getValue() + "testCase005.png")));

                return "✅ Lobby tab loaded correctly! URL: " + page.url();
            } else {
                return ERR_NAV_FAILED.getValue() + targetUrl + ERR_NAV_BUT_GOT.getValue() + page.url();
            }

        } catch (Exception e) {
            log.error(LOG_NAV_ERROR.getValue(), e);
            return ERR_NAV_GENERAL.getValue() + e.getMessage();
        }
    }

    @Override
    public String testCase006NavigateTabGamesProvidersTest() {
        try (Playwright playwright = Playwright.create(); Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false))) {
            Files.createDirectories(Paths.get(SCREENSHOT_PATH.getValue()));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            AliraLoginUtil.login(page);

            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(GAME_TAB.getValue())).click();
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Providers")).click();

            String targetUrl = BASE_URL.getValue() + "providers.aml";
            page.waitForURL("**/" + "providers.aml");

            if (page.url().equals(targetUrl)) {
                log.info("Success - Navigation to tab GAMES -> PROVIDERS");

                page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(SCREENSHOT_PATH.getValue() + "testCase006.png")));

                return "✅ Providers tab loaded correctly! URL: " + page.url();
            } else {
                return ERR_NAV_FAILED.getValue() + targetUrl + ERR_NAV_BUT_GOT.getValue() + page.url();
            }

        } catch (Exception e) {
            log.error(LOG_NAV_ERROR.getValue(), e);
            return ERR_NAV_GENERAL.getValue() + e.getMessage();
        }
    }

    @Override
    public String testCase007NavigateTabGamesThemesTagsTest() {
        try (Playwright playwright = Playwright.create(); Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false))) {
            Files.createDirectories(Paths.get(SCREENSHOT_PATH.getValue()));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            AliraLoginUtil.login(page);

            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(GAME_TAB.getValue())).click();
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Themes and Tags")).click();

            String targetUrl = BASE_URL.getValue() + "themesTags.aml";
            page.waitForURL("**/" + "themesTags.aml");

            if (page.url().equals(targetUrl)) {
                log.info("Success - Navigation to tab GAMES -> Themes & Tags");

                page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(SCREENSHOT_PATH.getValue() + "testCase007.png")));

                return "✅ Themes & Tags tab loaded correctly! URL: " + page.url();
            } else {
                return ERR_NAV_FAILED.getValue() + targetUrl + ERR_NAV_BUT_GOT.getValue() + page.url();
            }

        } catch (Exception e) {
            log.error(LOG_NAV_ERROR.getValue(), e);
            return ERR_NAV_GENERAL.getValue() + e.getMessage();
        }
    }

    @Override
    public String testCase008NavigateTabGamesExchangeProfileTest() {
        try (Playwright playwright = Playwright.create(); Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false))) {
            Files.createDirectories(Paths.get(SCREENSHOT_PATH.getValue()));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            AliraLoginUtil.login(page);

            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(GAME_TAB.getValue())).click();
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Exchange Profile")).click();

            String targetUrl = BASE_URL.getValue() + "exchangeProfile.aml";
            page.waitForURL("**/" + "exchangeProfile.aml");

            if (page.url().equals(targetUrl)) {
                log.info("Success - Navigation to tab GAMES -> Exchange Profile");

                page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(SCREENSHOT_PATH.getValue() + "testCase008.png")));

                return "✅ Exchange Profile tab loaded correctly! URL: " + page.url();
            } else {
                return ERR_NAV_FAILED.getValue() + targetUrl + ERR_NAV_BUT_GOT.getValue() + page.url();
            }

        } catch (Exception e) {
            log.error(LOG_NAV_ERROR.getValue(), e);
            return ERR_NAV_GENERAL.getValue() + e.getMessage();
        }
    }

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
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Configuration \uF3D0")).click();
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
}
