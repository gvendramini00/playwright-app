package org.gig.myplayrightapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.gig.myplayrightapp.service.AliraNavigateTabWebsiteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Alira — Website Tab", description = "Navigation tests for the Website tab in the Alira Back Office (CMS, Configuration)")
@RestController
@RequestMapping("/api/test/alira/navigation/website")
@RequiredArgsConstructor
public class AliraNavigateTabWebsiteController {

    private final AliraNavigateTabWebsiteService aliraNavigateTabWebsiteService;

    @Operation(
        summary = "TC-009 — Website → CMS",
        description = "Logs in, navigates to Website → CMS and verifies the URL resolves to cms.aml. " +
                      "A screenshot is saved to screenshots/alira/ on completion."
    )
    @ApiResponse(responseCode = "200", description = "OK — with loaded URL on success", content = @Content(mediaType = "text/plain"))
    @ApiResponse(responseCode = "500", description = "KO — with expected vs actual URL, or error details on failure", content = @Content(mediaType = "text/plain"))
    @GetMapping("/testCase009")
    public ResponseEntity<String> testCase009() {
        String result = aliraNavigateTabWebsiteService.testCase009NavigateTabWebsiteCMSTest();
        return result.startsWith("OK") || result.startsWith("SKIPPED")
                ? ResponseEntity.ok(result)
                : ResponseEntity.internalServerError().body(result);
    }

    @Operation(
        summary = "TC-010 — Website → Configuration → CMS Access",
        description = "Logs in, navigates to Website → Configuration → CMS Access and verifies the URL resolves to CMSAccess.aml. " +
                      "A screenshot is saved to screenshots/alira/ on completion."
    )
    @ApiResponse(responseCode = "200", description = "OK — with loaded URL on success", content = @Content(mediaType = "text/plain"))
    @ApiResponse(responseCode = "500", description = "KO — with expected vs actual URL, or error details on failure", content = @Content(mediaType = "text/plain"))
    @GetMapping("/testCase010")
    public ResponseEntity<String> testCase010() {
        String result = aliraNavigateTabWebsiteService.testCase010NavigateTabWebsiteConfigurationCMSAccessTest();
        return result.startsWith("OK") || result.startsWith("SKIPPED")
                ? ResponseEntity.ok(result)
                : ResponseEntity.internalServerError().body(result);
    }

    @Operation(
        summary = "TC-011 — Website → Configuration → Constants",
        description = "Logs in, navigates to Website → Configuration → Constants and verifies the URL resolves to CMSConstants.aml. " +
                      "A screenshot is saved to screenshots/alira/ on completion."
    )
    @ApiResponse(responseCode = "200", description = "OK — with loaded URL on success", content = @Content(mediaType = "text/plain"))
    @ApiResponse(responseCode = "500", description = "KO — with expected vs actual URL, or error details on failure", content = @Content(mediaType = "text/plain"))
    @GetMapping("/testCase011")
    public ResponseEntity<String> testCase011() {
        String result = aliraNavigateTabWebsiteService.testCase011NavigateTabWebsiteConfigurationConstantsTest();
        return result.startsWith("OK") || result.startsWith("SKIPPED")
                ? ResponseEntity.ok(result)
                : ResponseEntity.internalServerError().body(result);
    }
}
