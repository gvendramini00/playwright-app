package org.gig.myplayrightapp.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AliraVariables implements TestVariables {

    // ── Screenshot paths ──────────────────────────────────────────────────────
    SCREENSHOT_ALIRA_PATH("screenshots/alira/"),
    SCREENSHOT_STAGING_PATH("screenshots/staging/"),

    // ── Login / session ───────────────────────────────────────────────────────
    ALIRA_DASHBOARD("Alira Dashboard"),
    ALIRA_PLAYER("test"),
    LOGIN_URL("https://gms.pre.tecnalis.com/alira-server/login.jsp"),
    LOGIN_STAGING_URL("https://gms-staging.pre.tecnalis.com/alira-server/login.jsp"),
    BASE_URL("https://gms.pre.tecnalis.com/alira-server/"),
    BASE_STAGING_URL("https://gms-staging.pre.tecnalis.com/alira-server/"),
    PLACEHOLDER_USERNAME("User name"),
    PLACEHOLDER_PASSWORD("Password"),
    LINK_SIGN_IN("Sign in"),
    TEXT_ALIRA_DASHBOARD_SELECTOR("text=Alira Dashboard"),
    TEXT_PLAYER_PROFILE("Player Profile"),

    // ── Navigation tabs ───────────────────────────────────────────────────────
    GAME_TAB(" Games"),
    WEBSITE_TAB("\uF380 Website"),
    CONFIGURATION_TAB("Configuration \uF3D0"),
    DASHBOARD_TAB("Dashboard"),
    MARKETING_TAB(" Marketing"),
    BONUS_TAB("Bonus "),
    DEPOSIT_PROMOTIONS_TAB("Deposit Promotions"),
    NEW_DEPOSIT_PROMOTION_MODAL("New Deposit Promotion"),
    LINK_CMS("CMS"),
    WIDGET_ATTACHED_DOCUMENTATION("Attached Documentation"),

    // ── Error / log messages ──────────────────────────────────────────────────
    ERR_NAV_FAILED("KO — Navigation failed. Expected "),
    ERR_NAV_BUT_GOT(" but got "),
    ERR_NAV_GENERAL("KO — Error during navigation: "),
    LOG_NAV_ERROR("Navigation Error"),
    ERR_TABLE_EMPTY("KO — Page loaded but table has no data rows."),
    LOG_TABLE_SUCCESS("Table data verified — {} rows found"),

    // ── Staging — CMS Banner edit ─────────────────────────────────────────────
    STAGING_WEBSITE_TAB(" Website"),
    STAGING_BANNERS_TAB("Banners"),
    STAGING_BANNER_HOME_LANDSCAPE("Banner Home Landscape -"),
    STAGING_BANNER_LANDSCAPE_ORIGINAL("- Banner Landscape 1 template"),
    STAGING_BANNER_LANDSCAPE_EDIT_NAME("Banner Landscape 1 template edit"),
    STAGING_BANNER_LANDSCAPE_RESTORED_NAME("Banner Landscape 1 template"),
    STAGING_SUCCESS_SAVED("Success: Saved");

    private final String value;
}
