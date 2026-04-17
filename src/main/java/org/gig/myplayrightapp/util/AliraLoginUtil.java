package org.gig.myplayrightapp.util;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gig.myplayrightapp.enums.AliraVariables;

@Slf4j
@Getter
@RequiredArgsConstructor
public class AliraLoginUtil {

    private final String username;
    private final String password;
    private final String loginUrl;
    private final String envLabel;

    /**
     * Reusable login helper for any Alira Back Office environment.
     * Configured via {@link org.gig.myplayrightapp.config.LoginUtilConfig}.
     *
     * @param page The current Playwright Page instance
     */
    public void login(Page page) {
        log.info("Utility: Logging in {} user {}", envLabel, username);
        page.navigate(loginUrl);
        page.getByPlaceholder(AliraVariables.PLACEHOLDER_USERNAME.getValue()).fill(username);
        page.getByPlaceholder(AliraVariables.PLACEHOLDER_PASSWORD.getValue()).fill(password);
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(AliraVariables.LINK_SIGN_IN.getValue())).click();
        page.waitForSelector("text=" + AliraVariables.ALIRA_DASHBOARD.getValue(),
                new Page.WaitForSelectorOptions()
                        .setState(WaitForSelectorState.ATTACHED)
                        .setTimeout(10000));
        log.info("Utility: {} login successful.", envLabel);
    }
}
