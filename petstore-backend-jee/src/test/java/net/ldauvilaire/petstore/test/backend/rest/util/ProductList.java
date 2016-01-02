package net.ldauvilaire.petstore.test.backend.rest.util;

import java.util.List;

import net.ldauvilaire.petstore.backend.dto.ProductDTO;

public class ProductList extends XmlListWrapper<ProductDTO> {

	public ProductList() {
		super();
	}
	public ProductList(List<ProductDTO> items) {
		super(items);
	}
}
