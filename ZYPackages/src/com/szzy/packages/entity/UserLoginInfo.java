package com.szzy.packages.entity;

import java.util.List;

/**
 * �û���¼������ȡ��ǰ�û���Ϣ
 * @author mac
 *
 */
public class UserLoginInfo {

	private String state ;  //�û�״̬��0.δ��֤   1.����֤
	private String spid ;   //�û�ϵͳ���
	private List<MailBox> listBox ;  //���䣬��Ϊnull
	private List<ShowBox> listShowBox ;//չʾ��
	
	
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
