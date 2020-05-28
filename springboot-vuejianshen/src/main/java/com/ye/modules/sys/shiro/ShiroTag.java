package com.ye.modules.sys.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Component;

import com.ye.modules.sys.entity.SysUserEntity;

/**
 * Shiro权限标签
 * 
 */
@Component
public class ShiroTag {

	/**
	 * 是否拥有该权限
	 * @param permission  权限标识
	 * @return   true：是     false：否
	 */
	public boolean hasPermission(String permission) {
		Subject subject = SecurityUtils.getSubject();
		return subject != null && subject.isPermitted(permission);
	}
	public SysUserEntity getUser() {
		 
		return ShiroUtils.getUserEntity();
	}
}
