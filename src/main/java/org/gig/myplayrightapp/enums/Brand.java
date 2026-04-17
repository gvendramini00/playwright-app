package org.gig.myplayrightapp.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum Brand {

    CGM       ("/api/test/cgm"),
    GP_PT     ("/api/test/gp-pt"),
    STAGING   ("/api/test/alira-staging"),
    ALIRA     ("/api/test/alira");

    private final String urlPrefix;

    Brand(String urlPrefix) {
        this.urlPrefix = urlPrefix;
    }

    public static Optional<Brand> fromUri(String uri) {
        return Arrays.stream(values())
                .filter(b -> uri.startsWith(b.urlPrefix))
                .findFirst();
    }
}

