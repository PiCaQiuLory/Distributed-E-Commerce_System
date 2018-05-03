package cn.e3mall.sso.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.json.JSON;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;

@Service
public class TokenServiceImpl  implements TokenService {

	@Value("${TOKEN_KEY}")
	private String TOKEN_KEY;
	@Value("${TOKEN_EXPIRE}")
	private int TOKEN_EXPIRE;
	
	@Autowired
	private JedisClient jedisClient;

	@Override
	public E3Result getUserByToken(String token) {
		// 根据token到redis中获取用户信息
		String key = TOKEN_KEY + ":" + token;
		String jsonString = jedisClient.get(key);
		if (StringUtils.isBlank(jsonString)) {
			// 取不到用户信息,登录已经过期,返回登录过期
			return E3Result.build(400, "用户登录已经过期，请重新登录。");
		}

		// 取到用户信息,更新token的过期时间
		jedisClient.expire(key, TOKEN_EXPIRE);

		// 返回结果,E3Result包含TbUser对象
		return E3Result.ok(JsonUtils.jsonToPojo(jsonString, TbUser.class));
	}

}
