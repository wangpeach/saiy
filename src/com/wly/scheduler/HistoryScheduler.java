package com.wly.scheduler;

import com.wly.service.CqsscService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Component
@Service(value = "historyScheduler")
public class HistoryScheduler {

	@Resource
	private CqsscService cqsscService;

	//每晚备份
	public void exec() {
		cqsscService.holdCodes(null);
	}
}
