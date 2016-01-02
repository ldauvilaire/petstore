package net.ldauvilaire.petstore.backend.service;

import java.util.List;

import net.ldauvilaire.petstore.backend.model.Product;

/**
 * @author Laurent Dauvilaire
 */
public interface ProductService {

	public Product create(Product product);
	public Product findById(Long id);
	public List<Product> listAll(Integer startPosition, Integer maxResult);
	public void update(Product product);
	public boolean deleteById(Long id);
	public void refresh(Product product);
}
