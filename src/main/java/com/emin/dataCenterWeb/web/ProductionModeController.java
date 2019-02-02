package com.emin.dataCenterWeb.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.emin.base.dao.PageRequest;
import com.emin.base.dao.PagedResult;
import com.emin.base.exception.EminException;
import com.emin.base.service.Condition;
import com.emin.base.service.Condition.ConditionOperator;
import com.emin.base.service.Condition.ConditionType;
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterProductionModeCaller;
import com.emin.platform.dataCenter.domain.ProductionMode;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;


@Controller
@RequestMapping("/productionMode")
public class ProductionModeController  extends DCBaseController {
	private Logger logger = LoggerFactory.getLogger(ProductionModeController.class);
	
	@Autowired 
	@Qualifier("dcWebToDataCenterProductionModeCaller")
	private DcWebToDataCenterProductionModeCaller dcWebToDataCenterProductionModeCaller;
	
	//查询所有生产模式
	@RequestMapping("/loadProductionMode")
	@ResponseBody
	public void loadProductionMode(){
		
		List<Condition> conditions = new ArrayList<>();
		conditions.add(new Condition(ProductionMode.PROP_STATUS, ConditionOperator.EQ, ConditionType.OTHER, ProductionMode.STATUS_VALID));
		PagedResult<ProductionMode>  productionModePage= dcWebToDataCenterProductionModeCaller.loadPagedProductionModesByCondition(getPageRequestData(), conditions);
		
		JSONObject json = new JSONObject();
   		String message = "";
		boolean success = true;
		json.put("data", productionModePage);
		json.put("success", success);
		json.put("message", message);
		printJson(json);
		//return successResult(productionModePage);	
    }
	
	/**
	 * 查询并返回页面息
	 * @param keyword 模糊查询字段（生产线名称、生产模式名称）
	 */
	@RequestMapping("/index")
	@ResponseBody
	public void searchProductionMode(String keyword){
		
		Map<String, Object> data = new HashMap<>();
		
		try {
			PageRequest pageRequest = getPageRequestData();
			List<Condition> conditions = new ArrayList<>();
			PagedResult<ProductionMode>  productionModePage = null;		
			
            productionModePage= dcWebToDataCenterProductionModeCaller.loadPagedProductionModesByMatch(pageRequest, StringUtils.split(keyword," "));			
				
			data.put("data", productionModePage);
			data.put("keyword", keyword);
			data.put("cur", pageRequest.getCurrentPage());
			data.put("limit", pageRequest.getLimit());
			
		} catch (EminException e) {
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		printFtl("modules/production_mode/manage", data);
    }	
	
	/**
	 * 保存
	 * @param jsonStr 需要保存的信息
	 */
	@RequestMapping("/saveProductionMode")
	@ResponseBody
	public JSONObject saveProductionMode(String jsonStr){
		ProductionMode productionMode = JSON.parseObject(jsonStr, ProductionMode.class);
		dcWebToDataCenterProductionModeCaller.saveOrUpdateProductionMode(productionMode);
		return successResult();
    }	
	
	
	
	/**
	 * 删除
	 * @param ids 删除项的id
	 */
	@RequestMapping("/deleteProductionMode")
	@ResponseBody
	public JSONObject deleteProductionMode(Long[] ids){
		for (Long longTemp : ids) {
			dcWebToDataCenterProductionModeCaller.deleteProductionMode(longTemp);
		}
		
		return successResult();
    }		
	
	/**
	 * 通过ID，查看生产模式
	 * @param id 生产模式ID
	 */
	@RequestMapping("/findProductionModeById")
	@ResponseBody
	public void findProductionModeById(Long id){	
	
		ProductionMode productionMode = dcWebToDataCenterProductionModeCaller.findById(id);	
		
		JSONObject json = new JSONObject();
   		String message = "";
		boolean success = true;
		json.put("data", productionMode);
		json.put("success", success);
		json.put("message", message);
		printJson(json);
		//return successResult(productionMode);
	}
	
	/**
	 * 通过生产线ID查询生产模式	
	 * @param productionLineId  生产线ID
     * @param modeType  生产模式类型
	 * @param modeType  生产模式类型
	 */
	@RequestMapping("/findProductionMode")
	@ResponseBody
	public void findProductionMode(Long productionLineId,Integer modeType){	
		
		List<Condition> conditions = new ArrayList<>();
		if (null != productionLineId) {
			conditions.add(new Condition(ProductionMode.PROP_PRODUCTIONLINE_ID, ConditionOperator.EQ, ConditionType.OTHER, productionLineId));
		}
		if (null != modeType) {
			conditions.add(new Condition("modeType", ConditionOperator.EQ, ConditionType.OTHER, modeType));
		}
		conditions.add(new Condition(ProductionMode.PROP_STATUS, ConditionOperator.EQ, ConditionType.OTHER, ProductionMode.STATUS_VALID));
		
		List<ProductionMode> productionModes = dcWebToDataCenterProductionModeCaller.findProductionModes(conditions);			
		
		
		JSONObject json = new JSONObject();
   		String message = "";
		boolean success = true;
		json.put("data", productionModes);
		json.put("success", success);
		json.put("message", message);
		printJson(json);
		
		
		//return successResult(productionModes);
	}
	
	/**
	 * 发布生产模式
	 * @param id 生产模式ID
	 */
	@RequestMapping("/publishProductionMode")
	@ResponseBody
	public JSONObject publishProductionMode(Long id){	
	
		dcWebToDataCenterProductionModeCaller.publishProductionMode(id);
		
		return successResult();
	}
	
	/**
	 * form页面
	 * @param id 查询项的id
	 */
	@RequestMapping("/form")
	@ApiOperation(httpMethod="GET", value = "加载表单界面")
	@ApiImplicitParam(paramType="query",name="id",value="编号")
	public void editReport(Long id){
		Map<String, Object> data = new HashMap<>();
		if(id != null) {
			ProductionMode productionMode = dcWebToDataCenterProductionModeCaller.findById(id);
			data.put("productionMode", productionMode);
		}
		printFtl("modules/production_mode/form", data);
	}
	
}
