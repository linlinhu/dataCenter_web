package com.emin.dataCenterWeb.facade.caller;

import java.util.List;

import com.emin.base.dao.PageRequest;
import com.emin.base.dao.PagedResult;
import com.emin.base.service.Condition;
import com.emin.platform.dataCenter.domain.PublicNotice;

/**
 * Created by black on 2017/8/21.
 */
public interface DcWebToDataCenterPublicNoticeCaller {
    
	/**保存公告
	 * @param PublicNotice
	 * @return PublicNotice
	 * @see PublicNotice
	 */
	public PublicNotice saveOrUpdatePublicNotice(PublicNotice publicNotice) ;
	
	/**根据条件查询公告
	 * @param conditions
	 * @return PublicNotice
	 * @see PublicNotice
	 */
	public List<PublicNotice> findPublicNotices(List<Condition> conditions);
	
	/**根据条件分页查询公告
	 * @param pageRequest
	 * @param conditions
	 * @return {@link PagedResult}
	 * @see PublicNotice
	 */
	public PagedResult<PublicNotice> loadPagedPublicNoticesByCondition(PageRequest pageRequest, List<Condition> conditions);

	/**
	 * 根据关键字分页查询公告<br>
	 * 关键字可用项
	 * @param pageRequest 
	 * @param match
	 * @return {@link PagedResult}
	 * @see PublicNotice
	 */
	public PagedResult<PublicNotice> loadPagedPublicNoticesByMatch(PageRequest pageRequest , String... match);

 
	public void deletePublicNotice(Long id) ;
	
	public PublicNotice findById(Long id);
}
