package net.ldauvilaire.petstore.backend.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Laurent Dauvilaire
 */
@XmlRootElement
public class CategoryDTO implements Serializable {

	private static final long serialVersionUID = -1023606229176511295L;

	private Long id;
	private int version;
	private String name;
	private String description;

	// ======================================
	// = Constructors =
	// ======================================
	public CategoryDTO() {
	}
	public CategoryDTO(String name, String description) {
		this.name = name;
		this.description = description;
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
}
