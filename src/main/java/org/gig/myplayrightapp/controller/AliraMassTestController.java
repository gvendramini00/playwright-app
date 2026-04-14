package org.gig.myplayrightapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.gig.myplayrightapp.service.AliraMassTestService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Tag(name = "Alira Mass Test Controller", description = "Runs all Alira test cases and returns an Excel report")
@RestController
@RequestMapping("/api/test/alira/mass")
@RequiredArgsConstructor
public class AliraMassTestController {

    private final AliraMassTestService aliraMassTestService;

    private static final DateTimeFormatter FILE_FMT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    @Operation(summary = "Run all 17 Alira test cases and download an Excel report with results and screenshot links")
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
