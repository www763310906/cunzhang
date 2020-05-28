package com.ye.modules.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.ye.modules.cus.entity.ZanEntity;


public interface BaseDao<T> {
	
	void save(T t);
	
	void save(Map<String, Object> map);
	
	void saveBatch(List<T> list);
	
	int update(T t);
	
	int update(Map<String, Object> map);
	
	int delete(Object id);
	
	int deleteByChoose(T t);
	
	int delete(Map<String, Object> map);
	
	int deleteBatch(Object[] id);

	T queryObject(Object id);
	
	int queryIsExist(T t);
	
	List<T> queryList(Map<String, Object> map);
	
	List<T> queryListSearch(Map<String, Object> map);
	
	List<T> queryListByUser(Map<String, Object> map);
	
	List<T> queryList(Object id);
		
	List<T> queryListByBokeId(Object bokeId);
	
	int queryTotal(Map<String, Object> map);

	int queryTotal();
}
