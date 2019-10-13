package com.imooc.web.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MockQueue {
	private String placeOrder; //下单的消息
	private String completeOrder; //订单完成的消息
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public String getPlaceOrder() {
		return placeOrder;
	}
	public void setPlaceOrder(String placeOrder) throws Exception {
		new Thread(() -> {
			logger.info("接到下单请求" + placeOrder);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.completeOrder = placeOrder;
			logger.info("接到下单请求处理完毕" + placeOrder);
		}).start();;
		
	}
	public String getCompleteOrder() {
		return completeOrder;
	}
	public void setCompleteOrder(String completeOrder) {
		this.completeOrder = completeOrder;
	}
	
	
}
