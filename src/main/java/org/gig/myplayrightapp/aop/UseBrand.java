package org.gig.myplayrightapp.aop;

import org.gig.myplayrightapp.enums.Brand;
import java.lang.annotation.*;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UseBrand {
    Brand value();
}
