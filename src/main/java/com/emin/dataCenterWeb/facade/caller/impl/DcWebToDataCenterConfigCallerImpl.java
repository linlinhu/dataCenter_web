package com.emin.dataCenterWeb.facade.caller.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.emin.base.dao.PageRequest;
import com.emin.base.dao.PagedResult;
import com.emin.base.service.Condition;
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterConfigCaller;
import com.emin.platform.dataCenter.domain.Config;
import com.emin.platform.dataCenter.facade.accepters.ConfigAccepter;

/**
 * Created by black on 2017/11/7.
 */
@Service("dcWebToDataCenterConfigCaller")
public class DcWebToDataCenterConfigCallerImpl implements DcWebToDataCenterConfigCaller{

	@Reference(version="1.0.0")
    private ConfigAccepter configAccepter;

	@Override
	public Config saveOrUpdateConfig(Config config)  {
		return configAccepter.saveOrUpdateConfig(config);
	}

	@Override
	public List<Config> findConfigs(List<Condition> conditions)  {
		return configAccepter.findConfigs(conditions);
	}

	@Override
	public PagedResult<Config> loadPagedConfigsByCondition(PageRequest pageRequest, List<Condition> conditions) {
		return configAccepter.loadPagedConfigsByCondition(pageRequest, conditions);
	}

	@Override
	public PagedResult<Config> loadPagedConfigsByMatch(PageRequest pageRequest, String... match) {
		return configAccepter.loadPagedConfigsByMatch(pageRequest, match);
	}

	@Override
	public void deleteConfig(Long id) {
		configAccepter.deleteConfig(id);
	}

	@Override
	public Config findById(Long id) {
		return configAccepter.findById(id);
	}

	
	
}
