package org.gig.myplayrightapp.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.gig.myplayrightapp.enums.Brand;
import org.gig.myplayrightapp.model.BrandContext;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UseBrandAspect {

    @Around("@annotation(useBrand)")
    public Object setBrandContext(ProceedingJoinPoint pjp, UseBrand useBrand) throws Throwable {
        try {
            Brand brand = useBrand.value();
            BrandContext.set(brand);
            return pjp.proceed();
        } finally {
            BrandContext.clear();
        }
    }
}
