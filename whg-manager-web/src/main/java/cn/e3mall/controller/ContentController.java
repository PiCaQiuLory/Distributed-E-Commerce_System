package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.TbContent;

@Controller
public class ContentController {
	
	@Autowired
	private ContentService contentService;
	
	@RequestMapping("/content/query/list")
	@ResponseBody
	public EasyUIDataGridResult queryListByCategoryId(Long categoryId, Integer page, Integer rows) {
		return contentService.queryListByCategoryId(categoryId, page, rows);
	}
	
	/**
	 * 新增内容
	 * 
	 * @param tbContent
	 * @return
	 */
	@RequestMapping(value="/content/save",method=RequestMethod.POST)
	@ResponseBody
	public E3Result saveContent(TbContent tbContent) {
		return contentService.saveContent(tbContent);
	}

	/**
	 * 修改内容
	 * 
	 * @param id
	 * @param tbContent
	 * @return
	 */
	@RequestMapping(value="/content/edit",method=RequestMethod.POST)
	@ResponseBody
	public E3Result editContent(Long id, TbContent tbContent) {
		return contentService.editContent(id, tbContent);
	}

	/**
	 * 批量删除内容
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping("/content/delete")
	@ResponseBody
	public E3Result deleteContent(String ids) {
		return contentService.deleteContent(ids);
	}

}
