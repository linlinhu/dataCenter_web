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
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterBusinessCodeCaller;
import com.emin.platform.dataCenter.domain.BusinessCode;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import com.alibaba.fastjson.JSONObject;


@Controller
@RequestMapping("/businessCode")
public class BusinessCodeController  extends DCBaseController {
	
	@Autowired 
	@Qualifier("dcWebToDataCenterBusinessCodeCaller")
	private DcWebToDataCenterBusinessCodeCaller dcWebToDataCenterBusinessCodeCaller;	
	
	//查询所有
	@RequestMapping("/loadBusinessCode")
	@ResponseBody
	public JSONObject loadBusinessCode(){
		List<Condition> conditions = new ArrayList<>();
		conditions.add(new Condition("status",ConditionOperator.EQ,ConditionType.OTHER,BusinessCode.STATUS_VALID) );
		List<BusinessCode>  businessCodePage= dcWebToDataCenterBusinessCodeCaller.findBusinessCodes(conditions);
			
		return successResult(businessCodePage);
    }
	
	
	
	@RequestMapping("/form")
	@ApiOperation(httpMethod="GET", value = "加载表单界面")
	@ApiImplicitParam(paramType="query",name="id",value="编号")
	public void editReport(Long id){
		Map<String, Object> data = new HashMap<>();
		
		printFtl("modules/businessCode/form", data);
	}
	
	
	
	
}
