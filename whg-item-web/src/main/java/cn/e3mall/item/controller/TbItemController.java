package cn.e3mall.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3mall.item.vo.TbItemDto;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;
import cn.e3mall.service.TbItemDescService;

@Controller
@RequestMapping("/item")
public class TbItemController  {
	
	
	@Autowired
	private ItemService tbItemService;
	
	@Autowired
	private TbItemDescService tbItemDescService;

	/**
	 * 根据商品id获取商品详情并返回页面
	 * 
	 */
	@RequestMapping("/{itemId}")
	public String showItemInfo(@PathVariable Long itemId, Model model) {
		// 调用服务取商品基本信息
		TbItem tbItem = tbItemService.getItemById(itemId);
		// 把TbItem转换成Item对象
		// BeanUtils.copyProperties(tbItem, tbItemDto);// 性能较慢
		TbItemDto tbItemDto = new TbItemDto(tbItem);
		// 取商品描述信息
		TbItemDesc tbItemDesc = tbItemDescService.findById(itemId);
		// 把信息传递给页面
		model.addAttribute("item", tbItemDto);
		model.addAttribute("itemDesc", tbItemDesc);
		// 返回逻辑视图
		return "item";
	}
}
