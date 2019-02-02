package com.emin.dataCenterWeb.facade.caller;

import java.util.List;

import com.emin.platform.dataCenter.domain.ProductCategory;

/**
 * Created by black on 2017/11/8.
 */
public interface DcWebToDataCenterProductCategoryCaller {
    
	/**
     * 通过父节点ID查找
     * @return
     */
	public List<ProductCategory> findByParentId(Long parentId);
	
	public boolean synCategory();
}
