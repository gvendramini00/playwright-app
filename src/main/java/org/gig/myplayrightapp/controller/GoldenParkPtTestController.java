package org.gig.myplayrightapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.gig.myplayrightapp.service.pre.gp.pt.GoldenParkPtTestService;
import org.gig.myplayrightapp.service.pre.gp.pt.GpptMassTestService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Tag(name = "Golden Park PT — Registration Tests", description = "Automated tests for the Golden Park Portugal dev site — covers manual registration and duplicate NIF validation")
@RestController
@RequestMapping("/api/test/gp-pt")
@RequiredArgsConstructor
public class GoldenParkPtTestController {

    private final GoldenParkPtTestService service;
    private final GpptMassTestService gpptMassTestService;

    @Operation(summary = "Health check", description = "Simple ping to verify the API is up and responding.")
    @ApiResponse(responseCode = "200", description = "Returns 'PONG!'", content = @Content(mediaType = "text/plain"))
    @GetMapping("/ping")
    public String testPing() {
        return "PONG!";
    }

    @Operation(
        summary = "GP-PT Mass Test Runner",
        description = "Runs all Golden Park Portugal test cases in sequence and returns a downloadable Excel report with results and screenshot references."
    )
    @ApiResponse(responseCode = "200", description = "Excel report (.xlsx) with full test results",
            content = @Content(mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
    @GetMapping(value = "/runAll", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<byte[]> runAll() {
        byte[] report = gpptMassTestService.runAllGpptTestsAndGenerateReport();
        String filename = "gppt_report_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDisposition(ContentDisposition.attachment().filename(filename).build());
        return ResponseEntity.ok().headers(headers).body(report);
    }

    @Operation(
        summary = "GP-PT — Manual registration (happy path)",
        description = "Generates a unique Portuguese player and completes the full manual registration form on the Golden Park PT dev site. " +
                      "Verifies the page advances after submission with no visible errors. " +
                      "⚠️ This test creates a real player account in the dev environment. " +
                      "A screenshot is saved to screenshots/gp/ on completion."
    )
    @ApiResponse(responseCode = "200", description = "OK — on successful registration", content = @Content(mediaType = "text/plain"))
    @ApiResponse(responseCode = "500", description = "KO — if errors were found or page did not advance", content = @Content(mediaType = "text/plain"))
    @GetMapping("/register")
    public ResponseEntity<String> runManualRegistration() {
        String result = service.runManualRegistrationTest();
        return result.startsWith("OK") || result.startsWith("SKIPPED")
                ? ResponseEntity.ok(result)
                : ResponseEntity.internalServerError().body(result);
    }

    @Operation(
        summary = "GP-PT — Duplicate NIF validation",
        description = "Retrieves an existing player from the database and attempts to register again using the same NIF. " +
                      "Verifies that a duplicate warning appears ('já está em uso'). " +
                      "Skipped automatically if no existing player is found in the database. " +
                      "A screenshot is saved to screenshots/gp/ on completion."
    )
    @ApiResponse(responseCode = "200", description = "OK — if duplicate warning shown, SKIPPED — if no existing player found", content = @Content(mediaType = "text/plain"))
    @ApiResponse(responseCode = "500", description = "KO — if warning did not appear", content = @Content(mediaType = "text/plain"))
    @GetMapping("/register-duplicate")
    public ResponseEntity<String> runDuplicateNif() {
        String result = service.runDuplicateNifTest();
        return result.startsWith("OK") || result.startsWith("SKIPPED")
                ? ResponseEntity.ok(result)
                : ResponseEntity.internalServerError().body(result);
    }
}
