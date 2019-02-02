package com.emin.dataCenterWeb.facade.caller;

import java.util.List;
import com.emin.base.dao.PageRequest;
import com.emin.base.dao.PagedResult;
import com.emin.base.service.Condition;
import com.emin.platform.dataCenter.domain.Ecm;

/**
 * Created by black on 2017/10/27.
 */
public interface DcWebToDataCenterEcmCaller {
    
	/**保存主体
	 * @param Ecm
	 * @return Ecm
	 * @see Ecm
	 */
	public Ecm saveOrUpdateEcm(Ecm Ecm) ;
	
	/**根据条件查询主体
	 * @param conditions
	 * @return Ecm
	 * @see Ecm
	 */
	public List<Ecm> findEcms(List<Condition> conditions);
	
	/**根据条件分页查询主体
	 * @param pageRequest
	 * @param conditions
	 * @return {@link PagedResult}
	 * @see Ecm
	 */
	public PagedResult<Ecm> loadPagedEcmsByCondition(PageRequest pageRequest, List<Condition> conditions);
 
	public void deleteEcm(Long id) ;
	
	public Ecm findById(Long id);
	
	public Boolean synEcm(String ecmCode);
}
