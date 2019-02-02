package com.emin.dataCenterWeb.facade.caller;

import java.util.List;

import com.emin.base.dao.PageRequest;
import com.emin.base.dao.PagedResult;
import com.emin.base.service.Condition;
import com.emin.platform.dataCenter.domain.ProductionMode;

/**
 * Created by black on 2018/1/10.
 */
public interface DcWebToDataCenterProductionModeCaller {
    
	/**保存生产模式
	 * @param productionMode
	 * @return productionMode
	 * @see productionMode
	 */
	public ProductionMode saveOrUpdateProductionMode(ProductionMode productionMode) ;
	
	/**根据条件查询生产模式
	 * @param conditions
	 * @return productionMode
	 * @see productionMode
	 */
	public List<ProductionMode> findProductionModes(List<Condition> conditions);
	
	/**根据条件分页查询生产模式
	 * @param pageRequest
	 * @param conditions
	 * @return {@link PagedResult}
	 * @see ProductionMode
	 */
	public PagedResult<ProductionMode> loadPagedProductionModesByCondition(PageRequest pageRequest, List<Condition> conditions);

	/**
	 * 根据关键字分页查询生产模式<br>
	 * 关键字可用项
	 * @param pageRequest 
	 * @param match
	 * @return {@link PagedResult}
	 * @see ProductionMode
	 */
	public PagedResult<ProductionMode> loadPagedProductionModesByMatch(PageRequest pageRequest , String... match);

    /**
	 * 删除生产模式
	 * @param id 
	 * @return 
	 * @see ProductionMode
	 */
	public void deleteProductionMode(Long id) ;
	
    /**
	 * 通过ID查找生产模式
	 * @param id 
	 * @return ProductionMode
	 * @see ProductionMode
	 */
	public ProductionMode findById(Long id);

    /**
	 * 通过ID发布生产模式
	 * @param id 
	 * @return ProductionMode
	 * @see ProductionMode
	 */
	public void publishProductionMode(Long id);

    /**
	 * 根据关键字和条件,分页查询生产模式<br>
	 * 关键字可用项
	 * @param pageRequest 
	 * @param match
	 * @return {@link PagedResult}
	 * @see ProductionMode
	 */
	public PagedResult<ProductionMode> loadPagedProductionModesByMatchAndCondition(PageRequest pageRequest,List<Condition> conditions, String... match);
}
