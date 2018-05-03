package cn.e3mall.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.TbItemDescService;

@Service(value="tbTtemDescServiceImpl")
public class TbItemDescServiceImpl  implements TbItemDescService {

	@Value("${ITEM_INFO_PRE}")
	private String ITEM_INFO_PRE;
	@Value("${ITEM_INFO_EXPIRE}")
	private Integer ITEM_INFO_EXPIRE;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Autowired
	private TbItemDescMapper tbItemDescMapper;

	@Override
	public TbItemDesc findById(Long itemId) {
		// 查询缓存
		String key = ITEM_INFO_PRE + ":" + itemId + ":DESC";
		try {
			String jsonString = jedisClient.get(key);
			if (StringUtils.isNotBlank(jsonString)) {
				return JsonUtils.jsonToPojo(jsonString, TbItemDesc.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 查询数据库
		TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);

		if (tbItemDesc != null) {
			try {
				// 添加缓存
				jedisClient.set(key, JsonUtils.objectToJson(tbItemDesc));
				// 设置缓存过期时间
				jedisClient.expire(key, ITEM_INFO_EXPIRE);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return tbItemDesc;
	}

}
