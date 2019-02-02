package com.emin.dataCenterWeb.util;

import java.util.HashMap;
import java.util.Map;

public class Constant {
	public enum MatchingModeStringEnum{
		NUMBERS("numbers"),		
		CHARACTERS_LOWERCASE("lowercase"),
		CHARACTERS_UPPERCASE("uppercase"),
		UNDERLINE("underline"),
		HYPHEN("hyphen");
		
		private final static Map<String , MatchingModeStringEnum> ENUM_MAP = new HashMap<String, MatchingModeStringEnum>();  
		  
		  
	    static {  
	        for(MatchingModeStringEnum v : values()) {  
	            ENUM_MAP.put(v.pattern, v);   
	        }  
	    }  
	  
	    public static MatchingModeStringEnum fromString(String v) {  
	    	MatchingModeStringEnum matchingMode = ENUM_MAP.get(v);  
	        return matchingMode == null ? null :matchingMode;  
	    }  
			
		private String pattern;
		
		private MatchingModeStringEnum(String pattern){
			this.pattern = pattern;
		}

		public String getPattern() {
			return pattern;
		}

		public void setPattern(String pattern) {
			this.pattern = pattern;
		}
		
	}

	public enum UserOptionEnum {  
	    SAVE_USER("11"),  
	    GET_USER_BY_ID("22"),  
	    GET_USER_LIST("33"),  
	    DELETE_USER_BY_ID("44");  
		
		private String pattern;
		
		private UserOptionEnum(String pattern){
			this.pattern = pattern;
		}
		
	    private final static Map<String , UserOptionEnum> ENUM_MAP = new HashMap<String, UserOptionEnum>();  
	  
	  
	    static {  
	        for(UserOptionEnum v : values()) {  
	            ENUM_MAP.put(v.pattern, v);   
	        }  
	    }  
	  
	    public static UserOptionEnum fromString(String v) {  
	        UserOptionEnum userOptionEnum = ENUM_MAP.get(v);  
	        return userOptionEnum == null ? DELETE_USER_BY_ID :userOptionEnum;  
	    }  
	    
	}
	
//	public static void main(String[] args) {
//		
//		UserOptionEnum.ENUM_MAP.keySet();
//		UserOptionEnum ss  = UserOptionEnum.fromString("11");
//		
//		switch (ss) {
//		case SAVE_USER:
//			System.out.println("SAVE_USER");
//			break;
//
//		default:
//			break;
//		}
//	}
	
}