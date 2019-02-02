package com.emin.dataCenterWeb.facade.caller.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.emin.base.dao.PageRequest;
import com.emin.base.dao.PagedResult;
import com.emin.base.service.Condition;
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterBatchCaller;
import com.emin.platform.dataCenter.domain.Batch;
import com.emin.platform.dataCenter.facade.accepters.BatchAccepter;
import com.emin.platform.dataCenter.facade.accepters.EminCodeMateAccepter;

/**
 * Created by black on 2017/9/11.
 */
@Service("dcWebToDataCenterBatchCaller")
public class DcWebToDataCenterBatchCallerImpl implements DcWebToDataCenterBatchCaller{

	@Reference(version="1.0.0")
    private BatchAccepter batchAccepter;
	
	@Reference(version="1.0.0")
    private EminCodeMateAccepter eminCodeMateAccepter;
	
	@Override
	public void saveOrUpdateBatch(Batch batch) {
		batchAccepter.save(batch);
	}

	@Override
	public List<Batch> findBatchs(List<Condition> conditions) {
		return batchAccepter.findBatchList(conditions);
	}

	@Override
	public PagedResult<Batch> loadPagedBatchsByCondition(PageRequest pageRequest, List<Condition> conditions) {	
		return batchAccepter.findBatchPageList(pageRequest, conditions);
	}

	@Override
	public PagedResult<Batch> loadPagedBatchsByMatch(PageRequest pageRequest, String... match) {
		return batchAccepter.findBatchPageListByMatch(pageRequest, match);
	}

	@Override
	public void deleteBatch(Long id) {
		batchAccepter.delete(id);
	}

	@Override
	public Batch findById(Long id) {
		return batchAccepter.getBatchById(id);
	}   
	
	@Override
	public Boolean synBatch()  {
		return batchAccepter.synBatch();
	}

	@Override
	public void synEminCodeByBatchId(Long batchId) {
		eminCodeMateAccepter.synch(batchId);
	}
}
