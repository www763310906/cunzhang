package com.ye.modules.cus.entity;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.validator.constraints.NotBlank;

import com.ye.common.validator.group.AddGroup;
import com.ye.common.validator.group.UpdateGroup;

/**
 * 教练学员关系表
 */
public class UserRelationEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//ID
	private Integer id;
	//创建时间
	private Date createTime;
	
	private Integer userId;
	private String pic;
	private String phone;
	
	
	private Integer frendId;
	private String frend;
	private String frendPic;
	private String frendPhone;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Integer getFrendId() {
		return frendId;
	}
	public void setFrendId(Integer frendId) {
		this.frendId = frendId;
	}
	public String getFrend() {
		return frend;
	}
	public void setFrend(String frend) {
		this.frend = frend;
	}
	public String getFrendPic() {
		return frendPic;
	}
	public void setFrendPic(String frendPic) {
		this.frendPic = frendPic;
	}
	public String getFrendPhone() {
		return frendPhone;
	}
	public void setFrendPhone(String frendPhone) {
		this.frendPhone = frendPhone;
	}
	
	 
	
}
