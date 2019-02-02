package com.emin.dataCenterWeb.facade.caller.impl;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.emin.dataCenterWeb.facade.caller.DcWebToCodingCenterCaller;
import com.emin.platform.codingCenter.domain.CodingRule;
import com.emin.platform.codingCenter.expression.ExpressionCondition;
import com.emin.platform.codingCenter.facade.accepters.CodingCenterAccepter;

import net.sf.json.JSONObject;

/**
 * Created by black on 2017/8/21.
 */
@Service("dcWebToCodingCenterCaller")
public class DcWebToCodingCenterCallerImpl implements DcWebToCodingCenterCaller{
    
	
    @Reference(version="1.0.1")
    private CodingCenterAccepter codingCenterAccepter;
	
    /**
	 * 创建规则并返回规则ID
	 * @param name 规则名称
	 * @param randomLength 规则随机码长度
	 * @param conditions 规则表达式条件
	 * @return
	 */
	@Override
    public String createRule(String name,int randomLength,List<ExpressionCondition> conditions){
		return codingCenterAccepter.createRule(name, randomLength, conditions);
    }
    
    /**
	 * 更新规则名称及表达式 （只能更新草稿状态的规则）
	 * @param codeOrId 规则编号或者ID
	 * @param name 规则名称
	 * @param randomLength 规则随机码长度
	 * @param conditions 规则表达式条件
	 */
	@Override
    public void updateRule(String codeOrId, String name,int randomLength, List<ExpressionCondition> conditions){
		codingCenterAccepter.updateRule(codeOrId, name, randomLength, conditions);
	}


	/**
	 * 获取所有规则列表
	 * @return
	 */
	@Override
    public List<CodingRule> findAllRuleList(){
		return codingCenterAccepter.findAllRuleList();
	}

	/**
	 * 获取已发布的规则列表
	 * @return
	 */
	@Override
    public List<CodingRule> findPublishedRuleList(){
		return codingCenterAccepter.findAllRuleList();
	}


	/**
	 * 发布规则 只能发布草稿状态的规则
	 * @param codeOrId 规则编号或ID
	 */
	@Override
    public void publishRule(String codeOrId){
		codingCenterAccepter.publishRule(codeOrId);
	}


	/**
	 * 取消规则 只能取消发布状态的规则
	 * @param codeOrId
	 */
	@Override
    public void cancelRule(String codeOrId){
		codingCenterAccepter.cancelRule(codeOrId);
	}

	/*
	 * 生成编码
	 */
	@Override
    public Set<String> generateCodes(String codeOrId, JSONObject sourceData, int genCount) throws Exception{
		return codingCenterAccepter.generateCodes(codeOrId, sourceData, genCount);
	}

	/**
	 * 编码解析
	 * @param code
	 * @return
	 */
	@Override
    public JSONObject parseCode(String code){
		return codingCenterAccepter.parseCode(code);
	}

	@Override
	public int codeVerify(String ruleId, String code) {
		return codingCenterAccepter.codeVerify(code);
	}
	
	@Override
	public CodingRule findById(String ruleId) {
		return codingCenterAccepter.findById(ruleId);
	}
	
	@Override
	public CodingRule findByRuleCode(String ruleCode) {
		return codingCenterAccepter.findByRuleCode(ruleCode);
	}
}
