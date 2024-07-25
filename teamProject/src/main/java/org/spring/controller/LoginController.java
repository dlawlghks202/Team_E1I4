package org.spring.controller;


import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.domain.RegisterDTO;
import org.spring.domain.UserDTO;
import org.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/basic/*")
public class LoginController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/login")
	public String index(){
		return "login";
		}
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@PostMapping("/login")
	public String login(@RequestParam String user_email, @RequestParam String user_pw,
			HttpSession session, RedirectAttributes redirectAttributes) {
		logger.info("사용자 로그인 실행"+user_email);
		UserDTO user = new UserDTO(user_email); 
		user = userService.getUserInfo(user);
		System.out.println(user);
		
		if(userService.login(user_email, user_pw) != null) {
			
			session.setAttribute("loginUserID", user_email);
			session.setAttribute("loginType", "basic");
			logger.info("사용자 로그인 성공 : "+user_email);
			
			session.setAttribute("user_info", user);
			
			redirectAttributes.addFlashAttribute("메세지","로그인 성공");
		}else {
			logger.info("로그인 실패");
			redirectAttributes.addFlashAttribute("error","로그인 실패");
		}
		
		return "redirect:/main";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/main";
	}
	
	@GetMapping("/join")
	public String registerShow() {
		return "/join";
	}
	
	@PostMapping("/join")
	public String register(RegisterDTO registerDTO) {
		if(!userService.isIdDuplicated(registerDTO.getUser_email())) { //중복x
			userService.register(registerDTO);
			return "redirect:/login";
		}		
		return "/join";
	}
	
	@PostMapping("/checkId")
	@ResponseBody
	public String checkId(@RequestParam("user_email") String user_email) {
		boolean isDuplicated = userService.isIdDuplicated(user_email);
		System.out.println("값 내ㅇ놔 : " + isDuplicated);
		return isDuplicated ? "duplicate" : "available";
	}
	
	@GetMapping("/searchId")
	public String searchId() {
		return "/searchId";
	}
	
	@PostMapping("/searchId")
	@ResponseBody
	public String searchId(@RequestParam("user_name") String user_name, @RequestParam("user_phone") String user_phone) {
		String user_email = userService.searchId(user_name, user_phone);
		System.out.println("이멜 : " + user_email);
		
		return user_email != null ? user_email : "not found";
	}
}