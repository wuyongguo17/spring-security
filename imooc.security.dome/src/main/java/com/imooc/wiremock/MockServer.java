package com.imooc.wiremock;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.removeAllMappings;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;

public class MockServer {
	public static void main(String[] args) throws IOException {
		configureFor(8062); //端口号，WireMock设置的端口号
		removeAllMappings(); //清掉以前的配置
		
		mock("/order/1", "01");
		
	}

	private static void mock(String url, String file) throws IOException {
		ClassPathResource resource = new ClassPathResource("/mock/response/"+file+".txt");
		String content = StringUtils.join(FileUtils.readLines(resource.getFile(),"utf-8").toArray(),"\n");//从文件中读取内容
		stubFor(get(urlPathEqualTo(url)).willReturn(aResponse().withBody(content).withStatus(200)));
	}
}
