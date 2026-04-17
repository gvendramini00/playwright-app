package org.gig.myplayrightapp.config;

import org.gig.myplayrightapp.util.AliraLoginUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoginUtilConfig {

    @Bean("aliraLoginUtil")
    public AliraLoginUtil aliraLoginUtil(
            @Value("${alira.username}") String username,
            @Value("${alira.password}") String password,
            @Value("${alira.login-url}") String loginUrl) {
        return new AliraLoginUtil(username, password, loginUrl, "Pre");
    }

    @Bean("aliraStagingLoginUtil")
    public AliraLoginUtil aliraStagingLoginUtil(
            @Value("${alira.staging.username}") String username,
            @Value("${alira.staging.password}") String password,
            @Value("${alira.staging.login-url}") String loginUrl) {
        return new AliraLoginUtil(username, password, loginUrl, "Staging");
    }
}
