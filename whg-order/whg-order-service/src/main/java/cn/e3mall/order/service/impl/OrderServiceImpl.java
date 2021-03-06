package cn.e3mall.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.mapper.TbOrderItemMapper;
import cn.e3mall.mapper.TbOrderMapper;
import cn.e3mall.mapper.TbOrderShippingMapper;
import cn.e3mall.order.pojo.OrderInfo;
import cn.e3mall.order.service.OrderService;
import cn.e3mall.pojo.TbOrderItem;
import cn.e3mall.pojo.TbOrderShipping;
import cn.e3mall.pojo.TbUser;

@Service
public class OrderServiceImpl implements OrderService {

	/** 订单号id */
	@Value("${ORDER_ID_GEN_KEY}")
	private String ORDER_ID_GEN_KEY;
	/** 订单号初始值 */
	@Value("${ORDER_ID_BEGIN}")
	private String ORDER_ID_BEGIN;
	/** 订单明细id */
	@Value("${ORDER_ITEM_ID_GEN_KEY}")
	private String ORDER_ITEM_ID_GEN_KEY;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Autowired
	private TbOrderItemMapper tbOrderItemMapper;
	
	@Autowired
	private TbOrderMapper tbOrderMapper;
	
	@Autowired
	private TbOrderShippingMapper tbOrderShippingMapper;

	@Override
	public E3Result createOrder(OrderInfo orderInfo, TbUser tbUser) {
		// 判断是否有orderId
		if (!jedisClient.exists(ORDER_ID_GEN_KEY)) {
			// 设置初始值
			jedisClient.set(ORDER_ID_GEN_KEY, ORDER_ID_BEGIN);
		}

		// 生成订单号, 使用redis的incr生成
		String orderId = jedisClient.incr(ORDER_ID_GEN_KEY).toString();

		// 补全orderInfo的属性
		orderInfo.setOrderId(orderId);
		// 状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
		orderInfo.setStatus(1);
		orderInfo.setCreateTime(new Date());
		orderInfo.setUpdateTime(new Date());
		orderInfo.setUserId(tbUser.getId());
		orderInfo.setBuyerNick(tbUser.getUsername());
		// 插入订单表
		tbOrderMapper.insert(orderInfo);

		// 向订单明细表插入数据
		List<TbOrderItem> orderItems = orderInfo.getOrderItems();
		for (TbOrderItem tbOrderItem : orderItems) {
			// 生成订单明细id
			String orderItemId = jedisClient.incr(ORDER_ITEM_ID_GEN_KEY).toString();
			tbOrderItem.setId(orderItemId);
			tbOrderItem.setOrderId(orderId);
			// 插入数据
			tbOrderItemMapper.insert(tbOrderItem);
		}

		// 向订单物流表插入数据
		TbOrderShipping tbOrderShipping = orderInfo.getOrderShipping();
		tbOrderShipping.setOrderId(orderId);
		tbOrderShipping.setCreated(new Date());
		tbOrderShipping.setUpdated(new Date());
		tbOrderShipping.setReceiverName(tbUser.getUsername());
		tbOrderShipping.setReceiverMobile(tbUser.getPhone());
		// 插入数据
		tbOrderShippingMapper.insert(tbOrderShipping);

		// 返回E3Result,包含订单号
		return E3Result.ok(orderId);
	}

}