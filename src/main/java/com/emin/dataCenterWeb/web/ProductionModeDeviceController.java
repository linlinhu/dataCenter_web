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
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterProductionModeDeviceCaller;
import com.emin.platform.dataCenter.domain.ProductionModeDevice;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;


@Controller
@RequestMapping("/productionModeDevice")
public class ProductionModeDeviceController  extends DCBaseController {
	private Logger logger = LoggerFactory.getLogger(ProductionModeDeviceController.class);
	
	@Autowired 
	@Qualifier("dcWebToDataCenterProductionModeDeviceCaller")
	private DcWebToDataCenterProductionModeDeviceCaller dcWebToDataCenterProductionModeDeviceCaller;
	
	//查询所有生产模式设备明细
	@RequestMapping("/loadProductionModeDevice")
	@ResponseBody
	public JSONObject loadProductionModeDevice(){
		
		List<Condition> conditions = new ArrayList<>();
		conditions.add(new Condition(ProductionModeDevice.PROP_STATUS, ConditionOperator.EQ, ConditionType.OTHER, ProductionModeDevice.STATUS_VALID));
		PagedResult<ProductionModeDevice>  productionModeDevicePage= dcWebToDataCenterProductionModeDeviceCaller.loadPagedProductionModeDevicesByCondition(getPageRequestData(), conditions);

		return successResult(productionModeDevicePage);	
    }
	
	/**
	 * 查询并返回页面息
	 * @param keyword 模糊查询字段（生产线名称、生产模式设备明细名称）
	 */
	@RequestMapping("/index")
	@ResponseBody
	public void searchProductionModeDevice(String keyword){
		
		Map<String, Object> data = new HashMap<>();
		
		try {
			PageRequest pageRequest = getPageRequestData();
			List<Condition> conditions = new ArrayList<>();
			PagedResult<ProductionModeDevice>  productionModeDevicePage = null;		
			
            productionModeDevicePage= dcWebToDataCenterProductionModeDeviceCaller.loadPagedProductionModeDevicesByMatch(pageRequest, StringUtils.split(keyword," "));			
				
			data.put("productionModeDevices", productionModeDevicePage);
			data.put("keyword", keyword);
			
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
	@RequestMapping("/saveProductionModeDevice")
	@ResponseBody
	public JSONObject saveProductionModeDevice(String jsonStr){
		List<ProductionModeDevice> productionModeDeviceList = JSON.parseArray(jsonStr, ProductionModeDevice.class);
		if (productionModeDeviceList.isEmpty()) {
			return successResult();
		}
		
		List<Condition> conditions = new ArrayList<>();
		conditions.add(new Condition("productionMode.id", ConditionOperator.EQ, ConditionType.OTHER, productionModeDeviceList.get(0).getProductionMode().getId()));	
		conditions.add(new Condition(ProductionModeDevice.PROP_STATUS, ConditionOperator.EQ, ConditionType.OTHER, ProductionModeDevice.STATUS_VALID));
		List<ProductionModeDevice> productionModeDevices = dcWebToDataCenterProductionModeDeviceCaller.findProductionModeDevices(conditions);	
		
		
		//查找需要删除的，并且删除
		if (productionModeDevices.size() > 0 ) {
			List<Long>  ids =new ArrayList();
			for (ProductionModeDevice productionModeDeviceOld : productionModeDevices) {
				boolean delete = true;
				for (ProductionModeDevice productionModeDeviceNew : productionModeDeviceList) {
					if ( null != productionModeDeviceNew.getId()  && productionModeDeviceNew.getId() > 0   && productionModeDeviceOld.getId().longValue() == productionModeDeviceNew.getId().longValue() ) {
						delete = false;
						break;
					}
				}
				if (delete) {
					ids.add(productionModeDeviceOld.getId());
				}
				
			}
			
			if (ids.size() > 0) {
				for (Long longTemp : ids) {
					dcWebToDataCenterProductionModeDeviceCaller.deleteProductionModeDevice(longTemp);
				}
			}			
		}
		
	
		if (productionModeDeviceList != null && productionModeDeviceList.size()>0) {
			for (ProductionModeDevice productionModeDeviceTemp : productionModeDeviceList) {
				dcWebToDataCenterProductionModeDeviceCaller.saveOrUpdateProductionModeDevice(productionModeDeviceTemp);
			}
		}
		return successResult();
    }	
	
	
	
	/**
	 * 删除
	 * @param ids 删除项的id
	 */
	@RequestMapping("/deleteProductionModeDevice")
	@ResponseBody
	public JSONObject deleteProductionModeDevice(Long[] ids){
		for (Long longTemp : ids) {
			dcWebToDataCenterProductionModeDeviceCaller.deleteProductionModeDevice(longTemp);
		}
		
		return successResult();
    }		
	
	/**
	 * 通过ID，查看生产模式设备明细
	 * @param id 生产模式设备明细ID
	 */
	@RequestMapping("/findProductionModeDeviceById")
	@ResponseBody
	public void findProductionModeDeviceById(Long id){	
	
		ProductionModeDevice productionModeDevice = dcWebToDataCenterProductionModeDeviceCaller.findById(id);	
		JSONObject json = new JSONObject();
   		String message = "";
		boolean success = true;
		json.put("data", productionModeDevice);
		json.put("success", success);
		json.put("message", message);
		printJson(json);
		//return successResult(productionModeDevice);
	}
	
	/**
	 * 通过生产模式ID查询生产模式设备明细	
	 * @param productionLineId  生产线ID
     * @param modeType  生产模式设备明细类型
	 */
	@RequestMapping("/findProductionModeDevice")
	@ResponseBody
	public void findProductionModeDevice(Long productionModeId){	
		
		List<Condition> conditions = new ArrayList<>();
		if (null != productionModeId) {
			conditions.add(new Condition("productionMode.id", ConditionOperator.EQ, ConditionType.OTHER, productionModeId));
		}
		
		conditions.add(new Condition(ProductionModeDevice.PROP_STATUS, ConditionOperator.EQ, ConditionType.OTHER, ProductionModeDevice.STATUS_VALID));
		
		List<ProductionModeDevice> productionModeDevices = dcWebToDataCenterProductionModeDeviceCaller.findProductionModeDevices(conditions);			
		
		//return successResult(productionModeDevices);
		JSONObject json = new JSONObject();
   		String message = "";
		boolean success = true;
		json.put("data", productionModeDevices);
		json.put("success", success);
		json.put("message", message);
		printJson(json);
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
			ProductionModeDevice productionModeDevice = dcWebToDataCenterProductionModeDeviceCaller.findById(id);
			data.put("productionModeDevice", productionModeDevice);
		}
		printFtl("modules/production_mode_device/form", data);
	}
	
}
