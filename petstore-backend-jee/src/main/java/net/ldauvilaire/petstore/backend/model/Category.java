package net.ldauvilaire.petstore.backend.model;

import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Laurent Dauvilaire
 */
@Entity
@Cacheable
@SequenceGenerator(name="CATEGORY_SEQ", sequenceName="category_seq")
public class Category {

	// ======================================
	// = Attributes =
	// ======================================

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY, generator="CATEGORY_SEQ")
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Version
	@Column(name = "version")
	private int version;

	@Column(length = 30, nullable = false)
	@NotNull
	@Size(min = 1, max = 30)
	private String name;

	@Column(name="description", length = 3000, nullable = false)
	@NotNull
	@Size(max = 3000)
	private String description;

	@OneToMany(mappedBy="category")
	private List<Product> products;

	// ======================================
	// = Constructors =
	// ======================================
	public Category() {
	}
	public Category(String name, String description) {
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

	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}
}
