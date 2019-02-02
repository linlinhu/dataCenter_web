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
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterBatchCaller;
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterEcmCaller;
import com.emin.platform.dataCenter.domain.Batch;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;


@Controller
@RequestMapping("/batch")
public class BatchController  extends DCBaseController {
	private Logger logger = LoggerFactory.getLogger(BatchController.class);
	
	@Autowired 
	@Qualifier("dcWebToDataCenterBatchCaller")
	private DcWebToDataCenterBatchCaller dcWebToDataCenterBatchCaller;
	
	@Autowired 
	@Qualifier("dcWebToDataCenterEcmCaller")
	private DcWebToDataCenterEcmCaller dcWebToDataCenterEcmCaller;
	
	//查询所有
	@RequestMapping("/loadBatch")
	@ApiOperation(httpMethod="GET", value = "查询所有批次")
	@ResponseBody
	public JSONObject loadBatch(){
		
		JSONObject json = new JSONObject();
   		String message = "";
		boolean success = false;

		List<Condition> conditions = new ArrayList<>();
		conditions.add(new Condition(Batch.PROP_STATUS, ConditionOperator.EQ, ConditionType.OTHER, Batch.STATUS_VALID));
		PagedResult<Batch>  BatchPage= dcWebToDataCenterBatchCaller.loadPagedBatchsByCondition(getPageRequestData(), conditions);		
		
		json.put("data", BatchPage);
        success = true;
        json.put("success", success);
		json.put("message", message);
		return json;
    }
	
	//搜索：批次编号code、批次产品编号productNumber、批次车间编号shopNumber、批次生产线编号lineNumber   --编号不是ID
	@RequestMapping("/index")
	@ApiOperation(httpMethod="GET", value = "查询批次")
	@ResponseBody
	public void searchBatch(String keyword){
		
		Map<String, Object> data = new HashMap<>();
		
		try {
			PageRequest pageRequest = getPageRequestData();
			List<Condition> conditions = new ArrayList<>();
			PagedResult<Batch>  batchPage = null;
			if (StringUtils.isNotBlank(keyword)) {
				batchPage = dcWebToDataCenterBatchCaller.loadPagedBatchsByMatch(pageRequest, StringUtils.split(keyword," "));
			}else {
				conditions.add(new Condition(Batch.PROP_STATUS, ConditionOperator.EQ, ConditionType.OTHER, Batch.STATUS_VALID));
				batchPage= dcWebToDataCenterBatchCaller.loadPagedBatchsByCondition(pageRequest, conditions);
			}
				
			data.put("batchs", batchPage);
			data.put("cur", pageRequest.getCurrentPage());
			data.put("limit", pageRequest.getLimit());
			data.put("keyword", keyword);
		} catch (EminException e) {
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		printFtl("modules/batch/manage", data);
    }	
	
	//保存
	@RequestMapping("/saveBatch")
	@ApiOperation(httpMethod="POST", value = "编辑保存批次")
	@ResponseBody
	public JSONObject saveBatch(String jsonStr){
		
		JSONObject json = new JSONObject();
   		String message = "";
		boolean success = false;
		
		try {
			//状态字段batchState不可编辑
			Batch batch = JSON.parseObject(jsonStr, Batch.class);
			if ( batch != null && batch.getId() == null) {
				batch.setBatchState(1);   //新建的默认为草稿状态
				dcWebToDataCenterBatchCaller.saveOrUpdateBatch(batch);
				success = true;
			}else if (batch != null && batch.getId() != null && batch.getBatchState() == 1 ) {
				dcWebToDataCenterBatchCaller.saveOrUpdateBatch(batch);
				success = true;
			}else {
				message = "批次草稿状态才能编辑";
			}
			
		} catch (EminException e) {
			success = false;
			message = e.getLocalizedMessage();
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			success = false;
			message = "保存批次失败";
			logger.error(e.getMessage(),e);
		}
		json.put("success", success);
		json.put("message", message);
		return json;
    }	
	
	//发布
	@RequestMapping("/publishBatch")
	@ApiOperation(httpMethod="GET", value = "发布批次")
	@ResponseBody
	public JSONObject publishBatch(Long id){
		
		JSONObject json = new JSONObject();
   		String message = "";
		boolean success = false;
		
		try {
			//草稿状态才能发布			
			Batch batch = dcWebToDataCenterBatchCaller.findById(id);
			if (batch != null && batch.getBatchState() == 1) {
				batch.setBatchState(2);
				dcWebToDataCenterBatchCaller.saveOrUpdateBatch(batch);
				success = true;
			}else if (batch != null) {
				message = "只有草稿状态才能发布";
			}
            success = true;
		} catch (EminException e) {
			success = false;
			message = e.getLocalizedMessage();
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			success = false;
			message = "发布批次失败";
			logger.error(e.getMessage(),e);
		}
		json.put("success", success);
		json.put("message", message);
		return json;
    }
	
	//发布
	@RequestMapping("/synEminCodeByBatchId")
	@ApiOperation(httpMethod="GET", value = "同步本批次二维码数据")
	@ResponseBody
	public JSONObject synEminCodeByBatchId(Long id){
		
		JSONObject json = new JSONObject();
   		String message = "";
		boolean success = false;
		
		try {
			//打印完成状态才能同步			
			Batch batch = dcWebToDataCenterBatchCaller.findById(id);
			if (batch != null && batch.getBatchState() == 4) {
				dcWebToDataCenterBatchCaller.synEminCodeByBatchId(id);
				success = true;
			}else if (batch != null) {
				message = "只有打印完成状态才能同步二维码数据";
			}
            success = true;
		} catch (EminException e) {
			success = false;
			message = e.getLocalizedMessage();
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			success = false;
			message = "同步二维码接口调用失败";
			logger.error(e.getMessage(),e);
		}
		json.put("success", success);
		json.put("message", message);
		return json;
    }	
		
	//删除
	@RequestMapping("/deleteBatch")
	@ApiOperation(httpMethod="DELETE", value = "删除批次")
	@ResponseBody
	public JSONObject deleteBatch(Long[] ids){
		
		JSONObject json = new JSONObject();
   		String message = "";
		boolean success = false;
		
		try {
			
			for (Long id : ids) {
				//草稿状态才能删除
				Batch batch = dcWebToDataCenterBatchCaller.findById(id);
				if (batch != null && batch.getBatchState() == 1) {
					dcWebToDataCenterBatchCaller.deleteBatch(id);
					
				}else if (batch != null) {
					message = "只有草稿状态才能删除,批次编号"+id;
					json.put("success", success);
					json.put("message", message);
					return json;
				}	
			}				
			success = true;
            
		} catch (EminException e) {
			success = false;
			message = e.getLocalizedMessage();
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			success = false;
			message = "删除批次失败";
			logger.error(e.getMessage(),e);
		}
		json.put("success", success);
		json.put("message", message);
		return json;
    }	

	
	
	@RequestMapping("/form")
	@ApiOperation(httpMethod="GET", value = "加载表单界面")
	@ApiImplicitParam(paramType="query",name="id",value="编号")
	public void editReport(Long id){
		Map<String, Object> data = new HashMap<>();
		if(id != null) {
			Batch batch = dcWebToDataCenterBatchCaller.findById(id);
			data.put("batch", batch);
		}
		printFtl("modules/batch/form", data);
	}
	
	
	//同步用户信息
	@RequestMapping("/synBatch")
	@ResponseBody
	public JSONObject synBatch(){
		
		JSONObject json = new JSONObject();
   		String message = "";
		boolean success = false;
		
		try {
			if (dcWebToDataCenterBatchCaller.synBatch()) {
				message = "上传批次信息成功";
				success = true;
			}else {
				message = "上传批次信息失败";
				success = false;
			}
		} catch (EminException e) {
			success = false;
			message = e.getLocalizedMessage();
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			success = false;
			message = "上传批次信息失败";
			logger.error(e.getMessage(),e);
		}
		json.put("success", success);
		json.put("message", message);
		return json;
    }
	
}
