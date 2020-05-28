package com.ye.modules.cus.dao;

import java.util.List;
import java.util.Map;

import com.ye.modules.cus.entity.UserEntity;
import com.ye.modules.cus.entity.UserRelationEntity;
import com.ye.modules.sys.dao.BaseDao;
/**
 * 教练学员关系表
 */
public interface UserRelationDao extends BaseDao<UserRelationEntity> {
	//List<UserEntity> queryXueList(Map<String, Object> map);
}
