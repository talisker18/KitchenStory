package com.henz.service;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.henz.data_access.PasswordResetTokenRepository;
import com.henz.data_access.UserRepository;
import com.henz.data_access.VerificationTokenRepository;
import com.henz.entity.PasswordResetToken;
import com.henz.entity.User;
import com.henz.entity.VerificationToken;
import com.henz.model.UserModel;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private VerificationTokenRepository verificationTokenRepository;
	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder; //defined as Bean in SecurityConfig

	@Override
	public User registerUser(UserModel userModel) {
		User user  = new User();
		user.setUsername(userModel.getUsername());
		user.setEmail(userModel.getEmail());
		user.setFirstName(userModel.getFirstName());
		user.setLastName(userModel.getLastName());
		user.setRole("USER");
		user.setPassword(this.passwordEncoder.encode(userModel.getPassword()));
		
		//save to db
		this.userRepository.save(user);
		
		return user;
	}

	@Override
	public void saveVerificationTokenForUser(String token, User user) {
		VerificationToken verificationToken = new VerificationToken(token, user);
		this.verificationTokenRepository.save(verificationToken);
	}

	@Override
	public String validateVerificationToken(String token) {
		VerificationToken verificationToken = this.verificationTokenRepository.findByToken(token);
		
		if(verificationToken == null) {
			return "invalid token";
		}
		
		User user = verificationToken.getUser();
		Calendar cal = Calendar.getInstance();
		
		//check if token is older than expiration time
		if(verificationToken.getExpirationTime().getTime() - cal.getTime().getTime() <= 0) {
			//this.verificationTokenRepository.delete(verificationToken);
			return "expired token";
		}
		
		//enable the user
		user.setEnabled(true);
		
		//update the user (we can use save method because the id is set in this User object)
		this.userRepository.save(user);
		
		return "valid";
	}

	@Override
	public VerificationToken generateNewVerificationToken(String oldToken) {
		VerificationToken verificationToken = this.verificationTokenRepository.findByToken(oldToken);
		//set new token and refresh expiration time
		verificationToken.setToken(UUID.randomUUID().toString());
		verificationToken.refreshExpirationDate();
		//update the token
		this.verificationTokenRepository.save(verificationToken);
		return verificationToken;
	}

	@Override
	public User findUserByEmail(String email) {
		return this.userRepository.findByEmail(email);
	}

	@Override
	public void createPasswordResetTokenForUser(User user, String token) {
		PasswordResetToken passwordResetToken = new PasswordResetToken(token,user);
		this.passwordResetTokenRepository.save(passwordResetToken);
	}

	@Override
	public String validatePasswordResetToken(String token) {
		PasswordResetToken passwordResetToken = this.passwordResetTokenRepository.findByToken(token);
		
		if(passwordResetToken == null) {
			return "invalid token";
		}
		
		User user = passwordResetToken.getUser();
		Calendar cal = Calendar.getInstance();
		
		//check if token is older than expiration time
		if(passwordResetToken.getExpirationTime().getTime() - cal.getTime().getTime() <= 0) {
			return "expired token";
		}
		
		return "valid";
	}

	@Override
	public Optional<User> getUserByPasswordResetToken(String token) {
		return Optional.ofNullable(
				this.passwordResetTokenRepository.findByToken(token).getUser());
	}

	@Override
	public void changePassword(User user, String newPassword) {
		user.setPassword(this.passwordEncoder.encode(newPassword));
		this.userRepository.save(user);
	}

}
