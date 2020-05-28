package com.ye.modules.cus.service;

import java.util.List;
import java.util.Map;

import com.ye.modules.cus.entity.UserEntity;
import com.ye.modules.cus.entity.UserRelationEntity;

/**
 * 教练学员关系表
 */
public interface UserRelationService {
	
	UserRelationEntity queryObject(Integer id);
	
	List<UserRelationEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(UserRelationEntity userRelation);
	
	void update(UserRelationEntity userRelation);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
	
	List<UserEntity> queryXueList(Map<String, Object> map);
}
