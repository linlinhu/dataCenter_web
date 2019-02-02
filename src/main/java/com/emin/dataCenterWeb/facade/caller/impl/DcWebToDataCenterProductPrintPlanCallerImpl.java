package com.emin.dataCenterWeb.facade.caller.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.emin.base.dao.PageRequest;
import com.emin.base.dao.PagedResult;
import com.emin.base.service.Condition;
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterProductPrintPlanCaller;
import com.emin.platform.dataCenter.domain.ProductPrintPlan;
import com.emin.platform.dataCenter.facade.accepters.ProductPrintPlanAccepter;

/**
 * Created by black on 2017/9/1.
 */
@Service("dcWebToDataCenterProductPrintPlanCaller")
public class DcWebToDataCenterProductPrintPlanCallerImpl implements DcWebToDataCenterProductPrintPlanCaller{

	@Reference(version="1.0.0")
    private ProductPrintPlanAccepter productPrintPlanAccepter;
	
	@Override
	public ProductPrintPlan saveOrUpdateProductPrintPlan(ProductPrintPlan productPrintPlan)  {
		return productPrintPlanAccepter.saveOrUpdateProductPrintPlan(productPrintPlan);
	}

	@Override
	public List<ProductPrintPlan> findProductPrintPlans(List<Condition> conditions)  {
		return productPrintPlanAccepter.findProductPrintPlans(conditions);
	}

	@Override
	public PagedResult<ProductPrintPlan> loadPagedProductPrintPlansByCondition(PageRequest pageRequest,List<Condition> conditions)  {
		return productPrintPlanAccepter.loadPagedProductPrintPlansByCondition(pageRequest, conditions);
	}

	@Override
	public PagedResult<ProductPrintPlan> loadPagedProductPrintPlansByMatch(PageRequest pageRequest, String... match)
			 {
		return productPrintPlanAccepter.loadPagedProductPrintPlansByMatch(pageRequest, match);
	}

	@Override
	public void deleteProductPrintPlan(Long id)  {
		productPrintPlanAccepter.deleteProductPrintPlan(id);
	}

	@Override
	public ProductPrintPlan findById(Long id)  {
		return productPrintPlanAccepter.findById(id);
	}

	@Override
	public void deleteExistPlan(String productNumber, String lineNumber, Long deviceType) {
		productPrintPlanAccepter.deleteExistPlan(productNumber, lineNumber, deviceType);
		
	}
    
}
