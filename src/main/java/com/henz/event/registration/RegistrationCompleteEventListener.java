package com.henz.event.registration;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.henz.entity.User;
import com.henz.service.EmailSenderService;
import com.henz.service.UserService;

@Component //needed to trigger the events
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent>{
	
	@Autowired
	private UserService userService;
	@Autowired
	private EmailSenderService emailSenderService;

	@Override
	public void onApplicationEvent(RegistrationCompleteEvent event) {
		//create verification token for user. User have to click on link received by email after registration and then redirected to the application
		User user = event.getUser();
		String token = UUID.randomUUID().toString();
		
		this.userService.saveVerificationTokenForUser(token, user);
		
		//send mail
		String url = event.getApplicationUrl()+"/verifyRegistration?token="+token;
		
		this.emailSenderService.sendSimpleEmail(user.getEmail(), "Thanks for your registration at 'Kitchen Story'. Please open the following URL to finish your registration: "+url, "Registration for 'Kitchen Story'");
		
		
	}

}
