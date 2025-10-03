package org.gig.myplayrightapp.service;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gig.myplayrightapp.aop.UseBrand;
import org.gig.myplayrightapp.dto.InsertPlayerDTO;
import org.gig.myplayrightapp.dto.InsertPlayerGpPtDTO;
import org.gig.myplayrightapp.enums.Brand;
import org.gig.myplayrightapp.util.RegistrationDataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoldenParkPtTestService {

    private final PlayerService playerService;

    private static final String BASE_URL = "https://goldenpark-pt.dev.tecnalis.com/";
    private static final String SHOTS_DIR = "screenshots";

    @UseBrand(Brand.GP_PT)
    public String runManualRegistrationTest() {
        InsertPlayerGpPtDTO dto = RegistrationDataUtils.generateUniquePortugalInsertPlayerDTO(playerService);

        try (Playwright pw = Playwright.create()) {
            Files.createDirectories(Paths.get(SHOTS_DIR));

            Browser browser = pw.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            BrowserContext ctx = browser.newContext();
            Page page = ctx.newPage();

            page.navigate(BASE_URL);

            page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(Pattern.compile("Reg(í|i)strate|Registar|Registar-se", Pattern.CASE_INSENSITIVE))).click();

            // Step 1 - account
            page.locator("#user").fill(dto.alias());
            page.locator("#e_mail").fill(dto.email());
            page.locator("#re_mail").fill(dto.email());
            page.locator("#pwdField").fill(dto.password());
            page.locator("#c18old").evaluate("checkbox => checkbox.checked = true");
            page.locator("#step1").getByText(Pattern.compile("CONTINUAR", Pattern.CASE_INSENSITIVE)).click();

            // Step 2 - identification (PT)
            if (page.locator("#docType").isVisible() && dto.docTypeValue() != null) {
                page.locator("#docType").selectOption(dto.docTypeValue());
            }
            // NIF -> #cuitCuil
            if (page.locator("#cuitCuil").isVisible() && dto.cuitCuil() != null) {
                page.locator("#cuitCuil").fill(dto.cuitCuil());
            }
            // CC -> #nationalId
            page.locator("#nationalId").fill(dto.nationalId());

            page.locator("#name").fill(dto.firstName());
            page.locator("#middlename").fill(dto.middleName());
            page.getByText("Homem").click();
            page.locator("#step2").getByText(Pattern.compile("CONTINUAR", Pattern.CASE_INSENSITIVE)).click();

            // Step 3 (birth / extras if present)
            page.locator("#step3").getByText(Pattern.compile("CONTINUAR", Pattern.CASE_INSENSITIVE)).click();

            // Address / phone
            page.locator("#address").fill(dto.address());
            page.locator("#state").selectOption(String.valueOf(dto.state()));
            page.locator("#city_select").selectOption(dto.city());
            page.locator("#zipcode_select").selectOption(dto.zipCode());

            // Phone country (ensure Portugal stays selected)
            if (dto.phoneCountryLabel() != null) {
                page.getByRole(AriaRole.COMBOBOX,
                                new Page.GetByRoleOptions().setName(Pattern.compile("Portugal|País|Country", Pattern.CASE_INSENSITIVE)))
                        .click();
                page.getByText(Pattern.compile(dto.phoneCountryLabel(), Pattern.CASE_INSENSITIVE)).click();
            }

            // Phone number
            page.getByRole(AriaRole.TEXTBOX,
                            new Page.GetByRoleOptions().setName(Pattern.compile("Telem(ó|o)vel\\*", Pattern.CASE_INSENSITIVE)))
                    .fill(dto.phone());

            String before = page.url();
            page.getByRole(AriaRole.LINK,
                            new Page.GetByRoleOptions().setName(Pattern.compile("COMEÇAR|COMECAR", Pattern.CASE_INSENSITIVE)))
                    .click(new Locator.ClickOptions().setTimeout(10000));
            page.waitForTimeout(2500);

            String shot = SHOTS_DIR + "/gp_register_" + System.currentTimeMillis() + ".png";
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(shot)));

            var errors = page.locator(".error, .text-danger, .invalid-feedback, .form-errors, .ng-binding");
            boolean hasError = false;
            for (int i = 0; i < errors.count(); i++) {
                if (errors.nth(i).isVisible()) {
                    log.warn("⚠️ GP PT registration error: {}", errors.nth(i).innerText().trim());
                    hasError = true;
                    break;
                }
            }

            if (!hasError && !page.url().equals(before)) {
                log.info("✅ GP PT registration submitted for '{}' (email: {}, NIF: {}, CC: {}). Screenshot: {}",
                        dto.alias(), dto.email(), dto.cuitCuil(), dto.nationalId(), shot);
                browser.close();
                return "✅ Golden Park PT registration completed.";
            } else {
                log.warn("❌ GP PT registration did not proceed. Screenshot: {}", shot);
                browser.close();
                return "❌ Golden Park PT registration failed (errors or no navigation).";
            }
        } catch (Exception e) {
            log.error("❌ Error in GP PT registration flow", e);
            return "❌ Golden Park PT registration error: " + e.getMessage();
        }
    }

    @UseBrand(Brand.GP_PT)
    public String runDuplicateNifTest() {
        Optional<InsertPlayerDTO> maybeExisting = playerService.findAnyExistingPlayer();
        if (maybeExisting.isEmpty()) {
            return "⚠️ GP PT duplicate test skipped: no existing player to duplicate.";
        }
        var es = maybeExisting.get();

        try (Playwright pw = Playwright.create()) {
            Files.createDirectories(Paths.get(SHOTS_DIR));

            Browser browser = pw.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            BrowserContext ctx = browser.newContext();
            Page page = ctx.newPage();

            page.navigate(BASE_URL);

            page.getByRole(AriaRole.LINK,
                            new Page.GetByRoleOptions().setName(Pattern.compile("Reg(í|i)strate|Registar|Registar-se", Pattern.CASE_INSENSITIVE)))
                    .click();

            // Minimal required fields to reach the NIF validation
            page.locator("#name").fill(es.firstName());
            page.locator("#middlename").fill(es.middleName());
            page.locator("#surname").fill(es.lastName());
            page.getByText("Homem").click();

            if (page.locator("#docType").isVisible()) page.locator("#docType").selectOption("2");

            // 🔴 NIF -> #cuitCuil (use a known duplicate NIF if possible)
            // For a quick try, reuse es.nationalId(); ideally, fetch/keep a real duplicate NIF from GP DB.
            page.locator("#cuitCuil").fill(es.nationalId());

            page.locator("#step2, #step1, body").getByText(Pattern.compile("CONTINUAR", Pattern.CASE_INSENSITIVE))
                    .first().click();
            page.waitForTimeout(2000);

            Locator alerts = page.locator(".error, .text-danger, .invalid-feedback, .form-errors, .ng-binding");
            for (int i = 0; i < alerts.count(); i++) {
                if (!alerts.nth(i).isVisible()) continue;
                String txt = alerts.nth(i).innerText().toLowerCase(Locale.ROOT);
                if (txt.contains("já está em uso") || txt.contains("ja está em uso") || txt.contains("ya está en uso")) {
                    String shot = SHOTS_DIR + "/gp_duplicate_nif_" + System.currentTimeMillis() + ".png";
                    page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(shot)));
                    log.info("✅ Duplicate NIF detected for user '{}' (NIF attempted: {}). Screenshot: {}",
                            es.alias(), es.nationalId(), shot);
                    browser.close();
                    return "✅ GP PT duplicate test passed: warning shown.";
                }
            }

            String fail = SHOTS_DIR + "/gp_duplicate_nif_no_modal_" + System.currentTimeMillis() + ".png";
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(fail)));
            log.warn("❌ Expected duplicate NIF warning not found for '{}' (NIF attempted: {}). Screenshot: {}",
                    es.alias(), es.nationalId(), fail);
            browser.close();
            return "❌ GP PT duplicate test failed: no warning detected.";

        } catch (Exception e) {
            log.error("❌ Error in GP PT duplicate test", e);
            return "❌ GP PT duplicate test error: " + e.getMessage();
        }
    }

}
