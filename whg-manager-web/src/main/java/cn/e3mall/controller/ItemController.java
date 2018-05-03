package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.search.service.SearchItemService;
import cn.e3mall.search.service.SearchService;
import cn.e3mall.service.ItemService;

@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	@Autowired
	private SearchItemService searchItemService;
	
	@RequestMapping("/item/{itemId}")
	@ResponseBody
	public TbItem getItemById(@PathVariable("itemId") long itemId) {
		return itemService.getItemById(itemId);
	}
	
	
	@RequestMapping("/item/list")
	@ResponseBody
	public EasyUIDataGridResult getItemList(Integer page,Integer rows) {
		EasyUIDataGridResult result = itemService.getItemList(page, rows);
		return result;
	}
	
	/**
	 * 商品添加功能
	 */
	@RequestMapping(value="/item/save", method=RequestMethod.POST)
	@ResponseBody
	public E3Result addItem(TbItem item, String desc){
		E3Result result = itemService.addItem(item, desc);
		return result;
	}
	
	/**
	 * 商品删除
	 * @param item
	 * @param desc
	 * @return
	 */
	@RequestMapping(value="/rest/item/delete", method=RequestMethod.POST)
	@ResponseBody
	public E3Result deleteItem(long[] ids){
		E3Result result = itemService.deleteItem(ids);
		return result;
	}
	
	/**
	 * 商品下架
	 * @param item
	 * @param desc
	 * @return
	 */
	@RequestMapping(value="/rest/item/instock", method=RequestMethod.POST)
	@ResponseBody
	public E3Result downItem(long[] ids){
		E3Result result = itemService.downItem(ids);
		return result;
	}
	
	//上架
	@RequestMapping(value="/rest/item/reshelf", method=RequestMethod.POST)
	@ResponseBody
	public E3Result upItem(long[] ids){
		E3Result result = itemService.upItem(ids);
		return result;
	}
	
	@RequestMapping(value="/tb/item/desc/{id}",method=RequestMethod.GET)
	@ResponseBody
	public E3Result findById(@PathVariable("id") Long itemId) {
		TbItemDesc tbItemDesc = itemService.getItemDescById(itemId);
		return E3Result.ok(tbItemDesc);
	}
	
	/**
	 * 一键导入商品数据到索引库
	 * 
	 * @return
	 */
	@RequestMapping("/index/item/import")
	@ResponseBody
	public E3Result importAllItem() {
		return searchItemService.importAllItems();
	}
	
	
	@RequestMapping("/item/param/list")
	@ResponseBody
	public EasyUIDataGridResult getParamList(@RequestParam int page,@RequestParam int rows) {
		return itemService.getParamList(page, rows);
	}
	
	@RequestMapping("/item/param/query/itemcatid/{itemcatid}")
	@ResponseBody
	public E3Result getParamByCid(@PathVariable long itemcatid) {
		return itemService.getParamByCid(itemcatid);
	}
	
	/*@RequestMapping("/item/param/delete")
	public E3Result deleteParam()*/
}
