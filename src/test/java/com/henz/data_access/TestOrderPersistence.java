package com.henz.data_access;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import com.henz.KitchenStoryApplication;
import com.henz.entity.Category;
import com.henz.entity.Order;
import com.henz.entity.Product;
import com.henz.entity.User;
import com.henz.enums.CategoryText;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest(classes=KitchenStoryApplication.class)
public class TestOrderPersistence {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
	
	@Test
	@DirtiesContext
	public void testWithOrders() {
		//user
		User user = new User();
		user.setUsername("test_username1");
		user.setEmail("test_email1");
		user.setFirstName("test_firstname1");
		user.setLastName("test_lastname1");
		user.setPassword("test_password1");
		user.setRole("test_role1");
		this.userRepository.save(user);
		
		//retrieve the newly saved user
		User newUser = this.userRepository.findByUsername("test_username1");
		logger.info("user id: "+newUser.getId());
		
		//category and products
		Category c = this.categoryRepository.findById(1L); //BEVERAGES
		
		Product p1 = new Product();
		p1.setDescription("test_description1");
		p1.setCategory(c);
		this.productRepository.saveNewProduct(p1);
		
		Product p2 = new Product();
		p2.setDescription("test_description2");
		p2.setCategory(c);
		this.productRepository.saveNewProduct(p2);
		
		//this is needed because products of a Category are LAZY loaded
		Optional<List<Product>> productListAsOptional = this.categoryRepository.getProductsByCategoryId(c.getId());
		List<Product> productList = productListAsOptional.get();
		
		c.setProducts(productList);
		c.addProduct(p1); //now the lazy load exception doesnt get thrown anymore here
		c.addProduct(p2); //...or here
		
		//check if products are saved
		assertTrue(this.productRepository.findByDescription("test_description1").isPresent());
		assertTrue(this.productRepository.findByDescription("test_description2").isPresent());
		
		//check if category is 1L
		assertEquals(1L, this.productRepository.findByDescription("test_description1").get().getCategory().getId());
		assertEquals(1L, this.productRepository.findByDescription("test_description2").get().getCategory().getId());
		
		//add new order
		Order order = new Order();
		order.setUser(user);
		List<Product> listWithProducts = new ArrayList<Product>();
		listWithProducts.add(p1);
		listWithProducts.add(p2);
		order.setProducts(listWithProducts);
		this.orderRepository.saveNewOrder(order);
		
		//retrieve saved order by order id 1
		Order orderById = this.orderRepository.findById(1L); //exception is thrown when the order is not found
		
		//get orders by user id 
		Optional<List<Order>> orderListOfNewUser = this.orderRepository.getAllOrdersByUserId(newUser.getId());	
		assertTrue(orderListOfNewUser.isPresent());
		assertEquals(1, orderListOfNewUser.get().size());
		
		//get the products of the saved order
		List<Product> productsOfOrder = this.orderRepository.getAllProductsOfOrder(1L);
		assertEquals(p1.getDescription(), productsOfOrder.get(0).getDescription());
		assertEquals(p2.getDescription(), productsOfOrder.get(1).getDescription());
	}

}
