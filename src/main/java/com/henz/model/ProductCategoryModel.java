package com.henz.model;

public class ProductCategoryModel {
	
	private Long productId;
	private String productDescription;
	private String productImgSource;
	private String categoryText;
	private Long categoryId;
	private double productPrice;
	
	public ProductCategoryModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ProductCategoryModel(Long productId, String productDescription, String productImgSource, String categoryText,
			Long categoryId, double productPrice) {
		super();
		this.productId = productId;
		this.productDescription = productDescription;
		this.productImgSource = productImgSource;
		this.categoryText = categoryText;
		this.categoryId = categoryId;
		this.productPrice = productPrice;
	}
	
	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public String getProductImgSource() {
		return productImgSource;
	}

	public void setProductImgSource(String productImgSource) {
		this.productImgSource = productImgSource;
	}

	public String getCategoryText() {
		return categoryText;
	}

	public void setCategoryText(String categoryText) {
		this.categoryText = categoryText;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public double getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}

	@Override
	public String toString() {
		return "ProductCategoryModel [productId=" + productId + ", productDescription=" + productDescription
				+ ", productImgSource=" + productImgSource + ", categoryText=" + categoryText + ", categoryId="
				+ categoryId + ", productPrice=" + productPrice + "]";
	}
}
