package com.wly.scheduler;

import com.wly.service.CqsscService;
import com.wly.utils.FileOperate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;

@Component
@Service(value = "historyScheduler")
public class HistoryScheduler {

	public static final String SAVEPAHT = "D://lshm//";

	@Resource
	private CqsscService cqsscService;

	//每晚备份
	public void exec() {
		Calendar cal = Calendar.getInstance();
		String day = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH);
		String path = SAVEPAHT + day + ".json";
		//获取项目目录
		//FileOperate.getRootPath("saiy");
		//获取历史
		FileOperate.createNewFile(cqsscService.synchronize(day), path);
	}
	
}
