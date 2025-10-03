package org.gig.myplayrightapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.gig.myplayrightapp.service.CasinoGranMadridTestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Casino Gran Madrid Test Controller", description = "Endpoints to trigger automated browser tests")
@RestController
@RequestMapping("/api/test/cgm")
@RequiredArgsConstructor
public class CasinoGranMadridTestController {

    private final CasinoGranMadridTestService casinoGranMadridTestService;

    @Operation(summary = "Ping test")
    @GetMapping("/ping")
    public String testPing() {
        return "PONG!";
    }

    @Operation(summary = "Test Case 001 - Open pre-production website")
    @GetMapping("/testCase001")
    public String testCase001_GoToPreProdWebsite() {
        return casinoGranMadridTestService.testCase001RunPreProdAccessTest();
    }

    @Operation(summary = "Test Case 002 - Click register button on homepage")
    @GetMapping("/testCase002")
    public String testCase002_registerButton() {
        return casinoGranMadridTestService.testCase002RunRegisterButtonTest();
    }

    @Operation(summary = "Test Case 003 - Fast registration via Veridas")
    @GetMapping("/testCase003")
    public String testCase003_fastRegisterVeridas() {
        return casinoGranMadridTestService.testCase003RunFastRegisterVeridasTest();
    }

    @Operation(summary = "Test Case 004 - Manual registration path")
    @GetMapping("/testCase004")
    public String testCase004_manualRegister() {
        return casinoGranMadridTestService.testCase004RunManualRegisterTest();
    }

    @Operation(summary = "Test Case 005 - Manual registration with invalid name")
    @GetMapping("/testCase005")
    public String testCase005_manualRegisterWithInvalidData() {
        return casinoGranMadridTestService.testCase005RunInvalidNameRegistrationTest();
    }

    @Operation(summary = "Test Case 006 - Manual registration with duplicated user data")
    @GetMapping("/testCase006")
    public String testCase006_manualRegisterWithDuplicatedData() {
        return casinoGranMadridTestService.testCase006RunDuplicatedRegisterTest();
    }

}
