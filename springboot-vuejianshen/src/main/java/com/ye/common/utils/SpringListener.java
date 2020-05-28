package com.ye.common.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
 

public class SpringListener implements
		ApplicationListener<ContextRefreshedEvent> {
 
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (event.getApplicationContext().getParent() == null) {
			Map<String, Object> map = new HashMap<String, Object>();

			 
		}
	}

}