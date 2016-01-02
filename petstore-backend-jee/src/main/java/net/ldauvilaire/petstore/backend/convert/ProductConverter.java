package net.ldauvilaire.petstore.backend.convert;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import net.ldauvilaire.petstore.backend.dto.ProductDTO;
import net.ldauvilaire.petstore.backend.model.Product;

/**
 * @author Laurent Dauvilaire
 */
public class ProductConverter {

	@Inject
	CategoryConverter categoryConverter;

	public Product toModel(ProductDTO dto) {
		Product model = null;
		if (dto != null) {
			model = new Product();
			model.setId(dto.getId());
			model.setVersion(dto.getVersion());
			model.setName(dto.getName());
			model.setDescription(dto.getDescription());
			model.setCategory(categoryConverter.toModel(dto.getCategory()));
		}
		return model;
	}

	public ProductDTO toDTO(Product model) {
		ProductDTO dto = null;
		if (model != null) {
			dto = new ProductDTO();
			dto.setId(model.getId());
			dto.setVersion(model.getVersion());
			dto.setName(model.getName());
			dto.setDescription(model.getDescription());
			dto.setCategory(categoryConverter.toDTO(model.getCategory()));
		}
		return dto;
	}

	public List<ProductDTO> toDTO(List<Product> models) {
		List<ProductDTO> dtos = null;
		if (models != null) {
			dtos = new ArrayList<ProductDTO>(models.size());
			for (Product model : models) {
				dtos.add(toDTO(model));
			}
		}
		return dtos;
	}

	public static JsonObject toJson(ProductDTO dto) {
		JsonObject json = null;
		if (dto != null) {
			JsonObjectBuilder builder = Json.createObjectBuilder()
					.add("id", dto.getId())
					.add("version", dto.getVersion())
					.add("name", dto.getName())
					.add("description", dto.getDescription())
					.add("category", CategoryConverter.toJson(dto.getCategory()));
			json = builder.build();
		}
		return json;
	}

	public static JsonObject toJson(Product model) {
		JsonObject json = null;
		if (model != null) {
			JsonObjectBuilder builder = Json.createObjectBuilder()
					.add("id", model.getId())
					.add("version", model.getVersion())
					.add("name", model.getName())
					.add("description", model.getDescription())
					.add("category", CategoryConverter.toJson(model.getCategory()));
			json = builder.build();
		}
		return json;
	}
}
