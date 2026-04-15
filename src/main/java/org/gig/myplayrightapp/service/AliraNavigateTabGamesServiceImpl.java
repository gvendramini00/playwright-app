package org.gig.myplayrightapp.service;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gig.myplayrightapp.enums.AliraVariables;
import org.gig.myplayrightapp.util.AliraLoginUtil;
import org.gig.myplayrightapp.util.ScreenshotUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.gig.myplayrightapp.enums.AliraVariables.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AliraNavigateTabGamesServiceImpl implements AliraNavigateTabGamesService {

    private final AliraLoginUtil aliraLoginUtil;
    private final ScreenshotUtil screenshotUtil;

    @Value("${playwright.headless:true}")
    private boolean headless;

    @Override
    public String testCase003NavigateTabGamesRoomsTest() {
        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(headless))) {
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            aliraLoginUtil.login(page);

            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(GAME_TAB.getValue())).click();
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Rooms").setExact(true)).click();

            String targetUrl = BASE_URL.getValue() + "rooms.aml";
            page.waitForURL("**/" + "rooms.aml");

            if (page.url().contains(targetUrl) || targetUrl.contains(page.url())) {
                log.info("Success - Navigation to tab GAMES -> ROOMS");
                screenshotUtil.takeScreenshot(page, SCREENSHOT_ALIRA_PATH, "testCase003");
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
        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(headless))) {
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            aliraLoginUtil.login(page);

            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(GAME_TAB.getValue())).click();
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Process Rooms")).click();

            String targetUrl = BASE_URL.getValue() + "processRooms.aml";
            page.waitForURL("**/" + "processRooms.aml");

            if (page.url().contains(targetUrl) || targetUrl.contains(page.url())) {
                log.info("Success - Navigation to tab GAMES -> PROCESS ROOMS");
                screenshotUtil.takeScreenshot(page, SCREENSHOT_ALIRA_PATH, "testCase004");
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
        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(headless))) {
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            aliraLoginUtil.login(page);

            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(GAME_TAB.getValue())).click();
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Lobby Rooms")).click();

            String targetUrl = BASE_URL.getValue() + "lobby.aml";
            page.waitForURL("**/" + "lobby.aml");

            if (page.url().contains(targetUrl) || targetUrl.contains(page.url())) {
                log.info("Success - Navigation to tab GAMES -> LOBBY ROOMS");
                screenshotUtil.takeScreenshot(page, SCREENSHOT_ALIRA_PATH, "testCase005");
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
        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(headless))) {
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            aliraLoginUtil.login(page);

            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(GAME_TAB.getValue())).click();
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Providers")).click();

            String targetUrl = BASE_URL.getValue() + "providers.aml";
            page.waitForURL("**/" + "providers.aml");

            if (page.url().contains(targetUrl) || targetUrl.contains(page.url())) {
                log.info("Success - Navigation to tab GAMES -> PROVIDERS");
                screenshotUtil.takeScreenshot(page, SCREENSHOT_ALIRA_PATH, "testCase006");
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
        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(headless))) {
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            aliraLoginUtil.login(page);

            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(GAME_TAB.getValue())).click();
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Themes and Tags")).click();

            String targetUrl = BASE_URL.getValue() + "themesTags.aml";
            page.waitForURL("**/" + "themesTags.aml");

            if (page.url().contains(targetUrl) || targetUrl.contains(page.url())) {
                log.info("Success - Navigation to tab GAMES -> Themes & Tags");
                screenshotUtil.takeScreenshot(page, SCREENSHOT_ALIRA_PATH, "testCase007");
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
        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(headless))) {
            Files.createDirectories(Paths.get(SCREENSHOT_ALIRA_PATH.getValue()));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            aliraLoginUtil.login(page);

            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(GAME_TAB.getValue())).click();
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Exchange Profile")).click();

            String targetUrl = BASE_URL.getValue() + "exchangeProfile.aml";
            page.waitForURL("**/" + "exchangeProfile.aml");

            if (page.url().contains(targetUrl) || targetUrl.contains(page.url())) {
                log.info("Success - Navigation to tab GAMES -> Exchange Profile");
                screenshotUtil.takeScreenshot(page, SCREENSHOT_ALIRA_PATH, "testCase008");
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
