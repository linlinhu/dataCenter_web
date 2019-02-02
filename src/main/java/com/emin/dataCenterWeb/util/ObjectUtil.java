package com.emin.dataCenterWeb.util;

import java.lang.reflect.Field;

public class ObjectUtil {
	
	public static Object getValueByFiled(Object target,String filed) {
		 //filed = "product.productNumber";
		 String[] fileds = filed.split("\\.");
		 Object object = target;
		 for (String string : fileds) {
			 object = getValue(object,string);
		}
		 
		 return object;
	}
	
	
	public static Object  getValue(Object  object,String fieldName) {
		try {
			@SuppressWarnings("rawtypes")
			Class classBean  = (Class) object.getClass(); 
			
			Field[] fs = classBean.getDeclaredFields(); 
			 for(int i = 0 ; i < fs.length; i++){
				 Field f = fs[i];  
				 f.setAccessible(true); 
				 
		         if (fieldName.equals(f.getName())) {
		        	 Object value;
					
						value = f.get(object);
					    
					return value;
				} 
				
			 }
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return  null;
	}
}