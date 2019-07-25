package transact.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CommonUtils {
	
	private static Logger log = LoggerFactory.getLogger(CommonUtils.class);
	private static ObjectMapper mapper=new ObjectMapper();
	
	public static ObjectMapper getMapper() {
		return mapper;
	}

	public static String getString(Object obj){
		String str="";
		try{
			str=mapper.writeValueAsString(obj);
		}catch(Exception e){
			log.error(" could not convert obj into string "+e.getMessage());
		}
		return str;
	}
}
