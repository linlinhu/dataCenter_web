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
import com.emin.base.dao.PageRequest;
import com.emin.base.dao.PagedResult;
import com.emin.base.exception.EminException;
import com.emin.base.service.Condition;
import com.emin.base.service.Condition.ConditionOperator;
import com.emin.base.service.Condition.ConditionType;
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterBatchCaller;
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterProductCodeCaller;
import com.emin.platform.dataCenter.domain.Batch;
import com.emin.platform.dataCenter.domain.ProductCode;
import io.swagger.annotations.ApiOperation;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;


@Controller
@RequestMapping("/qrcodeExport")
public class QrcodeExportController  extends DCBaseController {
	private Logger logger = LoggerFactory.getLogger(QrcodeExportController.class);
	
	@Autowired 
	@Qualifier("dcWebToDataCenterBatchCaller")
	private DcWebToDataCenterBatchCaller dcWebToDataCenterBatchCaller;
	
	@Autowired 
	@Qualifier("dcWebToDataCenterProductCodeCaller")
	private DcWebToDataCenterProductCodeCaller dcWebToDataCenterProductCodeCaller;
	
	//查询产品的ProductCode
	@RequestMapping("/loadProductCode")
	@ResponseBody
	public void loadProductCode(Long productId){
		
		JSONObject json = new JSONObject();
   		String message = "";
		boolean success = false;
		
		try {
			List<Condition> conditions = new ArrayList<>();
			conditions.add(new Condition("product.id", ConditionOperator.EQ, ConditionType.OTHER, productId));
			conditions.add(new Condition(ProductCode.PROP_STATUS, ConditionOperator.EQ, ConditionType.OTHER, ProductCode.STATUS_VALID));
			
			List<ProductCode>  productCodeList= dcWebToDataCenterProductCodeCaller.findProductCodes(conditions);		
			
			json.put("data", productCodeList);
            success = true;
		} catch (EminException e) {
			success = false;
			message = e.getLocalizedMessage();
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			success = false;
			message = "获取产品的产品所属码失败";
			logger.error(e.getMessage(),e);
		}
		json.put("success", success);
		json.put("message", message);
		printJson(json,SerializerFeature.DisableCircularReferenceDetect);
    }
	
	//搜索：批次编号code、批次产品编号productNumber、批次车间编号shopNumber、批次生产线编号lineNumber   --编号不是ID
	@RequestMapping("/index")
	@ApiOperation(httpMethod="GET", value = "查询发布过的批次")
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
		printFtl("modules/qrcode_export/manage", data);
    }	
	
	
	
	
	
	
}
