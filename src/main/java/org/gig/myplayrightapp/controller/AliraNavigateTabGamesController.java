package org.gig.myplayrightapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.gig.myplayrightapp.service.AliraNavigateTabGamesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Alira Games Tab Navigation Controller", description = "Endpoints to trigger automated browser tests")
@RestController
@RequestMapping("/api/test/alira/navigation/games")
@RequiredArgsConstructor
public class AliraNavigateTabGamesController {

    private final AliraNavigateTabGamesService aliraNavigateTabGamesService;

    @Operation(summary = "Test Case 003 - Navigate to Tab Games -> Rooms")
    @GetMapping("/testCase003")
    public String testCase003() {
        return aliraNavigateTabGamesService.testCase003NavigateTabGamesRoomsTest();
    }

    @Operation(summary = "Test Case 004 - Navigate to Tab Games -> Process Rooms")
    @GetMapping("/testCase004")
    public String testCase004() {return aliraNavigateTabGamesService.testCase004NavigateTabGamesProcessRoomsTest();}

    @Operation(summary = "Test Case 005 - Navigate to Tab Games -> Lobby Rooms")
    @GetMapping("/testCase005")
    public String testCase005() {return aliraNavigateTabGamesService.testCase005NavigateTabGamesLobbyRoomsTest();}

    @Operation(summary = "Test Case 006 - Navigate to Tab Games -> Providers")
    @GetMapping("/testCase006")
    public String testCase006() {return aliraNavigateTabGamesService.testCase006NavigateTabGamesProvidersTest();}

    @Operation(summary = "Test Case 007 - Navigate to Tab Games -> Themes & Tags")
    @GetMapping("/testCase007")
    public String testCase007() {return aliraNavigateTabGamesService.testCase007NavigateTabGamesThemesTagsTest();}

    @Operation(summary = "Test Case 008 - Navigate to Tab Games -> Exchange Profile")
    @GetMapping("/testCase008")
    public String testCase008() {return aliraNavigateTabGamesService.testCase008NavigateTabGamesExchangeProfileTest();}
}
