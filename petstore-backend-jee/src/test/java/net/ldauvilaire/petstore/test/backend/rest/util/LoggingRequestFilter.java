package net.ldauvilaire.petstore.test.backend.rest.util;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Laurent Dauvilaire
 */
public class LoggingRequestFilter implements ClientRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(LoggingRequestFilter.class);

	@Override
	public void filter(ClientRequestContext requestContext) throws IOException {
		URI requestUri = requestContext.getUri();
		String requestMethod = requestContext.getMethod();
		Object entity = requestContext.getEntity();
		if (entity == null) {
			logger.info("JAX-RS Request: [{} {}].", requestMethod, requestUri.toString());
		} else {
			logger.info("JAX-RS Request: [{} {}]\n[{}].", requestMethod, requestUri.toString(), entity.toString());
		}
	}
}
