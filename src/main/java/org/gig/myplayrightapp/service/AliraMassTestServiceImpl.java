package org.gig.myplayrightapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.gig.myplayrightapp.dto.AliraTestResult;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
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

    private static final String SCREENSHOT_DIR = "screenshots/alira/";
    private static final DateTimeFormatter DATETIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter FILE_FMT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

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
                message = "❌ Unexpected error: " + e.getMessage();
                log.error("Error running {}", def.id(), e);
            }
            long duration = System.currentTimeMillis() - start;
            boolean passed = message != null && message.startsWith("✅");
            String screenshotPath = resolveScreenshotPath(def.id(), message);

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
        log.info("  Duration: {}", formatDuration(totalMs));
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        return generateExcel(results);
    }

    // -------------------------------------------------------------------------
    // Test definitions
    // -------------------------------------------------------------------------

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

    // -------------------------------------------------------------------------
    // Screenshot path resolution
    // -------------------------------------------------------------------------

    private String resolveScreenshotPath(String testCaseId, String message) {
        // Some messages embed the path after "Check screenshot: "
        if (message != null && message.contains("Check screenshot: ")) {
            return message.substring(message.indexOf("Check screenshot: ") + "Check screenshot: ".length()).trim();
        }
        // Timestamped tests (015-017) save files like testCase015_<timestamp>.png — find the latest
        String prefix = SCREENSHOT_DIR + testCaseId + "_";
        Path dir = Paths.get(SCREENSHOT_DIR);
        if (Files.exists(dir)) {
            try {
                return Files.list(dir)
                        .filter(p -> p.getFileName().toString().startsWith(testCaseId + "_"))
                        .max(Comparator.comparingLong(p -> p.toFile().lastModified()))
                        .map(Path::toString)
                        .orElse(SCREENSHOT_DIR + testCaseId + ".png");
            } catch (IOException e) {
                log.warn("Could not list screenshots for {}", testCaseId);
            }
        }
        // Fixed-name tests
        return SCREENSHOT_DIR + testCaseId + ".png";
    }

    // -------------------------------------------------------------------------
    // Excel generation
    // -------------------------------------------------------------------------

    private byte[] generateExcel(List<AliraTestResult> results) {
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            XSSFSheet sheet = workbook.createSheet("Alira Test Report");

            // Column widths
            sheet.setColumnWidth(0, 5 * 256);       // #
            sheet.setColumnWidth(1, 15 * 256);      // Test Case
            sheet.setColumnWidth(2, 55 * 256);      // Description
            sheet.setColumnWidth(3, 10 * 256);      // Status
            sheet.setColumnWidth(4, 65 * 256);      // Message
            sheet.setColumnWidth(5, 45 * 256);      // Screenshot
            sheet.setColumnWidth(6, 12 * 256);      // Duration
            sheet.setColumnWidth(7, 22 * 256);      // Ran At

            // Styles
            CellStyle titleStyle    = buildTitleStyle(workbook);
            CellStyle summaryStyle  = buildSummaryStyle(workbook);
            CellStyle headerStyle   = buildHeaderStyle(workbook);
            CellStyle passStyle     = buildRowStyle(workbook, new XSSFColor(new byte[]{(byte)198, (byte)239, (byte)206}, null));
            CellStyle failStyle     = buildRowStyle(workbook, new XSSFColor(new byte[]{(byte)255, (byte)199, (byte)206}, null));
            CellStyle passStatusStyle = buildStatusStyle(workbook, new XSSFColor(new byte[]{(byte)0, (byte)128, (byte)0}, null));
            CellStyle failStatusStyle = buildStatusStyle(workbook, new XSSFColor(new byte[]{(byte)192, (byte)0, (byte)0}, null));
            CellStyle linkStyle     = buildLinkStyle(workbook);

            long passed  = results.stream().filter(AliraTestResult::passed).count();
            long failed  = results.size() - passed;
            long totalMs = results.stream().mapToLong(AliraTestResult::durationMs).sum();

            // Row 0 — Title
            Row titleRow = sheet.createRow(0);
            titleRow.setHeightInPoints(28);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Alira Automated Test Report — " + LocalDateTime.now().format(DATETIME_FMT));
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));

            // Row 1 — Summary
            Row summaryRow = sheet.createRow(1);
            summaryRow.setHeightInPoints(20);
            Cell summaryCell = summaryRow.createCell(0);
            summaryCell.setCellValue(String.format(
                    "Total: %d   |   Passed: %d   |   Failed: %d   |   Total duration: %s  (%,.0f s)",
                    results.size(), passed, failed, formatDuration(totalMs), totalMs / 1000.0));
            summaryCell.setCellStyle(summaryStyle);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 7));

            // Row 2 — Headers
            Row headerRow = sheet.createRow(2);
            headerRow.setHeightInPoints(18);
            String[] headers = {"#", "Test Case", "Description", "Status", "Message", "Screenshot", "Duration", "Ran At"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data rows
            CreationHelper creationHelper = workbook.getCreationHelper();
            int rowNum = 3;
            for (int i = 0; i < results.size(); i++) {
                AliraTestResult r = results.get(i);
                Row row = sheet.createRow(rowNum++);
                row.setHeightInPoints(16);

                CellStyle rowStyle = r.passed() ? passStyle : failStyle;

                Cell numCell = row.createCell(0);
                numCell.setCellValue(i + 1);
                numCell.setCellStyle(rowStyle);

                Cell idCell = row.createCell(1);
                idCell.setCellValue(r.testCaseId());
                idCell.setCellStyle(rowStyle);

                Cell descCell = row.createCell(2);
                descCell.setCellValue(r.description());
                descCell.setCellStyle(rowStyle);

                Cell statusCell = row.createCell(3);
                statusCell.setCellValue(r.passed() ? "PASS" : "FAIL");
                statusCell.setCellStyle(r.passed() ? passStatusStyle : failStatusStyle);

                Cell msgCell = row.createCell(4);
                msgCell.setCellValue(r.message() != null ? r.message() : "");
                msgCell.setCellStyle(rowStyle);

                Cell screenshotCell = row.createCell(5);
                if (r.screenshotPath() != null && Files.exists(Paths.get(r.screenshotPath()))) {
                    Hyperlink link = creationHelper.createHyperlink(HyperlinkType.FILE);
                    link.setAddress(Paths.get(r.screenshotPath()).toAbsolutePath().toUri().toString());
                    screenshotCell.setCellValue(r.screenshotPath());
                    screenshotCell.setHyperlink(link);
                    screenshotCell.setCellStyle(linkStyle);
                } else {
                    screenshotCell.setCellValue(r.screenshotPath() != null ? r.screenshotPath() : "");
                    screenshotCell.setCellStyle(rowStyle);
                }

                Cell durCell = row.createCell(6);
                durCell.setCellValue(formatDuration(r.durationMs()));
                durCell.setCellStyle(rowStyle);

                Cell timeCell = row.createCell(7);
                timeCell.setCellValue(r.ranAt().format(DATETIME_FMT));
                timeCell.setCellStyle(rowStyle);
            }

            workbook.write(out);
            return out.toByteArray();

        } catch (IOException e) {
            log.error("Failed to generate Excel report", e);
            throw new RuntimeException("Failed to generate Excel report", e);
        }
    }

    // -------------------------------------------------------------------------
    // Style helpers
    // -------------------------------------------------------------------------

    private CellStyle buildTitleStyle(XSSFWorkbook wb) {
        XSSFCellStyle style = wb.createCellStyle();
        style.setFillForegroundColor(new XSSFColor(new byte[]{(byte)31, (byte)73, (byte)125}, null));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        XSSFFont font = wb.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        font.setColor(new XSSFColor(new byte[]{(byte)255, (byte)255, (byte)255}, null));
        style.setFont(font);
        return style;
    }

    private CellStyle buildSummaryStyle(XSSFWorkbook wb) {
        XSSFCellStyle style = wb.createCellStyle();
        style.setFillForegroundColor(new XSSFColor(new byte[]{(byte)68, (byte)114, (byte)196}, null));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        XSSFFont font = wb.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 11);
        font.setColor(new XSSFColor(new byte[]{(byte)255, (byte)255, (byte)255}, null));
        style.setFont(font);
        return style;
    }

    private CellStyle buildHeaderStyle(XSSFWorkbook wb) {
        XSSFCellStyle style = wb.createCellStyle();
        style.setFillForegroundColor(new XSSFColor(new byte[]{(byte)47, (byte)85, (byte)151}, null));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        setBorderThin(style);
        XSSFFont font = wb.createFont();
        font.setBold(true);
        font.setColor(new XSSFColor(new byte[]{(byte)255, (byte)255, (byte)255}, null));
        style.setFont(font);
        return style;
    }

    private CellStyle buildRowStyle(XSSFWorkbook wb, XSSFColor bg) {
        XSSFCellStyle style = wb.createCellStyle();
        style.setFillForegroundColor(bg);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(false);
        setBorderThin(style);
        return style;
    }

    private CellStyle buildStatusStyle(XSSFWorkbook wb, XSSFColor color) {
        XSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        setBorderThin(style);
        XSSFFont font = wb.createFont();
        font.setBold(true);
        font.setColor(color);
        style.setFont(font);
        return style;
    }

    private CellStyle buildLinkStyle(XSSFWorkbook wb) {
        XSSFCellStyle style = wb.createCellStyle();
        style.setFillForegroundColor(new XSSFColor(new byte[]{(byte)198, (byte)239, (byte)206}, null));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        setBorderThin(style);
        XSSFFont font = wb.createFont();
        font.setUnderline(FontUnderline.SINGLE);
        font.setColor(new XSSFColor(new byte[]{(byte)0, (byte)0, (byte)255}, null));
        style.setFont(font);
        return style;
    }

    private void setBorderThin(XSSFCellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
    }

    // -------------------------------------------------------------------------
    // Duration formatting
    // -------------------------------------------------------------------------

    private String formatDuration(long ms) {
        long totalSeconds = ms / 1000;
        long hours   = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;
        if (hours > 0) {
            return String.format("%dh %dm %ds", hours, minutes, seconds);
        } else if (minutes > 0) {
            return String.format("%dm %ds", minutes, seconds);
        } else {
            return String.format("%ds", seconds);
        }
    }

    // -------------------------------------------------------------------------
    // Inner record
    // -------------------------------------------------------------------------

    private record TestDefinition(String id, String description, Supplier<String> runner) {}
}
