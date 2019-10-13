package com.imooc.security.core.social.qq.connect;

import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.oauth2.OAuth2Template;

import com.imooc.security.core.social.qq.api.QQ;
import com.imooc.security.core.social.qq.api.QQImpl;


//服务提供商(qq)的代码
public class QQServiceProvider extends AbstractOAuth2ServiceProvider<QQ> {
	// 每个qq用户都有唯一的appId
	private String appId;
	private static final String URL_AUTHORIZE = "https://graph.qq.com/oauth2.0/authorize";

	private static final String URL_ACCESS_TOKEN = "https://graph.qq.com/oauth2.0/token";

	public QQServiceProvider(String appId, String appSecret) {
//		super(new OAuth2Template(appId, appSecret, URL_AUTHORIZE, URL_ACCESS_TOKEN));
		super(new QQOAuth2Template(appId, appSecret, URL_AUTHORIZE, URL_ACCESS_TOKEN)); //使用自己定义的OAuth2Template
	}

	@Override
	public QQ getApi(String accessToken) {

		return new QQImpl(accessToken, appId);
	}

}
