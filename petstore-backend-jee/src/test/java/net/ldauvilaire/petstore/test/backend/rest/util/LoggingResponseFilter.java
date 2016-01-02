package net.ldauvilaire.petstore.test.backend.rest.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Laurent Dauvilaire
 */
public class LoggingResponseFilter implements ClientResponseFilter {

	private static final Logger logger = LoggerFactory.getLogger(LoggingResponseFilter.class);

	@Override
	public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
		int status = responseContext.getStatus();
		MediaType mediaType = responseContext.getMediaType();
		MultivaluedMap<String, String> headers = responseContext.getHeaders();
		logger.info("JAX-RS Response:");
		logger.info("\tstatus: [{}]", status);
		logger.info("\ttype: [{}]", mediaType);
		if (headers == null) {
			logger.info("\theaders is null.");
		} else {
			logger.info("\theaders: {} entries.", headers.size());
			for (Map.Entry<String, List<String>> headerEntry : headers.entrySet()) {
				String headerKey = headerEntry.getKey();
				List<String> headerValues = headerEntry.getValue();
				StringBuilder builder = new StringBuilder();
				if (headerValues == null) {
					logger.info("\t\theader: [{}] is null.", headerKey);
				} else {
					boolean first = true;
					for (String headerValue : headerValues) {
						if (! first) {
							builder.append(", ");
						}
						first = false;
						builder.append(headerValue);
					}
					logger.info("\t\theader: [{}] = [{}].", headerKey, builder.toString());
				}
			}
		}
		boolean hasEntity = responseContext.hasEntity();
		if (hasEntity) {
//			logInputStream("JAX-RS Response: ", responseContext.getEntityStream(), true);
			EntityTag entityTag = responseContext.getEntityTag();
			if (entityTag == null) {
				logger.info("JAX-RS Response: has Entity but no Tag.");
			} else {
				String entityTagValue = entityTag.getValue();
				logger.info("JAX-RS Response: has Entity with Tag [{}].", entityTagValue);
			}
		} else {
			logger.info("JAX-RS Response: has no Entity.");
		}
	}

	private void logInputStream(String prefix, InputStream is, boolean reset) throws IOException {
		StringBuilder builder = new StringBuilder();
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line;
			boolean first = true;
			while ((line = reader.readLine()) != null) {
				if (! first) {
					builder.append("\n");
				}
				first = false;
				builder.append(line);
			}
			if (reset) {
				is.reset();
			}
		}
		logger.info("{}[{}].", prefix, builder.toString());
	}
}
