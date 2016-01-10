package com.szzy.packages.entity;

import java.io.UnsupportedEncodingException;

import com.szzy.packages.tool.Tools;

/**
 * �û��Ĵ��¼
 * 	 
 * * 
 * systemid:ϵͳ��¼��� 
	 * begtime:���ʱ�� 
	 * ename:������ 
	 * bname:������
	 * setman:�Ĵ���
	 * getman:ȡ����
	 * endtime:ȡ��ʱ�䣬�ɿ� 
	 * state:�Ĵ�״̬(0δȡ��1��ȡ)
	 * msg ����
 * @author mac
 *
 */
public class UserPostRecord {
	 private String  systemid;       //ϵͳ��¼��� 
	 private String  begtime;       //���ʱ�� 
	 private String  ename;       //������ 
	 private String  bname;       //������
	 private String  setman;       //�Ĵ���
	 private String  getman;       //ȡ����
	 private String  endtime;       //ȡ��ʱ�䣬�ɿ� 
	 private String getstyle ;
	 private String  state;       //�Ĵ�״̬(0δȡ��1��ȡ)
	 private String msg  ;//����
	 
	 
	public String getGetstyle() {
		return getstyle;
	}
	public void setGetstyle(String getstyle) {
		this.getstyle = getstyle;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		if(msg != null && msg.length() > 0){
			byte[] enameByte = Tools.HexString2Bytes(msg);
			try {
				this.msg = new String(enameByte, "GBK");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			this.msg = "" ;
		}
	}
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
				this.ename = new String(enameByte , "GBK");
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
