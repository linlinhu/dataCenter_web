package com.emin.dataCenterWeb.web;

import java.io.BufferedOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.emin.base.exception.EminException;
import com.emin.base.service.Condition;
import com.emin.base.service.Condition.ConditionOperator;
import com.emin.base.service.Condition.ConditionType;
import com.emin.dataCenterWeb.facade.caller.DcWebToCodingCenterCaller;
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterBatchCaller;
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterBusinessCodeCaller;
import com.emin.dataCenterWeb.util.Constant.MatchingModeStringEnum;
import com.emin.dataCenterWeb.util.ObjectUtil;
import com.emin.platform.codingCenter.domain.CodingRule;
import com.emin.platform.codingCenter.expression.ExpressionCondition;
import com.emin.platform.codingCenter.expression.ExpressionCondition.MatchingMode;
import com.emin.platform.codingCenter.expression.ExpressionSupport;
import com.emin.platform.dataCenter.domain.Batch;
import com.emin.platform.dataCenter.domain.BusinessCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/codingCenter")
public class CodingCenterController  extends DCBaseController {
	private Logger logger = LoggerFactory.getLogger(CodingCenterController.class);
	
	@Value("${eminCode.URL}")
	String EMIN_CODE_URL;
	
	@Autowired 
	@Qualifier("dcWebToCodingCenterCaller")
	private DcWebToCodingCenterCaller dcWebToCodingCenterCaller;

	@Autowired 
	@Qualifier("dcWebToDataCenterBatchCaller")
	private DcWebToDataCenterBatchCaller dcWebToDataCenterBatchCaller;
	
	@Autowired 
	@Qualifier("dcWebToDataCenterBusinessCodeCaller")
	private DcWebToDataCenterBusinessCodeCaller dcWebToDataCenterBusinessCodeCaller;
	
	@RequestMapping("/createRule")
	@ResponseBody
	public JSONObject createRule(@RequestParam(value = "name")String name,
			@RequestParam(value = "randomLength")String randomLength,
			@RequestParam(value = "conditions")String conditions){
		
		JSONObject json = new JSONObject();
   		String message = "";
		boolean success = false;
		
		try {
			List<ExpressionCondition> conditionsList = new ArrayList<ExpressionCondition>();
			
			List<JSONObject> jSONObjectArray = JSON.parseArray(conditions,JSONObject.class);
			for (JSONObject jsonObjectTemp : jSONObjectArray) {
				String businessCode = jsonObjectTemp.getString("businessCode");
				int index = jsonObjectTemp.getInt("index");
				String[] matchingModeStrArray = jsonObjectTemp.getString("matchingModes").split(",");
				int length = jsonObjectTemp.getInt("length");
				int size = matchingModeStrArray.length;
				MatchingMode[] matchingModes = new MatchingMode[size];
				int i = 0;		
				
				for (String stringTemp : matchingModeStrArray) {
					
					MatchingModeStringEnum matchingModeStringEnum = MatchingModeStringEnum.fromString(stringTemp);
					if (matchingModeStringEnum != null) {
						
						switch (matchingModeStringEnum) {
						case NUMBERS:
							matchingModes[i] = MatchingMode.NUMBERS;
							i = i + 1;
							break;
						case CHARACTERS_LOWERCASE:
							matchingModes[i] = MatchingMode.CHARACTERS_UPPERCASE;
							i = i + 1;
							break;
						case CHARACTERS_UPPERCASE:
							matchingModes[i] = MatchingMode.CHARACTERS_UPPERCASE;
							i = i + 1;
							break;
						case UNDERLINE:
							matchingModes[i] = MatchingMode.UNDERLINE;
							i = i + 1;
							break;
						case HYPHEN:
							matchingModes[i] = MatchingMode.HYPHEN;
							i = i + 1;
							break;							
						default:
							break;
						}
								
					}
					
					
				}
				ExpressionCondition expressionCondition = new ExpressionCondition(businessCode,length,index,matchingModes);
				conditionsList.add(expressionCondition);
			} 
						
			int randomLengthInt = 0;
			if (StringUtils.isNoneBlank(randomLength)) {
				randomLengthInt = Integer.parseInt(randomLength);
			}
			
			dcWebToCodingCenterCaller.createRule(name, randomLengthInt, conditionsList);
					
            success = true;	
		} catch (EminException e) {
			success = false;
			message = e.getLocalizedMessage();
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			success = false;
			message = "创建二维码规则失败";
			logger.error(e.getMessage(),e);
		}
		json.put("success", success);
		json.put("message", message);
		return json;
    }
	
	
	@RequestMapping("/updateRule")
	@ResponseBody
	public JSONObject updateRule(@RequestParam(value = "codeOrId")String codeOrId, 
			@RequestParam(value = "name")String name,
			@RequestParam(value = "randomLength")String randomLength, 
			@RequestParam(value = "conditions")String conditions){
	
		JSONObject json = new JSONObject();
   		String message = "";
		boolean success = false;
		
		try {			
            List<ExpressionCondition> conditionsList = JSON.parseArray(conditions, ExpressionCondition.class);
			
			int randomLengthInt = 0;
			if (StringUtils.isNoneBlank(randomLength)) {
				randomLengthInt = Integer.parseInt(randomLength);
			}
			
			dcWebToCodingCenterCaller.updateRule(codeOrId,name,randomLengthInt,conditionsList);
            success = true;
		} catch (EminException e) {
			success = false;
			message = e.getLocalizedMessage();
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			success = false;
			message = "更新二维码规则失败";
			logger.error(e.getMessage(),e);
		}
		json.put("success", success);
		json.put("message", message);
		return json;
    }
	@RequestMapping("/loadAllCodeRule")
	@ResponseBody
	public JSONObject loadAllCodeRule(){
		JSONObject json = new JSONObject();
   		String message = "";
		boolean success = false;
		try {
			List<CodingRule> codingRuleList = dcWebToCodingCenterCaller.findPublishedRuleList();
			ObjectMapper mapper = new ObjectMapper(); //转换器  
			//mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
			String jsonStr = mapper.writeValueAsString(codingRuleList); 
			
			json.put("codes", jsonStr);
			success = true;
		} catch (EminException e) {
			success = false;
			message = e.getLocalizedMessage();
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			success = false;
			message = "查询失败";
			logger.error(e.getMessage(),e);
		}
		json.put("success", success);
		json.put("message", message);
		return json;
	}
	
	@RequestMapping("/index")
	@ResponseBody
	public void findAllRuleList(){
		Map<String, Object> data = new HashMap<>();
		try {
			List<CodingRule> codingRuleList = dcWebToCodingCenterCaller.findAllRuleList();	
			data.put("codes", codingRuleList);
		} catch (EminException e) {
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		printFtl("modules/coding_center/manage", data);
    }
	
	
	@RequestMapping("/findPublishedRuleList")
	@ResponseBody
	public void findPublishedRuleList(){
		Map<String, Object> data = new HashMap<>();
		try {
			
			List<CodingRule> codingRuleList = dcWebToCodingCenterCaller.findPublishedRuleList();	
			data.put("codes", codingRuleList);
		} catch (EminException e) {
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		
		printFtl("modules/coding_center/manage", data);
    }
	
	
	@RequestMapping("/publishRule")
	@ResponseBody
	public JSONObject publishRule(String codeOrId){
	
		JSONObject json = new JSONObject();
   		String message = "";
		boolean success = false;
		
		try {
			
			dcWebToCodingCenterCaller.publishRule(codeOrId);		
            success = true;
		} catch (EminException e) {
			success = false;
			message = e.getLocalizedMessage();
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			success = false;
			message = "发布二维码规则失败";
			logger.error(e.getMessage(),e);
		}
		json.put("success", success);
		json.put("message", message);
		return json;
    }
	
	
	
	@RequestMapping("/cancelRule")
	@ResponseBody
	public JSONObject cancelRule(String codeOrId){
		
		JSONObject json = new JSONObject();
   		String message = "";
		boolean success = false;
		
		try {
			
			dcWebToCodingCenterCaller.cancelRule(codeOrId);
            success = true;
		} catch (EminException e) {
			success = false;
			message = e.getLocalizedMessage();
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			success = false;
			message = "取消二维码规则失败";
			logger.error(e.getMessage(),e);
		}
		json.put("success", success);
		json.put("message", message);
		return json;
    }
	
	
	@RequestMapping("/generateCodes")
	@ResponseBody
	public void generateCodes(Long batchId,String ruleId, String codeName, int genCount){
		
		try {
			Batch batch = dcWebToDataCenterBatchCaller.findById(batchId);
			
			//预案ruleId是id，现在产品码中ruleId存储的是规则的code
			CodingRule codingRule = dcWebToCodingCenterCaller.findByRuleCode(ruleId);
			if ( codingRule != null) {
                Set<String> groupNames= ExpressionSupport.getNamedGroupCandidates(codingRule.getPattern());
                net.sf.json.JSONObject sourceData = new net.sf.json.JSONObject();
				for (String groupName : groupNames) { 
					if (groupName.equals("URL")) {
						sourceData.put(groupName, EMIN_CODE_URL);
					}else if (!groupName.equals("random")) { //random排除
						
						List<Condition> conditions = new ArrayList<Condition>();	
						conditions.add(new Condition("businessCode",ConditionOperator.EQ, ConditionType.OTHER, groupName));	
						conditions.add(new Condition(BusinessCode.PROP_STATUS,ConditionOperator.EQ, ConditionType.OTHER, BusinessCode.STATUS_VALID));
						
						List<BusinessCode> businessCodeList = dcWebToDataCenterBusinessCodeCaller.findBusinessCodes(conditions);
						if (businessCodeList != null && businessCodeList.size() > 0) {
							Object object = batch;
							String  businessCodeValue = (String)ObjectUtil.getValueByFiled(object, businessCodeList.get(0).getFiled());							
							sourceData.put(groupName, businessCodeValue);
						}						
					}
				
				}
			
			
			    Set<String>  codes = dcWebToCodingCenterCaller.generateCodes(codingRule.getId(), sourceData, genCount);	
			    
				getResponse().setContentType("text/plain");
				String fileName = batch.getCode() + codeName;
				try {
					fileName = URLEncoder.encode(fileName, "UTF-8");
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
				getResponse().setHeader("Content-Disposition", "attachment; filename=" + fileName + ".txt");
				BufferedOutputStream buff = null;
				StringBuffer write = new StringBuffer();
				String enter = "\r\n";
				ServletOutputStream outSTr = null;
				try {
					outSTr = getResponse().getOutputStream(); // 建立
					buff = new BufferedOutputStream(outSTr);
					if (codes != null && codes.size() > 0) {
						for (String code : codes) {
							write.append(code);
							write.append(enter);
						}
					}
					
					
					buff.write(write.toString().getBytes("UTF-8"));
					buff.flush();
					buff.close();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						buff.close();
						outSTr.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			}
		} catch (EminException e) {
			logger.error(e.getLocalizedMessage(),e);
			printFtl("500",null);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			printFtl("500",null);
		}
    }
	
	@RequestMapping("/form")
	@ApiOperation(httpMethod="GET", value = "加载表单界面")
	@ApiImplicitParam(paramType="query",name="id",value="编号")
	public void editReport(String id){
		Map<String, Object> data = new HashMap<>();
		
		if(id != null) {
			CodingRule codingRule = dcWebToCodingCenterCaller.findById(id);
			data.put("codingRule", codingRule);
		}
		printFtl("modules/coding_center/form", data);
	}
	
	
	
	
}
