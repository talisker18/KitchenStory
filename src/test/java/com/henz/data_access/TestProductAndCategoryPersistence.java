package com.henz.data_access;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import com.henz.KitchenStoryApplication;
import com.henz.entity.Category;
import com.henz.entity.Product;
import com.henz.enums.CategoryText;

import static org.junit.jupiter.api.Assertions.*;

/*
 * 
 * these tests use h2 database to persist data. At the beginning of the tests, no data is stored,
 * so we can for example create a new category and this created category will always have the id 1
 * 
 * */

@SpringBootTest(classes=KitchenStoryApplication.class)
@Transactional
public class TestProductAndCategoryPersistence {
	
	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Test
	@DirtiesContext
	public void throwConstraintViolationExceptionOnDelete() {
		Category c = this.categoryRepository.findById(1L); // BEVERAGES
		
		Product p = new Product();
		p.setDescription("test");
		p.setCategory(c);
		
		this.entityManager.persist(p);
		
		try {
			this.entityManager.remove(c);
		} catch (ConstraintViolationException e) {
			//test will succeed if exception occurs
		}
		
	}
	
	@Test
	@DirtiesContext
	public void testRepositories() {
		//save category
		Category c = this.categoryRepository.findById(1L);
		
		//save product.
		Product p = new Product();
		p.setDescription("test1");
		p.setCategory(c);
		this.productRepository.saveNewProduct(p);
		
		Product p2= new Product();
		p2.setDescription("test2");
		p2.setCategory(c);
		this.productRepository.saveNewProduct(p2);
		
		//here we do not need to load the products of 'c' first because the test is with @Transactional. Without, we would get a lazy load exception, see line 69 of TestOrderPersistence.class
		c.addProduct(p);
		c.addProduct(p2);
		
		//load all products from BEVERAGES category
		List<Product> beverages = c.getProducts();
		assertEquals(p.getDescription(), beverages.get(0).getDescription());
		assertEquals(p2.getDescription(), beverages.get(1).getDescription());
		
		//find product by description
		Optional<Product> pByName1 = this.productRepository.findByDescription("test1");
		assertTrue(pByName1.isPresent());
		
		Optional<Product> pByName2 = this.productRepository.findByDescription("test2");
		assertTrue(pByName2.isPresent());
	}

}
