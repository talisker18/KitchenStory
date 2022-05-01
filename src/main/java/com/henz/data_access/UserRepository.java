package com.henz.data_access;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.henz.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	/**
	 * @param username: must match the field 'username' of User.class
	 * 
	 * */
	//method name must be findByUsername
	public User findByUsername(String username);

	public User findByEmail(String email);
}
