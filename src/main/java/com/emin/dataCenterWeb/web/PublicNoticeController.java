package com.emin.dataCenterWeb.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSON;
import com.emin.base.dao.PageRequest;
import com.emin.base.dao.PagedResult;
import com.emin.base.service.Condition;
import com.emin.base.service.Condition.ConditionOperator;
import com.emin.base.service.Condition.ConditionType;
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterPublicNoticeCaller;
import com.emin.platform.dataCenter.domain.PublicNotice;
import com.emin.platform.dataCenter.domain.User;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import com.alibaba.fastjson.JSONObject;


@Controller
@RequestMapping("/publicNotice")
public class PublicNoticeController  extends DCBaseController {
	
	@Autowired 
	@Qualifier("dcWebToDataCenterPublicNoticeCaller")
	private DcWebToDataCenterPublicNoticeCaller dcWebToDataCenterPublicNoticeCaller;
	
	
	//查询所有
	@RequestMapping("/loadPublicNotice")
	@ResponseBody
	public JSONObject loadPublicNotice(){
			
		List<Condition> conditions = new ArrayList<>();		
		PagedResult<PublicNotice>  publicNoticePage= dcWebToDataCenterPublicNoticeCaller.loadPagedPublicNoticesByCondition(getPageRequestData(), conditions);
		
		return successResult(publicNoticePage);
			
    }
	
	//搜索
	@RequestMapping("/index")
	@ResponseBody
	public void searchPublicNotice(String keyword){		
		Map<String, Object> data = new HashMap<>();

		PageRequest pageRequest = getPageRequestData();
		List<Condition> conditions = new ArrayList<>();
		PagedResult<PublicNotice>  publicNoticePage = null;
		if (StringUtils.isNotBlank(keyword)) {
			publicNoticePage = dcWebToDataCenterPublicNoticeCaller.loadPagedPublicNoticesByMatch(pageRequest, StringUtils.split(keyword," "));
		}else {
			publicNoticePage= dcWebToDataCenterPublicNoticeCaller.loadPagedPublicNoticesByCondition(pageRequest, conditions);
		}
			
		data.put("pulicNotices", publicNoticePage);
		data.put("cur", pageRequest.getCurrentPage());
		data.put("limit", pageRequest.getLimit());
		data.put("keyword", keyword);

		printFtl("modules/public_notice/manage", data);
    }	
	
	//保存
	@RequestMapping("/savePublicNotice")
	@ResponseBody
	public JSONObject savePublicNotice(String jsonStr){

		PublicNotice publicNotice = JSON.parseObject(jsonStr, PublicNotice.class);
		User user = (User)getRequest().getSession().getAttribute("user");
		publicNotice.setPublisher(user.getAcount());
	    dcWebToDataCenterPublicNoticeCaller.saveOrUpdatePublicNotice(publicNotice);
	    return successResult();
			
    }	
	
	
	
	//删除
	@RequestMapping("/deletePublicNotice")
	@ResponseBody
	public JSONObject deletePublicNotice(Long[] ids){
		
		for (Long longTemp : ids) {
			dcWebToDataCenterPublicNoticeCaller.deletePublicNotice(longTemp);
		}
		return successResult();
           
    }	

	//公告展示首页
	@RequestMapping("/board")
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
	
	@RequestMapping("/form")
	@ApiOperation(httpMethod="GET", value = "加载表单界面")
	@ApiImplicitParam(paramType="query",name="id",value="编号")
	public void editReport(Long id){
		Map<String, Object> data = new HashMap<>();
		
		if(id != null) {
			PublicNotice publicNotice = dcWebToDataCenterPublicNoticeCaller.findById(id);
			data.put("publicNotice", publicNotice);
		}
		printFtl("modules/public_notice/form", data);
	}
	
	
	
	
}
