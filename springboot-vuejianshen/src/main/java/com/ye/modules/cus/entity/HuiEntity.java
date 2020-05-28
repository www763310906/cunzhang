package com.ye.modules.cus.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class HuiEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String name;
	private String content;	
	private String pic;
	private Date createTime;
	private Integer userId;
	private String nickname;
	private String head;
	private List<CommentEntity> comments;
	private List<ZanEntity> zan;
	public List<ZanEntity> getZan() {
		return zan;
	}
	public void setZan(List<ZanEntity> zan) {
		this.zan = zan;
	}
	public List<CommentEntity> getComments() {
		return comments;
	}
	public void setComments(List<CommentEntity> comments) {
		this.comments = comments;
	}
	
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getContent() {
		return content;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getPic() {
		return pic;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getCreateTime() {
		return createTime;
	}
}
