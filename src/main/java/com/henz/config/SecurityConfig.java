package com.henz.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
//in this config, SecurityFilterChain is used instead if extending WebSecurityConfigurerAdapter
public class SecurityConfig { //extends WebSecurityConfigurerAdapter{
	
	//users can open this pages without login
	private static final String[] WHITE_LIST_URLS = {
			"/register",
			"/h2-console",
			"/verifyRegistration*",
			"/resendVerificationToken*"
	};
	
	@Autowired
	private UserDetailsService userdetailsService;
	
	@Bean
	AuthenticationProvider getAuthProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(this.userdetailsService);
		provider.setPasswordEncoder(this.passwordEncoder());
		return provider;
	}
	
	//for registration
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(11);
	}
	
	//authorize any request to the white list URLs. No login needed for these URLs
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.headers().frameOptions().disable() //needed to launch h2 console
			.and()
			.cors()
			.and()
			.csrf() //needed to launch h2 console
			.disable() //disable cors and csrf
			.authorizeRequests()
			.antMatchers("/admin/**").hasAuthority("ADMIN")
			.antMatchers("/home/**").hasAnyAuthority("ADMIN","USER")
			.antMatchers("/support/**").hasAnyAuthority("ADMIN","USER")
			.antMatchers("/viewProfile/**").hasAnyAuthority("ADMIN","USER")
			.antMatchers("/resetPasswordForm/**").hasAnyAuthority("ADMIN","USER")
			.antMatchers("/resetPassword/**").hasAnyAuthority("ADMIN","USER")
			.antMatchers("/verifyPasswordReset/**").hasAnyAuthority("ADMIN","USER")
			.antMatchers("/savePassword/**").hasAnyAuthority("ADMIN","USER")
			.antMatchers(WHITE_LIST_URLS).permitAll()
			.and().formLogin().loginPage("/login");
		
		return http.build();
	}
}
