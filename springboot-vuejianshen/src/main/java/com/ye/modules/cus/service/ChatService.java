package com.ye.modules.cus.service;

import com.ye.modules.cus.entity.ChatEntity;

import java.util.List;
import java.util.Map;

/**
 * 聊天
 */
public interface ChatService {
	
	ChatEntity queryObject(Integer id);
	
	List<ChatEntity> queryList(Map<String, Object> map);
	
	List<ChatEntity> queryChatList(ChatEntity chat);
	
	int queryTotal(Map<String, Object> map);
	
	void save(ChatEntity chat);
	
	void update(ChatEntity chat);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
}
