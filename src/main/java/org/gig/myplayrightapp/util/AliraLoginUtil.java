package org.gig.myplayrightapp.util;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import lombok.extern.slf4j.Slf4j;
import org.gig.myplayrightapp.enums.AliraVariables;

@Slf4j
public class AliraLoginUtil {

    /**
     * Reusable login helper for Alira Back Office.
     * @param page The current Playwright Page instance
     */
    public static void login(Page page) {
        log.info("Utility: Logging in user {}", AliraVariables.USER_NAME.getValue());
        page.navigate(AliraVariables.LOGIN_URL.getValue());
        page.getByPlaceholder("User name").fill(AliraVariables.USER_NAME.getValue());
        page.getByPlaceholder("Password").fill(AliraVariables.USER_PASSWORD.getValue());
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Sign in")).click();
        page.waitForSelector("text=" + AliraVariables.ALIRA_DASHBOARD.getValue(),
                new Page.WaitForSelectorOptions()
                        .setState(WaitForSelectorState.ATTACHED)
                        .setTimeout(10000));

        log.info("Utility: Login successful.");
    }
}
