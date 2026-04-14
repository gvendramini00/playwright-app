package org.gig.myplayrightapp.dto;

import java.time.LocalDateTime;

public record AliraTestResult(
        String testCaseId,
        String description,
        boolean passed,
        String message,
        String screenshotPath,
        long durationMs,
        LocalDateTime ranAt
) {}
