package org.gig.myplayrightapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.gig.myplayrightapp.service.AliraNavigateTabMarketingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Alira — Marketing Tab", description = "Tests for the Marketing tab in the Alira Back Office — covers Dashboard, Deposit Promotions (read, create, edit, delete)")
@RestController
@RequestMapping("/api/test/alira/navigation/marketing")
@RequiredArgsConstructor
public class AliraNavigateTabMarketingController {

    private final AliraNavigateTabMarketingService aliraNavigateTabMarketingService;

    @Operation(
        summary = "TC-012 — Dashboard table data",
        description = "Logs in, navigates to the Dashboard and verifies that the main table contains at least one data row. " +
                      "A screenshot is saved to screenshots/alira/ on completion."
    )
    @ApiResponse(responseCode = "200", description = "✅ with row count on success, ❌ if table is empty", content = @Content(mediaType = "text/plain"))
    @GetMapping("/testCase012")
    public String testCase012() {
        return aliraNavigateTabMarketingService.testCase012NavigateDashboardTest();
    }

    @Operation(
        summary = "TC-013 — Marketing → Bonus → Deposit Promotions table data",
        description = "Logs in, navigates to Marketing → Bonus → Deposit Promotions and verifies that the table contains at least one row. " +
                      "A screenshot is saved to screenshots/alira/ on completion."
    )
    @ApiResponse(responseCode = "200", description = "✅ with row count on success, ❌ if table is empty", content = @Content(mediaType = "text/plain"))
    @GetMapping("/testCase013")
    public String testCase013() {
        return aliraNavigateTabMarketingService.testCase013NavigateMarketingBonusDepositPromotionsTest();
    }

    @Operation(
        summary = "TC-014 — Deposit Promotions → New modal",
        description = "Logs in, navigates to Deposit Promotions, clicks the New button and verifies the creation modal appears. " +
                      "A screenshot is saved to screenshots/alira/ on completion."
    )
    @ApiResponse(responseCode = "200", description = "✅ if modal is visible, ❌ if modal did not appear", content = @Content(mediaType = "text/plain"))
    @GetMapping("/testCase014")
    public String testCase014() {
        return aliraNavigateTabMarketingService.testCase014NavigateMarketingBonusDepositPromotionsNewModalTest();
    }

    @Operation(
        summary = "TC-015 — Create Deposit Promotion",
        description = "Logs in and creates a new Deposit Promotion with a generated name (testme_<timestamp>), filling all required fields. " +
                      "Verifies no error messages appear after saving. " +
                      "⚠️ This test writes data to the pre-production environment. " +
                      "A screenshot is saved to screenshots/alira/ on completion."
    )
    @ApiResponse(responseCode = "200", description = "✅ with promotion name on success, ❌ with screenshot path if errors were found", content = @Content(mediaType = "text/plain"))
    @GetMapping("/testCase015")
    public String testCase015() {
        return aliraNavigateTabMarketingService.testCase015CreateDepositPromotionTest();
    }

    @Operation(
        summary = "TC-016 — Edit Deposit Promotion",
        description = "Logs in, searches for a promotion containing '_edit', opens the first result and renames it to test_edit<Month>_<Day>. " +
                      "Verifies the updated name appears in the table after saving. " +
                      "⚠️ This test modifies data in the pre-production environment. " +
                      "A screenshot is saved to screenshots/alira/ on completion."
    )
    @ApiResponse(responseCode = "200", description = "✅ with old → new name on success, ❌ with screenshot path if update was not reflected", content = @Content(mediaType = "text/plain"))
    @GetMapping("/testCase016")
    public String testCase016() {
        return aliraNavigateTabMarketingService.testCase016EditDepositPromotionTest();
    }

    @Operation(
        summary = "TC-017 — Delete Deposit Promotion",
        description = "Logs in, reads the ID of the first Deposit Promotion in the list, deletes it and verifies the ID no longer appears in the table. " +
                      "⚠️ This test permanently deletes data from the pre-production environment. " +
                      "A screenshot is saved to screenshots/alira/ on completion."
    )
    @ApiResponse(responseCode = "200", description = "✅ with deleted ID on success, ❌ with screenshot path if ID was still found after deletion", content = @Content(mediaType = "text/plain"))
    @GetMapping("/testCase017")
    public String testCase017() {
        return aliraNavigateTabMarketingService.testCase017DeleteDepositPromotionTest();
    }
}
