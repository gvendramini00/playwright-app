package org.gig.myplayrightapp.util;

import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;
import org.gig.myplayrightapp.enums.TestVariables;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Component
public class ScreenshotUtil {

    public String takeScreenshot(Page page, TestVariables variable, String fileName) {
        Path directoryPath = Paths.get(variable.getValue());

        try {
            // Ensure the specific directory exists before saving
            Files.createDirectories(directoryPath);

            Path filePath = directoryPath.resolve(fileName + "_" + System.currentTimeMillis() + ".png");
            page.screenshot(new Page.ScreenshotOptions().setPath(filePath));

            log.info("Screenshot saved to: {}", filePath);
            return filePath.toString();
        } catch (IOException e) {
            log.error("Could not create/access directory: {}", directoryPath, e);
            return "";
        }
    }
}