package org.gig.myplayrightapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.gig.myplayrightapp.service.CasinoGranMadridTestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Casino Gran Madrid — Registration Tests", description = "Automated tests for the Casino Gran Madrid pre-production site — covers site access, registration flows and validation")
@RestController
@RequestMapping("/api/test/cgm")
@RequiredArgsConstructor
public class CasinoGranMadridTestController {

    private final CasinoGranMadridTestService casinoGranMadridTestService;

    @Operation(summary = "Health check", description = "Simple ping to verify the API is up and responding.")
    @ApiResponse(responseCode = "200", description = "Returns 'PONG!'", content = @Content(mediaType = "text/plain"))
    @GetMapping("/ping")
    public String testPing() {
        return "PONG!";
    }

    @Operation(
        summary = "TC-001 — Pre-production site access",
        description = "Opens the Casino Gran Madrid pre-production homepage and returns the page title. " +
                      "A screenshot is saved to screenshots/cgm/ on completion."
    )
    @ApiResponse(responseCode = "200", description = "✅ with page title on success, ❌ with error details on failure", content = @Content(mediaType = "text/plain"))
    @GetMapping("/testCase001")
    public String testCase001_GoToPreProdWebsite() {
        return casinoGranMadridTestService.testCase001RunPreProdAccessTest();
    }

    @Operation(
        summary = "TC-002 — Register button opens modal",
        description = "Clicks the REGÍSTRATE button on the homepage and verifies the 'Regístrate ahora.' heading is visible in the registration modal. " +
                      "HAR traffic is recorded to browser/output.har. " +
                      "A screenshot is saved to screenshots/cgm/ on completion."
    )
    @ApiResponse(responseCode = "200", description = "✅ if heading is visible, ❌ if modal did not appear", content = @Content(mediaType = "text/plain"))
    @GetMapping("/testCase002")
    public String testCase002_registerButton() {
        return casinoGranMadridTestService.testCase002RunRegisterButtonTest();
    }

    @Operation(
        summary = "TC-003 — Fast registration via Veridas (camera prompt)",
        description = "Opens the fast registration flow (REGISTRO RÁPIDO), clicks Start inside the Veridas iframe and verifies the document capture prompt appears. " +
                      "Uses fake camera/media-stream flags so no real camera is required. " +
                      "A screenshot is saved to screenshots/cgm/ on completion."
    )
    @ApiResponse(responseCode = "200", description = "✅ if camera capture prompt is visible, ❌ with error details on failure", content = @Content(mediaType = "text/plain"))
    @GetMapping("/testCase003")
    public String testCase003_fastRegisterVeridas() {
        return casinoGranMadridTestService.testCase003RunFastRegisterVeridasTest();
    }

    @Operation(
        summary = "TC-004 — Manual registration (happy path)",
        description = "Generates a unique player and completes the full manual registration form (REGISTRO MANUAL). " +
                      "Verifies the page advances after submission with no visible errors. " +
                      "⚠️ This test creates a real player account in the pre-production environment. " +
                      "A screenshot is saved to screenshots/cgm/ on completion."
    )
    @ApiResponse(responseCode = "200", description = "✅ on successful registration, ❌ with error details or screenshot path on failure", content = @Content(mediaType = "text/plain"))
    @GetMapping("/testCase004")
    public String testCase004_manualRegister() {
        return casinoGranMadridTestService.testCase004RunManualRegisterTest();
    }

    @Operation(
        summary = "TC-005 — Manual registration validation (missing name)",
        description = "Attempts to advance past step 1 of the manual registration form without filling in the name field. " +
                      "Verifies the page does not advance, confirming client-side validation is active. " +
                      "A screenshot is saved to screenshots/cgm/ on completion."
    )
    @ApiResponse(responseCode = "200", description = "✅ if page stayed on step 1, ❌ if page advanced despite missing input", content = @Content(mediaType = "text/plain"))
    @GetMapping("/testCase005")
    public String testCase005_manualRegisterWithInvalidData() {
        return casinoGranMadridTestService.testCase005RunInvalidNameRegistrationTest();
    }

    @Operation(
        summary = "TC-006 — Manual registration with duplicated DNI/NIE",
        description = "Retrieves an existing player from the database and attempts to register again using the same DNI/NIE. " +
                      "Verifies the duplicate warning 'DNI/NIE ya está en uso' appears. " +
                      "Skipped automatically if no existing player is found in the database. " +
                      "A screenshot is saved to screenshots/cgm/ on completion."
    )
    @ApiResponse(responseCode = "200", description = "✅ if duplicate warning shown, ❌ if warning did not appear, ⚠️ if skipped (no existing player)", content = @Content(mediaType = "text/plain"))
    @GetMapping("/testCase006")
    public String testCase006_manualRegisterWithDuplicatedData() {
        return casinoGranMadridTestService.testCase006RunDuplicatedRegisterTest();
    }
}
