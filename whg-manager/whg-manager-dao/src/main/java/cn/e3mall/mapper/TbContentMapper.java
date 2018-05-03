package cn.e3mall.mapper;

import cn.e3mall.pojo.TbContent;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbContentMapper {
	int deleteByPrimaryKey(Long id);

    int insert(TbContent record);

    TbContent selectByPrimaryKey(Long id);

    List<TbContent> selectAll();

    int updateByPrimaryKey(TbContent record);

	List<TbContent> queryListByCategoryId(Long categoryId);

	void deleteByIds(@Param("contentIds") String[] contentIds);
}