package com.emin.dataCenterWeb.facade.caller.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.emin.base.dao.PageRequest;
import com.emin.base.dao.PagedResult;
import com.emin.base.service.Condition;
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterProductCaller;
import com.emin.platform.dataCenter.domain.Product;
import com.emin.platform.dataCenter.facade.accepters.ProductAccepter;

/**
 * Created by black on 2017/9/1.
 */
@Service("dcWebToDataCenterProductCaller")
public class DcWebToDataCenterProductCallerImpl implements DcWebToDataCenterProductCaller{

	@Reference(version="1.0.0")
    private ProductAccepter productAccepter;
	
	@Override
	public Product saveOrUpdateProduct(Product product)  {
		return productAccepter.saveOrUpdateProduct(product);
	}

	@Override
	public List<Product> findProducts(List<Condition> conditions)  {
		return productAccepter.findProducts(conditions);
	}

	@Override
	public PagedResult<Product> loadPagedProductsByCondition(PageRequest pageRequest,List<Condition> conditions)  {
		return productAccepter.loadPagedProductsByCondition(pageRequest, conditions);
	}

	@Override
	public PagedResult<Product> loadPagedProductsByMatch(PageRequest pageRequest, String... match) {
		return productAccepter.loadPagedProductsByMatch(pageRequest, match);
	}

	@Override
	public void deleteProduct(Long id)  {
		productAccepter.deleteProduct(id);
	}

	@Override
	public Product findById(Long id)  {
		return productAccepter.findById(id);
	}

	@Override
	public boolean synProduct()  {
		return productAccepter.synProduct();
	}

	@Override
	public PagedResult<Product> loadPagedProductsByMatchAndCondition(PageRequest pageRequest,List<Condition> conditions, String... match)  {
		return productAccepter.loadPagedProductsByMatchAndCondition(pageRequest,conditions, match);
	}
    
}
