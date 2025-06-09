package org.gig.myplayrightapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.gig.myplayrightapp.service.PlaywrightTestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Playwright Test Controller", description = "Endpoints to trigger automated browser tests")
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final PlaywrightTestService playwrightTestService;

    @Operation(summary = "Test Case 001 - Open pre-production website")
    @GetMapping("/testCase001")
    public String testCase001_GoToPreProdWebsite() {
        return playwrightTestService.testCase001RunPreProdAccessTest();
    }

    @Operation(summary = "Test Case 002 - Click register button on homepage")
    @GetMapping("/testCase002")
    public String testCase002_registerButton() {
        return playwrightTestService.testCase002RunRegisterButtonTest();
    }

    @Operation(summary = "Test Case 003 - Fast registration via Veridas")
    @GetMapping("/testCase003")
    public String testCase003_fastRegisterVeridas() {
        return playwrightTestService.testCase003RunFastRegisterVeridasTest();
    }

    @Operation(summary = "Test Case 004 - Manual registration path")
    @GetMapping("/testCase004")
    public String testCase004_manualRegister() {
        return playwrightTestService.testCase004RunManualRegisterTest();
    }

    @Operation(summary = "Test Case 005 - Manual registration with invalid name")
    @GetMapping("/testCase005")
    public String testCase005_manualRegisterWithInvalidData() {
        return playwrightTestService.testCase005RunInvalidNameRegistrationTest();
    }
}
