package cn.e3mall.order.service;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.order.pojo.OrderInfo;
import cn.e3mall.pojo.TbUser;

public interface OrderService {

	/**
	 * 生成订单
	 * 
	 * @param orderInfo
	 *            订单信息
	 * @param tbUser
	 *            当前登录用户
	 * @return
	 */
	E3Result createOrder(OrderInfo orderInfo, TbUser tbUser);
}
