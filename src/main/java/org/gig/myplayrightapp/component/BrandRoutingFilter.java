package org.gig.myplayrightapp.component;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.gig.myplayrightapp.enums.Brand;
import org.gig.myplayrightapp.model.BrandContext;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class BrandRoutingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String uri = req.getRequestURI();

        try {
            if (uri.startsWith("/api/test/gp-pt")) {
                BrandContext.set(Brand.GP_PT);
            } else {
                // default to CGM (or add more brands as you grow)
                BrandContext.set(Brand.CGM);
            }
            chain.doFilter(request, response);
        } finally {
            BrandContext.clear(); // avoid leakage across threads
        }
    }
}
