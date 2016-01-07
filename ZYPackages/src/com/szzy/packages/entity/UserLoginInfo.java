package com.szzy.packages.entity;

import java.util.List;

/**
 * 用户登录，并获取当前用户信息
 * @author mac
 *
 */
public class UserLoginInfo {

	private String state ;  //用户状态，0.未认证   1.已认证
	private String spid ;   //用户系统编号
	private List<MailBox> listBox ;  //信箱，可为null
	private List<ShowBox> listShowBox ;//展示柜
	
	
	public List<ShowBox> getListShowBox() {
		return listShowBox;
	}
	public void setListShowBox(List<ShowBox> listShowBox) {
		this.listShowBox = listShowBox;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getSpid() {
		return spid;
	}
	public void setSpid(String spid) {
		this.spid = spid;
	}
	public List<MailBox> getListBox() {
		return listBox;
	}
	public void setListBox(List<MailBox> listBox) {
		this.listBox = listBox;
	}
	
	
}
