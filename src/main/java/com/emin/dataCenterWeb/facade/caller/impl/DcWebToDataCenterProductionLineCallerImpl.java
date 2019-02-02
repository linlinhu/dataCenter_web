package com.emin.dataCenterWeb.facade.caller.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONArray;
import com.emin.base.dao.PageRequest;
import com.emin.base.dao.PagedResult;
import com.emin.base.service.Condition;
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterProductionLineCaller;
import com.emin.platform.dataCenter.domain.ProductionLine;
import com.emin.platform.dataCenter.facade.accepters.ProductionLineAccepter;

/**
 * Created by black on 2017/9/1.
 */
@Service("dcWebToDataCenterProductionLineCaller")
public class DcWebToDataCenterProductionLineCallerImpl implements DcWebToDataCenterProductionLineCaller{

	@Reference(version="1.0.0")
    private ProductionLineAccepter productionLineAccepter;
	
	@Override
	public ProductionLine saveOrUpdateProductionLine(ProductionLine productionLine)  {
		return productionLineAccepter.saveOrUpdateProductionLine(productionLine);
	}

	@Override
	public List<ProductionLine> findProductionLines(List<Condition> conditions)  {
		return productionLineAccepter.findProductionLines(conditions);
	}

	@Override
	public PagedResult<ProductionLine> loadPagedProductionLinesByCondition(PageRequest pageRequest,List<Condition> conditions)  {
		return productionLineAccepter.loadPagedProductionLinesByCondition(pageRequest, conditions);
	}

	@Override
	public PagedResult<ProductionLine> loadPagedProductionLinesByMatch(PageRequest pageRequest, String... match)
			 {
		return productionLineAccepter.loadPagedProductionLinesByMatch(pageRequest, match);
	}

	@Override
	public void deleteProductionLine(Long id)  {
		productionLineAccepter.deleteProductionLine(id);
	}

	@Override
	public ProductionLine findById(Long id)  {
		return productionLineAccepter.findById(id);
	}

	@Override
	public JSONArray loadSyncLineTree(Long parentId)  {
		return productionLineAccepter.loadSyncLineTree(parentId);
	}

	@Override
	public JSONArray loadAsyncLineTree(Long parentId)  {
		return productionLineAccepter.loadAsyncLineTree(parentId);
	}
    
}
