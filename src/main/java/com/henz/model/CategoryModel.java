package com.henz.model;

import com.henz.entity.Category;

public class CategoryModel {

	private Long categoryId;
	private String categoryText;
	
	public CategoryModel(Long categoryId, String categoryText) {
		super();
		this.categoryId = categoryId;
		this.categoryText = categoryText;
	}

	public CategoryModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryText() {
		return categoryText;
	}

	public void setCategoryText(String categoryText) {
		this.categoryText = categoryText;
	}
	
	public static CategoryModel convertCategoryAndRetunCategoryModel(Category c) {
		return new CategoryModel(c.getId(), c.getCategory().toString());
	}
}
