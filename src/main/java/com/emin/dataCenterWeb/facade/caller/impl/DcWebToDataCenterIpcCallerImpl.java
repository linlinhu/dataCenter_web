package com.emin.dataCenterWeb.facade.caller.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.emin.base.dao.PageRequest;
import com.emin.base.dao.PagedResult;
import com.emin.base.service.Condition;
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterIpcCaller;
import com.emin.platform.dataCenter.domain.IPC;
import com.emin.platform.dataCenter.facade.accepters.IPCAccepter;

/**
 * Created by black on 2017/9/1.
 */
@Service("dcWebToDataCenterIpcCaller")
public class DcWebToDataCenterIpcCallerImpl implements DcWebToDataCenterIpcCaller{

	@Reference(version="1.0.0")
    private IPCAccepter IPCAccepter;
	
	@Override
	public IPC saveOrUpdateIPC(IPC IPC)  {
		return IPCAccepter.saveOrUpdateIpc(IPC);
	}

	@Override
	public List<IPC> findIPCs(List<Condition> conditions)  {
		return IPCAccepter.findIpcs(conditions);
	}

	@Override
	public PagedResult<IPC> loadPagedIPCsByCondition(PageRequest pageRequest,
			List<Condition> conditions)  {
		return IPCAccepter.loadPagedIpcsByCondition(pageRequest, conditions);
	}

	@Override
	public PagedResult<IPC> loadPagedIPCsByMatch(PageRequest pageRequest, String... match)
			 {
		return IPCAccepter.loadPagedIpcsByMatch(pageRequest, match);
	}

	@Override
	public void deleteIPC(Long id)  {
		IPCAccepter.disableIpct(id);
	}

	@Override
	public IPC findById(Long id)  {
		return IPCAccepter.findById(id);
	}
    
}
