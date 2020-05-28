package com.ye.modules.sys.dao;

import java.util.List;
import java.util.Map;

import com.ye.modules.sys.entity.SysUserEntity;

public interface SysUserDao extends BaseDao<SysUserEntity> {
	
	
	
	/**
	 * 根据用户名，查询系统用户
	 */
	SysUserEntity queryByUserName(String username);
	
	/**
	 * 修改密码
	 */
	int updatePassword(Map<String, Object> map);
}
