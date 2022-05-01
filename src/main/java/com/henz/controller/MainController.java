package com.henz.controller;

import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.henz.auth.AuthenticationFacade;
import com.henz.auth.CustomUserDetails;
import com.henz.entity.User;
import com.henz.entity.VerificationToken;
import com.henz.event.registration.RegistrationCompleteEvent;
import com.henz.model.PasswordModel;
import com.henz.model.UserModel;
import com.henz.service.EmailSenderService;
import com.henz.service.UserService;

@Controller
public class MainController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private EmailSenderService emailSenderService;
	
	@Autowired
	private AuthenticationFacade authenticationFacade;
	
	@RequestMapping(value={"","welcome"}, method = RequestMethod.GET)
	public String showWelcomePage() {
		
		Authentication  auth = SecurityContextHolder.getContext().getAuthentication();
		
		if(auth == null || auth instanceof AnonymousAuthenticationToken) {
			return "welcome";
		}
		
		return "redirect:/home";
	}
	
	@RequestMapping(value="/register", method = RequestMethod.GET)
	public ModelAndView showRegisterForm() {
		UserModel userModel = new UserModel();
		ModelAndView mv = new ModelAndView();
		mv.addObject("userModel",userModel);
		mv.setViewName("register-form");
		
		return mv;
	}
	
	@RequestMapping(value="/registerConfirmation", method = RequestMethod.POST)
	public ModelAndView registerUser(@ModelAttribute UserModel userModel, HttpServletRequest request) {
		
		//generate user entity and publish registration event
		User user = this.userService.registerUser(userModel);
		this.publisher.publishEvent(
				new RegistrationCompleteEvent(user, this.getApplicationUrl(request)));
		
		//return the view
		ModelAndView mv = new ModelAndView();
		mv.setViewName("registration-confirmation");
		
		return mv;
	}
	
	@RequestMapping(value="/verifyRegistration", method = RequestMethod.GET)
	public ModelAndView verifyRegistration(@RequestParam("token") String token) {
		String result = this.userService.validateVerificationToken(token);
		
		if(result.equalsIgnoreCase("valid")) {
			ModelAndView mvValid = new ModelAndView("verify-registration-ok");
			return mvValid;
		}else if(result.equalsIgnoreCase("invalid token")){
			ModelAndView mvInvalid = new ModelAndView("verify-registration-nok-invalid");
			mvInvalid.addObject("result",result);
			return mvInvalid;
		}else if(result.equalsIgnoreCase("expired token")) {
			//user has the ability to re-generate a token
			ModelAndView mvExpired = new ModelAndView("verify-registration-nok-expired");
			mvExpired.addObject("result",result);
			//add the verification token so the user can re-generate a token
			mvExpired.addObject("oldVerificationToken",token);
			return mvExpired;
		}
		
		return null;
	}
	
	@RequestMapping(value="/resendVerificationToken", method = RequestMethod.GET)
	public ModelAndView resendVerificationToken(@RequestParam("token") String oldToken, HttpServletRequest request) {
		VerificationToken newToken = this.userService.generateNewVerificationToken(oldToken);
		User user = newToken.getUser();
		resendVerificationTokenMail(user, this.getApplicationUrl(request), newToken);
		
		ModelAndView mv = new ModelAndView("resend-verification-token");
		return mv;
	}
	
	@RequestMapping(value="/resetPasswordForm", method = RequestMethod.GET)
	public ModelAndView getResetPasswordForm() {
		PasswordModel passwordModel = new PasswordModel();
		ModelAndView mv = new ModelAndView("reset-password-form");
		mv.addObject("passwordModel",passwordModel);
		return mv;
	}
	
	@RequestMapping(value="/resetPassword", method = RequestMethod.POST)
	public ModelAndView resetPassword(@ModelAttribute("passwordModel") PasswordModel passwordModel, HttpServletRequest request) {
		User user = this.userService.findUserByEmail(passwordModel.getEmail());
		if(user != null) {
			String token = UUID.randomUUID().toString();
			this.userService.createPasswordResetTokenForUser(user, token);
			passwordResetTokenMail(user, this.getApplicationUrl(request), token);
		}
		
		ModelAndView mv = new ModelAndView("reset-password-confirm-sent-mail");
		mv.addObject("passwordModel",passwordModel);
		return mv;
	}
	
	@RequestMapping(value="/verifyPasswordReset", method = RequestMethod.GET)
	public ModelAndView verifyPasswordReset(@RequestParam("token") String token) {
		String result = this.userService.validatePasswordResetToken(token);
		
		if(result.equalsIgnoreCase("invalid token") || result.equalsIgnoreCase("expired token")) {
			ModelAndView mvInvalid = new ModelAndView("verify-pw-resettoken-nok");
			mvInvalid.addObject("result",result);
			return mvInvalid;
		}else {
			ModelAndView mvValid = new ModelAndView("verify-pw-resettoken-ok");
			mvValid.addObject("token",token);
			PasswordModel passwordModel = new PasswordModel();
			mvValid.addObject("passwordModel",passwordModel);
			return mvValid;
		}
		
		
	}
	
	@RequestMapping(value="/savePassword", method = RequestMethod.POST)
	public ModelAndView saveNewPassword(@RequestParam("token") String token, @ModelAttribute PasswordModel passwordModel) {
		//update the password
		Optional<User> user = this.userService.getUserByPasswordResetToken(token);
		if(user.isPresent()) {
			this.userService.changePassword(user.get(), passwordModel.getNewPassword());
			ModelAndView mvValid = new ModelAndView("pw-reset-ok");
			return mvValid;
		}else {
			ModelAndView mvUserNotFound = new ModelAndView("pw-reset-nok");
			return mvUserNotFound;
		}
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String showLoginPage() {
		Authentication  auth = SecurityContextHolder.getContext().getAuthentication();
		
		if(auth == null || auth instanceof AnonymousAuthenticationToken) {
			return "login";
		}
		
		return "redirect:/home";
	}
	
	@RequestMapping(value={"home"}, method = RequestMethod.GET)
	public ModelAndView showHomePage() {
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("home");
		
		return mv;
	}
	
	@RequestMapping(value={"support"}, method = RequestMethod.GET)
	public ModelAndView showSupportPage() {
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("support");
		
		return mv;
	}
	
	@RequestMapping(value={"viewProfile"}, method = RequestMethod.GET)
	public ModelAndView showProfilePage() {
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("view-profile");
		
		//get the current user 
		Authentication authentication = this.authenticationFacade.getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
		    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		    mv.addObject("user",userDetails.getUser());
		}
		
		return mv;
	}

	private String getApplicationUrl(HttpServletRequest request) {
		return "http://" + 
				request.getServerName() +
				":" + 
				request.getServerPort() + 
				request.getContextPath();
	}
	
	private void resendVerificationTokenMail(User user, String applicationUrl, VerificationToken verificationToken) {
		String url = applicationUrl+"/verifyRegistration?token="+verificationToken.getToken();
		this.emailSenderService.sendSimpleEmail(user.getEmail(), "Thanks for your registration at 'Kitchen Story'. Please open the following URL to finish your registration: "+url, "Registration for 'Kitchen Story'");
	}
	
	private void passwordResetTokenMail(User user, String applicationUrl, String token) {
		String url = applicationUrl+"/verifyPasswordReset?token="+token;
		this.emailSenderService.sendSimpleEmail(user.getEmail(), "Click on the following link to change your password: "+url, "Reset your password for 'Kitchen Story'");
	}
}
