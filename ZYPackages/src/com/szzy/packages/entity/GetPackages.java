package com.szzy.packages.entity;

import java.io.UnsupportedEncodingException;

import com.szzy.packages.tool.Tools;

public class GetPackages {
	String time ;			//投件时间
	String orderNo ;		//快递单号
	String lockCode ;		//快递柜编号
	String boxId ;			//快递柜箱号
	String address ;		//快递柜标识
	String gettime ;		//取件时间
	String getstyle ;		//取件方式
	String state ;			//当前状态
	String boxName ;		//箱子名称
	
	public String getBoxName() {
		return boxName;
	}
	public void setBoxName(String boxName) {
		this.boxName = boxName;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getLockCode() {
		return lockCode;
	}
	public void setLockCode(String lockCode) {
		this.lockCode = lockCode;
	}
	public String getBoxId() {
		return boxId;
	}
	public void setBoxId(String boxId) {
		this.boxId = boxId;
	}
	public String getAddress() {
		byte[] addressBytes = Tools.HexString2Bytes(address);
		String addr = null;
		try {
			addr = new String(addressBytes, "gb2312");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return addr;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getGettime() {
		return gettime;
	}
	public void setGettime(String gettime) {
		this.gettime = gettime;
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
