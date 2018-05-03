package cn.e3mall.order.intercepter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;

/**
 * 用户登录拦截器
 *
 * @author colg
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

	@Value("${TOKEN_KEY}")
	private String tokenKey;
	/** sso 系统的url地址 */
	@Value("${SSO_URL}")
	private String ssoUrl;
	@Value("${COOKIE_CART}")
	private String cookieCart;

	@Autowired
	private TokenService tokenService;
	@Autowired
	private CartService cartService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		/*
		 * 从cookie中取token
		 * 	token存在:	调用sso服务取用户信息
		 * 					取到:		是登录状态, 需要把用户信息写入request, 判断cookie中是否有购物车数据, 如果有就合并到服务端
		 * 					取不到:	用户登录已过期, 需要登录
		 * 	token不存在:	未登录状态, 跳转到sso系统的登录页面, 用户登录成功后, 跳转到当前请求的url
		 */

		String token = CookieUtils.getCookieValue(request, tokenKey);
		if (StringUtils.isBlank(token)) {
			// token不存在
			response.sendRedirect(ssoUrl + "/page/login?redirect=" + request.getRequestURL());
			// 拦截
			return false;
		}

		// token存在
		E3Result e3Result = tokenService.getUserByToken(token);
		if (e3Result.getStatus()!=200) {
			// 取不到
			response.sendRedirect(ssoUrl + "/page/login?redirect=" + request.getRequestURL());
			// 拦截
			return false;
		}

		TbUser tbUser = (TbUser) e3Result.getData();
		request.setAttribute("user", tbUser);
		request.getSession().setAttribute("user", tbUser);
		// 判断cookie中是否有购物车数据
		String jsonString = CookieUtils.getCookieValue(request, cookieCart, true);
		if (StringUtils.isNotBlank(jsonString)) {
			// 合并购物车
			cartService.mergeCart(tbUser.getId(), JsonUtils.jsonToList(jsonString, TbItem.class));
		}

		return true;
	}
}