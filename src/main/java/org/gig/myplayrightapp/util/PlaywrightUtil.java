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

    // Thread-local shared browser — used by mass runners to avoid spawning
    // a new Chromium subprocess per test. Each test still gets an isolated BrowserContext.
    private final ThreadLocal<Playwright> sharedPlaywright = new ThreadLocal<>();
    private final ThreadLocal<Browser>   sharedBrowser    = new ThreadLocal<>();

    /**
     * Opens a shared Playwright browser for the current thread.
     * While open, all {@link #withPage} calls reuse this browser
     * (new isolated context per call) instead of spawning a fresh process each time.
     * Must always be paired with {@link #closeSharedBrowser()} in a finally block.
     */
    public void openSharedBrowser() {
        Playwright pw = Playwright.create();
        Browser br = pw.chromium().launch(new BrowserType.LaunchOptions().setHeadless(headless));
        sharedPlaywright.set(pw);
        sharedBrowser.set(br);
        log.info("Shared browser opened for thread [{}]", Thread.currentThread().getName());
    }

    /**
     * Closes the shared browser opened by {@link #openSharedBrowser()}.
     */
    public void closeSharedBrowser() {
        Browser br = sharedBrowser.get();
        Playwright pw = sharedPlaywright.get();
        if (br != null) { br.close();    sharedBrowser.remove(); }
        if (pw != null) { pw.close();    sharedPlaywright.remove(); }
        log.info("Shared browser closed for thread [{}]", Thread.currentThread().getName());
    }

    // ── withPage overloads ────────────────────────────────────────────────────

    public <T> T withPage(PageAction<T> action) throws Exception {
        return withPage(new BrowserType.LaunchOptions(), null, action);
    }

    public <T> T withPage(BrowserType.LaunchOptions launchOptions, PageAction<T> action) throws Exception {
        return withPage(launchOptions, null, action);
    }

    public <T> T withPage(BrowserType.LaunchOptions launchOptions,
                          Browser.NewContextOptions contextOptions,
                          PageAction<T> action) throws Exception {

        Browser shared = sharedBrowser.get();
        if (shared != null) {
            // Mass-runner mode: reuse the shared browser, isolate via a new context
            BrowserContext context = contextOptions != null
                    ? shared.newContext(contextOptions)
                    : shared.newContext();
            Page page = context.newPage();
            try {
                return action.execute(page);
            } finally {
                context.close();
            }
        }

        // Individual-run mode: full lifecycle per call
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
