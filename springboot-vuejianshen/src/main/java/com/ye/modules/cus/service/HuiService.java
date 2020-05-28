package com.ye.modules.cus.service;

import com.ye.modules.cus.entity.HuiEntity;

import java.util.List;
import java.util.Map;

public interface HuiService {
	
	HuiEntity queryObject(Integer id);
	
	List<HuiEntity> queryList(Map<String, Object> map);
	
	List<HuiEntity> queryListByUser(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(HuiEntity hui);
	
	void update(HuiEntity hui);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
}
