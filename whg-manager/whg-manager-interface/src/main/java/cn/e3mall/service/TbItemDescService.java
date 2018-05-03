package cn.e3mall.service;

import cn.e3mall.pojo.TbItemDesc;

public interface TbItemDescService {

	/**
	 * 根据商品id获取商品描述
	 * 
	 * @param itemId
	 * @return
	 */
	TbItemDesc findById(Long itemId);

}
