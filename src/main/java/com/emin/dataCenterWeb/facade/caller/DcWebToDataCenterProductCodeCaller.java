package com.emin.dataCenterWeb.facade.caller;

import java.util.List;

import com.emin.base.dao.PageRequest;
import com.emin.base.dao.PagedResult;
import com.emin.base.service.Condition;
import com.emin.platform.dataCenter.domain.Product;
import com.emin.platform.dataCenter.domain.ProductCode;

/**
 * Created by black on 2017/8/21.
 */
public interface DcWebToDataCenterProductCodeCaller {
    
	/**保存产品所属码
	 * @param productCode
	 * @return productCode
	 * @see productCode
	 */
	public ProductCode saveOrUpdateProductCode(ProductCode productCode) ;
	
	/**根据条件查询产品所属码
	 * @param conditions
	 * @return productCode
	 * @see productCode
	 */
	public List<ProductCode> findProductCodes(List<Condition> conditions);
	
	/**根据条件分页查询产品所属码
	 * @param pageRequest
	 * @param conditions
	 * @return {@link PagedResult}
	 * @see Product
	 */
	public PagedResult<ProductCode> loadPagedProductCodesByCondition(PageRequest pageRequest, List<Condition> conditions);

	/**
	 * 根据关键字分页查询产品所属码<br>
	 * 关键字可用项
	 * @param pageRequest 
	 * @param match
	 * @return {@link PagedResult}
	 * @see ProductCode
	 */
	public PagedResult<ProductCode> loadPagedProductCodesByMatch(PageRequest pageRequest , String... match);

 
	public void deleteProductCode(Long id) ;
	
	public ProductCode findById(Long id);
}
