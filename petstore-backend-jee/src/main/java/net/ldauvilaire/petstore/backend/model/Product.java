package net.ldauvilaire.petstore.backend.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Laurent Dauvilaire
 */
@Entity
@Cacheable
@SequenceGenerator(name="PRODUCT_SEQ", sequenceName="product_seq")
@NamedQueries({
	@NamedQuery(name=Product.ALL, query="select p from Product p"),
	@NamedQuery(name=Product.ALL_IN_CATEGORY, query="select distinct p from Product p left join fetch p.category order by p.id")
})
public class Product {

	// ======================================
	// = Constants =
	// ======================================
	public static final String ALL = "net.ldauvilaire.petstore.backend.model.Product.ALL";
	public static final String ALL_IN_CATEGORY = "net.ldauvilaire.petstore.backend.model.Product.ALL_IN_CATEGORY";

	// ======================================
	// = Attributes =
	// ======================================

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY, generator="PRODUCT_SEQ")
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

	@ManyToOne(cascade = CascadeType.PERSIST)
/*
	@JoinColumn(name = "categoryId",
	            nullable = false,
	            foreignKey=@ForeignKey(name="fk_product_category"))
*/
	@JoinColumn(
		name = "categoryId",
		nullable = false,
		foreignKey = @ForeignKey(
			name="fk_product_category",
			foreignKeyDefinition = "FOREIGN KEY (categoryId) REFERENCES CATEGORY (id)"
		)
	)
	private Category category;

	// ======================================
	// = Constructors =
	// ======================================
	public Product() {
	}
	public Product(String name, String description, Category category) {
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
	public void setId(Long id) {
		this.id = id;
	}

	public int getVersion() {
		return this.version;
	}
	public void setVersion(int version) {
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

	public Category getCategory() {
		return this.category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
}
