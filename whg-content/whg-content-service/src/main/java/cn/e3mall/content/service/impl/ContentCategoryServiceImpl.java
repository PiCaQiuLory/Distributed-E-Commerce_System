package cn.e3mall.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentCategoryService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import cn.e3mall.pojo.TbContentCategoryExample.Criteria;

@Service(value="contentCategoryServiceImpl")
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;
	
	@Override
	public List<EasyUITreeNode> getContentCategoryList(Long parentId) {
		return contentCategoryMapper.getContentCategoryList(parentId);
	}

	@Override
	public E3Result addContentCategory(Long parentId, String name) {
		// 1、接收两个参数：parentId、name
		// 2、向tb_content_category表中插入数据。
		// a)创建一个TbContentCategory对象
		TbContentCategory tbContentCategory = new TbContentCategory();
		// b)补全TbContentCategory对象的属性
		tbContentCategory.setIsParent(false);
		tbContentCategory.setName(name);
		tbContentCategory.setParentId(parentId);
		//排列序号，表示同级类目的展现次序，如数值相等则按名称次序排列。取值范围:大于零的整数
		tbContentCategory.setSortOrder(1);
		//状态。可选值:1(正常),2(删除)
		tbContentCategory.setStatus(1);
		tbContentCategory.setCreated(new Date());
		tbContentCategory.setUpdated(new Date());
		// c)向tb_content_category表中插入数据
		contentCategoryMapper.insert(tbContentCategory);
		// 3、判断父节点的isparent是否为true，不是true需要改为true。
		TbContentCategory parentNode = contentCategoryMapper.selectByPrimaryKey(parentId);
		if (!parentNode.getIsParent()) {
			parentNode.setIsParent(true);
			//更新父节点
			contentCategoryMapper.updateByPrimaryKey(parentNode);
		}
		// 4、需要主键返回。
		// 5、返回E3Result，其中包装TbContentCategory对象
		return E3Result.ok(tbContentCategory);
	}

	@Override
	public E3Result updateContentCategory(Long id, String name) {
		TbContentCategory tbContentCategory = contentCategoryMapper.selectByPrimaryKey(id);
		tbContentCategory.setName(name);
		tbContentCategory.setUpdated(new Date());
		contentCategoryMapper.updateByPrimaryKey(tbContentCategory);
		return E3Result.ok();
	}

	@Override
	public E3Result deleteContentCategory(Long id) {
		TbContentCategory tbContentCategory = contentCategoryMapper.selectByPrimaryKey(id);
		if (tbContentCategory.getIsParent()) {
			return E3Result.build(500, "删除失败");
		}
		// 状态。可选值:1(正常),2(删除)
		tbContentCategory.setStatus(2);
		tbContentCategory.setUpdated(new Date());
		contentCategoryMapper.updateByPrimaryKey(tbContentCategory);

		// 判断父节点是否还有其他子节点，如果没有，把isParent该为false
		Long parentId = tbContentCategory.getParentId();
		List<TbContentCategory> list = contentCategoryMapper.selectByParentId(parentId);
		if (list == null || list.isEmpty()) {
			TbContentCategory tbContentCategory_db = contentCategoryMapper.selectByPrimaryKey(parentId);
			tbContentCategory_db.setIsParent(false);
			tbContentCategory_db.setUpdated(new Date());
			contentCategoryMapper.updateByPrimaryKey(tbContentCategory_db);
		}
		return E3Result.ok();
	}


}

