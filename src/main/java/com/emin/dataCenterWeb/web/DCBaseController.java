package com.emin.dataCenterWeb.web;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.emin.base.controller.BaseController;
import com.emin.base.dao.PageRequest;
import com.emin.base.dao.SortKey;
import com.emin.base.exception.EminException;
import com.emin.dataCenterWeb.exception.DCExceptionCode;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import com.alibaba.fastjson.JSONObject;

@Controller
public class DCBaseController extends BaseController{
	private Logger logger = LoggerFactory.getLogger(DCBaseController.class);
	
	@Autowired
	private Configuration configuration;

	@Override
	protected String getBasePath() {
		
		return getRequest().getScheme()+"://"+getRequest().getServerName()+":"+getRequest().getServerPort()+"/";
	}

	public void printFtl(String code, Map<String, Object> data) {
		
		Template t;
		try {
			if(data==null){
				data = new HashMap<>();
				
			}
			data.put("base", getBasePath());
			configuration.setDefaultEncoding("UTF-8");
			configuration.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
			//TemplateLoader loader = configuration.getTemplateLoader();
			getResponse().setHeader("Pragma","No-cache");
			getResponse().setHeader("Cache-Control","no-cache"); 
			getResponse().setDateHeader("Expires", 0);
			getResponse().setCharacterEncoding("UTF-8");
			getResponse().setContentType("text/html");
			Enumeration<String> names = getRequest().getSession().getAttributeNames();
			while (names.hasMoreElements()) {
				String name = (String) names.nextElement();
				data.put(name, getRequest().getSession().getAttribute(name));
			}
			t = configuration.getTemplate(code+".html");
			t.process(data, getResponse().getWriter());
		}catch(Exception e){
			e.printStackTrace();
		}
	   
	}
	public void printFail(String message){
		JSONObject obj = new JSONObject();
		obj.put("message", message);
		obj.put("success", false);
		printJson(obj);
	}
	public PageRequest getPageRequestData(){
		String ascend = getParameterValue("order")==null?null:(String) getParameterValue("order");
		String sortPropertys = getParameterValue("sort")==null?null:(String) getParameterValue("sort");
		String page = "1";
		if(getParameterValue("page")!=null){
			page =getParameterValue("page").toString();
		}
		int rowsPerPage = 20;
		if(getParameterValue("limit")!=null){
			rowsPerPage = Integer.parseInt(getParameterValue("limit").toString());
		}        
        int intPage = Integer.parseInt((page == null || page.equals("0")) ? "1":page);  
        int offset =(intPage-1)*rowsPerPage;
		boolean ascending = true;
		SortKey orderBy = null;
		SortKey[] sortKeys = null;
		if(sortPropertys != null && !sortPropertys.equals("") && ascend != null){
				if("desc".equals(ascend)){
					ascending = false;
				}else{
					ascending = true;
				}
				orderBy = ascending?SortKey.asc(sortPropertys):SortKey.desc(sortPropertys);
				sortKeys = new SortKey[]{orderBy};
		}
		
		int limit = rowsPerPage;
		PageRequest pageRequest = new PageRequest();
		pageRequest.setOffset(offset);
		pageRequest.setLimit(limit);
		pageRequest.setOrderBy(sortKeys);
		pageRequest.setCurrentPage(intPage);
		//logger.debug("PageRequest : " + JSONObject.fromObject(pageRequest));
		return pageRequest;
	}
	
    public void printJson(com.alibaba.fastjson.JSONObject json, SerializerFeature... features) {
		
    	OutputStream pw = null;
		getResponse().setCharacterEncoding("UTF-8");
		getResponse().setHeader("Pragma", "No-cache");
		getResponse().setHeader("Cache-Control", "no-cache");
		getResponse().setHeader("Cache-Control", "no-store");
		getResponse().setContentType("text/html;charset=UTF-8");
		try {
			pw = getResponse().getOutputStream();
			//System.out.println("printJson==================" + JSON.toJSONString(json,features));
			pw.write(JSON.toJSONString(json,features).getBytes("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
    @ExceptionHandler(EminException.class)
	@ResponseBody
	public JSONObject handlerEminException(HttpServletRequest request, EminException e) {
		logger.error(e.getCode(),e);
		return failResult(e.getCode(),e.getLocalizedMessage());
	}
	
	
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public JSONObject handlerException(HttpServletRequest request, Exception e) {
		logger.error(e.getMessage(),e);
		return failResult(DCExceptionCode.DC_WEB_SERVER_ERROR,"内部服务错误");
	}
	
	public JSONObject successResult(){
		
		JSONObject obj = new JSONObject();
		obj.put("success", true);
		return obj;
	}
	public JSONObject failResult(){
		
		JSONObject obj = new JSONObject();
		obj.put("success", false);
		return obj;
	} 
	public JSONObject failResult(String code,String message ){
		
		JSONObject obj = new JSONObject();
		obj.put("success", false);
		obj.put("message", message);
		obj.put("code", code);
		return obj;
	} 
	public <T> JSONObject successResult(T t){
		JSONObject obj = new JSONObject();
		obj.put("success", true);
		obj.put("data", t);
		return obj;
	}
	public <T> JSONObject successResult(List<T> t){
		JSONObject obj = new JSONObject();
		obj.put("success", true);
		obj.put("data", t);
		return obj;
	}
}
