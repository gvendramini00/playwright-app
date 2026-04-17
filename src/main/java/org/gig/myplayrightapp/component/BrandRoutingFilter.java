package org.gig.myplayrightapp.component;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.gig.myplayrightapp.enums.Brand;
import org.gig.myplayrightapp.model.BrandContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class BrandRoutingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String uri = req.getRequestURI();

        try {
            if (uri.startsWith("/api/test/")) {
                Brand brand = Brand.fromUri(uri)
                        .orElseThrow(() -> new IllegalStateException(
                                "No brand mapping found for URI: " + uri));
                BrandContext.set(brand);
            }
            chain.doFilter(request, response);
        } finally {
            BrandContext.clear();
        }
    }
}
