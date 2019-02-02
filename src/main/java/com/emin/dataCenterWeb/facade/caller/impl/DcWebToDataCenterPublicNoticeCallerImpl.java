package com.emin.dataCenterWeb.facade.caller.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.emin.base.dao.PageRequest;
import com.emin.base.dao.PagedResult;
import com.emin.base.service.Condition;
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterPublicNoticeCaller;
import com.emin.platform.dataCenter.domain.PublicNotice;
import com.emin.platform.dataCenter.facade.accepters.PublicNoticeAccepter;

/**
 * Created by black on 2017/9/1.
 */
@Service("dcWebToDataCenterPublicNoticeCaller")
public class DcWebToDataCenterPublicNoticeCallerImpl implements DcWebToDataCenterPublicNoticeCaller{

	@Reference(version="1.0.0")
    private PublicNoticeAccepter publicNoticeAccepter;
	
	@Override
	public PublicNotice saveOrUpdatePublicNotice(PublicNotice publicNotice)  {
		return publicNoticeAccepter.saveOrUpdatePublicNotice(publicNotice);
	}

	@Override
	public List<PublicNotice> findPublicNotices(List<Condition> conditions)  {
		return publicNoticeAccepter.findPublicNotices(conditions);
	}

	@Override
	public PagedResult<PublicNotice> loadPagedPublicNoticesByCondition(PageRequest pageRequest,List<Condition> conditions)  {
		return publicNoticeAccepter.loadPagedPublicNoticesByCondition(pageRequest, conditions);
	}

	@Override
	public PagedResult<PublicNotice> loadPagedPublicNoticesByMatch(PageRequest pageRequest, String... match)
			 {
		return publicNoticeAccepter.loadPagedPublicNoticesByMatch(pageRequest, match);
	}

	@Override
	public void deletePublicNotice(Long id)  {
		publicNoticeAccepter.deletePublicNotice(id);
	}

	@Override
	public PublicNotice findById(Long id)  {
		return publicNoticeAccepter.findById(id);
	}
    
}
