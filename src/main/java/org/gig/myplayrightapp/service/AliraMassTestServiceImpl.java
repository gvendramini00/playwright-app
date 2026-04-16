package org.gig.myplayrightapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gig.myplayrightapp.dto.AliraTestResult;
import org.gig.myplayrightapp.util.ExcelReportUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class AliraMassTestServiceImpl implements AliraMassTestService {

    private final AliraTestService aliraTestService;
    private final AliraNavigateTabGamesService gamesService;
    private final AliraNavigateTabWebsiteService websiteService;
    private final AliraNavigateTabMarketingService marketingService;
    private final ExcelReportUtil excelReportUtil;

    private static final String SCREENSHOT_DIR = "screenshots/alira/";
    private static final String REPORT_TITLE   = "Alira Automated Test Report";

    @Override
    public byte[] runAllAliraTestsAndGenerateReport() {
        List<TestDefinition> definitions = buildTestDefinitions();
        List<AliraTestResult> results = new ArrayList<>();

        for (TestDefinition def : definitions) {
            log.info("Running {} — {}", def.id(), def.description());
            LocalDateTime ranAt = LocalDateTime.now();
            long start = System.currentTimeMillis();
            String message;
            try {
                message = def.runner().get();
            } catch (Exception e) {
                message = "KO — Unexpected error: " + e.getMessage();
                log.error("Error running {}", def.id(), e);
            }
            long duration = System.currentTimeMillis() - start;
            boolean passed = message != null && (message.startsWith("OK") || message.startsWith("SKIPPED"));
            String screenshotPath = excelReportUtil.resolveScreenshotPath(def.id(), message, SCREENSHOT_DIR);

            results.add(new AliraTestResult(def.id(), def.description(), passed, message, screenshotPath, duration, ranAt));
            log.info("{} {} ({}ms)", passed ? "✅" : "❌", def.id(), duration);
        }

        long totalMs = results.stream().mapToLong(AliraTestResult::durationMs).sum();
        long passed  = results.stream().filter(AliraTestResult::passed).count();
        long failed  = results.size() - passed;

        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        log.info("Alira mass test finished");
        log.info("  Total:    {}", results.size());
        log.info("  ✅ Passed: {}", passed);
        log.info("  ❌ Failed: {}", failed);
        log.info("  Duration: {}", excelReportUtil.formatDuration(totalMs));
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        return excelReportUtil.generateReport(REPORT_TITLE, results);
    }

    private List<TestDefinition> buildTestDefinitions() {
        return List.of(
                new TestDefinition("testCase001", "Login to Alira Back Office",
                        aliraTestService::testCase001LoginTest),
                new TestDefinition("testCase002", "Navigate to Player Profile",
                        aliraTestService::testCase002NavigatePlayerProfile),
                new TestDefinition("testCase003", "Navigate → Games → Rooms",
                        gamesService::testCase003NavigateTabGamesRoomsTest),
                new TestDefinition("testCase004", "Navigate → Games → Process Rooms",
                        gamesService::testCase004NavigateTabGamesProcessRoomsTest),
                new TestDefinition("testCase005", "Navigate → Games → Lobby Rooms",
                        gamesService::testCase005NavigateTabGamesLobbyRoomsTest),
                new TestDefinition("testCase006", "Navigate → Games → Providers",
                        gamesService::testCase006NavigateTabGamesProvidersTest),
                new TestDefinition("testCase007", "Navigate → Games → Themes & Tags",
                        gamesService::testCase007NavigateTabGamesThemesTagsTest),
                new TestDefinition("testCase008", "Navigate → Games → Exchange Profile",
                        gamesService::testCase008NavigateTabGamesExchangeProfileTest),
                new TestDefinition("testCase009", "Navigate → Website → CMS",
                        websiteService::testCase009NavigateTabWebsiteCMSTest),
                new TestDefinition("testCase010", "Navigate → Website → Configuration → CMS Access",
                        websiteService::testCase010NavigateTabWebsiteConfigurationCMSAccessTest),
                new TestDefinition("testCase011", "Navigate → Website → Configuration → Constants",
                        websiteService::testCase011NavigateTabWebsiteConfigurationConstantsTest),
                new TestDefinition("testCase012", "Navigate → Dashboard — verify table has data",
                        marketingService::testCase012NavigateDashboardTest),
                new TestDefinition("testCase013", "Navigate → Marketing → Bonus → Deposit Promotions — verify table has data",
                        marketingService::testCase013NavigateMarketingBonusDepositPromotionsTest),
                new TestDefinition("testCase014", "Navigate → Marketing → Bonus → Deposit Promotions → click New — verify modal",
                        marketingService::testCase014NavigateMarketingBonusDepositPromotionsNewModalTest),
                new TestDefinition("testCase015", "Create a new Deposit Promotion",
                        marketingService::testCase015CreateDepositPromotionTest),
                new TestDefinition("testCase016", "Edit the first Deposit Promotion and verify update",
                        marketingService::testCase016EditDepositPromotionTest),
                new TestDefinition("testCase017", "Delete the first Deposit Promotion and verify removal",
                        marketingService::testCase017DeleteDepositPromotionTest)
        );
    }

    private record TestDefinition(String id, String description, Supplier<String> runner) {}
}
