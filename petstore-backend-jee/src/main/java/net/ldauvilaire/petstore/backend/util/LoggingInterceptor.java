package net.ldauvilaire.petstore.backend.util;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Laurent Dauvilaire
 */
@Loggable
@Interceptor
public class LoggingInterceptor {

	// ======================================
	// =             Attributes             =
	// ======================================
	private transient Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

	// ======================================
	// =          Business methods          =
	// ======================================

	@AroundInvoke
	private Object intercept(InvocationContext ic) throws Exception {
		logger.info(">>> " + ic.getTarget().getClass().getName() + "-" + ic.getMethod().getName());
		try {
			return ic.proceed();
		} finally {
			logger.info("<<< " + ic.getTarget().getClass().getName() + "-" + ic.getMethod().getName());
		}
	}
}
