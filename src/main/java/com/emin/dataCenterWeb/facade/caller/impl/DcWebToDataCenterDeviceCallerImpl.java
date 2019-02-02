package com.emin.dataCenterWeb.facade.caller.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.emin.base.dao.PageRequest;
import com.emin.base.dao.PagedResult;
import com.emin.base.service.Condition;
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterDeviceCaller;
import com.emin.platform.dataCenter.domain.Device;
import com.emin.platform.dataCenter.facade.accepters.DeviceAccepter;

/**
 * Created by black on 2017/9/1.
 */
@Service("dcWebToDataCenterDeviceCaller")
public class DcWebToDataCenterDeviceCallerImpl implements DcWebToDataCenterDeviceCaller{

	@Reference(version="1.0.0")
    private DeviceAccepter deviceAccepter;
	
	@Override
	public void saveOrUpdateDevice(Device Device)  {
		deviceAccepter.deviceRegistration(Device);
	}

	@Override
	public List<Device> findDevices(List<Condition> conditions)  {
		return deviceAccepter.findDevices(conditions);
	}

	@Override
	public PagedResult<Device> loadPagedDevicesByCondition(PageRequest pageRequest,List<Condition> conditions)  {
		return deviceAccepter.loadPagedDevicesByCondition(pageRequest, conditions);
	}

	@Override
	public PagedResult<Device> loadPagedDevicesByMatch(PageRequest pageRequest, String... match) {
		return deviceAccepter.loadPagedDevicesByMatch(pageRequest, match);
	}

	@Override
	public void deleteDevice(Long id)  {
		deviceAccepter.deviceDelete(id);
	}

	@Override
	public Device findById(Long id)  {
		return deviceAccepter.findById(id);
	}

	@Override
	public List<Device> getloadDeviceByProductionLineIdOrModel(Long productionLineId, String model) {
		return deviceAccepter.getloadDeviceByProductionLineIdOrModel(productionLineId,model);
	}
    
}
