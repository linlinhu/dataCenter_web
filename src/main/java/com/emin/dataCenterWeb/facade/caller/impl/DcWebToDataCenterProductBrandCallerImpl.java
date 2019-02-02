package com.emin.dataCenterWeb.facade.caller.impl;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterProductBrandCaller;
import com.emin.platform.dataCenter.facade.accepters.BrandAccepter;

/**
 * Created by black on 2017/11/8.
 */
@Service("dcWebToDataCenterProductBrandCaller")
public class DcWebToDataCenterProductBrandCallerImpl implements DcWebToDataCenterProductBrandCaller{

	@Reference(version="1.0.1",timeout = 10000)
    private BrandAccepter productBrandAccepter;

	
	@Override
	public boolean synBrand()  {
		return productBrandAccepter.synBrand();
	}
	
	
}
