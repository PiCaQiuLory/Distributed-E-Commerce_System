package cn.e3mall.order.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.order.pojo.OrderInfo;
import cn.e3mall.order.service.OrderService;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;


/**
 * 订单管理Controller
 *
 * @author colg
 */
@Controller
@RequestMapping("/order")
public class OrderController{
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private OrderService orderService;

	/**
	 * 展示订单结算页面
	 * 
	 * @param tbUser
	 *            注解@RequestAttribute可以被用于访问由过滤器或拦截器创建的、预先存在的请求属性
	 * @return
	 */
	@RequestMapping(value="/order-cart",method=RequestMethod.GET)
	public String showOrderCart(Model model,HttpServletRequest request) {
		
		TbUser tbUser = (TbUser) request.getAttribute("user");
		if(tbUser != null) {
			// 取用户id
			Long userId = tbUser.getId();
			List<TbItem> cartList = cartService.getCartList(userId);
	
			// 根据用户id取收货地址
			// 根据用户id取支付方式列表
	
			// 把购物车列表传递给jsp
			model.addAttribute("cartList", cartList);
		}
			// 返回逻辑视图
			return "order-cart";
	}

	/**
	 * 提交订单
	 * 
	 * @param tbUser
	 *            request里取得tbUser
	 * @param payment
	 *            传值给request
	 * @param orderInfo
	 *            订单信息
	 * @return
	 */
	@RequestMapping(value="/create",method=RequestMethod.POST)
	public String createOrderInfo(Model model, OrderInfo orderInfo,HttpServletRequest request) {
		TbUser tbUser = (TbUser) request.getSession().getAttribute("user");
		// 生成订单
		E3Result e3Result = orderService.createOrder(orderInfo, tbUser);
		model.addAttribute("orderId", e3Result.getData());
		model.addAttribute("payment", orderInfo.getPayment());

		// 清空当前用户的购物车
		cartService.deleteAllCart(tbUser.getId());

		// 返回结算成功页面
		return "success";
	}
}
