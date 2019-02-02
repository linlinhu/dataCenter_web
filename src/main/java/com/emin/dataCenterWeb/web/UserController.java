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
import com.emin.base.exception.EminException;
import com.emin.base.service.Condition;
import com.emin.base.service.Condition.ConditionOperator;
import com.emin.base.service.Condition.ConditionType;
import com.emin.base.util.EncryptUtils;
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterEcmCaller;
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterUserCaller;
import com.emin.platform.dataCenter.domain.Ecm;
import com.emin.platform.dataCenter.domain.User;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import com.alibaba.fastjson.JSONObject;


@Controller
@RequestMapping("/user")
public class UserController  extends DCBaseController {
	private Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired 
	@Qualifier("dcWebToDataCenterUserCaller")
	private DcWebToDataCenterUserCaller dcWebToDataCenterUserCaller;
	
	@Autowired 
	@Qualifier("dcWebToDataCenterEcmCaller")
	private DcWebToDataCenterEcmCaller dcWebToDataCenterEcmCaller;
	
	//密码不展示，不编辑
	//添加用户时，初始化密码
	//如果用户密码为初始化密码，登录失败，请修改密码
		
	//查询所有
	@RequestMapping("/loadUser")
	@ResponseBody
	public JSONObject loadUser(){
		
		JSONObject json = new JSONObject();
   		String message = "";
		boolean success = false;
		
		try {
	
			List<Condition> conditions = new ArrayList<>();
			conditions.add(new Condition(User.PROP_STATUS, ConditionOperator.EQ, ConditionType.OTHER, User.STATUS_VALID));
			User user = (User)getRequest().getSession().getAttribute("user");
			if (!"admin".equals(user.getAcount())) {
				conditions.add(new Condition(User.PROP_ACOUNT, ConditionOperator.EQ, ConditionType.OTHER, user.getAcount()));
			}
			PagedResult<User>  userPage= dcWebToDataCenterUserCaller.loadPagedUsersByCondition(getPageRequestData(), conditions);			
			
			json.put("data", userPage);
            success = true;
		} catch (EminException e) {
			success = false;
			message = e.getLocalizedMessage();
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			success = false;
			message = "获取用户失败";
			logger.error(e.getMessage(),e);
		}
		json.put("success", success);
		json.put("message", message);
		return json;
    }
	
	//搜索：用户编号、用户名称
	@RequestMapping("/index")
	@ResponseBody
	public void index(String keyword){
		
		Map<String, Object> data = new HashMap<>();
		
		try {
			PageRequest pageRequest = getPageRequestData();
			PagedResult<User>  userPage = null;
			
			User user = (User)getRequest().getSession().getAttribute("user");
			if (!"admin".equals(user.getAcount())) {
				//data.put("users", userPage);
			}else {
				List<Condition> conditions = new ArrayList<>();
				conditions.add(new Condition(User.PROP_ACOUNT,ConditionOperator.NE,ConditionType.OTHER,"admin"));
				if (StringUtils.isNotBlank(keyword)) {
					userPage = dcWebToDataCenterUserCaller.loadPagedUsersByMatch(pageRequest, StringUtils.split(keyword," "));
				}else {
					userPage= dcWebToDataCenterUserCaller.loadPagedUsersByCondition(pageRequest, conditions);
				}
				
			}
			data.put("data", userPage);
			data.put("cur", pageRequest.getCurrentPage());
			data.put("limit", pageRequest.getLimit());
			data.put("keyword", keyword);
			data.put("success", true);
		} catch (EminException e) {
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		printFtl("modules/user/manage", data);
    }	
	
	//保存
	@RequestMapping("/saveUser")
	@ResponseBody
	public JSONObject saveUser(String jsonStr){
		
		JSONObject json = new JSONObject();
   		String message = "";
		boolean success = false;
		
		try {
			//判断当前登录人是不是administrator
			User user = (User)getRequest().getSession().getAttribute("user");
			if (!"admin".equals(user.getAcount())) {
				message = "无此权限，请联系admin";
			}else {
				User userVo = JSON.parseObject(jsonStr, User.class);
				if (userVo.getId() == null) {
					//设置初始化密码
					userVo.setPassword(EncryptUtils.encodeMD5(User.INIT_PASSWORD));
				}
				dcWebToDataCenterUserCaller.saveOrUpdateUser(userVo);
				
	            success = true;
            }
		} catch (EminException e) {
			success = false;
			message = e.getLocalizedMessage();
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			success = false;
			message = "保存用户失败";
			logger.error(e.getMessage(),e);
		}
		json.put("success", success);
		json.put("message", message);
		return json;
    }	
	
	
	
	//删除
	@RequestMapping("/deleteUser")
	@ResponseBody
	public JSONObject deleteUser(Long[] ids){
		
		JSONObject json = new JSONObject();
   		String message = "";
		boolean success = false;
		
		try {
			for (Long longTemp : ids) {
				dcWebToDataCenterUserCaller.deleteUser(longTemp);
			}
			
            success = true;
		} catch (EminException e) {
			success = false;
			message = e.getLocalizedMessage();
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			success = false;
			message = "删除用户失败";
			logger.error(e.getMessage(),e);
		}
		json.put("success", success);
		json.put("message", message);
		return json;
    }	

	
	
	
//	//登录
//	@RequestMapping("/login")
//	@ResponseBody
//	public JSONObject login(String acount,String password){
//		
//		JSONObject json = new JSONObject();
//   		String message = "";
//		boolean success = false;
//		
//		try {
//			String  passwordEncrypt = MD5Encrypt.MD5Encode(password);
//			
//			List<Condition> conditions = new ArrayList<>();
//			conditions.add(new Condition(User.PROP_STATUS, ConditionOperator.EQ, ConditionType.OTHER, User.STATUS_VALID));
//			conditions.add(new Condition(User.PROP_ACOUNT, ConditionOperator.EQ, ConditionType.OTHER, acount));
//			conditions.add(new Condition(User.PROP_PASSWORD, ConditionOperator.EQ, ConditionType.OTHER, passwordEncrypt));
//		
//			List<User>  userList= dcWebToDataCenterUserCaller.findUsers(conditions);		
//			if (userList != null && userList.size() > 0) {
//				success = true;
//			}
//            
//		} catch (EminException e) {
//			success = false;
//			message = e.getLocalizedMessage();
//			logger.error(e.getLocalizedMessage(),e);
//		} catch (Exception e) {
//			success = false;
//			message = "登录失败";
//			logger.error(e.getMessage(),e);
//		}
//		json.put("success", success);
//		json.put("message", message);
//		return json;
//    }	
	
	//重置密码
	@RequestMapping("/initPassword")
	@ResponseBody
	public JSONObject initPassword(String account){
		
		JSONObject json = new JSONObject();
   		String message = "";
		boolean success = false;
		
		try {
			//判断当前登录人是不是administrator
			User user = (User)getRequest().getSession().getAttribute("user");
			if (!"admin".equals(user.getAcount())) {
				message = "无此权限，请联系admin";
			}else {
				List<Condition> conditions = new ArrayList<>();
				conditions.add(new Condition(User.PROP_STATUS, ConditionOperator.EQ, ConditionType.OTHER, User.STATUS_VALID));
				conditions.add(new Condition(User.PROP_ACOUNT, ConditionOperator.EQ, ConditionType.OTHER, account));
				List<User>  userList= dcWebToDataCenterUserCaller.findUsers(conditions);
				if (userList != null && userList.size() > 0) {
					User useTemp = userList.get(0);
					useTemp.setPassword(EncryptUtils.encodeMD5(User.INIT_PASSWORD));
					dcWebToDataCenterUserCaller.saveOrUpdateUser(useTemp);
					success = true;
				}else {
					message = "无此有效账号：" + account;
				}
			}
            
		} catch (EminException e) {
			success = false;
			message = e.getLocalizedMessage();
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			success = false;
			message = "重置密码失败";
			logger.error(e.getMessage(),e);
		}
		json.put("success", success);
		json.put("message", message);
		return json;
    }	
	
	//修改密码
	@RequestMapping("/updatePassword")
	@ResponseBody
	public JSONObject updatePassword(String account,String oldPassword,String newPassword){
		
		JSONObject json = new JSONObject();
   		String message = "";
		boolean success = false;
		
		try {
			//判断当前登录人是不是account
			User user = (User)getRequest().getSession().getAttribute("user");
			
			if (StringUtils.isBlank(oldPassword) || StringUtils.isBlank(newPassword) || StringUtils.isBlank(account)  ) { 
				message = "账号密码不能为空";
				json.put("success", success);
				json.put("message", message);
				return json;
			}
			if (newPassword.equals(User.INIT_PASSWORD) ) { 
				message = "新密码不能为初始密码";
				json.put("success", success);
				json.put("message", message);
				return json;
			}
			
			if (!account.equals(user.getAcount())) {  //判断当前登录人是不是acount
				message = "账号有误";
				json.put("success", success);
				json.put("message", message);
				return json;
			}
			
			String  passwordEncryptOld = EncryptUtils.encodeMD5(oldPassword);			
			List<Condition> conditions = new ArrayList<>();
			conditions.add(new Condition(User.PROP_STATUS, ConditionOperator.EQ, ConditionType.OTHER, User.STATUS_VALID));
			conditions.add(new Condition(User.PROP_ACOUNT, ConditionOperator.EQ, ConditionType.OTHER, account));
			conditions.add(new Condition(User.PROP_PASSWORD, ConditionOperator.EQ, ConditionType.OTHER, passwordEncryptOld));		
			List<User>  userList= dcWebToDataCenterUserCaller.findUsers(conditions);
			if (userList != null && userList.size() > 0) {
				User useTemp = userList.get(0);
				useTemp.setPassword(EncryptUtils.encodeMD5(newPassword));
				dcWebToDataCenterUserCaller.saveOrUpdateUser(useTemp);
				success = true;
			}else {
				message = "原账号密码有误";
			}
			
            
		} catch (EminException e) {
			success = false;
			message = e.getLocalizedMessage();
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			success = false;
			message = "修改密码失败";
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
			User user = dcWebToDataCenterUserCaller.findById(id);
			data.put("userinfo", user);
		}
		printFtl("modules/user/form", data);
	}
	
	
	//同步用户信息
	@RequestMapping("/synUser")
	@ResponseBody
	public JSONObject synUser(){
		
		JSONObject json = new JSONObject();
   		String message = "";
		boolean success = false;
		
		try {
			
			List<Condition> conditions = new ArrayList<>();
			conditions.add(new Condition("status", ConditionOperator.EQ, ConditionType.OTHER, Ecm.STATUS_VALID));
			List<Ecm> ecmList = dcWebToDataCenterEcmCaller.findEcms(conditions);
			
			if (ecmList.isEmpty()) {
				message = "没有主体信息，不能同步";
				success = false;
			}else {
				String sn = ecmList.get(0).getSn();
				if (dcWebToDataCenterUserCaller.synUser(sn)) {
					message = "同步用户信息成功";
		            success = true;	
				}else {
					message = "同步用户信息失败";
		            success = false;	
				}
							
			}
			
			
		} catch (EminException e) {
			success = false;
			message = e.getLocalizedMessage();
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			success = false;
			message = "同步用户信息失败";
			logger.error(e.getMessage(),e);
		}
		json.put("success", success);
		json.put("message", message);
		return json;
    }
	
	// 禁用
	@RequestMapping("/disableUser")
	@ResponseBody
	public JSONObject disablePerson(Long ids){
		
		JSONObject json = new JSONObject();
   		String message = "";
		boolean success = false;
		try {
			
			dcWebToDataCenterUserCaller.disableUser(ids, true);
			
			message = "禁用用户成功";
            success = true;	
		} catch (EminException e) {
			logger.error(e.getLocalizedMessage(),e);
			message = "禁用用户失败";
            success = true;	
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			message = "禁用用户失败";
            success = true;	
		}
		json.put("success", success);
		json.put("message", message);
		return json;
    }
		
	// 启用
	@RequestMapping("/enableUser")
	@ResponseBody
	public JSONObject enablePerson(Long ids){
		
		JSONObject json = new JSONObject();
   		String message = "";
		boolean success = false;
		try {
			
			dcWebToDataCenterUserCaller.disableUser(ids, false);
			
			message = "启用用户成功";
            success = true;	
		} catch (EminException e) {
			logger.error(e.getLocalizedMessage(),e);
			message = "启用用户失败";
            success = false;	
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			message = "启用用户失败";
            success = false;	
		}
		json.put("success", success);
		json.put("message", message);
		return json;
    }
	
	
	
}
