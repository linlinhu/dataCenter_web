package com.emin.dataCenterWeb.facade.caller.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.emin.base.dao.PageRequest;
import com.emin.base.dao.PagedResult;
import com.emin.base.service.Condition;
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterShopCaller;
import com.emin.platform.dataCenter.domain.Shop;
import com.emin.platform.dataCenter.facade.accepters.ShopAccepter;

/**
 * Created by black on 2017/9/1.
 */
@Service("dcWebToDataCenterShopCaller")
public class DcWebToDataCenterShopCallerImpl implements DcWebToDataCenterShopCaller{

	@Reference(version="1.0.0")
    private ShopAccepter shopAccepter;
	
	@Override
	public Shop saveOrUpdateShop(Shop shop)  {
		return shopAccepter.saveOrUpdateShop(shop);
	}

	@Override
	public List<Shop> findShops(List<Condition> conditions)  {
		return shopAccepter.findShops(conditions);
	}

	@Override
	public PagedResult<Shop> loadPagedShopsByCondition(PageRequest pageRequest,List<Condition> conditions)  {
		return shopAccepter.loadPagedShopsByCondition(pageRequest, conditions);
	}

	@Override
	public PagedResult<Shop> loadPagedShopsByMatch(PageRequest pageRequest, String... match)
			 {
		return shopAccepter.loadPagedShopsByMatch(pageRequest, match);
	}

	@Override
	public void deleteShop(Long id)  {
		shopAccepter.deleteShop(id);
	}

	@Override
	public Shop findById(Long id)  {
		return shopAccepter.findById(id);
	}
    
}
