package com.emin.dataCenterWeb.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.emin.base.exception.EminException;
import com.emin.base.service.Condition;
import com.emin.base.service.Condition.ConditionOperator;
import com.emin.base.service.Condition.ConditionType;
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterConfigCaller;
import com.emin.platform.dataCenter.domain.Config;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;


@Controller
@RequestMapping("/systemConfig")
public class SystemConfigController  extends DCBaseController {
	private Logger logger = LoggerFactory.getLogger(ProductionLineController.class);
	
	@Autowired 
	@Qualifier("dcWebToDataCenterConfigCaller")
	private DcWebToDataCenterConfigCaller dcWebToDataCenterConfigCaller;
	
	
	//查询第一级
	@RequestMapping("/loadSystemConfig")
	@ResponseBody
	public JSONObject loadSystemConfig(){
		JSONObject json = new JSONObject();
   		String message = "";
		boolean success = false;
		
		try {
			List<Condition> conditions = new ArrayList<>();
			conditions.add(new Condition(Config.PROP_STATUS, ConditionOperator.EQ, ConditionType.OTHER, Config.STATUS_VALID));
			conditions.add(new Condition(Config.PRO_CONFIG_PARENTID, ConditionOperator.EQ, ConditionType.OTHER, null));
			List<Config>  systemConfigList = dcWebToDataCenterConfigCaller.findConfigs(conditions);
			json.put("data", systemConfigList);
            success = true;
		} catch (EminException e) {
			success = false;
			message = e.getLocalizedMessage();
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			success = false;
			message = "获取配置信息失败";
			logger.error(e.getMessage(),e);
		}
		json.put("success", success);
		json.put("message", message);
		return json;
    }
	
	
	//查询所有parentId的子节点
	@RequestMapping("/loadAsyncSystemConfig")
	@ResponseBody
	public List<Config> loadAsyncSystemConfig(Long id){
		JSONObject json = new JSONObject();
   		String message = "";
		boolean success = false;
		List<Config>  systemConfigList = null;
		try {	
			
			List<Condition> conditions = new ArrayList<>();
			conditions.add(new Condition(Config.PROP_STATUS, ConditionOperator.EQ, ConditionType.OTHER, Config.STATUS_VALID));
			conditions.add(new Condition(Config.PRO_CONFIG_PARENTID, ConditionOperator.EQ, ConditionType.OTHER, id));
			systemConfigList = dcWebToDataCenterConfigCaller.findConfigs(conditions);
			json.put("data", systemConfigList);
            success = true;
		} catch (EminException e) {
			success = false;
			message = e.getLocalizedMessage();
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			success = false;
			message = "获取数据失败";
			logger.error(e.getMessage(),e);
		}
		json.put("success", success);
		json.put("message", message);
		return systemConfigList;
    }
		
	//查询查询第一级
	@RequestMapping("/index")
	@ResponseBody
	public void index(){		
		Map<String, Object> data = new HashMap<>();		
		
		printFtl("modules/system_config/manage", data);
    }	
	
	//保存
	@RequestMapping("/saveSystemConfig")
	@ResponseBody
	public JSONObject saveSystemConfig(Long id,String name, String value, String explain, Long parentId){		
		JSONObject json = new JSONObject();
   		String message = "";
		boolean success = false;
		
		try {
			Config config;
			if (id == null) {
				config = new Config();
				config.setName(name);
				config.setValue(value);
				config.setExplain(explain);
				config.setParentId(parentId);
			}else {
				config = dcWebToDataCenterConfigCaller.findById(id);
				config.setName(name);
				config.setValue(value);
				config.setExplain(explain);
			}
			
			dcWebToDataCenterConfigCaller.saveOrUpdateConfig(config);
            success = true;
		} catch (EminException e) {
			success = false;
			message = e.getLocalizedMessage();
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			success = false;
			message = "保存配置失败";
			logger.error(e.getMessage(),e);
		}
		json.put("success", success);
		json.put("message", message);
		return json;
    }
	
	//查看详情
	@RequestMapping("/findSystemConfigById")
	@ResponseBody
	public JSONObject findSystemConfigById(Long id){		
		JSONObject json = new JSONObject();
   		String message = "";
		boolean success = false;
		
		try {
			Config config = dcWebToDataCenterConfigCaller.findById(id);
			json.put("data", config);
            success = true;
		} catch (EminException e) {
			success = false;
			message = e.getLocalizedMessage();
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			success = false;
			message = "查看详情失败";
			logger.error(e.getMessage(),e);
		}
		json.put("success", success);
		json.put("message", message);
		return json;
    }
	
	
	//删除
	@RequestMapping("/deleteSystemConfig")
	@ResponseBody
	public JSONObject deleteSystemConfig(Long[] ids){
		
		JSONObject json = new JSONObject();
   		String message = "";
		boolean success = false;
		
		try {
			for (Long id : ids) {
				dcWebToDataCenterConfigCaller.deleteConfig(id);
			}
            success = true;
            message = "删除配置成功";
		} catch (EminException e) {
			success = false;
			message = e.getLocalizedMessage();
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			success = false;
			message = "删除配置失败";
			logger.error(e.getMessage(),e);
		}
		json.put("success", success);
		json.put("message", message);
		return json;
    }	

	
	
	@RequestMapping("/form")
	@ApiOperation(httpMethod="GET", value = "加载表单界面")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType="query",name="id",value="编号"),
		@ApiImplicitParam(paramType="query",name="parentId",value="父级编号")
	})
	public void editReport(Long id, Long parentId){
		Map<String, Object> data = new HashMap<>();
		data.put("parentId", parentId);
		if(id != null) {
			Config config = dcWebToDataCenterConfigCaller.findById(id);
			data.put("config", config);
			if (config != null) {
				parentId = config.getParentId();
			}
			data.put("parentId", parentId);
		}
		
		printFtl("modules/system_config/form", data);
	}
	
}
