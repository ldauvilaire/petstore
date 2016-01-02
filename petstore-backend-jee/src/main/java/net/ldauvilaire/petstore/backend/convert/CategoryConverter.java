package net.ldauvilaire.petstore.backend.convert;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import net.ldauvilaire.petstore.backend.dto.CategoryDTO;
import net.ldauvilaire.petstore.backend.model.Category;

/**
 * @author Laurent Dauvilaire
 */
public class CategoryConverter {

	public Category toModel(CategoryDTO dto) {
		Category model = null;
		if (dto != null) {
			model = new Category();
			{
				model.setId(dto.getId());
				model.setVersion(dto.getVersion());
				model.setName(dto.getName());
				model.setDescription(dto.getDescription());
			}
		}
		return model;
	}

	public CategoryDTO toDTO(Category model) {
		CategoryDTO dto = null;
		if (model != null) {
			dto = new CategoryDTO();
			{
				dto.setId(model.getId());
				dto.setVersion(model.getVersion());
				dto.setName(model.getName());
				dto.setDescription(model.getDescription());
			}
		}
		return dto;
	}

	public static JsonObject toJson(CategoryDTO dto) {
		JsonObject json = null;
		if (dto != null) {
			JsonObjectBuilder builder = Json.createObjectBuilder()
					.add("id", dto.getId())
					.add("version", dto.getVersion())
					.add("name", dto.getName())
					.add("description", dto.getDescription());
			json = builder.build();
		}
		return json;
	}

	public static JsonObject toJson(Category model) {
		JsonObject json = null;
		if (model != null) {
			JsonObjectBuilder builder = Json.createObjectBuilder()
					.add("id", model.getId())
					.add("version", model.getVersion())
					.add("name", model.getName())
					.add("description", model.getDescription());
			json = builder.build();
		}
		return json;
	}
}
