package org.gig.myplayrightapp.service;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gig.myplayrightapp.util.AliraLoginUtil;
import org.gig.myplayrightapp.util.PlaywrightUtil;
import org.gig.myplayrightapp.util.ScreenshotUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

import static org.gig.myplayrightapp.enums.AliraVariables.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AliraNavigateTabMarketingServiceImpl implements AliraNavigateTabMarketingService {

    private final AliraLoginUtil aliraLoginUtil;
    private final ScreenshotUtil screenshotUtil;
    private final PlaywrightUtil playwrightUtil;

    @Override
    public String testCase012NavigateDashboardTest() {
        try {
            return playwrightUtil.withPage(page -> {
                aliraLoginUtil.login(page);
                page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(DASHBOARD_TAB.getValue())).click();
                page.waitForLoadState(LoadState.NETWORKIDLE);
                Locator rows = page.locator("table tbody tr");
                int rowCount = rows.count();
                screenshotUtil.takeScreenshot(page, SCREENSHOT_ALIRA_PATH, "testCase012");
                if (rowCount > 0) {
                    log.info(LOG_TABLE_SUCCESS.getValue(), rowCount);
                    return "OK — Dashboard loaded with table containing " + rowCount + " rows.";
                }
                log.warn("Dashboard table has no rows");
                return ERR_TABLE_EMPTY.getValue();
            });
        } catch (Exception e) {
            log.error(LOG_NAV_ERROR.getValue(), e);
            return ERR_NAV_GENERAL.getValue() + e.getMessage();
        }
    }

    @Override
    public String testCase013NavigateMarketingBonusDepositPromotionsTest() {
        try {
            return playwrightUtil.withPage(page -> {
                aliraLoginUtil.login(page);
                page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(MARKETING_TAB.getValue())).click();
                page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(BONUS_TAB.getValue())).click();
                page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(DEPOSIT_PROMOTIONS_TAB.getValue())).click();
                page.waitForLoadState(LoadState.NETWORKIDLE);
                Locator rows = page.locator("table tbody tr");
                int rowCount = rows.count();
                screenshotUtil.takeScreenshot(page, SCREENSHOT_ALIRA_PATH, "testCase013");
                if (rowCount > 0) {
                    log.info(LOG_TABLE_SUCCESS.getValue(), rowCount);
                    return "OK — Marketing → Bonus → Deposit Promotions loaded with table containing " + rowCount + " rows.";
                }
                log.warn("Deposit Promotions table has no rows");
                return ERR_TABLE_EMPTY.getValue();
            });
        } catch (Exception e) {
            log.error(LOG_NAV_ERROR.getValue(), e);
            return ERR_NAV_GENERAL.getValue() + e.getMessage();
        }
    }

    @Override
    public String testCase014NavigateMarketingBonusDepositPromotionsNewModalTest() {
        try {
            return playwrightUtil.withPage(page -> {
                aliraLoginUtil.login(page);
                page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(MARKETING_TAB.getValue())).click();
                page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(BONUS_TAB.getValue())).click();
                page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(DEPOSIT_PROMOTIONS_TAB.getValue())).click();
                page.waitForLoadState(LoadState.NETWORKIDLE);
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("New")).click();
                Locator modalTitle = page.getByText(NEW_DEPOSIT_PROMOTION_MODAL.getValue());
                modalTitle.waitFor(new Locator.WaitForOptions().setTimeout(10000));
                boolean modalVisible = modalTitle.isVisible();
                screenshotUtil.takeScreenshot(page, SCREENSHOT_ALIRA_PATH, "testCase014");
                if (modalVisible) {
                    log.info("New Deposit Promotion modal is visible");
                    return "OK — 'New Deposit Promotion' modal opened successfully.";
                }
                log.warn("Modal did not appear after clicking New");
                return "KO — Modal did not appear after clicking New.";
            });
        } catch (Exception e) {
            log.error(LOG_NAV_ERROR.getValue(), e);
            return ERR_NAV_GENERAL.getValue() + e.getMessage();
        }
    }

    @Override
    public String testCase015CreateDepositPromotionTest() {
        String promotionName = "testme_" + System.currentTimeMillis();

        try {
            return playwrightUtil.withPage(page -> {
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
                        .waitFor(new Locator.WaitForOptions().setTimeout(10000));
                page.waitForTimeout(500);
                page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("Any"))
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

                String screenshotPath = screenshotUtil.takeScreenshot(page, SCREENSHOT_ALIRA_PATH, "testCase015");

                if (hasError) {
                    log.warn("❌ Deposit promotion '{}' save returned errors. Screenshot: {}", promotionName, screenshotPath);
                    return "KO — Deposit promotion save failed with visible errors. Check screenshot: " + screenshotPath;
                }

                log.info("Deposit promotion '{}' created successfully. Screenshot: {}", promotionName, screenshotPath);
                return "OK — Deposit promotion '" + promotionName + "' created successfully.";
            });
        } catch (Exception e) {
            log.error("❌ Error in testCase015", e);
            return ERR_NAV_GENERAL.getValue() + e.getMessage();
        }
    }

    @Override
    public String testCase016EditDepositPromotionTest() {
        try {
            return playwrightUtil.withPage(page -> {
                aliraLoginUtil.login(page);

                // Navigate to Deposit Promotions
                page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(MARKETING_TAB.getValue())).click();
                page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(BONUS_TAB.getValue())).click();
                page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(DEPOSIT_PROMOTIONS_TAB.getValue())).click();
                page.waitForLoadState(LoadState.NETWORKIDLE);

                // Search for promotions containing "_edit"
                Locator searchBox = page.getByPlaceholder("Search").nth(1);
                searchBox.click();
                searchBox.fill("_edit");
                page.waitForLoadState(LoadState.NETWORKIDLE);

                // Click the edit link on the first result
                Locator firstRow = page.locator("table tbody tr").first();
                firstRow.waitFor(new Locator.WaitForOptions().setTimeout(10000));
                firstRow.locator("a").first().click();

                // Wait for the edit modal to open and for the name field to be populated
                Locator nameField = page.locator("#dprName");
                nameField.waitFor(new Locator.WaitForOptions().setTimeout(10000));
                page.waitForFunction("document.querySelector('#dprName').value.trim().length > 0");
                String originalName = nameField.inputValue();
                LocalDate today = LocalDate.now();
                String updatedName = "test_edit" + today.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + "_" + today.getDayOfMonth();

                log.info("Editing deposit promotion: '{}' → '{}'", originalName, updatedName);

                // Update the name
                nameField.click();
                nameField.fill(updatedName);

                // Save
                page.locator("#saveDepositPromo").click();
                page.waitForLoadState(LoadState.NETWORKIDLE);

                // Close the modal
                page.locator("#DepositPromoEditModal").getByLabel("Close").click();
                page.waitForLoadState(LoadState.NETWORKIDLE);

                // Verify updated name appears in the table
                Locator updatedCell = page.getByRole(AriaRole.GRIDCELL, new Page.GetByRoleOptions().setName(updatedName));
                boolean nameVisible = updatedCell.count() > 0 && updatedCell.first().isVisible();

                String screenshotPath = screenshotUtil.takeScreenshot(page, SCREENSHOT_ALIRA_PATH, "testCase016");

                if (nameVisible) {
                    log.info("Deposit promotion renamed '{}' → '{}'. Screenshot: {}", originalName, updatedName, screenshotPath);
                    return "OK — Deposit promotion updated: '" + originalName + "' → '" + updatedName + "'.";
                } else {
                    log.warn("Updated name '{}' not found in table after save. Screenshot: {}", updatedName, screenshotPath);
                    return "KO — Save appeared to succeed but updated name '" + updatedName + "' not found in table. Check screenshot: " + screenshotPath;
                }
            });
        } catch (Exception e) {
            log.error("❌ Error in testCase016", e);
            return ERR_NAV_GENERAL.getValue() + e.getMessage();
        }
    }

    @Override
    public String testCase017DeleteDepositPromotionTest() {
        try {
            return playwrightUtil.withPage(page -> {
                aliraLoginUtil.login(page);

                // Navigate to Deposit Promotions
                page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(MARKETING_TAB.getValue())).click();
                page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(BONUS_TAB.getValue())).click();
                page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(DEPOSIT_PROMOTIONS_TAB.getValue())).click();
                page.waitForLoadState(LoadState.NETWORKIDLE);

                // Read the ID of the first row before deleting
                Locator firstRow = page.locator("table tbody tr").first();
                firstRow.waitFor(new Locator.WaitForOptions().setTimeout(10000));
                String deletedId = firstRow.locator("td").nth(1).innerText().trim();

                // Click the delete button (second link, nth(1)) on the first row
                firstRow.locator("a").nth(1).click();

                // Confirm deletion in the modal
                page.locator("#DeleteDepositPromoModal")
                        .getByText("Delete", new Locator.GetByTextOptions().setExact(true))
                        .click();
                page.waitForLoadState(LoadState.NETWORKIDLE);

                // Search by the deleted ID to verify it no longer exists
                Locator searchBox = page.getByPlaceholder("Search").first();
                searchBox.click();
                searchBox.fill(deletedId);
                page.waitForLoadState(LoadState.NETWORKIDLE);

                // Check if any ID cell exactly matches the deleted ID
                Locator idCells = page.locator("table tbody tr td:nth-child(2)");
                boolean stillExists = false;
                for (int i = 0; i < idCells.count(); i++) {
                    if (idCells.nth(i).innerText().trim().equals(deletedId)) {
                        stillExists = true;
                        break;
                    }
                }

                String screenshotPath = screenshotUtil.takeScreenshot(page, SCREENSHOT_ALIRA_PATH, "testCase017");

                if (!stillExists) {
                    log.info("Deposit promotion ID '{}' deleted successfully. Screenshot: {}", deletedId, screenshotPath);
                    return "OK — Deposit promotion ID '" + deletedId + "' deleted successfully.";
                } else {
                    log.warn("Deposit promotion ID '{}' still visible after deletion. Screenshot: {}", deletedId, screenshotPath);
                    return "KO — Deletion failed: ID '" + deletedId + "' still present in table. Check screenshot: " + screenshotPath;
                }
            });
        } catch (Exception e) {
            log.error("❌ Error in testCase017", e);
            return ERR_NAV_GENERAL.getValue() + e.getMessage();
        }
    }
}
    