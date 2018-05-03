package cn.e3mall.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/page")
public class RegisterController  {

	/**
	 * 跳转 注册页面
	 * 
	 * @return
	 */
	@RequestMapping("/register")
	public String register() {
		return "register";
	}

	/**
	 * 跳转 登录页面
	 * 
	 * 实现 sso 系统的页面回调
	 * 
	 * @param redirect
	 * @return 页面传递过来的url
	 */
	@RequestMapping("/login")
	public String login(String redirect, Model model) {
		// 登录成功后,判断 页面传递的url, 返回页面传递的url
		model.addAttribute("redirect", redirect);
		return "login";
	}
}