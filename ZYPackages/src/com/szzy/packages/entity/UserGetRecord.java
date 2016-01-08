package com.szzy.packages.entity;

import java.io.UnsupportedEncodingException;

import com.szzy.packages.tool.Tools;

/**
 * private String 用户取件记录 private String @author mac
 * 
 */
public class UserGetRecord {

	private String systemid;; // 系统记录编号
	private String begtime;; // 存放时间
	private String ename;; // 柜名称
	private String bname;; // 箱名称
	private String postman;; // 投递员
	private String order;; // 订单编号 ， 可空
	private String endtime;; // 取件时间，可空
	private String getstyle;; // 取件凡是，(未取是0)
	private String state; // 寄存状态(0未取，1已取)

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
		if(ename != null && ename.length() > 0){
			byte[] enameByte = Tools.HexString2Bytes(ename);
			try {
				this.ename = new String(enameByte, "gbk");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			this.ename = "" ;
		}
	}

	public String getBname() {
		return bname;
	}

	public void setBname(String bname) {
		this.bname = bname;
	}

	public String getPostman() {
		return postman;
	}

	public void setPostman(String postman) {
		this.postman = postman;
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
	};

}
