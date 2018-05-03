package cn.e3mall.user.test;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.util.DigestUtils;

public class UserTest {
	
	@Test
	public void userTest() {
		String pwd = "cuoai1995";
		String md5Pwd = DigestUtils.md5DigestAsHex(pwd.getBytes());
		System.out.println(md5Pwd);
		String s1 = "e10adc3949ba59abbe56e057f20f883e";
		System.out.println(s1.length());
		System.out.println(md5Pwd.length());
	}
	
	@Test
	public void getCookie() {
		String val = getDomainName();
		System.out.println(val);
	}
	
	private static final String getDomainName() {
		String domainName = null;

		String serverName = "http://118.25.14.35:8082/";
		if (StringUtils.isBlank(serverName)) {
			domainName = "";
		} else {
			serverName = serverName.toLowerCase().substring(7);
			final int end = serverName.indexOf("/");
			serverName = serverName.substring(0, end);
			final String[] domains = serverName.split("\\.");
			int len = domains.length;
			if (len > 3) {
				// www.xxx.com.cn
				//domainName = "." + domains[len - 3] + "." + domains[len - 2] + "." + domains[len - 1];
				domainName  = serverName;
			} else if (len <= 3 && len > 1) {
				// xxx.com or xxx.cn
				domainName = "." + domains[len - 2] + "." + domains[len - 1];
			} else {
				domainName = serverName;
			}
		}

		if (domainName != null && domainName.indexOf(":") > 0) {
			String[] ary = domainName.split("\\:");
			domainName = ary[0];
		}
		return domainName;
	}
}
