package com.emin.dataCenterWeb.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterEcmCaller;
import com.emin.platform.dataCenter.domain.Ecm;

@Controller
@RequestMapping("/ecm")
public class EcmController extends DCBaseController{
	private Logger logger = LoggerFactory.getLogger(EcmController.class);
	
	@Autowired 
	@Qualifier("dcWebToDataCenterEcmCaller")
	private DcWebToDataCenterEcmCaller dcWebToDataCenterEcmCaller;
	
	@RequestMapping("/index")
	@ResponseBody
	public void goManage(){

		Map<String, Object> data = new HashMap<>();
		List<Ecm> ecms = dcWebToDataCenterEcmCaller.findEcms(null);
		if (ecms != null && ecms.size() > 0) {
			data.put("ecm", ecms.get(0));
		}
		printFtl("modules/ecm/manage", data);
    }
	
	/***
	 * 根据公司信息同步主体信息
	 * @param companyName
	 * @param companyCode
	 * @return
	 */
	@RequestMapping("/synEcm")
	@ResponseBody
	public JSONObject synEcm(String companyName, String companyCode){
		logger.info("companyCode:"+ companyCode);
		Boolean	success = dcWebToDataCenterEcmCaller.synEcm(companyCode);
		
		if (success) {
			return successResult();
		}else {
			return failResult();
		}
		
    }
	/**
	 * 跳转至表单页面
	 * @param id 存在id,则查询实体返回到页面
	 */
	@RequestMapping("/form")
	@ResponseBody
	public void goForm(Long id){
		Map<String, Object> data = new HashMap<>();
		if (id != null) {
			Ecm ecm = dcWebToDataCenterEcmCaller.findById(id);
			data.put("ecm", ecm);
		}
		
		printFtl("modules/ecm/form", data);
    }
	
	/**
	 * 保存主体信息
	 * @return 保存成功需要将ecm实体更新到主页
	 */
	@RequestMapping("/saveEcm")
	@ResponseBody
	public JSONObject saveEcm(Ecm ecm){
		dcWebToDataCenterEcmCaller.saveOrUpdateEcm(ecm);
			
		return successResult();
    }
}
