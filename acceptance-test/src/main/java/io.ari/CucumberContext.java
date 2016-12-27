package io.ari;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Scope(value="cucumber-glue")
public class CucumberContext {

	public void publishValue(String key, Object value){
		context.put(key,value);
	}
	
	public Object getValue(String key){
		return context.get(key);
	}
	
	private Map<String,Object> context = new HashMap<>();
}
