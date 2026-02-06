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
}
