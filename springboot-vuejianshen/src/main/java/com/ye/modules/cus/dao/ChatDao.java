package com.ye.modules.cus.dao;

import java.util.List;

import com.ye.modules.cus.entity.ChatEntity;
import com.ye.modules.sys.dao.BaseDao;
/**
 * 聊天
 */
public interface ChatDao extends BaseDao<ChatEntity> {
	List<ChatEntity> queryChatList(ChatEntity chat);
}
