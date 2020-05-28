package com.ye.modules.cus.service;

import com.ye.modules.cus.entity.GonggaoEntity;

import java.util.List;
import java.util.Map;

/**
 * 公告
 */
public interface GonggaoService {
	
	GonggaoEntity queryObject(Integer id);
	
	List<GonggaoEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(GonggaoEntity gonggao);
	
	void update(GonggaoEntity gonggao);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
}
