package com.ye.modules.cus.entity;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.validator.constraints.NotBlank;

import com.ye.common.validator.group.AddGroup;
import com.ye.common.validator.group.UpdateGroup;

/**
 * 图书浏览记录
 */
public class ViewEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//用户编号
	private Integer userId;
	//图书
	private Integer goodsId;
	//时间
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
	 * 设置：用户编号
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	/**
	 * 获取：用户编号
	 */
	public Integer getUserId() {
		return userId;
	}
	/**
	 * 设置：图书
	 */
	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}
	/**
	 * 获取：图书
	 */
	public Integer getGoodsId() {
		return goodsId;
	}
	/**
	 * 设置：时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
}
