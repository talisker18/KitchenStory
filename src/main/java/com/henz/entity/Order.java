package com.henz.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="order_table") //with name = order did not work because its reserved word
public class Order {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="order_id")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY) //order is the owning side
	@JoinColumn(name="user_id", nullable = false)
	User user;
	
	@ManyToMany
	@JoinColumn(nullable = false) //empty order does not make sense, so use nullable = false
	@JoinTable(
			name="order_product",
			joinColumns = @JoinColumn(name="order_id"),
			inverseJoinColumns = @JoinColumn(name="product_id")
	)
	List<Product> products = new ArrayList<Product>();

	@CreationTimestamp
	@Column(name="created_at")
	private LocalDateTime created;
	
	private double total;
}
