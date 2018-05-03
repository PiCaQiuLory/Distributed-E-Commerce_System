package cn.e3mall.sso.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3mall.sso.service.TbUserService;

@Controller
public class LogoutController {
	
	@Autowired
	private TbUserService userService;

	@RequestMapping("/logout")
	public String logout(HttpServletRequest request,Model model) {
		Cookie[] cookies = request.getCookies();
		String token = null;
		for(Cookie cookie:cookies) {
			if(cookie.getName().trim().equals("token")) {
				token = "token" + ":"+cookie.getValue();
			}
		}
		model.addAttribute("url", "http://118.25.14.35:8082/");
		userService.logout(token);
		return "logout";
	}
}
