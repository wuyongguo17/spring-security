package com.imooc.web.async;

import java.util.concurrent.Callable;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
public class AsyncController {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MockQueue mockQueue;

	@Autowired
	private DeferredResultHodler deferredResultHodler;
	
	//使用DeferredResult
	@RequestMapping("/order1")
	public DeferredResult<String> deferredOrder() throws Exception {
		logger.info("主线程开始");

		String orderNumber = RandomStringUtils.randomNumeric(8);
		mockQueue.setPlaceOrder(orderNumber);

		DeferredResult<String> result = new DeferredResult<>();
		deferredResultHodler.getMap().put(orderNumber, result);
		logger.info("主线程返回");
		return result;
	}
	
	//使用Runable
	@RequestMapping("/order")
	public Callable<String> order(){
		logger.info("主线程开始");
		Callable<String> result = new Callable<String>() {
			//主线程瞬间返回，副线程执行了1秒，这样主线程可以有时间来处理其他的请求。
			@Override 
			public String call() throws Exception {
				logger.info("副线程开始");
			Thread.sleep(1000); logger.info("副线程返回"); return "success"; 
			} 
		};
		logger.info("主线程返回");
		return result;
	}
}
