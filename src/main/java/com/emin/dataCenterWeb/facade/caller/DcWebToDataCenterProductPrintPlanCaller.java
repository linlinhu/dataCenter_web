package com.emin.dataCenterWeb.facade.caller;

import java.util.List;

import com.emin.base.dao.PageRequest;
import com.emin.base.dao.PagedResult;
import com.emin.base.service.Condition;
import com.emin.platform.dataCenter.domain.ProductPrintPlan;

/**
 * Created by black on 2017/9/1.
 */
public interface DcWebToDataCenterProductPrintPlanCaller {
    
	/**保存产品所属码打印方案
	 * @param ProductPrintPlan
	 * @return ProductPrintPlan
	 * @see ProductPrintPlan
	 */
	public ProductPrintPlan saveOrUpdateProductPrintPlan(ProductPrintPlan productPrintPlan) ;
	
	/**根据条件查询产品所属码打印方案
	 * @param conditions
	 * @return ProductPrintPlan
	 * @see ProductPrintPlan
	 */
	public List<ProductPrintPlan> findProductPrintPlans(List<Condition> conditions);
	
	/**根据条件分页查询产品所属码打印方案
	 * @param pageRequest
	 * @param conditions
	 * @return {@link PagedResult}
	 * @see ProductPrintPlan
	 */
	public PagedResult<ProductPrintPlan> loadPagedProductPrintPlansByCondition(PageRequest pageRequest, List<Condition> conditions);

	/**
	 * 根据关键字分页查询产品所属码打印方案<br>
	 * 关键字可用项
	 * @param pageRequest 
	 * @param match
	 * @return {@link PagedResult}
	 * @see ProductPrintPlan
	 */
	public PagedResult<ProductPrintPlan> loadPagedProductPrintPlansByMatch(PageRequest pageRequest , String... match);

 
	public void deleteProductPrintPlan(Long id) ;
	
	public ProductPrintPlan findById(Long id);

	public void deleteExistPlan(String productNumber, String lineNumber, Long deviceType);
}
