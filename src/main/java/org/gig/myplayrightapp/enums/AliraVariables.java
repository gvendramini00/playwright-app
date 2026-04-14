package org.gig.myplayrightapp.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AliraVariables {
    SCREENSHOT_PATH("screenshots/alira/"),
    ALIRA_DASHBOARD("Alira Dashboard"),
    ALIRA_PLAYER("test"),
    LOGIN_URL("https://gms.pre.tecnalis.com/alira-server/login.jsp"),
    BASE_URL("https://gms.pre.tecnalis.com/alira-server/"),
    ERR_NAV_FAILED("❌ Navigation failed. Expected "),
    ERR_NAV_BUT_GOT(" but got "),
    ERR_NAV_GENERAL("❌ Error during navigation: "),
    LOG_NAV_ERROR("Navigation Error"),
    GAME_TAB(" Games"),
    WEBSITE_TAB("\uF380 Website"),
    CONFIGURATION_TAB("Configuration \uF3D0"),
    DASHBOARD_TAB("Dashboard"),
    MARKETING_TAB(" Marketing"),
    BONUS_TAB("Bonus "),
    DEPOSIT_PROMOTIONS_TAB("Deposit Promotions"),
    NEW_DEPOSIT_PROMOTION_MODAL("New Deposit Promotion"),
    ERR_TABLE_EMPTY("❌ Page loaded but table has no data rows."),
    LOG_TABLE_SUCCESS("Table data verified — {} rows found");

    private final String value;
}
