package org.gig.myplayrightapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.gig.myplayrightapp.service.widgets.AttachedDocService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Alira — Widgets", description = "Tests for Player Profile widgets in the Alira Back Office — covers Attached Documentation (add, edit, delete)")
@RestController
@RequestMapping("/api/test/alira/widgets")
@RequiredArgsConstructor
public class AliraWidgetsController {

    private final AttachedDocService attachedDocService;

    @Operation(
        summary = "TC-018 — Attached Documentation: Add New Attach",
        description = "Logs in, navigates to the configured test player's profile, adds the Attached Documentation widget, " +
                      "clicks New Attach and fills in the form (file upload, status, expiry date, document type, visibility, description, note). " +
                      "Verifies no error messages appear after saving. " +
                      "⚠️ This test uploads a file and writes data to the pre-production environment. " +
                      "A screenshot is saved to screenshots/alira/ on completion."
    )
    @ApiResponse(responseCode = "200", description = "OK — attachment added successfully", content = @Content(mediaType = "text/plain"))
    @ApiResponse(responseCode = "500", description = "KO — with error details or screenshot path on failure", content = @Content(mediaType = "text/plain"))
    @GetMapping("/testCase018")
    public ResponseEntity<String> testCase018() {
        String result = attachedDocService.testCase018AttachedDocAddNewAttachTest();
        return result.startsWith("OK") || result.startsWith("SKIPPED")
                ? ResponseEntity.ok(result)
                : ResponseEntity.internalServerError().body(result);
    }
}
