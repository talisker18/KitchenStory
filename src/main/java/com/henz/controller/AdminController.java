package com.henz.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.henz.data_access.CategoryRepository;
import com.henz.data_access.ProductRepository;
import com.henz.entity.Category;
import com.henz.entity.Product;
import com.henz.model.CategoryModel;
import com.henz.model.ProductCategoryModel;

@Controller
public class AdminController {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@RequestMapping(value={"/admin","/admin/"}, method = RequestMethod.GET)
	public ModelAndView admin() {
		ModelAndView mv = new ModelAndView("admin");
		return mv;
	}
	
	@RequestMapping(value="/admin/addProduct", method = RequestMethod.GET)
	public ModelAndView adminAddProduct() {
		ModelAndView mv = new ModelAndView("admin-product-form");
		ProductCategoryModel productCategoryModel = new ProductCategoryModel();
		mv.addObject("productCategoryModel",productCategoryModel);
		
		List<CategoryModel> categoryModelList = this.categoryRepository.getAllCategoriesAndReturnAsCategoryModels();
		mv.addObject("categoryModelList",categoryModelList);
		
		return mv;
	}
	
	@RequestMapping(value="/admin/addProduct", method = RequestMethod.POST)
	public ModelAndView adminSaveProduct(@RequestParam("file") MultipartFile file, @ModelAttribute("productCategoryModel") ProductCategoryModel productCategoryModel) {
		boolean success = this.productRepository.saveNewProductByUsingProductCategoryModel(
				productCategoryModel,
				file
		);
		
		ModelAndView mv = new ModelAndView("add-product-result");
		mv.addObject("success",success);
		
		return mv;
	}
	
	@RequestMapping(value="/admin/productList", method = RequestMethod.GET)
	public ModelAndView showProductsInAdminDomain() {
		List<ProductCategoryModel> productList = this.productRepository.getAllProductsByProductCategoryModel();
		
		ModelAndView mv = new ModelAndView("admin-productlist");
		mv.addObject("productList",productList);
		
		return mv;
	}
	
	@RequestMapping(value="/admin/showUpdateForm", method = RequestMethod.GET)
	public ModelAndView showProductUpdateForm(@RequestParam("productId") Long productId) {
		ModelAndView mv = new ModelAndView("admin-product-form");
		ProductCategoryModel productCategoryModel = 
				this.productRepository.findByIdAndReturnProductCategoryModel(productId);
		
		mv.addObject("productCategoryModel",productCategoryModel);
		
		List<CategoryModel> categoryModelList = this.categoryRepository.getAllCategoriesAndReturnAsCategoryModels();
		mv.addObject("categoryModelList",categoryModelList);
		
		return mv;
	}
	
	
	@RequestMapping(value="/admin/deleteProduct", method = RequestMethod.GET)
	public String deleteProduct(@RequestParam Long productId) {
		this.productRepository.deleteById(productId);
		return "redirect:/admin/productList";
	}
}
