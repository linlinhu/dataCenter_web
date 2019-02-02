package com.emin.dataCenterWeb.facade.caller;

import java.util.List;

import com.emin.base.dao.PageRequest;
import com.emin.base.dao.PagedResult;
import com.emin.base.service.Condition;
import com.emin.platform.dataCenter.domain.Product;

/**
 * Created by black on 2017/8/21.
 */
public interface DcWebToDataCenterProductCaller {
    
	/**保存产品
	 * @param product
	 * @return product
	 * @see product
	 */
	public Product saveOrUpdateProduct(Product product) ;
	
	/**根据条件查询产品
	 * @param conditions
	 * @return product
	 * @see product
	 */
	public List<Product> findProducts(List<Condition> conditions);
	
	/**根据条件分页查询产品
	 * @param pageRequest
	 * @param conditions
	 * @return {@link PagedResult}
	 * @see Product
	 */
	public PagedResult<Product> loadPagedProductsByCondition(PageRequest pageRequest, List<Condition> conditions);

	/**
	 * 根据关键字分页查询产品<br>
	 * 关键字可用项
	 * @param pageRequest 
	 * @param match
	 * @return {@link PagedResult}
	 * @see Product
	 */
	public PagedResult<Product> loadPagedProductsByMatch(PageRequest pageRequest , String... match);

 
	public void deleteProduct(Long id) ;
	
	public Product findById(Long id);

	public boolean synProduct();

	public PagedResult<Product> loadPagedProductsByMatchAndCondition(PageRequest pageRequest,List<Condition> conditions, String... match);
}
