package com.emin.dataCenterWeb.facade.caller;

import java.util.List;
import java.util.Set;

import com.emin.platform.codingCenter.domain.CodingRule;
import com.emin.platform.codingCenter.expression.ExpressionCondition;

import net.sf.json.JSONObject;

/**
 * Created by black on 2017/8/21.
 */
public interface DcWebToCodingCenterCaller {
    
    /**
	 * 创建规则并返回规则ID
	 * @param name 规则名称
	 * @param randomLength 规则随机码长度
	 * @param conditions 规则表达式条件
	 * @return
	 */
    public String createRule(String name,int randomLength,List<ExpressionCondition> conditions);
    
    /**
	 * 更新规则名称及表达式 （只能更新草稿状态的规则）
	 * @param codeOrId 规则编号或者ID
	 * @param name 规则名称
	 * @param randomLength 规则随机码长度
	 * @param conditions 规则表达式条件
	 */
    public void updateRule(String codeOrId, String name,int randomLength, List<ExpressionCondition> conditions);


	/**
	 * 获取所有规则列表
	 * @return
	 */
    public List<CodingRule> findAllRuleList();

	/**
	 * 获取已发布的规则列表
	 * @return
	 */
    public List<CodingRule> findPublishedRuleList();


	/**
	 * 发布规则 只能发布草稿状态的规则
	 * @param codeOrId 规则编号或ID
	 */
    public void publishRule(String codeOrId);


	/**
	 * 取消规则 只能取消发布状态的规则
	 * @param codeOrId
	 */
    public void cancelRule(String codeOrId);

	/*
	 * 生成编码
	 */
    public Set<String> generateCodes(String codeOrId, JSONObject sourceData, int genCount) throws Exception;

	/**
	 * 编码解析
	 * @param code
	 * @return
	 */
    public JSONObject parseCode(String code);
    
    /**
	 * 校验编码
	 * @param ruleId
	 * @param code
	 * @return  int
	 */
    public int codeVerify(String ruleId,String code);

    /**
	 * 通过ID查找规则
	 * @param ruleId
	 * @return  public
	 */
    public CodingRule findById(String ruleId);

    /**
	 * 通过规则code查找规则
	 * @param code
	 * @return  public
	 */
    public CodingRule findByRuleCode(String ruleCode);
}
