package cn.e3mall.sso.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TbUserService;

@RestController
@RequestMapping("/user")
public class TbUserController{

	/** cookie中保存token的key */
	@Value("${TOKEN_KEY}")
	private String TOKEN_KEY;
	
	@Autowired
	private TbUserService tbUserService;

	@RequestMapping("/{id}")
	public E3Result find(@PathVariable Long id) {
		TbUser tbUser = tbUserService.findById(id);
		return E3Result.ok(tbUser);
	}

	/**
	 * 注册,校验
	 * 
	 * @param param
	 * @param type
	 *            参数类型 - 1:用户名, 2:手机, 3:邮箱
	 * @return
	 */
	@RequestMapping("/check/{param}/{type}")
	public E3Result checkData(@PathVariable String param, @PathVariable Integer type) {
		return tbUserService.checkData(param, type);
	}

	/**
	 * 注册,添加
	 * 
	 * @param tbUser
	 * @return
	 */
	@RequestMapping("/register")
	public E3Result register(TbUser tbUser) {
		return tbUserService.createUser(tbUser);
	}

	/**
	 * 登录
	 * 
	 * 登录成功后，把token写入cookie
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	@RequestMapping("/login")
	public E3Result login(String username, String password, HttpServletRequest request, HttpServletResponse response) {
		E3Result e3Result = tbUserService.userLogin(username, password);
		// 判断是否登录成功
		if (e3Result.getStatus().intValue()==200) {
			Map<String, Object> map = (Map<String, Object>) e3Result.getData();
			String token = map.get("token").toString();
			int role = (int) map.get("role");
			e3Result.setMsg(String.valueOf(role));
			// 如果登录成功需要把token写入cookie
			CookieUtils.setCookie(request, response, TOKEN_KEY, token);
		}
		return e3Result;
	}

}
