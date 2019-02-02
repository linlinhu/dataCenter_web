package com.emin.dataCenterWeb.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.emin.base.service.Condition;
import com.emin.base.service.Condition.ConditionOperator;
import com.emin.base.service.Condition.ConditionType;
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterPublicNoticeCaller;
import com.emin.platform.dataCenter.domain.PublicNotice;


@Controller
@RequestMapping("/start")
public class StartController  extends DCBaseController {
	
	@Autowired 
	@Qualifier("dcWebToDataCenterPublicNoticeCaller")
	private DcWebToDataCenterPublicNoticeCaller dcWebToDataCenterPublicNoticeCaller;
	
	//公告展示首页
	@RequestMapping("/index")
	@ResponseBody
	public void board(){
		Map<String, Object> data = new HashMap<>();
		
		List<Condition> conditions = new ArrayList<Condition>();
		conditions.add(new Condition(PublicNotice.PROP_END_TIME, ConditionOperator.GE, ConditionType.OTHER, System.currentTimeMillis()));
		conditions.add(new Condition(PublicNotice.PROP_STATUS, ConditionOperator.EQ, ConditionType.OTHER, PublicNotice.STATUS_VALID));
						
		List<PublicNotice>  publicNoticeList= dcWebToDataCenterPublicNoticeCaller.findPublicNotices(conditions);
		data.put("pulicNotices", publicNoticeList);
		printFtl("modules/start/board", data);
			
    }
}
