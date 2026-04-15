package org.gig.myplayrightapp.util;

import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;
import org.gig.myplayrightapp.enums.AliraVariables;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Component
public class ScreenshotUtil {

    public void takeScreenshot(Page page, AliraVariables variable, String fileName) {
        Path directoryPath = Paths.get(variable.getValue());

        try {
            // Ensure the specific directory exists before saving
            Files.createDirectories(directoryPath);

            Path filePath = directoryPath.resolve(fileName + ".png");
            page.screenshot(new Page.ScreenshotOptions().setPath(filePath));

            log.info("Screenshot saved to: {}", filePath);
        } catch (IOException e) {
            log.error("Could not create/access directory: {}", directoryPath, e);
        }
    }
}