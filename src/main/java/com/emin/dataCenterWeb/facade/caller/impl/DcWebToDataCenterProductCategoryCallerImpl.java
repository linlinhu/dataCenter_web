package com.emin.dataCenterWeb.facade.caller.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterProductCategoryCaller;
import com.emin.platform.dataCenter.domain.ProductCategory;
import com.emin.platform.dataCenter.facade.accepters.ProductCategoryAccepter;

/**
 * Created by black on 2017/11/8.
 */
@Service("dcWebToDataCenterProductCategoryCaller")
public class DcWebToDataCenterProductCategoryCallerImpl implements DcWebToDataCenterProductCategoryCaller{

	@Reference(version="1.0.0")
    private ProductCategoryAccepter productCategoryAccepter;

	@Override
	public List<ProductCategory> findByParentId(Long parentId)  {
		
		return productCategoryAccepter.findByParentId(parentId);
	}

	@Override
	public boolean synCategory()  {
		return productCategoryAccepter.synch();
	}
	
	
}
