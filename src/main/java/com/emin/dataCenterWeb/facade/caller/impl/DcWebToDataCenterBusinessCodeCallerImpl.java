package com.emin.dataCenterWeb.facade.caller.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.emin.base.service.Condition;
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterBusinessCodeCaller;
import com.emin.platform.dataCenter.domain.BusinessCode;
import com.emin.platform.dataCenter.facade.accepters.BusinessCodeAccepter;

/**
 * Created by black on 2017/9/1.
 */
@Service("dcWebToDataCenterBusinessCodeCaller")
public class DcWebToDataCenterBusinessCodeCallerImpl implements DcWebToDataCenterBusinessCodeCaller{

	@Reference(version="1.0.0")
    private BusinessCodeAccepter businessCodeAccepter;
	
	@Override
	public BusinessCode saveOrUpdateBusinessCode(BusinessCode businessCode) {
		return businessCodeAccepter.saveOrUpdateBusinessCode(businessCode);
	}

	@Override
	public List<BusinessCode> findBusinessCodes(List<Condition> conditions) {
		return businessCodeAccepter.findBusinessCodes(conditions);
	}

	@Override
	public void deleteBusinessCode(Long id) {
		businessCodeAccepter.deleteBusinessCode(id);
	}
    
}
