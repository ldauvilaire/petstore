package net.ldauvilaire.petstore.backend.facade;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import net.ldauvilaire.petstore.backend.model.Product;
import net.ldauvilaire.petstore.backend.service.ProductService;

@Stateless(name="PetstoreFacade")
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class PetstoreFacadeImpl implements PetstoreFacade {

	@Inject
	private ProductService productService;

	/* (non-Javadoc)
	 * @see net.ldauvilaire.petstore.backend.facade.PetstoreFacade#createProduct(net.ldauvilaire.petstore.backend.model.Product)
	 */
	@Override
	public Product createProduct(Product product) {
		return productService.create(product);
	}

	/* (non-Javadoc)
	 * @see net.ldauvilaire.petstore.backend.facade.PetstoreFacade#findProductById(java.lang.Long)
	 */
	@Override
	public Product findProductById(Long id) {
		return productService.findById(id);
	}

	/* (non-Javadoc)
	 * @see net.ldauvilaire.petstore.backend.facade.PetstoreFacade#deleteProductById(java.lang.Long)
	 */
	@Override
	public boolean deleteProductById(Long id) {
		return productService.deleteById(id);
	}

	/* (non-Javadoc)
	 * @see net.ldauvilaire.petstore.backend.facade.PetstoreFacade#listAllProducts(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<Product> listAllProducts(Integer startPosition, Integer maxResult) {
		return productService.listAll(startPosition, maxResult);
	}

	/* (non-Javadoc)
	 * @see net.ldauvilaire.petstore.backend.facade.PetstoreFacade#updateProduct(net.ldauvilaire.petstore.backend.model.Product)
	 */
	@Override
	public void updateProduct(Product product) {
		productService.update(product);
	}
}
