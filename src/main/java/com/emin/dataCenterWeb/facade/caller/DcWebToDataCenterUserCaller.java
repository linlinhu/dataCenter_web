package com.emin.dataCenterWeb.facade.caller;

import java.util.List;
import com.emin.base.dao.PageRequest;
import com.emin.base.dao.PagedResult;
import com.emin.base.service.Condition;
import com.emin.platform.dataCenter.domain.User;

/**
 * Created by black on 2017/9/5.
 */
public interface DcWebToDataCenterUserCaller {
    
	/**保存用户
	 * @param User
	 * @return User
	 * @see User
	 */
	public User saveOrUpdateUser(User user) ;
	
	/**根据条件查询用户
	 * @param conditions
	 * @return User
	 * @see User
	 */
	public List<User> findUsers(List<Condition> conditions);
	
	/**根据条件分页查询用户
	 * @param pageRequest
	 * @param conditions
	 * @return {@link PagedResult}
	 * @see User
	 */
	public PagedResult<User> loadPagedUsersByCondition(PageRequest pageRequest, List<Condition> conditions);

	/**
	 * 根据关键字分页查询用户<br>
	 * 关键字可用项
	 * @param pageRequest 
	 * @param match
	 * @return {@link PagedResult}
	 * @see User
	 */
	public PagedResult<User> loadPagedUsersByMatch(PageRequest pageRequest , String... match);

 
	public void deleteUser(Long id) ;
	
	public User findById(Long id);
	
	public Boolean synUser(String ecmCode);
		
	//禁用启用user
	public void disableUser(Long id,Boolean disable);
}
