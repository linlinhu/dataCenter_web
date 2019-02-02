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
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterEcmCaller;
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterProductBrandCaller;
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterProductCaller;
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterProductCategoryCaller;
import com.emin.platform.dataCenter.domain.Product;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;


@Controller
@RequestMapping("/product")
public class ProductController  extends DCBaseController {
	private Logger logger = LoggerFactory.getLogger(ProductController.class);
	
	@Autowired 
	@Qualifier("dcWebToDataCenterProductCaller")
	private DcWebToDataCenterProductCaller dcWebToDataCenterProductCaller;
	

	@Autowired 
	@Qualifier("dcWebToDataCenterEcmCaller")
	private DcWebToDataCenterEcmCaller dcWebToDataCenterEcmCaller;
	
	@Autowired 
	@Qualifier("dcWebToDataCenterProductCategoryCaller")
	private DcWebToDataCenterProductCategoryCaller dcWebToDataCenterProductCategoryCaller;
	
	@Autowired 
	@Qualifier("dcWebToDataCenterProductBrandCaller")
	private DcWebToDataCenterProductBrandCaller dcWebToDataCenterProductBrandCaller;
	
	//查询所有产品
	@RequestMapping("/loadProduct")
	@ResponseBody
	public void loadProduct(){
		
		JSONObject json = new JSONObject();
   		String message = "";
		boolean success = false;
		
		try {
			List<Condition> conditions = new ArrayList<>();
			conditions.add(new Condition(Product.PROP_STATUS, ConditionOperator.EQ, ConditionType.OTHER, Product.STATUS_VALID));
			PagedResult<Product>  productPage= dcWebToDataCenterProductCaller.loadPagedProductsByCondition(getPageRequestData(), conditions);
			
			
			json.put("data", productPage);
            success = true;
		} catch (EminException e) {
			success = false;
			message = e.getLocalizedMessage();
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			success = false;
			message = "获取产品失败";
			logger.error(e.getMessage(),e);
		}
		json.put("success", success);
		json.put("message", message);
		printJson(json);
		
		
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
		
		//return json;
    }
	
	/**
	 * 查询并返回页面
	 * @param productBrandId 品牌id
	 * @param productCategoryMsg 树节点的文字信息
	 * @param keyword 模糊查询字段（产品编号、产品名称）
	 */
	@RequestMapping("/index")
	@ResponseBody
	public void searchProduct(Long[] productCategoryIds,Long productBrandId, String productCategoryMsg,String keyword){
		
		Map<String, Object> data = new HashMap<>();
		
		try {
			PageRequest pageRequest = getPageRequestData();
			List<Condition> conditions = new ArrayList<>();
			PagedResult<Product>  productPage = null;
			
			if (productCategoryIds != null  && productCategoryIds.length > 0 ) {
				conditions.add(new Condition(Product.PROP_PRODUCT_CATEGORY, ConditionOperator.IN, ConditionType.OTHER, productCategoryIds));
			}
			if (productBrandId != null) {
				conditions.add(new Condition(Product.PROP_PRODUCT_BRAND, ConditionOperator.EQ, ConditionType.OTHER, productBrandId));
			}
			conditions.add(new Condition(Product.PROP_STATUS, ConditionOperator.EQ, ConditionType.OTHER, Product.STATUS_VALID));
            productPage= dcWebToDataCenterProductCaller.loadPagedProductsByMatchAndCondition(pageRequest, conditions, StringUtils.split(keyword," "));
			
				
			data.put("products", productPage);
			data.put("cur", pageRequest.getCurrentPage());
			data.put("limit", pageRequest.getLimit());
			data.put("productCategoryIds", StringUtils.join(productCategoryIds, ","));
			data.put("productBrandId", productBrandId);
			data.put("productCategoryMsg", productCategoryMsg);
			data.put("keyword", keyword);
			
		} catch (EminException e) {
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		printFtl("modules/product/manage", data);
    }	
	
	/**
	 * 保存
	 * @param jsonStr 需要保存的信息
	 */
	@RequestMapping("/saveProduct")
	@ResponseBody
	public JSONObject saveProduct(String jsonStr){
		
		JSONObject json = new JSONObject();
   		String message = "";
		boolean success = false;
		
		try {
			
			Product product = JSON.parseObject(jsonStr, Product.class);
			dcWebToDataCenterProductCaller.saveOrUpdateProduct(product);
			
            success = true;
		} catch (EminException e) {
			success = false;
			message = e.getLocalizedMessage();
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			success = false;
			message = "保存产品失败";
			logger.error(e.getMessage(),e);
		}
		json.put("success", success);
		json.put("message", message);
		return json;
    }	
	
	
	
	/**
	 * 删除
	 * @param ids 删除项的id
	 */
	@RequestMapping("/deleteProduct")
	@ResponseBody
	public JSONObject deleteProduct(Long[] ids){
		
		JSONObject json = new JSONObject();
   		String message = "";
		boolean success = false;
		
		try {
			for (Long longTemp : ids) {
				dcWebToDataCenterProductCaller.deleteProduct(longTemp);
			}
			
            success = true;
		} catch (EminException e) {
			success = false;
			message = e.getLocalizedMessage();
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			success = false;
			message = "删除产品失败";
			logger.error(e.getMessage(),e);
		}
		json.put("success", success);
		json.put("message", message);
		return json;
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
			Product product = dcWebToDataCenterProductCaller.findById(id);
			data.put("product", product);
		}
		printFtl("modules/product/form", data);
	}
	
	//同步用户信息
	@RequestMapping("/synProduct")
	@ResponseBody
	public JSONObject synProduct(){
		
		JSONObject json = new JSONObject();
   		String message = "";
		boolean success = false;
		
		try {
				if (!dcWebToDataCenterProductBrandCaller.synBrand()) {
					message = "同步产品品牌信息失败，请重试";
		            success = false;	
				}else if (!dcWebToDataCenterProductCategoryCaller.synCategory()) {
					message = "同步产品分类信息失败，请重试";
		            success = false;	
				}else if (dcWebToDataCenterProductCaller.synProduct()) {
					message = "同步产品信息成功";
		            success = true;	
				}else {
					message = "同步产品信息失败，请重试";
		            success = false;	
				}
		} catch (EminException e) {
			success = false;
			message = e.getLocalizedMessage();
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			success = false;
			message = "同步产品信息失败";
			logger.error(e.getMessage(),e);
		}
		json.put("success", success);
		json.put("message", message);
		return json;
    }	
}
