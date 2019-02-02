package com.emin.dataCenterWeb.facade.caller.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.emin.base.dao.PageRequest;
import com.emin.base.dao.PagedResult;
import com.emin.base.service.Condition;
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterUserCaller;
import com.emin.platform.dataCenter.domain.User;
import com.emin.platform.dataCenter.facade.accepters.UserAccepter;

/**
 * Created by black on 2017/9/5.
 */
@Service("dcWebToDataCenterUserCaller")
public class DcWebToDataCenterUserCallerImpl implements DcWebToDataCenterUserCaller{

	@Reference(version="1.0.0")
    private UserAccepter userAccepter;
	
	@Override
	public User saveOrUpdateUser(User user)  {
		return userAccepter.saveOrUpdateUser(user);
	}

	@Override
	public List<User> findUsers(List<Condition> conditions)  {
		return userAccepter.findUsers(conditions);
	}

	@Override
	public PagedResult<User> loadPagedUsersByCondition(PageRequest pageRequest,List<Condition> conditions)  {
		return userAccepter.loadPagedUsersByCondition(pageRequest, conditions);
	}

	@Override
	public PagedResult<User> loadPagedUsersByMatch(PageRequest pageRequest, String... match)
			 {
		return userAccepter.loadPagedUsersByMatch(pageRequest, match);
	}

	@Override
	public void deleteUser(Long id)  {
		userAccepter.deleteUser(id);
	}

	@Override
	public User findById(Long id)  {
		return userAccepter.findById(id);
	}

	@Override
	public Boolean synUser(String ecmCode)  {
		return userAccepter.synUser(ecmCode);
	}

	@Override
	public void disableUser(Long id, Boolean disable)  {
		userAccepter.status(id, disable);
	}
    
}
