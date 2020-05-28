package com.ye.modules.cus.entity;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.validator.constraints.NotBlank;

import com.ye.common.validator.group.AddGroup;
import com.ye.common.validator.group.UpdateGroup;

/**
 * 聊天
 */
public class ChatEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//消息
	private String content;
	//好友
	private Integer touserId;
	//用户
	private Integer userId;
	
	private Date createTime;
	
	private String username;
	
	private String tousername;
	
	private String head;
	private String tohead;
	private String pic;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getTouserId() {
		return touserId;
	}
	public void setTouserId(Integer touserId) {
		this.touserId = touserId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getTousername() {
		return tousername;
	}
	public void setTousername(String tousername) {
		this.tousername = tousername;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public String getTohead() {
		return tohead;
	}
	public void setTohead(String tohead) {
		this.tohead = tohead;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	

	
}
