package com.emin.dataCenterWeb.web;

/*import java.io.IOException;*/
/*import java.io.OutputStream;*/
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*import org.apache.commons.beanutils.BeanUtils;*/
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
/*import com.alibaba.fastjson.JSONArray;*/
import com.emin.base.dao.PageRequest;
import com.emin.base.dao.PagedResult;
import com.emin.base.exception.EminException;
import com.emin.base.service.Condition;
import com.emin.base.service.Condition.ConditionOperator;
import com.emin.base.service.Condition.ConditionType;
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterProductCodeCaller;
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterProductPrintPlanCaller;
import com.emin.platform.dataCenter.domain.Product;
import com.emin.platform.dataCenter.domain.ProductCode;
import com.emin.platform.dataCenter.domain.ProductPrintPlan;
import com.emin.platform.dataCenter.domain.ProductionLine;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;


@Controller
@RequestMapping("/productPrintPlan")
public class ProductPrintPlanController  extends DCBaseController {
	private Logger logger = LoggerFactory.getLogger(ProductPrintPlanController.class);
	
	@Autowired 
	@Qualifier("dcWebToDataCenterProductPrintPlanCaller")
	private DcWebToDataCenterProductPrintPlanCaller dcWebToDataCenterProductPrintPlanCaller;
	
	@Autowired 
	@Qualifier("dcWebToDataCenterProductCodeCaller")
	private DcWebToDataCenterProductCodeCaller dcWebToDataCenterProductCodeCaller;
	//查询所有
	@RequestMapping("/loadProductPrintPlan")
	@ResponseBody
	public void loadProductPrintPlan(){
		Map<String, Object> data = new HashMap<>();
		
		try {
			List<Condition> conditions = new ArrayList<>();
			//时间、产品编号、产品名称  条件
			PagedResult<ProductPrintPlan>  ProductPrintPlanPage= dcWebToDataCenterProductPrintPlanCaller.loadPagedProductPrintPlansByCondition(getPageRequestData(), conditions);
			
			
			data.put("plans", ProductPrintPlanPage);
		} catch (EminException e) {
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		printFtl("modules/print_plan/manage", data);
    }
	
	//搜索
	@RequestMapping("/index")
	@ResponseBody
	public void searchProductPrintPlan(String product,String productionLine){
		
		Map<String, Object> data = new HashMap<>();
		
		try {
			Product productObj = null;
			
			if (StringUtils.isNotBlank(product)) {
				productObj = JSON.parseObject(product, Product.class);
			}
			
			ProductionLine productionLineObj = null;
			
			if (StringUtils.isNotBlank(productionLine)) {
				productionLineObj = JSON.parseObject(productionLine, ProductionLine.class);
			}
			
			PageRequest pageRequest = getPageRequestData();
			List<Condition> conditions = new ArrayList<>();
			PagedResult<ProductPrintPlan>  productPrintPlanPage = null;
			
			if (productObj != null ) {
				conditions.add(new Condition("productCode.product.productNumber",ConditionOperator.EQ,ConditionType.OTHER,productObj.getProductNumber()));
			}
			if (productionLineObj != null) {
				conditions.add(new Condition("productionLine.lineNumber",ConditionOperator.EQ,ConditionType.OTHER,productionLineObj.getLineNumber()));
			}
			
			conditions.add(new Condition(ProductPrintPlan.PROP_STATUS,ConditionOperator.EQ,ConditionType.OTHER,ProductPrintPlan.STATUS_VALID));
			productPrintPlanPage= dcWebToDataCenterProductPrintPlanCaller.loadPagedProductPrintPlansByCondition(pageRequest, conditions);
			
				
			data.put("plans", productPrintPlanPage);
			data.put("cur", pageRequest.getCurrentPage());
			data.put("limit", pageRequest.getLimit());
			data.put("product", productObj);
			data.put("productionLine", productionLineObj);
		} catch (EminException e) {
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		printFtl("modules/print_plan/manage", data);
    }	
	
	//保存
	@RequestMapping("/saveProductPrintPlan")
	@ResponseBody
	public JSONObject saveProductPrintPlan(String jsonStr){
		
		JSONObject json = new JSONObject();
   		String message = "";
		boolean success = false;
		
		try {
			
			List<ProductPrintPlan> ProductPrintPlanList = JSON.parseArray(jsonStr, ProductPrintPlan.class);
			if (ProductPrintPlanList != null && ProductPrintPlanList.size()>0) {
				for (ProductPrintPlan ProductPrintPlanTemp : ProductPrintPlanList) {
					dcWebToDataCenterProductPrintPlanCaller.saveOrUpdateProductPrintPlan(ProductPrintPlanTemp);
				}
			}
			
            success = true;
		} catch (EminException e) {
			success = false;
			message = e.getLocalizedMessage();
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			success = false;
			message = "保存产品所属码打码、扫码方案失败";
			logger.error(e.getMessage(),e);
		}
		json.put("success", success);
		json.put("message", message);
		return json;
    }	
	
	
	
	//删除
	@RequestMapping("/deleteProductPrintPlan")
	@ResponseBody
	public JSONObject deleteProductPrintPlan(Long[] ids){
		
		JSONObject json = new JSONObject();
   		String message = "";
		boolean success = false;
		
		try {
			for (Long longTemp : ids) {
				dcWebToDataCenterProductPrintPlanCaller.deleteProductPrintPlan(longTemp);
			}
			
            success = true;
		} catch (EminException e) {
			success = false;
			message = e.getLocalizedMessage();
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			success = false;
			message = "删除产品所属码打码、扫码方案失败";
			logger.error(e.getMessage(),e);
		}
		json.put("success", success);
		json.put("message", message);
		return json;
    }	

	//获取产品所属码
	@RequestMapping("/getProductCode")
	@ResponseBody
	public void getProductCode(Long productId){
		
		JSONObject json = new JSONObject();
   		String message = "";
		boolean success = false;
		
		try {
			List<Condition> conditions = new ArrayList<>();
            conditions.add(new Condition(ProductCode.PROP_STATUS,ConditionOperator.EQ,ConditionType.OTHER,ProductCode.STATUS_VALID));
            conditions.add(new Condition("product.id",ConditionOperator.EQ,ConditionType.OTHER,productId));
			List<ProductCode> productCodeList = dcWebToDataCenterProductCodeCaller.findProductCodes(conditions);
			//String productCodeListStr = JSONArray.toJSONString(productCodeList, SerializerFeature.DisableCircularReferenceDetect);
			
//			for (ProductCode productCode : productCodeList) {
//				//ProductCode productCode = productCodeList.get(0);		//Product.class.newInstance()	
//				Product product = new  Product();
//				product.setProductName("asdasdas");
//				BeanUtils.copyProperties(product, productCode.getProduct());
//				productCode.setProduct(product );
//				//productCode.setProduct( (Product)BeanUtils.cloneBean(productCode.getProduct()) );
//				
//			}
			
			
			json.put("data", productCodeList);
            success = true;
		} catch (EminException e) {
			success = false;
			message = e.getLocalizedMessage();
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			success = false;
			message = "获取产品所属码失败";
			logger.error(e.getMessage(),e);
		}
		json.put("success", success);
		json.put("message", message);
		
		printJson(json);
		//printJson(json,SerializerFeature.DisableCircularReferenceDetect);
		//return json;
		
//		OutputStream pw = null;
//		getResponse().setCharacterEncoding("UTF-8");
//		getResponse().setHeader("Pragma", "No-cache");
//		getResponse().setHeader("Cache-Control", "no-cache");
//		getResponse().setHeader("Cache-Control", "no-store");
//		getResponse().setContentType("text/html;charset=UTF-8");
//		try {
//			pw = getResponse().getOutputStream();
//			System.out.println("printJson==================" + JSON.toJSONString(json,SerializerFeature.DisableCircularReferenceDetect));
//			pw.write(JSON.toJSONString(json,SerializerFeature.DisableCircularReferenceDetect).getBytes("UTF-8"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
    }	
	
	
	//查询是否存在打码方案
	@RequestMapping("/getroductPrintPlanCount")
	@ResponseBody
	public JSONObject getroductPrintPlanCount(String productNumber,String lineNumber, Long deviceType){
		JSONObject json = new JSONObject();
   		String message = "";
		boolean success = false;
		
		try {
			List<Condition> conditions = new ArrayList<>();
			List<ProductPrintPlan>  productPrintPlan = null;
			
			conditions.add(new Condition("productCode.product.productNumber",ConditionOperator.EQ,ConditionType.OTHER,productNumber));
			conditions.add(new Condition("productionLine.lineNumber",ConditionOperator.EQ,ConditionType.OTHER,lineNumber));
			conditions.add(new Condition("deviceType",ConditionOperator.EQ,ConditionType.OTHER,deviceType));
			conditions.add(new Condition(ProductPrintPlan.PROP_STATUS,ConditionOperator.EQ,ConditionType.OTHER,ProductPrintPlan.STATUS_VALID));
			
			productPrintPlan= dcWebToDataCenterProductPrintPlanCaller.findProductPrintPlans(conditions);
			
			if (productPrintPlan == null ) {
				json.put("count", 0);
			}else  {
				json.put("count", productPrintPlan.size());
			}
			
            success = true;
		} catch (EminException e) {
			success = false;
			message = e.getLocalizedMessage();
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			success = false;
			message = "查询是否存在打码方案失败";
			logger.error(e.getMessage(),e);
		}
		json.put("success", success);
		json.put("message", message);
		return json;
    }	
	
	//删除已经存在打码或者扫码方案
	@RequestMapping("/delProductPrintPlan")
	@ResponseBody
	public JSONObject delProductPrintPlan(String productNumber,String lineNumber, Long deviceType){
		JSONObject json = new JSONObject();
   		String message = "";
		boolean success = false;
		
		try {
			
			dcWebToDataCenterProductPrintPlanCaller.deleteExistPlan(productNumber, lineNumber, deviceType);
			
            success = true;
		} catch (EminException e) {
			success = false;
			message = e.getLocalizedMessage();
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			success = false;
			message = "删除已经存在打码或者扫码方案失败";
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
			ProductPrintPlan productPrintPlan = dcWebToDataCenterProductPrintPlanCaller.findById(id);
			data.put("productPrintPlan", productPrintPlan);
		}
		printFtl("modules/print_plan/form", data);
	}
	
	
	
	
}
