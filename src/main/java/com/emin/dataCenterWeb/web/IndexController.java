package com.emin.dataCenterWeb.web;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/*import com.alibaba.fastjson.JSON;*/
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterEcmCaller;
import com.emin.platform.dataCenter.domain.Ecm;

import io.swagger.annotations.ApiOperation;
@Controller
public class IndexController extends DCBaseController{

	
	
	@Value("0.0.1-SNAPSHOT")
	private String version;
	
	@Autowired 
	@Qualifier("dcWebToDataCenterEcmCaller")
	private DcWebToDataCenterEcmCaller dcWebToDataCenterEcmCaller;
	
	@RequestMapping("/")
	@ApiOperation(httpMethod="GET",value="加载主页，包含已授权的菜单列表")
	public void index(Map<String,Object> map,HttpServletRequest request){
		List<Ecm> ecms = dcWebToDataCenterEcmCaller.findEcms(null);
		if (ecms.size() > 0) {
			map.put("existEcm", 1);
		} else  {
			map.put("existEcm", 0);
		}
		printFtl("index", map);
	}
	
	@RequestMapping("/404")
	@ApiOperation(httpMethod="GET",value="页面没找到")
	public void pageNotFound(){
		printFtl("404", null);
	}

	@RequestMapping("/500")
	@ApiOperation(httpMethod="GET",value="页面错误")
	public void pageError(){
		printFtl("500", null);
	}
	
	@RequestMapping("/{module}/module")
	public void module(@PathVariable String module){
		if(module!=null && module.indexOf("-")!=-1){
			module = module.replaceAll("-", "/");
		}
		printFtl(module, null);
	}
	
	
}
