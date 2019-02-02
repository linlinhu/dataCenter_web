package com.emin.dataCenterWeb.facade.caller;

import java.util.List;

import com.emin.base.dao.PageRequest;
import com.emin.base.dao.PagedResult;
import com.emin.base.service.Condition;
import com.emin.platform.dataCenter.domain.Device;
import com.emin.platform.dataCenter.domain.Product;

/**
 * Created by black on 2017/9/1.
 */
public interface DcWebToDataCenterDeviceCaller {
    
	/**保存产品
	 * @param Device
	 * @return Device
	 * @see Device
	 */
	public void saveOrUpdateDevice(Device device) ;
	
	/**根据条件查询产品
	 * @param conditions
	 * @return Device
	 * @see Device
	 */
	public List<Device> findDevices(List<Condition> conditions);
	
	/**根据条件分页查询产品
	 * @param pageRequest
	 * @param conditions
	 * @return {@link PagedResult}
	 * @see Product
	 */
	public PagedResult<Device> loadPagedDevicesByCondition(PageRequest pageRequest, List<Condition> conditions);

	/**
	 * 根据关键字分页查询产品<br>
	 * 关键字可用项
	 * @param pageRequest 
	 * @param match
	 * @return {@link PagedResult}
	 * @see Device
	 */
	public PagedResult<Device> loadPagedDevicesByMatch(PageRequest pageRequest , String... match);


	public void deleteDevice(Long id) ;
	
	
	public Device findById(Long id);

	public List<Device> getloadDeviceByProductionLineIdOrModel(Long productionLineId, String model);
}
