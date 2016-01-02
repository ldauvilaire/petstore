package net.ldauvilaire.petstore.test.backend.rest;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.ldauvilaire.petstore.backend.dto.CategoryDTO;
import net.ldauvilaire.petstore.backend.dto.ProductDTO;
import net.ldauvilaire.petstore.backend.rest.ProductEndpoint;
import net.ldauvilaire.petstore.test.backend.rest.util.LoggingRequestFilter;
import net.ldauvilaire.petstore.test.backend.rest.util.LoggingResponseFilter;
import net.ldauvilaire.petstore.test.backend.rest.util.XmlListWrapper;

/**
 * @author Laurent Dauvilaire
 */
@RunWith(Arquillian.class)
@RunAsClient
public class ProductEndpointIT {

	private static final Logger logger = LoggerFactory.getLogger(ProductEndpointIT.class);

	@ArquillianResource
	private URI baseURL;

	@Deployment(testable = false)
	public static WebArchive createDeployment() {

		ClassLoader mainClassLoader = ProductEndpoint.class.getClassLoader();
		ClassLoader testClassLoader = ProductEndpointIT.class.getClassLoader();

		File[] runtimeDependencies = Maven.resolver()
			.loadPomFromFile("pom.xml")
			.importRuntimeDependencies()
			.resolve()
			.withTransitivity()
			.asFile();

		File[] loggerDependencies = Maven.resolver()
				.loadPomFromFile("pom.xml")
				.resolve("ch.qos.logback:logback-classic")
				.withTransitivity()
				.asFile();

		WebArchive war = ShrinkWrap.create(WebArchive.class, "test-petstore.war")
				.addPackages(true, "net.ldauvilaire.petstore.backend")
				.addAsLibraries(runtimeDependencies)
				.addAsLibraries(loggerDependencies)
				.addAsResource("init_db.sql");

		if (mainClassLoader.getResource("META-INF/beans.xml") != null) {
			war.addAsResource("META-INF/beans.xml");
		}
		if (testClassLoader.getResource("META-INF/test-persistence.xml") != null) {
			war.addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml");
		}

		if (mainClassLoader.getResource("logback.xml") != null) {
			war.addAsResource(mainClassLoader.getResource("logback.xml"), "logback.xml");
		}

		if (testClassLoader.getResource("META-INF/jboss-deployment-structure.xml") != null) {
			war.addAsManifestResource("META-INF/test-wildfly-ds.xml", "test-wildfly-ds.xml");
			war.addAsManifestResource("META-INF/jboss-deployment-structure.xml", "jboss-deployment-structure.xml");
		} else if (testClassLoader.getResource("WEB-INF/glassfish-resources.xml") != null) {
			war.addAsWebInfResource("WEB-INF/glassfish-resources.xml", "glassfish-resources.xml");
		}

		String deploymentContent = war.toString(true);
		logger.info("Test Deployment Content: {}", deploymentContent);

		return war;
	}

	@Test
	@RunAsClient
	public void getProductsJSONTest() {

		Client client = getClient();
		WebTarget target = client.target(baseURL).path("rest").path("products");

		//-- Call Remote Resource --
		Response response = target.request(MediaType.APPLICATION_JSON).get();

		Assert.assertNotNull(response);
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		Assert.assertTrue(response.hasEntity());

		String responseBody = response.readEntity(String.class);
		client.close();

		logger.debug("--- JAX-RS Response Body: [{}] ---.", responseBody);

		List<ProductDTO> products = fromJsonList(responseBody);
		Assert.assertNotNull(products);

		logProducts(MediaType.APPLICATION_JSON_TYPE, products);
		Assert.assertEquals(16, products.size());
	}

	@Test
	@RunAsClient
	public void getProductsXMLTest() {

		Client client = getClient();
		WebTarget target = client.target(baseURL).path("rest").path("products");

		//-- Call Remote Resource --
		Response response = target.request(MediaType.APPLICATION_XML).get();

		Assert.assertNotNull(response);
		Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		Assert.assertTrue(response.hasEntity());

		String responseBody = response.readEntity(String.class);
		client.close();

		logger.debug("--- JAX-RS Response Body: [{}] ---.", responseBody);

		List<ProductDTO> products = fromXmlList(responseBody, ProductDTO.class);
		Assert.assertNotNull(products);

		logProducts(MediaType.APPLICATION_XML_TYPE, products);

		Assert.assertEquals(16, products.size());
	}

	@Ignore
	private static Client getClient() {
		Client client = ClientBuilder.newClient();
		{
			client.register(new LoggingRequestFilter());
			client.register(new LoggingResponseFilter());

			Configuration configuration = client.getConfiguration();
			if (configuration != null) {
				Map<String, Object> properties = configuration.getProperties();
				if (properties != null) {
//					properties.put("expandEntityReferences", true);
//					properties.put("enableSecureProcessingFeature", false);
//					properties.put("disableDTDs", true);
				} else {
					logger.warn("JAX-RS Client has no Configuration Properties");
				}
			} else {
				logger.warn("JAX-RS Client has no Configuration");
			}
		}
		return client;
	}

	@Ignore
	private static void logProducts(MediaType mediaType, List<ProductDTO> products) {
		if (products == null) {
			logger.info("List of products is null.");
		} else if (products.isEmpty()) {
			logger.info("List of products is empty.");
		} else {

			if (MediaType.APPLICATION_JSON_TYPE.equals(mediaType)) {

				ObjectMapper jacksonObjectMapper = new ObjectMapper();
				jacksonObjectMapper.enable(SerializationFeature.INDENT_OUTPUT);
				try {
					String json = jacksonObjectMapper
							.writer()
							.withDefaultPrettyPrinter()
							.writeValueAsString(products);
					logger.info("products: {}.", json);
				} catch (JsonProcessingException ex) {
					logger.error("a JsonProcessingException has occured", ex);
				}

			} else if (MediaType.APPLICATION_XML_TYPE.equals(mediaType)) {

				try {
					JAXBContext jaxbContext = JAXBContext.newInstance(ProductDTO.class, CategoryDTO.class, XmlListWrapper.class);
					Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
					jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
					jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

					StringWriter writer = new StringWriter();
					marshal(jaxbMarshaller, products, "productDTOs", writer);
					logger.info("products: {}", writer.toString());

				} catch (JAXBException ex) {
					logger.error("a JAXBException has occured", ex);
				}

			} else {
				int nbProducts = products.size();
				for (int i=0; i<nbProducts; i++) {
					ProductDTO product = products.get(i);
					logger.info("product[{}]: id={}.", i, product.getId());
					logger.info("product[{}]: version={}.", i, product.getVersion());
					logger.info("product[{}]: name={}.", i, product.getName());
					logger.info("product[{}]: description={}.", i, product.getDescription());
					CategoryDTO category = product.getCategory();
					if (category == null) {
						logger.info("product[{}]: category is null.", i);
					} else {
						logger.info("product[{}]: category.id={}.", i, category.getId());
						logger.info("product[{}]: category.version={}.", i, category.getVersion());
						logger.info("product[{}]: category.name={}.", i, category.getName());
					}
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	@Ignore
	private static <T> void marshal(Marshaller marshaller, List<T> list, String name, Writer writer) throws JAXBException {
		QName qName = new QName(name);
		XmlListWrapper<T> wrapper = new XmlListWrapper<T>(list);
		JAXBElement<XmlListWrapper> jaxbElement = new JAXBElement<XmlListWrapper>(qName, XmlListWrapper.class, wrapper);
		marshaller.marshal(jaxbElement, writer);
	}

	@Ignore
	private static <T> List<T> fromJsonList(String json) {
		List<T> list = null;
		if ((json != null) && (! json.trim().isEmpty())) {
			Type listType = new TypeToken<List<T>>(){}.getType();
			list = new Gson().fromJson(json, listType);
		}
		return list;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Ignore
	private static <T> List<T> fromXmlList(String xml, Class<T> declaredType) {
		List<T> list = null;
		if ((xml != null) && (! xml.trim().isEmpty())) {
			try {
				JAXBContext jaxbContext = JAXBContext.newInstance(declaredType, XmlListWrapper.class);
				Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
				StreamSource src = new StreamSource(new StringReader(xml));
				JAXBElement<XmlListWrapper> jaxbElement = unmarshaller.unmarshal(src, XmlListWrapper.class);
				XmlListWrapper element = ((jaxbElement == null) ? null : jaxbElement.getValue());
				if (element != null) {
					XmlListWrapper<T> typedElement = (XmlListWrapper<T>) element;
					list = typedElement.getItems();
				}
			} catch (JAXBException ex) {
				logger.warn("a JAXBException has occured while parsing [" + xml + "].", ex);
			}
		}
		return list;
	}
}
