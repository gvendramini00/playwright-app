package org.gig.myplayrightapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.gig.myplayrightapp.service.AliraTestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Alira Test Controller", description = "Endpoints to trigger automated browser tests")
@RestController
@RequestMapping("/api/test/alira")
@RequiredArgsConstructor
public class AliraTestController {

    private final AliraTestService aliraTestService;

    @Operation(summary = "Ping test")
    @GetMapping("/ping")
    public String testPing() {
        return "PONG!";
    }

    @Operation(summary = "Test Case 001 - Login into Alira Back Office")
    @GetMapping("/testCase001")
    public String testCase001() {
        return aliraTestService.testCase001LoginTest();
    }

    @Operation(summary = "Test Case 002 - Navigate to Player Profile")
    @GetMapping("/testCase002")
    public String testCase002() {
        return aliraTestService.testCase002NavigatePlayerProfile();
    }

    @Operation(summary = "Test Case 003 - Navigate to Tab Games -> Rooms")
    @GetMapping("/testCase003")
    public String testCase003() {
        return aliraTestService.testCase003NavigateTabGamesRoomsTest();
    }

    @Operation(summary = "Test Case 004 - Navigate to Tab Games -> Process Rooms")
    @GetMapping("/testCase004")
    public String testCase004() {return aliraTestService.testCase004NavigateTabGamesProcessRoomsTest();}

    @Operation(summary = "Test Case 005 - Navigate to Tab Games -> Lobby Rooms")
    @GetMapping("/testCase005")
    public String testCase005() {return aliraTestService.testCase005NavigateTabGamesLobbyRoomsTest();}

    @Operation(summary = "Test Case 006 - Navigate to Tab Games -> Providers")
    @GetMapping("/testCase006")
    public String testCase006() {return aliraTestService.testCase006NavigateTabGamesProvidersTest();}

    @Operation(summary = "Test Case 007 - Navigate to Tab Games -> Themes & Tags")
    @GetMapping("/testCase007")
    public String testCase007() {return aliraTestService.testCase007NavigateTabGamesThemesTagsTest();}

    @Operation(summary = "Test Case 008 - Navigate to Tab Games -> Exchange Profile")
    @GetMapping("/testCase008")
    public String testCase008() {return aliraTestService.testCase008NavigateTabGamesExchangeProfileTest();}

    @Operation(summary = "Test Case 009 - Navigate to Tab Website -> CMS")
    @GetMapping("/testCase009")
    public String testCase009() {return aliraTestService.testCase009NavigateTabWebsiteCMSTest();}

    @Operation(summary = "Test Case 010 - Navigate to Tab Website -> Configuration -> CMS Access")
    @GetMapping("/testCase010")
    public String testCase010() {return aliraTestService.testCase010NavigateTabWebsiteConfigurationCMSAccessTest();}
}
