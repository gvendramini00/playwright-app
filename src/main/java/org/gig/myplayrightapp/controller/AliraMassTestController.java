package org.gig.myplayrightapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.gig.myplayrightapp.service.pre.alira.AliraMassTestService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Tag(name = "Alira — Mass Test Runner", description = "Runs all 17 Alira test cases in sequence and returns a downloadable Excel report with results and screenshot references")
@RestController
@RequestMapping("/api/test/alira/mass")
@RequiredArgsConstructor
public class AliraMassTestController {

    private final AliraMassTestService aliraMassTestService;

    private static final DateTimeFormatter FILE_FMT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    @Operation(
        summary = "Run all Alira tests and download Excel report",
        description = "Executes all 17 Alira test cases (TC-001 to TC-017) sequentially and generates an Excel report. " +
                      "Each row contains the test case ID, result (✅/❌), the returned message, and the path to the screenshot taken during that test. " +
                      "⚠️ TC-015 creates a Deposit Promotion, TC-016 edits one and TC-017 deletes one — running this will write and delete data in the pre-production environment."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Excel file (.xlsx) downloaded as alira_report_<yyyyMMdd_HHmmss>.xlsx",
        content = @Content(mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    )
    @GetMapping("/run-all")
    public ResponseEntity<byte[]> runAll() {
        byte[] report = aliraMassTestService.runAllAliraTestsAndGenerateReport();
        String filename = "alira_report_" + LocalDateTime.now().format(FILE_FMT) + ".xlsx";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDisposition(ContentDisposition.attachment().filename(filename).build());
        headers.setContentLength(report.length);

        return ResponseEntity.ok().headers(headers).body(report);
    }
}
