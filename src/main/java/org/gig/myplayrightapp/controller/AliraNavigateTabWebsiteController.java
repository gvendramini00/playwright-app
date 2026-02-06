package org.gig.myplayrightapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.gig.myplayrightapp.service.AliraNavigateTabWebsiteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Alira Website Tab Navigation Controller", description = "Endpoints to trigger automated browser tests")
@RestController
@RequestMapping("/api/test/alira/navigation/website")
@RequiredArgsConstructor
public class AliraNavigateTabWebsiteController {

    private final AliraNavigateTabWebsiteService aliraNavigateTabWebsiteService;

    @Operation(summary = "Test Case 009 - Navigate to Tab Website -> CMS")
    @GetMapping("/testCase009")
    public String testCase009() {return aliraNavigateTabWebsiteService.testCase009NavigateTabWebsiteCMSTest();}

    @Operation(summary = "Test Case 010 - Navigate to Tab Website -> Configuration -> CMS Access")
    @GetMapping("/testCase010")
    public String testCase010() {return aliraNavigateTabWebsiteService.testCase010NavigateTabWebsiteConfigurationCMSAccessTest();}

    @Operation(summary = "Test Case 011 - Navigate to Tab Website -> Configuration -> Constants")
    @GetMapping("/testCase011")
    public String testCase011() {return aliraNavigateTabWebsiteService.testCase011NavigateTabWebsiteConfigurationConstantsTest();}
}
