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
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterShopCaller;
import com.emin.platform.dataCenter.domain.Shop;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import com.alibaba.fastjson.JSONObject;


@Controller
@RequestMapping("/shop")
public class ShopController  extends DCBaseController {
	private Logger logger = LoggerFactory.getLogger(ShopController.class);
	
	@Autowired 
	@Qualifier("dcWebToDataCenterShopCaller")
	private DcWebToDataCenterShopCaller dcWebToDataCenterShopCaller;
	
	
	//查询所有
	@RequestMapping("/loadShop")
	@ResponseBody
	public JSONObject loadShop(){
		PageRequest pageRequest = getPageRequestData();
		List<Condition> conditions = new ArrayList<>();
		conditions.add(new Condition(Shop.PROP_STATUS, ConditionOperator.EQ, ConditionType.OTHER, Shop.STATUS_VALID));
		PagedResult<Shop>  shopPage= dcWebToDataCenterShopCaller.loadPagedShopsByCondition(pageRequest, conditions);

		return successResult(shopPage);
    }
	
	//搜索
	@RequestMapping("/index")
	@ResponseBody
	public void searchShop(String keyword){
		logger.info("keyword:"+keyword);
		Map<String, Object> data = new HashMap<>();

		PageRequest pageRequest = getPageRequestData();
		List<Condition> conditions = new ArrayList<>();
		PagedResult<Shop>  shopPage = null;
		if (StringUtils.isNotBlank(keyword)) {
			shopPage = dcWebToDataCenterShopCaller.loadPagedShopsByMatch(pageRequest, StringUtils.split(keyword," "));
		}else {
			shopPage= dcWebToDataCenterShopCaller.loadPagedShopsByCondition(pageRequest, conditions);
		}
			
		data.put("shops", shopPage);
		data.put("cur", pageRequest.getCurrentPage());
		data.put("limit", pageRequest.getLimit());
		data.put("keyword", keyword);
		
		printFtl("modules/shop/manage",data);
    }	
	
	//保存
	@RequestMapping("/saveShop")
	@ResponseBody
	public JSONObject saveShop(String jsonStr){
		logger.info("jsonStr:"+jsonStr);
		Shop shop = JSON.parseObject(jsonStr, Shop.class);
		dcWebToDataCenterShopCaller.saveOrUpdateShop(shop);
			
		return successResult();
    }	
	
	
	//删除
	@RequestMapping("/deleteShop")
	@ResponseBody
	public JSONObject deleteShop(Long[] ids){
		for (Long longTemp : ids) {
			dcWebToDataCenterShopCaller.deleteShop(longTemp);
		}
			
		return successResult();
    }	

	
	
	@RequestMapping("/form")
	@ApiOperation(httpMethod="GET", value = "加载表单界面")
	@ApiImplicitParam(paramType="query",name="id",value="编号")
	public void editReport(Long id){
		Map<String, Object> data = new HashMap<>();
		
		if(id != null) {
			Shop shop = dcWebToDataCenterShopCaller.findById(id);
			data.put("shop", shop);
		}
		printFtl("modules/shop/form", data);
	}
	
	
	
	
}
