package com.emin.dataCenterWeb.facade.caller;

import java.util.List;

import com.emin.base.service.Condition;
import com.emin.platform.dataCenter.domain.BusinessCode;

/**
 * Created by black on 2017/8/21.
 */
public interface DcWebToDataCenterBusinessCodeCaller {
    
	/**保存BusinessCode
	 * @param BusinessCode
	 * @return BusinessCode
	 * @see BusinessCode
	 */
	public BusinessCode saveOrUpdateBusinessCode(BusinessCode businessCode) ;
	
	/**根据条件查询BusinessCode
	 * @param conditions
	 * @return BusinessCode
	 * @see BusinessCode
	 */
	public List<BusinessCode> findBusinessCodes(List<Condition> conditions) ;
	

	public void deleteBusinessCode(Long id) ;
}
