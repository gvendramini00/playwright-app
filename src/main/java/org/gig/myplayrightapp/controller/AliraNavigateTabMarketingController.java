package org.gig.myplayrightapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.gig.myplayrightapp.service.AliraNavigateTabMarketingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Alira Marketing Tab Navigation Controller", description = "Endpoints to trigger automated browser tests")
@RestController
@RequestMapping("/api/test/alira/navigation/marketing")
@RequiredArgsConstructor
public class AliraNavigateTabMarketingController {

    private final AliraNavigateTabMarketingService aliraNavigateTabMarketingService;

    @Operation(summary = "Test Case 012 - Navigate to Dashboard and verify table data")
    @GetMapping("/testCase012")
    public String testCase012() {
        return aliraNavigateTabMarketingService.testCase012NavigateDashboardTest();
    }

    @Operation(summary = "Test Case 013 - Navigate to Marketing -> Bonus -> Deposit Promotions and verify table data")
    @GetMapping("/testCase013")
    public String testCase013() {
        return aliraNavigateTabMarketingService.testCase013NavigateMarketingBonusDepositPromotionsTest();
    }

    @Operation(summary = "Test Case 014 - Navigate to Marketing -> Bonus -> Deposit Promotions -> click New and verify modal appears")
    @GetMapping("/testCase014")
    public String testCase014() {
        return aliraNavigateTabMarketingService.testCase014NavigateMarketingBonusDepositPromotionsNewModalTest();
    }

    @Operation(summary = "Test Case 015 - Create a new Deposit Promotion and verify it saves successfully")
    @GetMapping("/testCase015")
    public String testCase015() {
        return aliraNavigateTabMarketingService.testCase015CreateDepositPromotionTest();
    }
}
