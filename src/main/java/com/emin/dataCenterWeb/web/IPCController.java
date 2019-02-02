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
import com.emin.base.dao.PageRequest;
import com.emin.base.dao.PagedResult;
import com.emin.base.service.Condition;
import com.emin.base.service.Condition.ConditionOperator;
import com.emin.base.service.Condition.ConditionType;
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterIpcCaller;
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterProductionLineCaller;
import com.emin.platform.dataCenter.domain.IPC;
import com.emin.platform.dataCenter.domain.ProductionLine;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import com.alibaba.fastjson.JSONObject;


@Controller
@RequestMapping("/ipc")
public class IPCController  extends DCBaseController {
	private Logger logger = LoggerFactory.getLogger(IPCController.class);
	
	@Autowired 
	@Qualifier("dcWebToDataCenterIpcCaller")
	private DcWebToDataCenterIpcCaller dcWebToDataCenterIpcCaller;
	
	@Autowired 
	@Qualifier("dcWebToDataCenterProductionLineCaller")
	private DcWebToDataCenterProductionLineCaller dcWebToDataCenterProductionLineCaller;
	
	//查询所有
	@RequestMapping("/loadIpc")
	@ResponseBody
	public JSONObject loadIpc(){

		List<Condition> conditions = new ArrayList<>();		
		PagedResult<IPC>  ipcPage= dcWebToDataCenterIpcCaller.loadPagedIPCsByCondition(getPageRequestData(), conditions);

		return successResult(ipcPage);
    }
	
	//搜索
	@RequestMapping("/index")
	@ResponseBody
	public void searchIpc(String keyword){
		logger.info("keyword:"+keyword);
		/*JSONObject json = new JSONObject();
   		String message = "";
		boolean success = false;
		
		try {
			List<Condition> conditions = new ArrayList<>();
			PagedResult<IPC>  ipcPage = null;
			if (StringUtils.isNotBlank(keyword)) {
				ipcPage = dcWebToDataCenterIpcCaller.loadPagedIPCsByMatch(getPageRequestData(), StringUtils.split(" "));
			}else {
				ipcPage= dcWebToDataCenterIpcCaller.loadPagedIPCsByCondition(getPageRequestData(), conditions);
			}
				
			json.put("data", ipcPage);
            success = true;
		} catch (EminException e) {
			success = false;
			message = e.getLocalizedMessage();
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			success = false;
			message = "搜索车间失败";
			logger.error(e.getMessage(),e);
		}
		json.put("success", success);
		json.put("message", message);
		printJson(json);*/
		Map<String, Object> data = new HashMap<>();
		
		PageRequest pageRequest = getPageRequestData();
		List<Condition> conditions = new ArrayList<>();
		PagedResult<IPC>  ipcPage = null;
		if (StringUtils.isNotBlank(keyword)) {
			ipcPage = dcWebToDataCenterIpcCaller.loadPagedIPCsByMatch(pageRequest, StringUtils.split(keyword," "));
		}else {
			ipcPage= dcWebToDataCenterIpcCaller.loadPagedIPCsByCondition(pageRequest, conditions);
		}	
		data.put("data", ipcPage);
		data.put("cur", pageRequest.getCurrentPage());
		data.put("limit", pageRequest.getLimit());
		data.put("keyword", keyword);
		data.put("success", true);		
		
		printFtl("modules/ipc/manage", data);
		
    }	
	
	//保存
	@RequestMapping("/saveIpc")
	@ResponseBody
	public JSONObject saveIpc(String jsonStr){
		logger.info("jsonStr:"+jsonStr);
		IPC ipc = JSON.parseObject(jsonStr, IPC.class);
		dcWebToDataCenterIpcCaller.saveOrUpdateIPC(ipc);
       
		return successResult();
    }	
	
	
	
	//删除
	@RequestMapping("/deleteIpc")
	@ResponseBody
	public JSONObject deleteIpc(Long[] ids){
		for (Long longTemp : ids) {
			dcWebToDataCenterIpcCaller.deleteIPC(longTemp);
		}
			
		return successResult();
    }	

	//查询所有
	@RequestMapping("/findAllPline")
	@ResponseBody
	public JSONObject findAllPline(){
        List<Condition> conditions = new ArrayList<>();
        conditions.add(new Condition(ProductionLine.PROP_STATUS,ConditionOperator.EQ,ConditionType.OTHER,ProductionLine.STATUS_VALID));
		List<ProductionLine>  productionLineList= dcWebToDataCenterProductionLineCaller.findProductionLines(conditions);
        
		return successResult(productionLineList);
		
    }
	
	@RequestMapping("/form")
	@ApiOperation(httpMethod="GET", value = "加载表单界面")
	@ApiImplicitParam(paramType="query",name="id",value="编号")
	public void editReport(Long id){
		Map<String, Object> data = new HashMap<>();
		
		if(id != null) {
			IPC ipc = dcWebToDataCenterIpcCaller.findById(id);
			data.put("ipc", ipc);
		}
		printFtl("modules/ipc/form", data);
	}
	
	
	
	
}
