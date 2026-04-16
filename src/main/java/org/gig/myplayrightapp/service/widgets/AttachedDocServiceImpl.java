package org.gig.myplayrightapp.service.widgets;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gig.myplayrightapp.util.AliraLoginUtil;
import org.gig.myplayrightapp.util.AliraTestUtils;
import org.gig.myplayrightapp.util.PlaywrightUtil;
import org.gig.myplayrightapp.util.ScreenshotUtil;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

import static org.gig.myplayrightapp.enums.AliraVariables.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttachedDocServiceImpl implements AttachedDocService {

    private final AliraLoginUtil aliraLoginUtil;
    private final AliraTestUtils aliraTestUtils;
    private final ScreenshotUtil screenshotUtil;
    private final PlaywrightUtil playwrightUtil;

    @Override
    public String testCase018AttachedDocAddNewAttachTest() {
        try {
            return playwrightUtil.withPage(page -> {
                aliraLoginUtil.login(page);

                // Navigate to player profile
                aliraTestUtils.navigateToPlayerProfile(page, ALIRA_PLAYER.getValue());

                // Select Attached Documentation widget
                aliraTestUtils.selectWidget(page, "Attached Documentation");
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("New attach")).click();

                // Upload file
                page.locator("#atcFileupload").setInputFiles(Paths.get("src/main/resources/images/dummy.png"));

                // Select status: Approved
                page.locator("button").filter(new Locator.FilterOptions().setHasText("Nothing Selected")).first().click();
                page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("Approved")).click();

                // Set expiry date: navigate to next month from current, pick day 15
                page.getByLabel("Expiry Date").click();
                String currentMonth = LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
                page.getByRole(AriaRole.ROW, new Page.GetByRoleOptions().setName(currentMonth))
                        .locator("span").nth(1).click();
                page.getByRole(AriaRole.CELL, new Page.GetByRoleOptions().setName("15")).click();

                // Select document type
                page.locator("button").filter(new Locator.FilterOptions().setHasText("Nothing Selected")).nth(1).click();
                page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("Admin")).click();

                // Select visibility: Other
                page.getByTitle("Other").click();
                page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("Other")).click();

                // Fill description and note
                page.getByLabel("Write description").fill("test");
                page.getByLabel("Note").fill("test");

                // Save
                page.locator("#saveAttach").click();
                page.waitForLoadState(LoadState.NETWORKIDLE);

                // Verify: check for visible errors
                Locator errors = page.locator(".error, .text-danger, .alert-danger");
                for (int i = 0; i < errors.count(); i++) {
                    if (errors.nth(i).isVisible()) {
                        String screenshotPath = screenshotUtil.takeScreenshot(page, SCREENSHOT_ALIRA_PATH, "testCase018_failed");
                        log.warn("Attached doc save returned errors. Screenshot: {}", screenshotPath);
                        return "KO — Attached doc save failed with visible errors. Check screenshot: " + screenshotPath;
                    }
                }

                String screenshotPath = screenshotUtil.takeScreenshot(page, SCREENSHOT_ALIRA_PATH, "testCase018");
                log.info("Attached doc added for player: {}. Screenshot: {}", ALIRA_PLAYER.getValue(), screenshotPath);
                return "OK — Attached Documentation added successfully for player: " + ALIRA_PLAYER.getValue();
            });
        } catch (Exception e) {
            log.error("Error in testCase018", e);
            return "KO — Error in testCase018: " + e.getMessage();
        }
    }
}
