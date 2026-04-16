package org.gig.myplayrightapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.gig.myplayrightapp.service.AliraNavigateTabGamesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Alira — Games Tab", description = "Navigation tests for the Games tab in the Alira Back Office (Rooms, Providers, Themes & Tags, Exchange Profile)")
@RestController
@RequestMapping("/api/test/alira/navigation/games")
@RequiredArgsConstructor
public class AliraNavigateTabGamesController {

    private final AliraNavigateTabGamesService aliraNavigateTabGamesService;

    @Operation(
        summary = "TC-003 — Games → Rooms",
        description = "Logs in, navigates to Games → Rooms and verifies the URL resolves to rooms.aml. " +
                      "A screenshot is saved to screenshots/alira/ on completion."
    )
    @ApiResponse(responseCode = "200", description = "OK — with loaded URL on success", content = @Content(mediaType = "text/plain"))
    @ApiResponse(responseCode = "500", description = "KO — with expected vs actual URL, or error details on failure", content = @Content(mediaType = "text/plain"))
    @GetMapping("/testCase003")
    public ResponseEntity<String> testCase003() {
        String result = aliraNavigateTabGamesService.testCase003NavigateTabGamesRoomsTest();
        return result.startsWith("OK") || result.startsWith("SKIPPED")
                ? ResponseEntity.ok(result)
                : ResponseEntity.internalServerError().body(result);
    }

    @Operation(
        summary = "TC-004 — Games → Process Rooms",
        description = "Logs in, navigates to Games → Process Rooms and verifies the URL resolves to processRooms.aml. " +
                      "A screenshot is saved to screenshots/alira/ on completion."
    )
    @ApiResponse(responseCode = "200", description = "OK — with loaded URL on success", content = @Content(mediaType = "text/plain"))
    @ApiResponse(responseCode = "500", description = "KO — with expected vs actual URL, or error details on failure", content = @Content(mediaType = "text/plain"))
    @GetMapping("/testCase004")
    public ResponseEntity<String> testCase004() {
        String result = aliraNavigateTabGamesService.testCase004NavigateTabGamesProcessRoomsTest();
        return result.startsWith("OK") || result.startsWith("SKIPPED")
                ? ResponseEntity.ok(result)
                : ResponseEntity.internalServerError().body(result);
    }

    @Operation(
        summary = "TC-005 — Games → Lobby Rooms",
        description = "Logs in, navigates to Games → Lobby Rooms and verifies the URL resolves to lobby.aml. " +
                      "A screenshot is saved to screenshots/alira/ on completion."
    )
    @ApiResponse(responseCode = "200", description = "OK — with loaded URL on success", content = @Content(mediaType = "text/plain"))
    @ApiResponse(responseCode = "500", description = "KO — with expected vs actual URL, or error details on failure", content = @Content(mediaType = "text/plain"))
    @GetMapping("/testCase005")
    public ResponseEntity<String> testCase005() {
        String result = aliraNavigateTabGamesService.testCase005NavigateTabGamesLobbyRoomsTest();
        return result.startsWith("OK") || result.startsWith("SKIPPED")
                ? ResponseEntity.ok(result)
                : ResponseEntity.internalServerError().body(result);
    }

    @Operation(
        summary = "TC-006 — Games → Providers",
        description = "Logs in, navigates to Games → Providers and verifies the URL resolves to providers.aml. " +
                      "A screenshot is saved to screenshots/alira/ on completion."
    )
    @ApiResponse(responseCode = "200", description = "OK — with loaded URL on success", content = @Content(mediaType = "text/plain"))
    @ApiResponse(responseCode = "500", description = "KO — with expected vs actual URL, or error details on failure", content = @Content(mediaType = "text/plain"))
    @GetMapping("/testCase006")
    public ResponseEntity<String> testCase006() {
        String result = aliraNavigateTabGamesService.testCase006NavigateTabGamesProvidersTest();
        return result.startsWith("OK") || result.startsWith("SKIPPED")
                ? ResponseEntity.ok(result)
                : ResponseEntity.internalServerError().body(result);
    }

    @Operation(
        summary = "TC-007 — Games → Themes & Tags",
        description = "Logs in, navigates to Games → Themes and Tags and verifies the URL resolves to themesTags.aml. " +
                      "A screenshot is saved to screenshots/alira/ on completion."
    )
    @ApiResponse(responseCode = "200", description = "OK — with loaded URL on success", content = @Content(mediaType = "text/plain"))
    @ApiResponse(responseCode = "500", description = "KO — with expected vs actual URL, or error details on failure", content = @Content(mediaType = "text/plain"))
    @GetMapping("/testCase007")
    public ResponseEntity<String> testCase007() {
        String result = aliraNavigateTabGamesService.testCase007NavigateTabGamesThemesTagsTest();
        return result.startsWith("OK") || result.startsWith("SKIPPED")
                ? ResponseEntity.ok(result)
                : ResponseEntity.internalServerError().body(result);
    }

    @Operation(
        summary = "TC-008 — Games → Exchange Profile",
        description = "Logs in, navigates to Games → Exchange Profile and verifies the URL resolves to exchangeProfile.aml. " +
                      "A screenshot is saved to screenshots/alira/ on completion."
    )
    @ApiResponse(responseCode = "200", description = "OK — with loaded URL on success", content = @Content(mediaType = "text/plain"))
    @ApiResponse(responseCode = "500", description = "KO — with expected vs actual URL, or error details on failure", content = @Content(mediaType = "text/plain"))
    @GetMapping("/testCase008")
    public ResponseEntity<String> testCase008() {
        String result = aliraNavigateTabGamesService.testCase008NavigateTabGamesExchangeProfileTest();
        return result.startsWith("OK") || result.startsWith("SKIPPED")
                ? ResponseEntity.ok(result)
                : ResponseEntity.internalServerError().body(result);
    }
}
