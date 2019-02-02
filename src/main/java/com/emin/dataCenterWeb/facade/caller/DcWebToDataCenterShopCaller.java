package com.emin.dataCenterWeb.facade.caller;

import java.util.List;

import com.emin.base.dao.PageRequest;
import com.emin.base.dao.PagedResult;
import com.emin.base.service.Condition;
import com.emin.platform.dataCenter.domain.Product;
import com.emin.platform.dataCenter.domain.Shop;

/**
 * Created by black on 2017/9/1.
 */
public interface DcWebToDataCenterShopCaller {
    
	/**保存车间
	 * @param Shop
	 * @return Shop
	 * @see Shop
	 */
	public Shop saveOrUpdateShop(Shop shop) ;
	
	/**根据条件查询车间
	 * @param conditions
	 * @return Shop
	 * @see Shop
	 */
	public List<Shop> findShops(List<Condition> conditions);
	
	/**根据条件分页查询车间
	 * @param pageRequest
	 * @param conditions
	 * @return {@link PagedResult}
	 * @see Product
	 */
	public PagedResult<Shop> loadPagedShopsByCondition(PageRequest pageRequest, List<Condition> conditions);

	/**
	 * 根据关键字分页查询车间<br>
	 * 关键字可用项
	 * @param pageRequest 
	 * @param match
	 * @return {@link PagedResult}
	 * @see Shop
	 */
	public PagedResult<Shop> loadPagedShopsByMatch(PageRequest pageRequest , String... match);

 
	public void deleteShop(Long id) ;
	
	public Shop findById(Long id);
}
