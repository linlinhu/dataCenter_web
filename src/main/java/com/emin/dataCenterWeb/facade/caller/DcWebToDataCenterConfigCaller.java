package com.emin.dataCenterWeb.facade.caller;

import java.util.List;

import com.emin.base.dao.PageRequest;
import com.emin.base.dao.PagedResult;
import com.emin.base.service.Condition;
import com.emin.platform.dataCenter.domain.Config;

/**
 * Created by black on 2017/11/7.
 */
public interface DcWebToDataCenterConfigCaller {
    
	/**保存配置
	 * @param Config
	 * @return Config
	 * @see Config
	 */
	public Config saveOrUpdateConfig(Config config) ;
	
	/**根据条件查询配置
	 * @param conditions
	 * @return Config
	 * @see Config
	 */
	public List<Config> findConfigs(List<Condition> conditions);
	
	/**根据条件分页查询配置
	 * @param pageRequest
	 * @param conditions
	 * @return {@link PagedResult}
	 * @see Config
	 */
	public PagedResult<Config> loadPagedConfigsByCondition(PageRequest pageRequest, List<Condition> conditions);

	/**
	 * 根据关键字分页查询配置<br>
	 * 关键字可用项
	 * @param pageRequest 
	 * @param match
	 * @return {@link PagedResult}
	 * @see Config
	 */
	public PagedResult<Config> loadPagedConfigsByMatch(PageRequest pageRequest , String... match);

	/**根据ID删除配置
	 * @param id
	 * @return 
	 * @see 
	 */
	public void deleteConfig(Long id) ;
	
	/**根据ID查找配置
	 * @param id
	 * @return 
	 * @see 
	 */
	public Config findById(Long id);

    
}
