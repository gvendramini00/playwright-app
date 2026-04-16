package org.gig.myplayrightapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.gig.myplayrightapp.service.staging.AliraStagingMassTestService;
import org.gig.myplayrightapp.service.staging.AliraStagingTestService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Alira — Staging Tests", description = "Tests for the Alira Staging Back Office (gms-staging.pre.tecnalis.com) — separate DB at 10.64.134.11")
@RestController
@RequestMapping("/api/test/alira-staging")
@RequiredArgsConstructor
public class AliraStagingController {

    private final AliraStagingTestService aliraStagingTestService;
    private final AliraStagingMassTestService aliraStagingMassTestService;

    @Operation(summary = "Health check", description = "Simple ping to verify the staging API is up and responding.")
    @ApiResponse(responseCode = "200", description = "Returns 'PONG!'", content = @Content(mediaType = "text/plain"))
    @GetMapping("/ping")
    public String testPing() {
        return "PONG!";
    }

    @Operation(
        summary = "NL2-TC-001 — Staging Login",
        description = "Login succeeds and landing dashboard loads with no DB errors in tomcat log. Validates back office auth and initial OGP pool connectivity via ProxySQL\n " +
                      "A screenshot is saved to screenshots/staging/ on completion."
    )
    @ApiResponse(responseCode = "200", description = "OK — staging login succeeded", content = @Content(mediaType = "text/plain"))
    @ApiResponse(responseCode = "500", description = "KO — with error details on failure", content = @Content(mediaType = "text/plain"))
    @GetMapping("/testCase001")
    public ResponseEntity<String> testCase001() {
        String result = aliraStagingTestService.testCase001LoginTest();
        return result.startsWith("OK") || result.startsWith("SKIPPED")
                ? ResponseEntity.ok(result)
                : ResponseEntity.internalServerError().body(result);
    }

    @Operation(
        summary = "NL2-TC-002 — Staging Player Profile navigation",
        description = "Logs in to Alira Staging and searches for the configured test player, then verifies the Player Profile page loads correctly. " +
                      "A screenshot is saved to screenshots/staging/ on completion."
    )
    @ApiResponse(responseCode = "200", description = "OK — Staging Player Profile loaded", content = @Content(mediaType = "text/plain"))
    @ApiResponse(responseCode = "500", description = "KO — with error details on failure", content = @Content(mediaType = "text/plain"))
    @GetMapping("/testCase002")
    public ResponseEntity<String> testCase002() {
        String result = aliraStagingTestService.testCase002NavigatePlayerProfile();
        return result.startsWith("OK") || result.startsWith("SKIPPED")
                ? ResponseEntity.ok(result)
                : ResponseEntity.internalServerError().body(result);
    }

    @Operation(
            summary = "NL2-TC-003 — Staging Dashboard - CMS - Website - Edit Banner",
            description = "Logs in to Alira Staging and navigates to Dashboard - CMS - Website - Edit Banner and edit some information." +
                    "A screenshot is saved to screenshots/staging/ on completion."
    )
    @ApiResponse(responseCode = "200", description = "OK — Banner Edited", content = @Content(mediaType = "text/plain"))
    @ApiResponse(responseCode = "500", description = "KO — with error details on failure", content = @Content(mediaType = "text/plain"))
    @GetMapping("/testCase003")
    public ResponseEntity<String> testCase003() {
        String result = aliraStagingTestService.testCase003CmsWebsiteEditBanner();
        return result.startsWith("OK") || result.startsWith("SKIPPED")
                ? ResponseEntity.ok(result)
                : ResponseEntity.internalServerError().body(result);
    }

    @Operation(
        summary = "Staging Mass Test Runner",
        description = "Runs all Alira Staging test cases in sequence and returns a downloadable Excel report with results and screenshot references."
    )
    @ApiResponse(responseCode = "200", description = "Excel report (.xlsx) returned as a file download", content = @Content(mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
    @GetMapping(value = "/runAll", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<byte[]> runAll() {
        byte[] report = aliraStagingMassTestService.runAllStagingTestsAndGenerateReport();
        String filename = "staging-test-report.xlsx";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(report);
    }
}
