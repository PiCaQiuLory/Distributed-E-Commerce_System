package cn.e3mall.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemCatMapper;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.mapper.TbItemParamMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemCat;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.pojo.TbItemExample.Criteria;
import cn.e3mall.pojo.TbItemParam;
import cn.e3mall.pojo.TbItemParamExample;
import cn.e3mall.pojo.TbItemParamVo;
import cn.e3mall.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;
	
	@Autowired
	private TbItemCatMapper tbItemCatMapper;
	
	@Autowired
	private TbItemParamMapper tbItemParamMapper;

	@Autowired
	private TbItemDescMapper itemDescMapper;

	@Resource(name="jmsTemplate")
	private JmsTemplate jmsTemplate;

	@Resource(name="topicDestination")
	private Destination destination;

	@Autowired
	private JedisClient jedisClient;

	@Value("${ITEM_INFO_PRE}")
	private String ITEM_INFO_PRE;

	@Value("${ITEM_INFO_EXPIRE}")
	private Integer ITEM_INFO_EXPIRE;

	@Override
	public TbItem getItemById(long itemId) {

		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(itemId);
		List<TbItem> items = itemMapper.selectByExample(example);
		if (items != null && items.size() > 0) {
			return items.get(0);
		}
		return null;
	}

	@Override
	public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
		PageHelper.startPage(page, rows);
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		PageInfo<TbItem> info = new PageInfo<>(list);
		EasyUIDataGridResult result = new EasyUIDataGridResult(info.getTotal(), list);
		return result;
	}

	@Override
	public E3Result addItem(TbItem item, String desc) {
		// 生成商品id
		final long itemId = IDUtils.genItemId();
		// 补全item的属性
		item.setId(itemId);
		// 商品状态，1-正常，2-下架，3-删除
		item.setStatus((byte) 1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		// 向商品表插入数据
		itemMapper.insert(item);
		// 创建一个商品描述表对应的pojo对象
		TbItemDesc itemDesc = new TbItemDesc();
		// 补全属性
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		// 向商品描述表插入数据
		itemDescMapper.insert(itemDesc);
		// 发送商品添加消息
		jmsTemplate.send(destination, new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage message = session.createTextMessage(itemId + "");
				return message;
			}
		});

		// 返回成功
		return E3Result.ok();
	}

	/**
	 * 批量删除
	 */
	@Override
	public E3Result deleteItem(long[] ids) {
		int num = itemMapper.deleteItem(ids);
		if (num > 0) {
			return E3Result.ok();
		} else {
			return E3Result.build(1, "删除失败");
		}

	}

	/**
	 * 批量下架
	 */
	@Override
	public E3Result downItem(long[] ids) {
		int num = itemMapper.downItem(ids);
		if (num > 0) {
			return E3Result.ok();
		} else {
			return E3Result.build(1, "下架失败");
		}
	}

	/**
	 * 批量上架
	 */
	@Override
	public E3Result upItem(long[] ids) {
		int num = itemMapper.upItem(ids);
		if (num > 0) {
			return E3Result.ok();
		} else {
			return E3Result.build(1, "上架失败");
		}
	}

	@Override
	public TbItemDesc getItemDescById(long itemId) { // 查询缓存 try {

		String json = jedisClient.get(ITEM_INFO_PRE + ":" + itemId + ":DESC");
		try {
			if (StringUtils.isNotBlank(json)) {
				TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return tbItemDesc;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 缓存中没有，查询数据库 //
		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId); // 把结果添加到缓存
		try {
			jedisClient.set(ITEM_INFO_PRE + ":" + itemId + ":DESC", JsonUtils.objectToJson(itemDesc));
			// 设置过期时间
			jedisClient.expire(ITEM_INFO_PRE + ":" + itemId + ":DESC", ITEM_INFO_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemDesc;
	}

	@Override
	public EasyUIDataGridResult getParamList(Integer page, Integer rows) {
		PageHelper.startPage(page, rows);
		List<TbItemParamVo> list = tbItemParamMapper.getParamList();
		PageInfo<TbItemParamVo> info = new PageInfo<>(list);
		EasyUIDataGridResult result = new EasyUIDataGridResult(info.getTotal(), list);
		return result;
	}

	@Override
	public E3Result getParamByCid(long itemcatid) {
		TbItemCat tbItemCat = tbItemCatMapper.selectByPrimaryKey(itemcatid);
		return E3Result.ok(tbItemCat);
	}

}
