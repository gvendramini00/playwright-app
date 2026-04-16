package org.gig.myplayrightapp.service.staging;

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
public class AliraStagingMassTestServiceImpl implements AliraStagingMassTestService {

    private final AliraStagingTestService aliraStagingTestService;
    private final ExcelReportUtil excelReportUtil;

    private static final String SCREENSHOT_DIR = "screenshots/staging/";
    private static final String REPORT_TITLE   = "Alira Staging Automated Test Report";

    @Override
    public byte[] runAllStagingTestsAndGenerateReport() {
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
        log.info("Staging mass test finished");
        log.info("  Total:    {}", results.size());
        log.info("  ✅ Passed: {}", passed);
        log.info("  ❌ Failed: {}", failed);
        log.info("  Duration: {}", excelReportUtil.formatDuration(totalMs));
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        return excelReportUtil.generateReport(REPORT_TITLE, results);
    }

    private List<TestDefinition> buildTestDefinitions() {
        return List.of(
                new TestDefinition("NL2-TC-001", "Login to Alira Staging Back Office",
                        aliraStagingTestService::testCase001LoginTest),
                new TestDefinition("NL2-TC-002", "Navigate to Staging Player Profile",
                        aliraStagingTestService::testCase002NavigatePlayerProfile)
        );
    }

    private record TestDefinition(String id, String description, Supplier<String> runner) {}
}
