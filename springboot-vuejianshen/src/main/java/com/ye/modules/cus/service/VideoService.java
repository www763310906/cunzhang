package com.ye.modules.cus.service;

import com.ye.modules.cus.entity.VideoEntity;

import java.util.List;
import java.util.Map;

/**
 * 视频
 */
public interface VideoService {
	
	VideoEntity queryObject(Integer id);
	
	List<VideoEntity> queryList(Map<String, Object> map);
	
	List<VideoEntity> queryListSearch(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(VideoEntity video);
	
	void update(VideoEntity video);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
	
	public List<VideoEntity> recommendList(Integer userId);
}
