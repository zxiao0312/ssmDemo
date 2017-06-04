package com.zxiao.service.quartz;


import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * 定时任务
 */
@Service
public class QuartzServiceTest {
	private Logger logger = Logger.getLogger(QuartzServiceTest.class);

	
	public void test(){
		logger.error("定时器执行了!");
	}
}