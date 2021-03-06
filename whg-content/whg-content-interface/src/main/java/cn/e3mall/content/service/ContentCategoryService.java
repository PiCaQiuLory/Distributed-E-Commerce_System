package cn.e3mall.content.service;

import java.util.List;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.utils.E3Result;

public interface ContentCategoryService {
	
	/**
	 * 内容分类tree
	 * 
	 * @param parentId
	 * @return
	 */
	List<EasyUITreeNode> getContentCategoryList(Long parentId);

	/**
	 * 内容分类添加节点
	 * 
	 * @param parentId
	 * @param name
	 * @return
	 */
	E3Result addContentCategory(Long parentId, String name);

	/**
	 * 修改内容分类节点
	 * 
	 * @param id
	 * @param name
	 * @return
	 */
	E3Result updateContentCategory(Long id, String name);

	/**
	 * 删除内容分类节点
	 * 
	 * @param id
	 * @return
	 */
	E3Result deleteContentCategory(Long id);

}
