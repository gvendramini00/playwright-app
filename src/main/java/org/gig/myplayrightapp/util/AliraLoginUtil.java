package org.gig.myplayrightapp.util;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import lombok.extern.slf4j.Slf4j;
import org.gig.myplayrightapp.enums.AliraVariables;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AliraLoginUtil {

    @Value("${alira.username}")
    private String username;

    @Value("${alira.password}")
    private String password;

    @Value("${alira.login-url}")
    private String loginUrl;

    /**
     * Reusable login helper for Alira Back Office.
     * @param page The current Playwright Page instance
     */
    public void login(Page page) {
        log.info("Utility: Logging in user {}", username);
        page.navigate(loginUrl);
        page.getByPlaceholder("User name").fill(username);
        page.getByPlaceholder("Password").fill(password);
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Sign in")).click();
        page.waitForSelector("text=" + AliraVariables.ALIRA_DASHBOARD.getValue(),
                new Page.WaitForSelectorOptions()
                        .setState(WaitForSelectorState.ATTACHED)
                        .setTimeout(10000));
        log.info("Utility: Login successful.");
    }
}
