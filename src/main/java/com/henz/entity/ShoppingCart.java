package com.henz.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.henz.model.ProductCategoryModel;

@Component
@Scope(value="session", proxyMode=ScopedProxyMode.TARGET_CLASS) //proxyMode is needed so we can start the application. At start of app, there are no logged in users and no active sessions
public class ShoppingCart {
	
	private List<ProductCategoryModel> listOfProducts = new ArrayList<ProductCategoryModel>();
	
	public void addProductToShoppingCart(ProductCategoryModel p) {
		this.listOfProducts.add(p);
	}
	
	public void removeFromShoppingCart(Long productId) {
		
		System.out.println("list before remove: ");
		this.listOfProducts.stream().forEach(System.out::println);
		
		for(ProductCategoryModel pcm: this.listOfProducts) {
			if(pcm.getProductId() == productId) {
				this.listOfProducts.remove(pcm);
				break;
			}
		}
		
		System.out.println("list after remove: ");
		this.listOfProducts.stream().forEach(System.out::println);
	}
	
	public void emptyShoppingCart() {
		this.listOfProducts.clear();
	}
	
	public List<ProductCategoryModel> getListOfProducts(){
		return this.listOfProducts;
	}
}
