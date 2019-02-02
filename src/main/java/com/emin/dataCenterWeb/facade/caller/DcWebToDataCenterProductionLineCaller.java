package com.emin.dataCenterWeb.facade.caller;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.emin.base.dao.PageRequest;
import com.emin.base.dao.PagedResult;
import com.emin.base.service.Condition;
import com.emin.platform.dataCenter.domain.Product;
import com.emin.platform.dataCenter.domain.ProductionLine;

/**
 * Created by black on 2017/9/1.
 */
public interface DcWebToDataCenterProductionLineCaller {
    
	/**保存生产线
	 * @param ProductionLine
	 * @return ProductionLine
	 * @see ProductionLine
	 */
	public ProductionLine saveOrUpdateProductionLine(ProductionLine productionLine) ;
	
	/**根据条件查询生产线
	 * @param conditions
	 * @return ProductionLine
	 * @see ProductionLine
	 */
	public List<ProductionLine> findProductionLines(List<Condition> conditions);
	
	/**根据条件分页查询生产线
	 * @param pageRequest
	 * @param conditions
	 * @return {@link PagedResult}
	 * @see Product
	 */
	public PagedResult<ProductionLine> loadPagedProductionLinesByCondition(PageRequest pageRequest, List<Condition> conditions);

	/**
	 * 根据关键字分页查询生产线<br>
	 * 关键字可用项
	 * @param pageRequest 
	 * @param match
	 * @return {@link PagedResult}
	 * @see ProductionLine
	 */
	public PagedResult<ProductionLine> loadPagedProductionLinesByMatch(PageRequest pageRequest , String... match);
	/**
	 * 异步加载生产线树形结构
	 * @param parentId
	 * @return
	 */
	public JSONArray loadSyncLineTree(Long parentId);
	/**
	 * 同步加载生产线属性结构 子线信息属性为children
	 * 基本属性参见 {@link ProductionLine} 类
	 * @param parentId
	 * @return
	 */
	public JSONArray loadAsyncLineTree(Long parentId);
 
	public void deleteProductionLine(Long id) ;
	
	public ProductionLine findById(Long id);
}
