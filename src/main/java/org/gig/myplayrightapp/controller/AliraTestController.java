package org.gig.myplayrightapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.gig.myplayrightapp.service.AliraTestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Alira — Core Tests", description = "Login and player profile navigation tests for the Alira Back Office")
@RestController
@RequestMapping("/api/test/alira")
@RequiredArgsConstructor
public class AliraTestController {

    private final AliraTestService aliraTestService;

    @Operation(summary = "Health check", description = "Simple ping to verify the API is up and responding.")
    @ApiResponse(responseCode = "200", description = "Returns 'PONG!'", content = @Content(mediaType = "text/plain"))
    @GetMapping("/ping")
    public String testPing() {
        return "PONG!";
    }

    @Operation(
        summary = "TC-001 — Login",
        description = "Opens the Alira Back Office login page, fills in credentials and verifies the Dashboard loads. " +
                      "A screenshot is saved to screenshots/alira/ on completion."
    )
    @ApiResponse(responseCode = "200", description = "✅ on successful login, ❌ with error details on failure", content = @Content(mediaType = "text/plain"))
    @GetMapping("/testCase001")
    public String testCase001() {
        return aliraTestService.testCase001LoginTest();
    }

    @Operation(
        summary = "TC-002 — Player Profile navigation",
        description = "Logs in and searches for the configured test player, then verifies the Player Profile page loads correctly. " +
                      "A screenshot is saved to screenshots/alira/ on completion."
    )
    @ApiResponse(responseCode = "200", description = "✅ on successful navigation, ❌ with error details on failure", content = @Content(mediaType = "text/plain"))
    @GetMapping("/testCase002")
    public String testCase002() {
        return aliraTestService.testCase002NavigatePlayerProfile();
    }
}
