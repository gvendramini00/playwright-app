package org.gig.myplayrightapp.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GpptVariables implements TestVariables {

    // ── Screenshot path ───────────────────────────────────────────────────────
    SCREENSHOT_GP_PATH("screenshots/gp/"),

    // ── Site navigation ───────────────────────────────────────────────────────
    GPPT_BASE_URL("https://goldenpark-pt.dev.tecnalis.com/");

    private final String value;
}
