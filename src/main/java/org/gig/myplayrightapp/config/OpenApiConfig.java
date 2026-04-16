package org.gig.myplayrightapp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("GIG Automated Tests API")
                        .version("1.0.0")
                        .description("API for triggering Playwright test cases through HTTP endpoints.")
                        .contact(new Contact()
                                .name("QA Automation GIG Team")
                                .email("gabriel.vendramini@gig-ext.com")
                                .url("https://github.com/gvendramini00/playwright-app"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .tags(List.of(
                        new Tag()
                                .name("Alira — Core Tests")
                                .description("Login and player profile navigation tests for the Alira Back Office"),
                        new Tag()
                                .name("Alira — Games Tab")
                                .description("Navigation tests for the Games tab in the Alira Back Office (Rooms, Providers, Themes & Tags, Exchange Profile)"),
                        new Tag()
                                .name("Alira — Website Tab")
                                .description("Navigation tests for the Website tab in the Alira Back Office (CMS, Configuration)"),
                        new Tag()
                                .name("Alira — Marketing Tab")
                                .description("Tests for the Marketing tab in the Alira Back Office — covers Dashboard, Deposit Promotions (read, create, edit, delete)"),
                        new Tag()
                                .name("Alira — Widgets")
                                .description("Tests for Player Profile widgets in the Alira Back Office — covers Attached Documentation (add, edit, delete)"),
                        new Tag()
                                .name("Alira — Staging Tests")
                                .description("Tests for the Alira Staging Back Office (gms-staging.pre.tecnalis.com) — separate DB at 10.64.134.11"),
                        new Tag()
                                .name("Alira — Mass Test Runner")
                                .description("Runs all 17 Alira test cases in sequence and returns a downloadable Excel report with results and screenshot references"),
                        new Tag()
                                .name("Casino Gran Madrid — Registration Tests")
                                .description("Automated tests for the Casino Gran Madrid pre-production site — covers site access, registration flows and validation"),
                        new Tag()
                                .name("Golden Park PT — Registration Tests")
                                .description("Automated tests for the Golden Park Portugal dev site — covers manual registration and duplicate NIF validation")
                ));
    }
}
