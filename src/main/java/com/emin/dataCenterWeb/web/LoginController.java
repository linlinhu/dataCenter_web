package com.emin.dataCenterWeb.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONObject;
import com.emin.base.service.Condition;
import com.emin.base.service.Condition.ConditionOperator;
import com.emin.base.service.Condition.ConditionType;
import com.emin.base.util.CookieUtil;
import com.emin.base.util.EncryptUtils;
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterUserCaller;
import com.emin.platform.dataCenter.domain.User;
import io.swagger.annotations.ApiOperation;

@Controller
public class LoginController extends DCBaseController{
	@Autowired 
	@Qualifier("dcWebToDataCenterUserCaller")
	private DcWebToDataCenterUserCaller dcWebToDataCenterUserCaller;
	
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public void loginPage(@RequestParam Map<String,Object> map,HttpServletRequest request){
		
		printFtl("login", map);
	}
	@RequestMapping(value="/login",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject login(String account,String password){
		JSONObject json = new JSONObject();
   		String message = "";
		boolean success = false;

		if (StringUtils.isBlank(account) || StringUtils.isBlank(password) ) {
			message = "账号、密码不能为空";
		}else {
			String  passwordEncrypt = EncryptUtils.encodeMD5(password);		
			List<Condition> conditions = new ArrayList<>();
			//conditions.add(new Condition("disable", ConditionOperator.EQ, ConditionType.OTHER, false));//没有禁用:false表示没有被禁用
			conditions.add(new Condition(User.PROP_STATUS, ConditionOperator.EQ, ConditionType.OTHER, User.STATUS_VALID));//没有删除
			conditions.add(new Condition(User.PROP_ACOUNT, ConditionOperator.EQ, ConditionType.OTHER, account));
			conditions.add(new Condition(User.PROP_PASSWORD, ConditionOperator.EQ, ConditionType.OTHER, passwordEncrypt));
		
			List<User>  userList= dcWebToDataCenterUserCaller.findUsers(conditions);		
			if (userList != null && userList.size() > 0) {
				User user = userList.get(0);
				if ( user.getDisable() == true) {
					message = "账号被禁用";
				}else if ( password.equals(User.INIT_PASSWORD) ) {
					message = "密码不能为初始密码，请修改";
					json.put("result", "init");
				}else{
					success = true;
				}			
				getRequest().getSession().setAttribute("user", user);
				CookieUtil.addCookie(getResponse(), "DCUserAccount", user.getAcount(), 60*30);			
									
			}else{
				message = "账号或者密码错误";
			}
		}						
            
		json.put("success", success);
		json.put("message", message);
		return json;
	}
	
	@RequestMapping(value="/signout",method=RequestMethod.GET)
	public void logout(){
		Cookie[] cookies=getRequest().getCookies();

		for(Cookie cookie: cookies){

		cookie.setValue(null);
		cookie.setMaxAge(0);		

		getResponse().addCookie(cookie);

		}
		getRequest().getSession().removeAttribute("user");
		getRequest().getSession().invalidate();
		//printOk();
	}
	
	
	@RequestMapping("/modify_initial_password")
	@ApiOperation(httpMethod="POST",value=" ")
	public void modify_initial_password(String account){
		Map<String, Object> data = new HashMap<>();
		data.put("account", account);
		printFtl("modify_initial_password", data);
	}
	
}
