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
public class AliraNavigateTabGamesServiceImpl implements AliraNavigateTabGamesService {
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
}
