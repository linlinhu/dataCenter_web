package com.emin.dataCenterWeb.facade.caller;

import java.util.List;

import com.emin.base.dao.PageRequest;
import com.emin.base.dao.PagedResult;
import com.emin.base.service.Condition;
import com.emin.platform.dataCenter.domain.Batch;

/**
 * Created by black on 2017/9/11.
 */
public interface DcWebToDataCenterBatchCaller {
    
	/**保存批次
	 * @param Batch
	 * @return Batch
	 * @see Batch
	 */
	public void saveOrUpdateBatch(Batch batch) ;
	
	/**根据条件查询批次
	 * @param conditions
	 * @return Batch
	 * @see Batch
	 */
	public List<Batch> findBatchs(List<Condition> conditions) ;
	
	/**根据条件分页查询批次
	 * @param pageRequest
	 * @param conditions
	 * @return {@link PagedResult}
	 * @see Batch
	 */
	public PagedResult<Batch> loadPagedBatchsByCondition(PageRequest pageRequest, List<Condition> conditions) ;

	/**
	 * 根据关键字分页查询批次<br>
	 * 关键字可用项
	 * @param pageRequest 
	 * @param match
	 * @return {@link PagedResult}
	 * @see Batch
	 */
	public PagedResult<Batch> loadPagedBatchsByMatch(PageRequest pageRequest , String... match) ;

 
	public void deleteBatch(Long id) ;
	
	public Batch findById(Long id) ;

	public Boolean synBatch();
	
	public void synEminCodeByBatchId(Long batchId);
}
