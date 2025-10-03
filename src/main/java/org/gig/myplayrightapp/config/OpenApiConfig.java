package org.gig.myplayrightapp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
                                .url("https://opensource.org/licenses/MIT")));
    }
}
