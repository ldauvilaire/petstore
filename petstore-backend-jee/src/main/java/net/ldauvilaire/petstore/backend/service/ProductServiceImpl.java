package net.ldauvilaire.petstore.backend.service;

import java.util.List;

import javax.inject.Inject;

import net.ldauvilaire.petstore.backend.dao.ProductDao;
import net.ldauvilaire.petstore.backend.model.Product;
import net.ldauvilaire.petstore.backend.util.Loggable;

/**
 * @author Laurent Dauvilaire
 */
@Loggable
public class ProductServiceImpl implements ProductService {

	@Inject
	private ProductDao dao;

	/* (non-Javadoc)
	 * @see net.ldauvilaire.petstore.backend.service.ProductService#create(net.ldauvilaire.petstore.backend.model.Product)
	 */
	@Override
	public Product create(Product product) {
		return dao.createAndRefresh(product);
	}

	/* (non-Javadoc)
	 * @see net.ldauvilaire.petstore.backend.service.ProductService#findById(java.lang.Long)
	 */
	@Override
	public Product findById(Long id) {
		return dao.find(Product.class, id);
	}

	/* (non-Javadoc)
	 * @see net.ldauvilaire.petstore.backend.service.ProductService#listAll(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<Product> listAll(Integer startPosition, Integer maxResult) {
		int start = ((startPosition == null) ? 0 : startPosition.intValue());
		int max = ((maxResult == null) ? 0 : maxResult.intValue());
		return dao.findWithNamedQuery(Product.ALL_IN_CATEGORY, start, max);
	}

	/* (non-Javadoc)
	 * @see net.ldauvilaire.petstore.backend.service.ProductService#update(net.ldauvilaire.petstore.backend.model.Product)
	 */
	@Override
	public void update(Product product) {
		dao.update(product);
	}

	/* (non-Javadoc)
	 * @see net.ldauvilaire.petstore.backend.service.ProductService#deleteById(java.lang.Long)
	 */
	@Override
	public boolean deleteById(Long id) {
		return dao.delete(Product.class, id);
	}

	/* (non-Javadoc)
	 * @see net.ldauvilaire.petstore.backend.service.ProductService#refresh(net.ldauvilaire.petstore.backend.model.Product)
	 */
	@Override
	public void refresh(Product product) {
		dao.refresh(product);
	}
}
