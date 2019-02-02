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
import com.emin.base.service.Condition;
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterDeviceCaller;
import com.emin.platform.dataCenter.domain.Device;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;


@Controller
@RequestMapping("/device")
public class DeviceController extends DCBaseController {
	private Logger logger = LoggerFactory.getLogger(DeviceController.class);
	
	@Autowired 
	@Qualifier("dcWebToDataCenterDeviceCaller")
	private DcWebToDataCenterDeviceCaller dcWebToDataCenterDeviceCaller;
	
	
	//查询所有设备
	@RequestMapping("/loadDevice")
	@ResponseBody
	public JSONObject loadDevice(String keyword){
		logger.info("keyword:"+keyword);
		List<Condition> conditions = new ArrayList<>();

		PagedResult<Device>  devicePage = null;
		if (StringUtils.isNotBlank(keyword)) {
			devicePage = dcWebToDataCenterDeviceCaller.loadPagedDevicesByMatch(getPageRequestData(), StringUtils.split(keyword," "));
		}else {
			devicePage= dcWebToDataCenterDeviceCaller.loadPagedDevicesByCondition(getPageRequestData(), conditions);
		}
            
		return successResult(devicePage);
    }
	
	//查询所有设备
	@RequestMapping("/loadDeviceByProductionLineId")
	@ResponseBody
	public JSONObject loadDeviceByProductionLineId(Long productionLineId,String model){
		logger.info("ProductionLineId = {},model = {}",productionLineId,model);
		
		List<Device>  devices = dcWebToDataCenterDeviceCaller.getloadDeviceByProductionLineIdOrModel(productionLineId,model);
		
		return successResult(devices);
    }
	
	//搜索
	@RequestMapping("/index")
	@ResponseBody
	public void searchDevice(String keyword){
		logger.info("keyword:"+keyword);
		Map<String, Object> data = new HashMap<>();

		PageRequest pageRequest = getPageRequestData();
		List<Condition> conditions = new ArrayList<>();
		PagedResult<Device>  devicePage = null;
		try {
			if (StringUtils.isNotBlank(keyword)) {
				devicePage = dcWebToDataCenterDeviceCaller.loadPagedDevicesByMatch(pageRequest, StringUtils.split(keyword," "));
			}else {
				devicePage= dcWebToDataCenterDeviceCaller.loadPagedDevicesByCondition(pageRequest, conditions);
			}
		}catch (Exception e){
			printFtl("500", null);
		}
		
		data.put("devices", devicePage);
		data.put("cur", pageRequest.getCurrentPage());
		data.put("limit", pageRequest.getLimit());
		data.put("keyword", keyword);
		
		printFtl("modules/device/manage", data);
    }	
	
	//保存  
	@RequestMapping("/saveDevice")
	@ResponseBody
	public JSONObject saveDevice(String jsonStr){
		logger.info("jsonStr:"+jsonStr);
		Device device = JSON.parseObject(jsonStr, Device.class);
		dcWebToDataCenterDeviceCaller.saveOrUpdateDevice(device);

		return successResult();
    }	
		
	
	//删除
	@RequestMapping("/deleteDevice")
	@ResponseBody
	public JSONObject deleteDevice(Long[] ids){
		for (Long longTemp : ids) {
			dcWebToDataCenterDeviceCaller.deleteDevice(longTemp);
		}

		return successResult();
    }		
	
	@RequestMapping("/form")
	@ApiOperation(httpMethod="GET", value = "加载表单界面")
	@ApiImplicitParam(paramType="query",name="id",value="编号")
	public void editReport(Long id){
		Map<String, Object> data = new HashMap<>();
		
		if(id != null) {
			Device device = dcWebToDataCenterDeviceCaller.findById(id);
			data.put("device", device);
		}
		printFtl("modules/device/form", data);
	}
	
	
}
