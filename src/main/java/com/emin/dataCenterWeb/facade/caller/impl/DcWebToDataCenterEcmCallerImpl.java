package com.emin.dataCenterWeb.facade.caller.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.emin.base.dao.PageRequest;
import com.emin.base.dao.PagedResult;
import com.emin.base.service.Condition;
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterEcmCaller;
import com.emin.platform.dataCenter.domain.Ecm;
import com.emin.platform.dataCenter.facade.accepters.EcmAccepter;

/**
 * Created by black on 2017/9/1.
 */
@Service("dcWebToDataCenterEcmCaller")
public class DcWebToDataCenterEcmCallerImpl implements DcWebToDataCenterEcmCaller{

	@Reference(version="1.0.0")
    private EcmAccepter ecmAccepter;
	
	@Override
	public Ecm saveOrUpdateEcm(Ecm ecm)  {
		return ecmAccepter.saveOrUpdateEcm(ecm);
	}

	@Override
	public List<Ecm> findEcms(List<Condition> conditions)  {
		return ecmAccepter.findEcms(conditions);
	}

	@Override
	public PagedResult<Ecm> loadPagedEcmsByCondition(PageRequest pageRequest,
			List<Condition> conditions)  {
		return ecmAccepter.loadPagedEcmsByCondition(pageRequest, conditions);
	}

	@Override
	public void deleteEcm(Long id)  {
		ecmAccepter.deleteEcm(id);
	}

	@Override
	public Ecm findById(Long id)  {
		return ecmAccepter.findById(id);
	}

	@Override
	public Boolean synEcm(String ecmCode)  {
		return ecmAccepter.synEcm(ecmCode);
	}
    
}
