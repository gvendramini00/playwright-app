package org.gig.myplayrightapp.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AliraVariables {
    USER_NAME("gvendramini"),
    USER_PASSWORD("56689272vG!"),
    SCREENSHOT_PATH("screenshots/alira/"),
    ALIRA_DASHBOARD("Alira Dashboard"),
    ALIRA_PLAYER("test"),
    LOGIN_URL("https://gms.dev.tecnalis.com/alira-server/login.jsp");

    private final String value;
}
