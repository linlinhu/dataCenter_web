package com.emin.dataCenterWeb.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONObject;
import com.emin.base.exception.EminException;
import com.emin.dataCenterWeb.facade.caller.DcWebToDataCenterProductCategoryCaller;
import com.emin.platform.dataCenter.domain.ProductCategory;

/**
 * 控制层-产品分类查询
 * @author kakadanica
 *
 */
@Controller
@RequestMapping("/category")
public class CategoryController extends DCBaseController{
	private Logger logger = LoggerFactory.getLogger(CategoryController.class);

	@Autowired 
	@Qualifier("dcWebToDataCenterProductCategoryCaller")
	private DcWebToDataCenterProductCategoryCaller dcWebToDataCenterProductCategoryCaller;

	@RequestMapping("/categoryTree")
	@ResponseBody
	public List<ProductCategory> trees(Long parentId){
		
		List<ProductCategory> productCategoryList = null;
		
		try {
            productCategoryList= dcWebToDataCenterProductCategoryCaller.findByParentId(parentId);
		}  catch (EminException e) {
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return productCategoryList;
    }
	
	@RequestMapping("/tree")
	@ResponseBody
	public JSONObject goManage(Long parentId){
		JSONObject json = new JSONObject();
   		String message = "";
		boolean success = false;
		
		try {
//			String resStr = categoryApiFeign.getTree(ecmId, parentId);
//			String resStr = "{result:[{\"id\":\"" + System.currentTimeMillis() + "\", \"name\":\"分类" + System.currentTimeMillis() + "\",\"isParent\":true},"
//					+ "{\"id\":\"" + System.currentTimeMillis() + 1 + "\", \"name\":\"分类" + System.currentTimeMillis() + "\",\"isParent\":false}]}";

            List<ProductCategory>  productCategoryList= dcWebToDataCenterProductCategoryCaller.findByParentId(parentId);
			json.put("data", productCategoryList);
            success = true;
		}  catch (EminException e) {
			success = false;
			message = e.getLocalizedMessage();
			logger.error(e.getLocalizedMessage(),e);
		} catch (Exception e) {
			success = false;
			message = "获取产品分类失败";
			logger.error(e.getMessage(),e);
		}
		json.put("success", success);
		json.put("message", message);
		return json;
    }
	
}
