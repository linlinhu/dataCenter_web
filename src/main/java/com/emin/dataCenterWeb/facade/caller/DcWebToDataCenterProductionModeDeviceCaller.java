package com.emin.dataCenterWeb.facade.caller;

import java.util.List;

import com.emin.base.dao.PageRequest;
import com.emin.base.dao.PagedResult;
import com.emin.base.service.Condition;
import com.emin.platform.dataCenter.domain.ProductionModeDevice;

/**
 * Created by black on 2017/8/21.
 */
public interface DcWebToDataCenterProductionModeDeviceCaller {
    
	/**保存生产模式设备明细
	 * @param productionModeDevice
	 * @return productionModeDevice
	 * @see productionModeDevice
	 */
	public ProductionModeDevice saveOrUpdateProductionModeDevice(ProductionModeDevice productionModeDevice) ;
	
	/**根据条件查询生产模式设备明细
	 * @param conditions
	 * @return productionModeDevice
	 * @see productionModeDevice
	 */
	public List<ProductionModeDevice> findProductionModeDevices(List<Condition> conditions);
	
	/**根据条件分页查询生产模式设备明细
	 * @param pageRequest
	 * @param conditions
	 * @return {@link PagedResult}
	 * @see ProductionModeDevice
	 */
	public PagedResult<ProductionModeDevice> loadPagedProductionModeDevicesByCondition(PageRequest pageRequest, List<Condition> conditions);

	/**
	 * 根据关键字分页查询生产模式设备明细<br>
	 * 关键字可用项
	 * @param pageRequest 
	 * @param match
	 * @return {@link PagedResult}
	 * @see ProductionModeDevice
	 */
	public PagedResult<ProductionModeDevice> loadPagedProductionModeDevicesByMatch(PageRequest pageRequest , String... match);

 
	public void deleteProductionModeDevice(Long id) ;
	
	public ProductionModeDevice findById(Long id);

	public PagedResult<ProductionModeDevice> loadPagedProductionModeDevicesByMatchAndCondition(PageRequest pageRequest,List<Condition> conditions, String... match);
}
