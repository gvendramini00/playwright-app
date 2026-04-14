package org.gig.myplayrightapp.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AliraVariables {
    SCREENSHOT_PATH("screenshots/alira/"),
    ALIRA_DASHBOARD("Alira Dashboard"),
    ALIRA_PLAYER("test"),
    LOGIN_URL("https://gms.dev.tecnalis.com/alira-server/login.jsp"),
    BASE_URL("https://gms.dev.tecnalis.com/alira-server/"),
    ERR_NAV_FAILED("❌ Navigation failed. Expected "),
    ERR_NAV_BUT_GOT(" but got "),
    ERR_NAV_GENERAL("❌ Error during navigation: "),
    LOG_NAV_ERROR("Navigation Error"),
    GAME_TAB(" Games"),
    WEBSITE_TAB("\uF380 Website"),
    CONFIGURATION_TAB("Configuration \uF3D0");

    private final String value;
}
