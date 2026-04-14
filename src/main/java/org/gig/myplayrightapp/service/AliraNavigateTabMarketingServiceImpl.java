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
public class AliraNavigateTabMarketingServiceImpl implements AliraNavigateTabMarketingService {

    private final AliraLoginUtil aliraLoginUtil;

    @Value("${playwright.headless:true}")
    private boolean headless;

    @Override
    public String testCase012NavigateDashboardTest() {
        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(headless))) {
            Files.createDirectories(Paths.get(SCREENSHOT_PATH.getValue()));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            aliraLoginUtil.login(page);

            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(DASHBOARD_TAB.getValue())).click();
            page.waitForLoadState(LoadState.NETWORKIDLE);

            Locator rows = page.locator("table tbody tr");
            int rowCount = rows.count();

            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get(SCREENSHOT_PATH.getValue() + "testCase012.png"))
                    .setFullPage(true));

            if (rowCount > 0) {
                log.info(LOG_TABLE_SUCCESS.getValue(), rowCount);
                return "✅ Dashboard loaded with table containing " + rowCount + " rows.";
            } else {
                log.warn("Dashboard table has no rows");
                return ERR_TABLE_EMPTY.getValue();
            }

        } catch (Exception e) {
            log.error(LOG_NAV_ERROR.getValue(), e);
            return ERR_NAV_GENERAL.getValue() + e.getMessage();
        }
    }

    @Override
    public String testCase013NavigateMarketingBonusDepositPromotionsTest() {
        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(headless))) {
            Files.createDirectories(Paths.get(SCREENSHOT_PATH.getValue()));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            aliraLoginUtil.login(page);

            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(MARKETING_TAB.getValue())).click();
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(BONUS_TAB.getValue())).click();
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(DEPOSIT_PROMOTIONS_TAB.getValue())).click();
            page.waitForLoadState(LoadState.NETWORKIDLE);

            Locator rows = page.locator("table tbody tr");
            int rowCount = (int) rows.count();

            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get(SCREENSHOT_PATH.getValue() + "testCase013.png"))
                    .setFullPage(true));

            if (rowCount > 0) {
                log.info(LOG_TABLE_SUCCESS.getValue(), rowCount);
                return "✅ Marketing → Bonus → Deposit Promotions loaded with table containing " + rowCount + " rows.";
            } else {
                log.warn("Deposit Promotions table has no rows");
                return ERR_TABLE_EMPTY.getValue();
            }

        } catch (Exception e) {
            log.error(LOG_NAV_ERROR.getValue(), e);
            return ERR_NAV_GENERAL.getValue() + e.getMessage();
        }
    }

    @Override
    public String testCase014NavigateMarketingBonusDepositPromotionsNewModalTest() {
        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(headless))) {
            Files.createDirectories(Paths.get(SCREENSHOT_PATH.getValue()));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            aliraLoginUtil.login(page);

            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(MARKETING_TAB.getValue())).click();
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(BONUS_TAB.getValue())).click();
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(DEPOSIT_PROMOTIONS_TAB.getValue())).click();
            page.waitForLoadState(LoadState.NETWORKIDLE);

            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("New")).click();

            Locator modalTitle = page.getByText(NEW_DEPOSIT_PROMOTION_MODAL.getValue());
            modalTitle.waitFor(new Locator.WaitForOptions().setTimeout(10000));

            boolean modalVisible = modalTitle.isVisible();

            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get(SCREENSHOT_PATH.getValue() + "testCase014.png"))
                    .setFullPage(true));

            if (modalVisible) {
                log.info("New Deposit Promotion modal is visible");
                return "✅ 'New Deposit Promotion' modal opened successfully.";
            } else {
                log.warn("Modal did not appear after clicking New");
                return "❌ Modal did not appear after clicking New.";
            }

        } catch (Exception e) {
            log.error(LOG_NAV_ERROR.getValue(), e);
            return ERR_NAV_GENERAL.getValue() + e.getMessage();
        }
    }

    @Override
    public String testCase015CreateDepositPromotionTest() {
        String promotionName = "testme_" + System.currentTimeMillis();

        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(headless))) {
            Files.createDirectories(Paths.get(SCREENSHOT_PATH.getValue()));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            aliraLoginUtil.login(page);

            // Navigate to Deposit Promotions
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(MARKETING_TAB.getValue())).click();
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(BONUS_TAB.getValue())).click();
            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(DEPOSIT_PROMOTIONS_TAB.getValue())).click();
            page.waitForLoadState(LoadState.NETWORKIDLE);

            // Open New modal
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("New")).click();
            page.getByText(NEW_DEPOSIT_PROMOTION_MODAL.getValue())
                    .waitFor(new Locator.WaitForOptions().setTimeout(10000));

            // Fill promotion name
            page.locator("#dprName").click();
            page.locator("#dprName").fill(promotionName);

            // Set status to Enabled
            page.locator("#depositPromoForm").getByText("Enabled").click();

            // Fill Which balance?
            page.locator("#dprBalanceType_div button").click();
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search")).nth(2).click();
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Search")).nth(2).fill("bonus");
            page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("Altenar Sportsbook Bonus")).click();

            // Fill amount and rollover
            page.locator("#dprAmount").click();
            page.locator("#dprAmount").fill("1");
            page.locator("#dprMinRollover").click();
            page.locator("#dprMinRollover").click();
            page.locator("#dprMinRollover").fill("1");
            page.locator("#dprRolloverAmount").click();
            page.locator("#dprRolloverAmount").fill("1");
            page.locator("#dprMaxAmountConvert").click();
            page.locator("#dprMaxAmountConvert").fill("1");

            // Midnight expiry
            page.getByText("Midnight expiry").click();

            // Select day: Monday
            page.locator("button").filter(new Locator.FilterOptions().setHasText("Nothing Selected")).nth(1).click();
            page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("Monday")).click();

            // Select: "and this days of the Month"
            page.locator("button").filter(new Locator.FilterOptions().setHasText("Nothing Selected")).nth(1).click();
            page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("1").setExact(true)).click();


            // Fill deposit range
            page.getByPlaceholder("Minimum").click();
            page.getByPlaceholder("Minimum").fill("1");
            page.getByPlaceholder("Maximum").click();
            page.getByPlaceholder("Maximum").fill("1");

            // Fill first/last time
            page.locator("#dprFirstTime").click();
            page.locator("#dprFirstTime").fill("1");
            page.locator("#dprLastTime").click();
            page.locator("#dprLastTime").fill("1");


            // Select AND ALSO player selected
            page.locator("#promo_selection_div button").click();
            page.getByRole(AriaRole.LISTBOX)
                    .getByRole(AriaRole.OPTION, new Locator.GetByRoleOptions().setName("Any"))
                    .click();
            // Wait for the dropdown to close before continuing
            page.getByRole(AriaRole.LISTBOX)
                    .waitFor(new Locator.WaitForOptions()
                            .setState(WaitForSelectorState.HIDDEN)
                            .setTimeout(5000));

            // Select player title: Anonimo
            page.getByTitle("Nothing selected", new Page.GetByTitleOptions().setExact(true)).click();
            page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("Anonimo")).click();
            page.getByTitle("Anonimo").click();

            // Save
            page.locator("#saveDepositPromo").click();
            page.waitForLoadState(LoadState.NETWORKIDLE);

            // Check for visible errors
            Locator errors = page.locator(".error, .text-danger, .alert-danger");
            boolean hasError = false;
            for (int i = 0; i < errors.count(); i++) {
                if (errors.nth(i).isVisible()) {
                    hasError = true;
                    break;
                }
            }

            String screenshotPath = SCREENSHOT_PATH.getValue() + "testCase015_" + System.currentTimeMillis() + ".png";
            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get(screenshotPath))
                    .setFullPage(true));

            if (hasError) {
                log.warn("❌ Deposit promotion '{}' save returned errors. Screenshot: {}", promotionName, screenshotPath);
                return "❌ Deposit promotion save failed with visible errors. Check screenshot: " + screenshotPath;
            }

            log.info("✅ Deposit promotion '{}' created successfully. Screenshot: {}", promotionName, screenshotPath);
            return "✅ Deposit promotion '" + promotionName + "' created successfully.";

        } catch (Exception e) {
            log.error("❌ Error in testCase015", e);
            return ERR_NAV_GENERAL.getValue() + e.getMessage();
        }
    }
}
