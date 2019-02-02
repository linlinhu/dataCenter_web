package com.emin.dataCenterWeb.facade.caller;

import java.util.List;

import com.emin.base.dao.PageRequest;
import com.emin.base.dao.PagedResult;
import com.emin.base.service.Condition;
import com.emin.platform.dataCenter.domain.IPC;
import com.emin.platform.dataCenter.domain.Product;

/**
 * Created by black on 2017/9/1.
 */
public interface DcWebToDataCenterIpcCaller {
    
	/**保存IPC
	 * @param IPC
	 * @return IPC
	 * @see IPC
	 */
	public IPC saveOrUpdateIPC(IPC ipc) ;
	
	/**根据条件查询IPC
	 * @param conditions
	 * @return IPC
	 * @see IPC
	 */
	public List<IPC> findIPCs(List<Condition> conditions);
	
	/**根据条件分页查询IPC
	 * @param pageRequest
	 * @param conditions
	 * @return {@link PagedResult}
	 * @see Product
	 */
	public PagedResult<IPC> loadPagedIPCsByCondition(PageRequest pageRequest, List<Condition> conditions);

	/**
	 * 根据关键字分页查询IPC<br>
	 * 关键字可用项
	 * @param pageRequest 
	 * @param match
	 * @return {@link PagedResult}
	 * @see IPC
	 */
	public PagedResult<IPC> loadPagedIPCsByMatch(PageRequest pageRequest , String... match);

 
	public void deleteIPC(Long id) ;
	
	public IPC findById(Long id);
}
