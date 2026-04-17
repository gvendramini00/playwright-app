package org.gig.myplayrightapp.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CgmVariables implements TestVariables {

    // ── Screenshot path ───────────────────────────────────────────────────────
    SCREENSHOT_CGM_PATH("screenshots/cgm/"),

    // ── Site navigation ───────────────────────────────────────────────────────
    CGM_BASE_URL("https://casinogranmadridonline.pre.tecnalis.com/"),
    CGM_BTN_REGISTER("REGÍSTRATE"),
    CGM_LINK_MANUAL_REGISTER("REGISTRO MANUAL (~12 horas)"),
    CGM_LINK_FAST_REGISTER("REGISTRO RÁPIDO (~30 segs, só");

    private final String value;
}
