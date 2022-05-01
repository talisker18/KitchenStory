package com.henz.data_access;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.henz.entity.Order;
import com.henz.entity.Product;

@Repository
@Transactional
public class OrderRepository {

	@Autowired
	private EntityManager entityManager;
	
	public Optional<List<Order>> getAllOrdersByUserId(Long userId){ //use JPQL because orders of an user are LAZY fetched
		TypedQuery<Order> query = this.entityManager.createQuery("Select o from Order o where o.user.id=:userId",Order.class);
		query.setParameter("userId", userId);
		
		return Optional.ofNullable(query.getResultList());
	}
	
	public void saveNewOrder(Order order) {
		this.entityManager.persist(order);
	}
	
	public Order findById(Long id) {
		Order o = this.entityManager.find(Order.class, id);
		
		if(o != null) {
			return o;
		}else {
			throw new EntityNotFoundException("order with id: "+id+" not found");
		}
	}
	
	public List<Product> getAllProductsOfOrder(Long orderId){ //use JPQL because products of an order are LAZY fetched
		TypedQuery<Product> query = this.entityManager.createQuery("Select p from Product p JOIN p.orders o where o.id=:orderId", Product.class);
		query.setParameter("orderId", orderId);
		return query.getResultList();
	}
}
