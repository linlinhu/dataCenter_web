package com.emin.dataCenterWeb.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*import org.apache.commons.lang3.StringUtils;*/
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
/*import com.emin.base.dao.PageRequest;*/
/*import com.emin.base.dao.PagedResult;*/
import com.emin.base.exception.EminException;
import com.emin.base.service.Condition;
import com.emin.base.service.Condition.ConditionOperator;
import com.emin.base.service.Condition.ConditionType;
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterProductionLineCaller;
import com.emin.platform.dataCenter.domain.ProductionLine;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import com.alibaba.fastjson.JSONObject;


@Controller
@RequestMapping("/productionLine")
public class ProductionLineController  extends DCBaseController {
	private Logger logger = LoggerFactory.getLogger(ProductionLineController.class);
	
	@Autowired 
	@Qualifier("dcWebToDataCenterProductionLineCaller")
	private DcWebToDataCenterProductionLineCaller dcWebToDataCenterProductionLineCaller;
	
	
	//查询所有
	@RequestMapping("/loadProductionLine")
	@ResponseBody
	public JSONObject loadProductionLine(){

		List<Condition> conditions = new ArrayList<>();
		//conditions.add(new Condition(ProductionLine.PROP_PARENT_ID, ConditionOperator.EQ, ConditionType.OTHER, null));
		//PagedResult<ProductionLine>  productionLinePage= dcWebToDataCenterProductionLineCaller.loadPagedProductionLinesByCondition(getPageRequestData(), conditions);	
		conditions.add(new Condition(ProductionLine.PROP_STATUS, ConditionOperator.EQ, ConditionType.OTHER, ProductionLine.STATUS_VALID));
		List<ProductionLine>  productionLineList = dcWebToDataCenterProductionLineCaller.findProductionLines(conditions);
			
		return successResult(productionLineList);
    }
	
	
	//查询所有parentId的子生产线
	@RequestMapping("/loadAsyncProductionLine")
	@ResponseBody
	public JSONObject loadAsyncProductionLine(Long parentId){
			
		JSONArray  productionLine = dcWebToDataCenterProductionLineCaller.loadAsyncLineTree(parentId);						
		return successResult(productionLine);
    }
		
	//搜索
	@RequestMapping("/index")
	@ResponseBody
	public void index(){
		
		Map<String, Object> data = new HashMap<>();
			
		List<Condition> conditions = new ArrayList<>();
		conditions.add(new Condition(ProductionLine.PROP_STATUS, ConditionOperator.EQ, ConditionType.OTHER, ProductionLine.STATUS_VALID));
		List<ProductionLine>  productionLineList = dcWebToDataCenterProductionLineCaller.findProductionLines(conditions);
		data.put("productionLines", productionLineList);
		
		printFtl("modules/production_line/manage", data);
    }	
	
	//保存
	@RequestMapping("/saveProductionLine")
	@ResponseBody
	public JSONObject saveProductionLine(String jsonStr){
		
		JSONObject json = new JSONObject();
   		String message = "";
		boolean success = false;
		
		try {
			
			ProductionLine productionLine = JSON.parseObject(jsonStr, ProductionLine.class);
			dcWebToDataCenterProductionLineCaller.saveOrUpdateProductionLine(productionLine);
			
            success = true;
		} catch (EminException e) {
			success = false;
			message = e.getLocalizedMessage();
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			success = false;
			message = "保存生产线失败";
			logger.error(e.getMessage(),e);
		}
		json.put("success", success);
		json.put("message", message);
		return json;
    }	
	
	//通过ID查找ProductionLine
	@RequestMapping("/findProductionLineById")
	@ResponseBody
	public JSONObject findProductionLineById(Long id){
		
		JSONObject json = new JSONObject();
   		String message = "";
		boolean success = false;
		
		try {
			
			ProductionLine productionLine = dcWebToDataCenterProductionLineCaller.findById(id);
			json.put("productionLine", productionLine);
			
            success = true;
		} catch (EminException e) {
			success = false;
			message = e.getLocalizedMessage();
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			success = false;
			message = "通过ID查找生产线失败";
			logger.error(e.getMessage(),e);
		}
		json.put("success", success);
		json.put("message", message);
		return json;
    }	
	
	//删除
	@RequestMapping("/deleteProductionLine")
	@ResponseBody
	public JSONObject deleteProductionLine(Long[] ids){
		
		JSONObject json = new JSONObject();
   		String message = "";
		boolean success = false;
		
		try {
			for (Long longTemp : ids) {
				dcWebToDataCenterProductionLineCaller.deleteProductionLine(longTemp);
			}
			
            success = true;
		} catch (EminException e) {
			success = false;
			message = e.getLocalizedMessage();
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			success = false;
			message = "删除生产线失败";
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
	public void editReport(Long id, Long parentId, String parentName){
		Map<String, Object> data = new HashMap<>();
		data.put("parentId", parentId);
		data.put("parentName", parentName);
		if(id != null) {
			ProductionLine productionLine = dcWebToDataCenterProductionLineCaller.findById(id);
			data.put("productionLine", productionLine);
		}
		
		printFtl("modules/production_line/form", data);
	}
	
	
	
	
}
