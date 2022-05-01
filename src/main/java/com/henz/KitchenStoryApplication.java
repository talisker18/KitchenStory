package com.henz;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import com.henz.data_access.CategoryRepository;
import com.henz.data_access.ProductRepository;
import com.henz.data_access.UserRepository;
import com.henz.entity.Category;
import com.henz.entity.Product;
import com.henz.entity.User;
import com.henz.enums.CategoryText;

@SpringBootApplication
@Transactional
public class KitchenStoryApplication implements CommandLineRunner{
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(KitchenStoryApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//save the categories
		Category c1 = new Category();
		c1.setCategory(CategoryText.BEVERAGES);
		this.categoryRepository.saveNewCategory(c1);
		
		Category c2 = new Category();
		c2.setCategory(CategoryText.FISH);
		this.categoryRepository.saveNewCategory(c2);
		
		Category c3 = new Category();
		c3.setCategory(CategoryText.FRUITS);
		this.categoryRepository.saveNewCategory(c3);
		
		Category c4 = new Category();
		c4.setCategory(CategoryText.MEAT);
		this.categoryRepository.saveNewCategory(c4);
		
		Category c5 = new Category();
		c5.setCategory(CategoryText.VEGETABLES);
		this.categoryRepository.saveNewCategory(c5);
		
		Category c6 = new Category();
		c6.setCategory(CategoryText.OTHER);
		this.categoryRepository.saveNewCategory(c6);
		
		//add some products
		Product p1 = new Product();
		p1.setDescription("Curry Rice");
		p1.setCategory(c6);
		p1.setImgSource("../img/curry_rice.jpeg");
		p1.setPrice(5.66);
		this.productRepository.saveNewProduct(p1);
		
		Product p2 = new Product();
		p2.setDescription("Green Beans");
		p2.setCategory(c5);
		p2.setImgSource("../img/Green-Bean-scaled.jpg");
		p2.setPrice(3.74);
		this.productRepository.saveNewProduct(p2);
		
		Product p3 = new Product();
		p3.setDescription("Chicken Nuggets");
		p3.setCategory(c4);
		p3.setImgSource("../img/chicken_nuggets.jpg");
		p3.setPrice(6.99);
		this.productRepository.saveNewProduct(p3);
		
		Product p4 = new Product();
		p4.setDescription("Apples");
		p4.setCategory(c3);
		p4.setImgSource("../img/apples.jpg");
		p4.setPrice(3);
		this.productRepository.saveNewProduct(p4);
		
		Product p5 = new Product();
		p5.setDescription("Bananas");
		p5.setCategory(c3);
		p5.setImgSource("../img/bananas.jpg");
		p5.setPrice(3);
		this.productRepository.saveNewProduct(p5);
		
		Product p6 = new Product();
		p6.setDescription("Strawberries");
		p6.setCategory(c3);
		p6.setImgSource("../img/strawberries.jpg");
		p6.setPrice(3);
		this.productRepository.saveNewProduct(p6);
		
		Product p7 = new Product();
		p7.setDescription("Ice Tea");
		p7.setCategory(c1);
		p7.setImgSource("../img/ice_tea.jpg");
		p7.setPrice(1.5);
		this.productRepository.saveNewProduct(p7);
		
		Product p8 = new Product();
		p8.setDescription("Ginger Beer");
		p8.setCategory(c1);
		p8.setImgSource("../img/ginger_beer.jpg");
		p8.setPrice(2);
		this.productRepository.saveNewProduct(p8);
		
		Product p9 = new Product();
		p9.setDescription("Fish and Chips");
		p9.setCategory(c2);
		p9.setImgSource("../img/fish_and_chips.jpg");
		p9.setPrice(7.63);
		this.productRepository.saveNewProduct(p9);
		
		Product p10 = new Product();
		p10.setDescription("Spaghetti");
		p10.setCategory(c6);
		p10.setImgSource("../img/spaghetti.jpg");
		p10.setPrice(5.52);
		this.productRepository.saveNewProduct(p10);
		
		Product p11 = new Product();
		p11.setDescription("Pizza");
		p11.setCategory(c6);
		p11.setImgSource("../img/pizza.jpg");
		p11.setPrice(4.99);
		this.productRepository.saveNewProduct(p11);
	}
}
