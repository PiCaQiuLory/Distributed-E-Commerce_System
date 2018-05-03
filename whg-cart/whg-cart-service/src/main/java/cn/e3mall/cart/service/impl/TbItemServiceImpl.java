package cn.e3mall.cart.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.cart.service.TbItemService;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;

@Service
public class TbItemServiceImpl  implements TbItemService {

	@Autowired
	private TbItemMapper tbItemMapper;
	
	@Override
	public TbItem getTbItemById(Long id) {
		return tbItemMapper.selectByPrimaryKey(id);
	}

}
