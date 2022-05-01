package com.henz.controller;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.henz.auth.AuthenticationFacade;
import com.henz.auth.CustomUserDetails;
import com.henz.data_access.OrderRepository;
import com.henz.data_access.ProductRepository;
import com.henz.entity.Order;
import com.henz.entity.Product;
import com.henz.entity.ShoppingCart;
import com.henz.entity.User;
import com.henz.model.ProductCategoryModel;

@Controller
public class UserController {
	
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private AuthenticationFacade authenticationFacade;
	
	@Autowired
	private ShoppingCart shoppingCart;
	
	@RequestMapping(value = "/home/showAllProducts", method = RequestMethod.GET)
	public ModelAndView showAllProducts() {
		ModelAndView mv = new ModelAndView("user-all-products");
		List<ProductCategoryModel> listWithProducts = this.productRepository.getAllProductsByProductCategoryModel();
		mv.addObject("productList",listWithProducts);
		
		return mv;
	}
	
	@RequestMapping(value = "/home/addToCart", method = RequestMethod.POST)
	public String addProductToCart(@RequestParam("productId") Long productId) {
		ProductCategoryModel p = this.productRepository.findByIdAndReturnProductCategoryModel(productId);
		this.shoppingCart.addProductToShoppingCart(p);
		return "redirect:/home/showAllProducts";
	}
	
	@RequestMapping(value = "/home/shoppingCart", method = RequestMethod.GET)
	public ModelAndView showShoppingCart() {
		ModelAndView mv = new ModelAndView("shopping-cart");
		List<ProductCategoryModel> productList = this.shoppingCart.getListOfProducts();
		mv.addObject("productList", productList);
		
		double total = 0;
		for(ProductCategoryModel p: productList) {
			total += p.getProductPrice();
		}
		
		mv.addObject("total",total);

		return mv;
	}
	
	@RequestMapping(value ="home/order", method = RequestMethod.POST)
	public ModelAndView showOrderConfirmation() {
		ModelAndView mv = new ModelAndView("order-confirmation");

		if(this.shoppingCart.getListOfProducts().isEmpty()) {
			mv.addObject("confirmationMessage","Your shopping cart was empty. No order created!");
		}else {
			//create order
			Order o = new Order();
			User user = null;
			
			Authentication authentication = this.authenticationFacade.getAuthentication();
			if (!(authentication instanceof AnonymousAuthenticationToken)) {
			    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
			    user = userDetails.getUser();
			}
			
			o.setUser(user);
			
			List<Product> productList = this.productRepository
					.convertListWithModelObjectsToListWithProductEntities(this.shoppingCart.getListOfProducts());
			
			o.setProducts(productList);
			
			double total = 0;
			
			for(Product p: productList) {
				total += p.getPrice();
			}
			
			o.setTotal(total);
			
			this.orderRepository.saveNewOrder(o);
			mv.addObject("confirmationMessage","Thanks for your order! (Order No = "+o.getId()+")");
			
			//clear shopping cart for next order
			this.shoppingCart.emptyShoppingCart();
		}
		
		return mv;
	}
	
	@RequestMapping(value ="/home/showOrderlist", method = RequestMethod.GET)
	public ModelAndView showOrderList() {
		ModelAndView mv = new ModelAndView("order-list");
		
		User user = null;
		
		Authentication authentication = this.authenticationFacade.getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
		    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		    user = userDetails.getUser();
		}
		
		Optional<List<Order>> orderList = this.orderRepository.getAllOrdersByUserId(user.getId());
		mv.addObject("orderList", orderList.get());
		
		return mv;
	}
	
	@RequestMapping(value = "/home/showOrderDetails", method = RequestMethod.GET)
	public ModelAndView showOrderDetails(@RequestParam("orderId") Long orderId) {
		ModelAndView mv = new ModelAndView("order-details");
		Order order = this.orderRepository.findById(orderId);
		List<Product> productList = this.orderRepository.getAllProductsOfOrder(orderId);
		List<ProductCategoryModel> productModelList = 
				this.productRepository.convertListWithProductEntitiesToListWithModelObjects(productList);
		
		mv.addObject("productModelList", productModelList);
		mv.addObject("order", order);
		
		return mv;
	}
	
	@RequestMapping(value = "/home/removeFromCart", method = RequestMethod.POST)
	public String removeFromShoppingCart(@RequestParam("productId") Long productId) {
		this.shoppingCart.removeFromShoppingCart(productId);
		
		return "redirect:/home/shoppingCart";
	}
}
