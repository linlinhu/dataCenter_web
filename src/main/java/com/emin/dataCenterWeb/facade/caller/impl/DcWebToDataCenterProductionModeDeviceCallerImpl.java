package com.emin.dataCenterWeb.facade.caller.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.emin.base.dao.PageRequest;
import com.emin.base.dao.PagedResult;
import com.emin.base.service.Condition;
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterProductionModeDeviceCaller;
import com.emin.platform.dataCenter.domain.ProductionModeDevice;
import com.emin.platform.dataCenter.facade.accepters.ProductionModeDeviceAccepter;

/**
 * Created by black on 2017/9/1.
 */
@Service("dcWebToDataCenterProductionModeDeviceCaller")
public class DcWebToDataCenterProductionModeDeviceCallerImpl implements DcWebToDataCenterProductionModeDeviceCaller{

	@Reference(version="1.0.0")
    private ProductionModeDeviceAccepter productionModeDeviceAccepter;
	
	@Override
	public ProductionModeDevice saveOrUpdateProductionModeDevice(ProductionModeDevice productionModeDevice)  {
		return productionModeDeviceAccepter.saveOrUpdateProductionModeDevice(productionModeDevice);
	}

	@Override
	public List<ProductionModeDevice> findProductionModeDevices(List<Condition> conditions)  {
		return productionModeDeviceAccepter.findProductionModeDevices(conditions);
	}

	@Override
	public PagedResult<ProductionModeDevice> loadPagedProductionModeDevicesByCondition(PageRequest pageRequest,List<Condition> conditions)  {
		return productionModeDeviceAccepter.loadPagedProductionModeDevicesByCondition(pageRequest, conditions);
	}

	@Override
	public PagedResult<ProductionModeDevice> loadPagedProductionModeDevicesByMatch(PageRequest pageRequest, String... match) {
		return productionModeDeviceAccepter.loadPagedProductionModeDevicesByMatch(pageRequest, match);
	}

	@Override
	public void deleteProductionModeDevice(Long id)  {
		productionModeDeviceAccepter.deleteProductionModeDevice(id);
	}

	@Override
	public ProductionModeDevice findById(Long id)  {
		return productionModeDeviceAccepter.findById(id);
	}

	@Override
	public PagedResult<ProductionModeDevice> loadPagedProductionModeDevicesByMatchAndCondition(PageRequest pageRequest,List<Condition> conditions, String... match)  {
		return productionModeDeviceAccepter.loadPagedProductionModeDevicesByMatchAndCondition(pageRequest,conditions, match);
	}
}
