package net.ldauvilaire.petstore.backend.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Laurent Dauvilaire
 */
@XmlRootElement
public class ProductDTO implements Serializable {

	private static final long serialVersionUID = 6703746494424372546L;

	private Long id;
	private int version;
	private String name;
	private String description;
	private CategoryDTO category;

	// ======================================
	// = Constructors =
	// ======================================
	public ProductDTO() {
	}
	public ProductDTO(String name, String description, CategoryDTO category) {
		this.name = name;
		this.description = description;
		this.category = category;
	}

	// ======================================
	// = Getters & setters =
	// ======================================
	public Long getId() {
		return this.id;
	}
	public void setId(final Long id) {
		this.id = id;
	}

	public int getVersion() {
		return this.version;
	}
	public void setVersion(final int version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public CategoryDTO getCategory() {
		return this.category;
	}
	public void setCategory(final CategoryDTO category) {
		this.category = category;
	}
}
