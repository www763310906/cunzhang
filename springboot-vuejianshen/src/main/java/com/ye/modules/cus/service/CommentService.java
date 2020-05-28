package com.ye.modules.cus.service;

import com.ye.modules.cus.entity.CommentEntity;

import java.util.List;
import java.util.Map;

/**
 * 评论
 */
public interface CommentService {
	
	CommentEntity queryObject(Integer id);
	
	List<CommentEntity> queryList(Map<String, Object> map);
	
	List<CommentEntity> queryListByBokeId(Integer integer);
	
	int queryTotal(Map<String, Object> map);
	
	void save(CommentEntity comment);
	
	void update(CommentEntity comment);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
}
