package com.henz.data_access;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.henz.entity.Category;
import com.henz.entity.Product;
import com.henz.model.CategoryModel;

@Repository
@Transactional
public class CategoryRepository {
	
	@Autowired
	private EntityManager entityManager;
	
	public Category findById(Long id) {
		Category c = this.entityManager.find(Category.class, id);
		
		if(c != null) {
			return c;
		}else {
			throw new EntityNotFoundException("category with id: "+id+" not found");
		}
	}
	
	public void deleteById(Long id) {
		Category c = this.findById(id);
		
		if(c != null) {
			this.entityManager.remove(c);
		}else {
			throw new EntityNotFoundException("category with id: "+id+" not found");
		}
	}
	
	public void saveNewCategory(@NotNull Category c) {
		this.entityManager.persist(c);
	}
	
	public void updateCategory(@NotNull Category c) {
		this.entityManager.merge(c);
	}
	
	public List<Category> getAllCategories(){
		return this.entityManager.createQuery("Select c from Category c", Category.class).getResultList();
	}
	
	public List<CategoryModel> getAllCategoriesAndReturnAsCategoryModels(){
		
		List<Category> listWithCategories= this.entityManager.createQuery("Select c from Category c", Category.class).getResultList();
		
		List<CategoryModel> listWithCategoryModels = new ArrayList<CategoryModel>();
		
		for(Category c: listWithCategories) {
			listWithCategoryModels.add(CategoryModel.convertCategoryAndRetunCategoryModel(c));
		}
		
		return listWithCategoryModels;
	}
	
	public Optional<List<Product>> getProductsByCategoryId(Long categoryId) {
		TypedQuery<Product> query = this.entityManager.createQuery("Select p from Product p where p.category.id =:categoryId", Product.class);
		query.setParameter("categoryId", categoryId);
		
		return Optional.ofNullable(query.getResultList());
	}
}
