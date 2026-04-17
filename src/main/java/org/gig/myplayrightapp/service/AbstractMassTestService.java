package org.gig.myplayrightapp.service;

import lombok.extern.slf4j.Slf4j;
import org.gig.myplayrightapp.dto.AliraTestResult;
import org.gig.myplayrightapp.util.ExcelReportUtil;
import org.gig.myplayrightapp.util.PlaywrightUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Shared orchestration logic for all mass test runners.
 * Subclasses provide the test definitions, screenshot directory, report title,
 * and environment label — everything else is handled here.
 * <p>
 * All tests in a single run share one Playwright browser process via
 * {@link PlaywrightUtil#openSharedBrowser()}. Each test still gets its own
 * isolated {@code BrowserContext}, so cookies and sessions do not leak between tests.
 */
@Slf4j
public abstract class AbstractMassTestService {

    protected final ExcelReportUtil excelReportUtil;
    protected final PlaywrightUtil  playwrightUtil;

    protected AbstractMassTestService(ExcelReportUtil excelReportUtil, PlaywrightUtil playwrightUtil) {
        this.excelReportUtil = excelReportUtil;
        this.playwrightUtil  = playwrightUtil;
    }

    protected abstract String screenshotDir();
    protected abstract String reportTitle();
    protected abstract String environmentLabel();
    protected abstract List<TestDefinition> buildTestDefinitions();

    protected byte[] runAndGenerateReport() {
        List<TestDefinition> definitions = buildTestDefinitions();
        List<AliraTestResult> results    = new ArrayList<>();

        playwrightUtil.openSharedBrowser();
        try {
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
                String screenshotPath = excelReportUtil.resolveScreenshotPath(def.id(), message, screenshotDir());

                results.add(new AliraTestResult(def.id(), def.description(), passed, message, screenshotPath, duration, ranAt));
                log.info("{} {} ({}ms)", passed ? "✅" : "❌", def.id(), duration);
            }
        } finally {
            playwrightUtil.closeSharedBrowser();
        }

        long totalMs = results.stream().mapToLong(AliraTestResult::durationMs).sum();
        long passed  = results.stream().filter(AliraTestResult::passed).count();
        long failed  = results.size() - passed;

        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        log.info("{} mass test finished", environmentLabel());
        log.info("  Total:    {}", results.size());
        log.info("  ✅ Passed: {}", passed);
        log.info("  ❌ Failed: {}", failed);
        log.info("  Duration: {}", excelReportUtil.formatDuration(totalMs));
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        return excelReportUtil.generateReport(reportTitle(), results);
    }

    public record TestDefinition(String id, String description, Supplier<String> runner) {}
}
