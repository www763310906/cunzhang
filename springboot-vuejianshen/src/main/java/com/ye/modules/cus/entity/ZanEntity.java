package com.ye.modules.cus.entity;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.validator.constraints.NotBlank;

import com.ye.common.validator.group.AddGroup;
import com.ye.common.validator.group.UpdateGroup;

/**
 * 点赞
 */
public class ZanEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//博客
	private Integer bokeId;
	//用户
	private Integer userId;
	private String username;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	//
	private Date createTime;

	/**
	 * 设置：
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：博客
	 */
	public void setBokeId(Integer bokeId) {
		this.bokeId = bokeId;
	}
	/**
	 * 获取：博客
	 */
	public Integer getBokeId() {
		return bokeId;
	}
	/**
	 * 设置：用户
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	/**
	 * 获取：用户
	 */
	public Integer getUserId() {
		return userId;
	}
	/**
	 * 设置：
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：
	 */
	public Date getCreateTime() {
		return createTime;
	}
}
