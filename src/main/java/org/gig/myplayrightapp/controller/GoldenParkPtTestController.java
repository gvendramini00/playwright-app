package org.gig.myplayrightapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.gig.myplayrightapp.service.GoldenParkPtTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Golden Park PT Test Controller", description = "Endpoints to trigger automated browser tests")
@RestController
@RequestMapping("/api/test/gp-pt")
@RequiredArgsConstructor
public class GoldenParkPtTestController {

    @Autowired
    private final GoldenParkPtTestService service;

    @Operation(summary = "Ping test")
    @GetMapping("/ping")
    public String testPing() {
        return "PONG!";
    }

    @Operation(summary = "GP-PT - Manual registration flow")
    @GetMapping("/register")
    public String runManualRegistration() {
        return service.runManualRegistrationTest();
    }

    @Operation(summary = "GP-PT - Duplicate NIF check (stops at validation)")
    @GetMapping("/register-duplicate")
    public String runDuplicateNif() {
        return service.runDuplicateNifTest();
    }
}
