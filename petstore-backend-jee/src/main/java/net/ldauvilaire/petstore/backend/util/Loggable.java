package net.ldauvilaire.petstore.backend.util;

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.*;

/**
 * @author Laurent Dauvilaire
 */
@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
@Documented
public @interface Loggable {
}
