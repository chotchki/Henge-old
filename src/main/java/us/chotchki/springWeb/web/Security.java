package us.chotchki.springWeb.web;

import java.util.Arrays;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import us.chotchki.springWeb.db.pojo.User;
import us.chotchki.springWeb.db.service.UsersService;
import us.chotchki.springWeb.service.TokenService;
import us.chotchki.springWeb.web.pojo.Register;

@Controller
public class Security {
	private static final Logger log = LoggerFactory.getLogger(Security.class);
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private UsersService usersService;
	
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public String index() {
		return "security/login";
	}
	
	@RequestMapping(value="/login/error", method = RequestMethod.GET)
	public String indexError(Model mod) {
		mod.addAttribute("error", "Unable to login, please try again.");
		return "security/login";
	}
	
	@RequestMapping(value="/register", method = RequestMethod.GET)
	public String register() {
		return "security/register";
	}
	
	@RequestMapping(value="/register", method = RequestMethod.POST)
	public String registerHandler(@Valid Register register, BindingResult rRegister, RedirectAttributes redirectAttributes) {
		if(rRegister.hasErrors()) {
			redirectAttributes.addFlashAttribute("error", rRegister.getFieldError().getDefaultMessage());
			redirectAttributes.addFlashAttribute("register", register);
			return "redirect:/register";
		}
		
		if(!tokenService.getToken().equals(register.getToken())){
			redirectAttributes.addFlashAttribute("error", "The token value is incorrect");
			redirectAttributes.addFlashAttribute("register", register);
			return "redirect:/register";
		}
		
		try {
			User user = new User();
			user.setUsername(register.getUser().getUsername());
			user.setPassword(bCryptPasswordEncoder.encode(register.getUser().getPassword()));
			user.setEnabled(true);
			user.setAuthorities(Arrays.asList("ADMIN"));
			usersService.create(user);
		} catch (Exception e){
			log.error("Had an error creating the user", e);
			redirectAttributes.addFlashAttribute("error", "Had an error creating the user.");
			redirectAttributes.addFlashAttribute("register", register);
			return "redirect:/register";
		}
		
		redirectAttributes.addFlashAttribute("success", "Created user " + register.getUser().getUsername());
		return "redirect:/";
	}
}
