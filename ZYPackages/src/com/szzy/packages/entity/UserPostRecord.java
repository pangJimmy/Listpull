package com.szzy.packages.entity;

import com.szzy.packages.tool.Tools;

/**
 * 用户寄存记录
 * 	 
 * * 
 * systemid:系统记录编号 
	 * begtime:存放时间 
	 * ename:柜名称 
	 * bname:箱名称
	 * setman:寄存人
	 * getman:取件人
	 * endtime:取件时间，可空 
	 * state:寄存状态(0未取，1已取)
 * @author mac
 *
 */
public class UserPostRecord {
	 private String  systemid;       //系统记录编号 
	 private String  begtime;       //存放时间 
	 private String  ename;       //柜名称 
	 private String  bname;       //箱名称
	 private String  setman;       //寄存人
	 private String  getman;       //取件人
	 private String  endtime;       //取件时间，可空 
	 private String  state;       //寄存状态(0未取，1已取)
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
			this.ename = new String(enameByte);
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
	public String getSetman() {
		return setman;
	}
	public void setSetman(String setman) {
		this.setman = setman;
	}
	public String getGetman() {
		return getman;
	}
	public void setGetman(String getman) {
		this.getman = getman;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	 
	 
	 
}
