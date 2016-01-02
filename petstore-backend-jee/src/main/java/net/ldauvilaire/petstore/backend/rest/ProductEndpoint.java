package net.ldauvilaire.petstore.backend.rest;

import java.net.URI;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;

import javax.ws.rs.core.UriBuilder;

import net.ldauvilaire.petstore.backend.convert.ProductConverter;
import net.ldauvilaire.petstore.backend.dto.ProductDTO;
import net.ldauvilaire.petstore.backend.facade.PetstoreFacade;
import net.ldauvilaire.petstore.backend.model.Product;
import net.ldauvilaire.petstore.backend.util.Loggable;

/**
 * @author Laurent Dauvilaire
 */
@Path("/products")
@Loggable
public class ProductEndpoint {

	@Inject
	private ProductConverter converter;

	@Inject
	private PetstoreFacade facade;

	@Inject
	private Logger logger;

	// ======================================
	// =          Business methods          =
	// ======================================

	@POST
	@Consumes( {MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Produces( {MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response create(ProductDTO product) {

		Product model = converter.toModel(product);
		model = facade.createProduct(model);
		long productId = model.getId();

		URI location = UriBuilder.fromResource(ProductEndpoint.class)
				.path(String.valueOf(productId))
				.build();
		return Response.created(location).build();
	}

	@DELETE
	@Path("/{id:[0-9][0-9]*}")
	public Response deleteById(
			@PathParam("id") Long id) {

		boolean success = facade.deleteProductById(id);
		if (! success) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.noContent().build();
	}

	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces( {MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response findById(
			@Context HttpHeaders headers,
			@PathParam("id") Long id) {

		Product model = facade.findProductById(id);
		ProductDTO product = converter.toDTO(model);

		if (product == null) {
			if (logger.isInfoEnabled()) {
				List<MediaType> mediaTypes = headers.getAcceptableMediaTypes();
				logger.info("Returning a Status Not Found for {}", mediaTypes);
			}
			return Response.status(Status.NOT_FOUND).build();
		}
		if (logger.isInfoEnabled()) {
			List<MediaType> mediaTypes = headers.getAcceptableMediaTypes();
			logger.info("Returning a Product for {}", mediaTypes);
		}
		return Response.ok(product).build();
	}

	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public List<ProductDTO> listAll(
			@Context HttpHeaders headers,
			@QueryParam("start") Integer startPosition,
			@QueryParam("max") Integer maxResult) {

		List<Product> products = facade.listAllProducts(startPosition, maxResult);
		List<ProductDTO> results = converter.toDTO(products);

		if (logger.isInfoEnabled()) {
			List<MediaType> mediaTypes = headers.getAcceptableMediaTypes();
			logger.info("Returning a list of {} products for {}", ((results == null) ? 0 : results.size()), mediaTypes);
		}
		return results;
	}

	@PUT
	@Path("/{id:[0-9][0-9]*}")
	@Consumes( {MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response update(ProductDTO product) {
		Product model = converter.toModel(product);
		facade.updateProduct(model);
		return Response.noContent().build();
	}
}
