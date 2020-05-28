package com.ye.modules.cus.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.ye.modules.cus.dao.ChatDao;
import com.ye.modules.cus.entity.ChatEntity;
import com.ye.modules.cus.service.ChatService;



@Service("chatService")
public class ChatServiceImpl implements ChatService {
	@Autowired
	private ChatDao chatDao;
	
	@Override
	public ChatEntity queryObject(Integer id){
		return chatDao.queryObject(id);
	}
	
	@Override
	public List<ChatEntity> queryList(Map<String, Object> map){
		return chatDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return chatDao.queryTotal(map);
	}
	
	@Override
	public void save(ChatEntity chat){
		chatDao.save(chat);
	}
	
	@Override
	public void update(ChatEntity chat){
		chatDao.update(chat);
	}
	
	@Override
	public void delete(Integer id){
		chatDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		chatDao.deleteBatch(ids);
	}

	@Override
	public List<ChatEntity> queryChatList(ChatEntity chat) {
		// TODO Auto-generated method stub
		return chatDao.queryChatList(chat);
	}
	
}
