package net.ldauvilaire.petstore.backend.facade;

import java.util.List;

import javax.ejb.Local;

import net.ldauvilaire.petstore.backend.model.Product;

@Local
public interface PetstoreFacade {

	public Product createProduct(Product product);
	public Product findProductById(Long id);
	public boolean deleteProductById(Long id);
	public List<Product> listAllProducts(Integer startPosition, Integer maxResult);
	public void updateProduct(Product product);
}
