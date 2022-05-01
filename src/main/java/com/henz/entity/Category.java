package com.henz.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.henz.enums.CategoryText;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="category")
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="category_id")
	private Long id;
	
	@Column(nullable = false, unique = true)
	@Enumerated(EnumType.STRING)
	private CategoryText category;
	
	@OneToMany(mappedBy = "category") //**toMany is LAZY fetching by default
	private List<Product> products = new ArrayList<Product>();
	
	public void addProduct(Product p) {
		
		if(this.products == null) {
			this.products = this.getProducts();
		}
		
		this.products.add(p);
	}
}
