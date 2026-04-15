package org.gig.myplayrightapp.util;

import com.microsoft.playwright.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PlaywrightUtil {

    @Value("${playwright.headless:true}")
    private boolean headless;

    @FunctionalInterface
    public interface PageAction<T> {
        T execute(Page page) throws Exception;
    }

    public <T> T withPage(PageAction<T> action) throws Exception {
        return withPage(new BrowserType.LaunchOptions(), null, action);
    }

    public <T> T withPage(BrowserType.LaunchOptions launchOptions, PageAction<T> action) throws Exception {
        return withPage(launchOptions, null, action);
    }

    public <T> T withPage(BrowserType.LaunchOptions launchOptions, Browser.NewContextOptions contextOptions, PageAction<T> action) throws Exception {
        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.chromium().launch(launchOptions.setHeadless(headless))) {
            BrowserContext context = contextOptions != null
                    ? browser.newContext(contextOptions)
                    : browser.newContext();
            Page page = context.newPage();
            return action.execute(page);
        }
    }
}
