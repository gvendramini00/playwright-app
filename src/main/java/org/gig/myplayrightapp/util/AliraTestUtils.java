package org.gig.myplayrightapp.util;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AliraTestUtils {

    /**
     * Navigates to a player profile in the Alira Back Office.
     * Searches for the given player name, waits for the Player Profile section
     * to attach to the DOM, then waits for the page to reach network idle.
     *
     * @param page       The current Playwright Page instance
     * @param playerName The player name or alias to search for
     */
    public void navigateToPlayerProfile(Page page, String playerName) {
        log.info("Utility: Navigating to player profile for '{}'", playerName);
        page.locator("#playerSearcherText").click();
        page.locator("#playerSearcherText").pressSequentially(playerName, new Locator.PressSequentiallyOptions().setDelay(100));
        page.locator("#playerSearcherBtn").click();
        page.waitForSelector("text=Player Profile",
                new Page.WaitForSelectorOptions()
                        .setState(WaitForSelectorState.ATTACHED)
                        .setTimeout(10000));
        page.waitForLoadState(LoadState.NETWORKIDLE);
        log.info("Utility: Player profile loaded for '{}'", playerName);
    }

    /**
     * Opens the Widgets dropdown in the Alira Back Office player profile,
     * selects the given widget by name, then closes the dropdown.
     *
     * @param page       The current Playwright Page instance
     * @param widgetName The exact label of the widget to select (e.g. "Attached Documentation")
     */
    public void selectWidget(Page page, String widgetName) {
        log.info("Utility: Selecting widget '{}'", widgetName);
        Locator widgetsBtn = page.locator("button:has(span.filter-option:has-text('Widgets'))");
        widgetsBtn.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(10000));
        widgetsBtn.scrollIntoViewIfNeeded();
        widgetsBtn.click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("None")).click();
        page.getByRole(AriaRole.COMBOBOX)
                .getByRole(AriaRole.OPTION, new Locator.GetByRoleOptions().setName(widgetName))
                .click();
        widgetsBtn.click();
        log.info("Utility: Widget '{}' selected", widgetName);
    }
}
