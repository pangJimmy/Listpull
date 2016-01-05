package com.szzy.packages.entity;

/**
 * 投递员投递记录
 * @author mac
 *
 */
public class PosterPostRecord {

	private String systemid ;   //系统记录编号
	private String begtime ;   //存放时间
	private String ename ;   //柜名称
	private String bname ;   //箱名称
	private String getuserphone ;   //取件人手机
	private String order ;   //订单编号
	private String endtime ;   //取件时间
	private String getstyle ;   //取件方式
	private String state ;   //寄存状态
	public String getSystemid() {
		return systemid;
	}
	public void setSystemid(String systemid) {
		this.systemid = systemid;
	}
	public String getBegtime() {
		return begtime;
	}
	public void setBegtime(String begtime) {
		this.begtime = begtime;
	}
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	public String getBname() {
		return bname;
	}
	public void setBname(String bname) {
		this.bname = bname;
	}
	public String getGetuserphone() {
		return getuserphone;
	}
	public void setGetuserphone(String getuserphone) {
		this.getuserphone = getuserphone;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getGetstyle() {
		return getstyle;
	}
	public void setGetstyle(String getstyle) {
		this.getstyle = getstyle;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	
	
}
