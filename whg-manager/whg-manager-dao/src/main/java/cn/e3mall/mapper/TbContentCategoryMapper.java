package cn.e3mall.mapper;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.pojo.TbContentCategory;
import java.util.List;

public interface TbContentCategoryMapper {
	int deleteByPrimaryKey(Long id);

    int insert(TbContentCategory record);

    TbContentCategory selectByPrimaryKey(Long id);

    List<TbContentCategory> selectAll();

    int updateByPrimaryKey(TbContentCategory record);

	List<EasyUITreeNode> getContentCategoryList(Long parentId);

	List<TbContentCategory> selectByParentId(Long parentId);
    
    
}