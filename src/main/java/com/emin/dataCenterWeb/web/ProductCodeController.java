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
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterProductCodeCaller;
import com.emin.platform.dataCenter.domain.ProductCode;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;


@Controller
@RequestMapping("/productCode")
public class ProductCodeController  extends DCBaseController {
	private Logger logger = LoggerFactory.getLogger(ProductCodeController.class);
	
	@Autowired 
	@Qualifier("dcWebToDataCenterProductCodeCaller")
	private DcWebToDataCenterProductCodeCaller dcWebToDataCenterProductCodeCaller;
	
	
	//查询所有
	@RequestMapping("/loadProductCode")
	@ResponseBody
	public JSONObject loadProductCode(){

		List<Condition> conditions = new ArrayList<>();
		//时间、产品编号、产品名称  条件
		PagedResult<ProductCode>  productCodePage= dcWebToDataCenterProductCodeCaller.loadPagedProductCodesByCondition(getPageRequestData(), conditions);
			
		return successResult(productCodePage);
    }
	
	//搜索：产品编号、产品名称
	@RequestMapping("/index")
	@ResponseBody
	public void searchProductCode(String keyword){
		logger.info("keyword:"+keyword);
		Map<String, Object> data = new HashMap<>();

		PageRequest pageRequest = getPageRequestData();
		List<Condition> conditions = new ArrayList<>();
		PagedResult<ProductCode>  productCodePage = null;
		if (StringUtils.isNotBlank(keyword)) {
			productCodePage = dcWebToDataCenterProductCodeCaller.loadPagedProductCodesByMatch(pageRequest, StringUtils.split(keyword," "));
		}else {
			productCodePage= dcWebToDataCenterProductCodeCaller.loadPagedProductCodesByCondition(pageRequest, conditions);
		}
			
		data.put("pcodes", productCodePage);
		data.put("cur", pageRequest.getCurrentPage());
		data.put("limit", pageRequest.getLimit());
		data.put("keyword", keyword);
		
		printFtl("modules/product_code/manage",data);
    }	
	
	//保存：添加、修改   --  产品接口、设备接口	
	@RequestMapping("/saveProductCode")
	@ResponseBody
	public JSONObject saveProductCode(String jsonStr,Long[] deleteIds){
		if (deleteIds != null && deleteIds.length > 0) {
			for (Long longTemp : deleteIds) {
				dcWebToDataCenterProductCodeCaller.deleteProductCode(longTemp);
			}
		}
  		List<ProductCode> productCodeList = JSON.parseArray(jsonStr, ProductCode.class);
		if (productCodeList != null && productCodeList.size()>0) {
			for (ProductCode productCodeTemp : productCodeList) {
				dcWebToDataCenterProductCodeCaller.saveOrUpdateProductCode(productCodeTemp);
			}
		}
			
		return successResult();
    }	
	
	
	
	//删除
	@RequestMapping("/deleteProductCode")
	@ResponseBody
	public JSONObject deleteProductCode(Long[] ids){

		if (ids != null && ids.length > 0) {
			for (Long longTemp : ids) {
				dcWebToDataCenterProductCodeCaller.deleteProductCode(longTemp);
			}
		}
		return successResult();
    }	

	
	
	@RequestMapping("/form")
	@ApiOperation(httpMethod="GET", value = "加载表单界面")
	@ApiImplicitParam(paramType="query",name="id",value="编号")
	public void editReport(Long id){
		Map<String, Object> data = new HashMap<>();
		
		if(id != null) {
			ProductCode productCode = dcWebToDataCenterProductCodeCaller.findById(id);
			data.put("productCode", productCode);
		}
		printFtl("modules/product_code/form", data);
	}
	
	
	
	
}
