package com.ye.modules.cus.entity;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.validator.constraints.NotBlank;

import com.ye.common.validator.group.AddGroup;
import com.ye.common.validator.group.UpdateGroup;

/**
 * 公告
 */
public class GonggaoEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//公告标题
	private String name;
	//公告内容
	private String content;
	//公告封面
	private String pic;
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
	 * 设置：公告标题
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：公告标题
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：公告内容
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * 获取：公告内容
	 */
	public String getContent() {
		return content;
	}
	/**
	 * 设置：公告封面
	 */
	public void setPic(String pic) {
		this.pic = pic;
	}
	/**
	 * 获取：公告封面
	 */
	public String getPic() {
		return pic;
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
