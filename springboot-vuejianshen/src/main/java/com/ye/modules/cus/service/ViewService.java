package com.ye.modules.cus.service;

import com.ye.modules.cus.entity.ViewEntity;

import java.util.List;
import java.util.Map;

/**
 * 图书浏览记录
 */
public interface ViewService {
	
	ViewEntity queryObject(Integer id);
	
	List<ViewEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(ViewEntity view);
	
	void update(ViewEntity view);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
}
