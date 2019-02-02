package com.emin.dataCenterWeb.facade.caller.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.emin.base.dao.PageRequest;
import com.emin.base.dao.PagedResult;
import com.emin.base.service.Condition;
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterProductionModeCaller;
import com.emin.platform.dataCenter.domain.ProductionMode;
import com.emin.platform.dataCenter.facade.accepters.ProductionModeAccepter;

/**
 * Created by black on 2017/9/1.
 */
@Service("dcWebToDataCenterProductionModeCaller")
public class DcWebToDataCenterProductionModeCallerImpl implements DcWebToDataCenterProductionModeCaller{

	@Reference(version="1.0.0")
    private ProductionModeAccepter productionModeAccepter;
	
	@Override
	public ProductionMode saveOrUpdateProductionMode(ProductionMode productionMode)  {
		return productionModeAccepter.saveOrUpdateProductionMode(productionMode);
	}

	@Override
	public List<ProductionMode> findProductionModes(List<Condition> conditions)  {
		return productionModeAccepter.findProductionModes(conditions);
	}

	@Override
	public PagedResult<ProductionMode> loadPagedProductionModesByCondition(PageRequest pageRequest,List<Condition> conditions)  {
		return productionModeAccepter.loadPagedProductionModesByCondition(pageRequest, conditions);
	}

	@Override
	public PagedResult<ProductionMode> loadPagedProductionModesByMatch(PageRequest pageRequest, String... match) {
		return productionModeAccepter.loadPagedProductionModesByMatch(pageRequest, match);
	}

	@Override
	public void deleteProductionMode(Long id)  {
		productionModeAccepter.deleteProductionMode(id);
	}

	@Override
	public ProductionMode findById(Long id)  {
		return productionModeAccepter.findById(id);
	}


	@Override
	public PagedResult<ProductionMode> loadPagedProductionModesByMatchAndCondition(PageRequest pageRequest,List<Condition> conditions, String... match)  {
		return productionModeAccepter.loadPagedProductionModesByMatchAndCondition(pageRequest,conditions, match);
	}

	@Override
	public void publishProductionMode(Long id) {
	    productionModeAccepter.publishProductionMode(id);		
	}
    
}
