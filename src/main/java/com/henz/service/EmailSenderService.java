package com.henz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	public void sendSimpleEmail(String email, String body, String subject) {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom("springhenz@gmail.com");
		simpleMailMessage.setTo(email);
		simpleMailMessage.setText(body);
		simpleMailMessage.setSubject(subject);
		
		this.mailSender.send(simpleMailMessage);
	}
}
