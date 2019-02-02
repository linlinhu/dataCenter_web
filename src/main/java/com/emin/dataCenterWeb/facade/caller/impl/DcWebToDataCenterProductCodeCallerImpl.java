package com.emin.dataCenterWeb.facade.caller.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.emin.base.dao.PageRequest;
import com.emin.base.dao.PagedResult;
import com.emin.base.service.Condition;
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterProductCodeCaller;
import com.emin.platform.dataCenter.domain.ProductCode;
import com.emin.platform.dataCenter.facade.accepters.ProductCodeAccepter;

/**
 * Created by black on 2017/9/1.
 */
@Service("dcWebToDataCenterProductCodeCaller")
public class DcWebToDataCenterProductCodeCallerImpl implements DcWebToDataCenterProductCodeCaller{

	@Reference(version="1.0.0")
    private ProductCodeAccepter productCodeAccepter;
	
	@Override
	public ProductCode saveOrUpdateProductCode(ProductCode productCode)  {
		return productCodeAccepter.saveOrUpdateProductCode(productCode);
	}

	@Override
	public List<ProductCode> findProductCodes(List<Condition> conditions)  {
		return productCodeAccepter.findProductCodes(conditions);
	}

	@Override
	public PagedResult<ProductCode> loadPagedProductCodesByCondition(PageRequest pageRequest,List<Condition> conditions)  {
		return productCodeAccepter.loadPagedProductCodesByCondition(pageRequest, conditions);
	}

	@Override
	public PagedResult<ProductCode> loadPagedProductCodesByMatch(PageRequest pageRequest, String... match)
			 {
		return productCodeAccepter.loadPagedProductCodesByMatch(pageRequest, match);
	}

	@Override
	public void deleteProductCode(Long id)  {
		productCodeAccepter.deleteProductCode(id);
	}

	@Override
	public ProductCode findById(Long id)  {
		return productCodeAccepter.findById(id);
	}
    
}
