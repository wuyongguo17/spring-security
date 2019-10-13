package com.imooc.web.async;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
//ContextRefreshedEvent事件是整个spring初始化完毕的事件
public class QueueListener implements ApplicationListener<ContextRefreshedEvent>{
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private MockQueue mockQueue;
	
	@Autowired
	private DeferredResultHodler deferredResultHodler;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		//新开一个线程
		new Thread(() -> {
			while(true) {
				if(StringUtils.isNoneBlank(mockQueue.getCompleteOrder())) {
					String orderNumber = mockQueue.getCompleteOrder();
					logger.info("返回订单处理结果" + orderNumber);
					deferredResultHodler.getMap().get(orderNumber).setResult("place order success");
					
					mockQueue.setCompleteOrder(null);
					
				}else {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
		}).start();
		
	}
}
