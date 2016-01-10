package com.szzy.packages.activity;

import java.util.List;

import com.szzy.packages.entity.Box;
import com.szzy.packages.entity.BoxInfo;
import com.szzy.packages.entity.PostBoxInfo;
import com.szzy.packages.entity.UserInfo;
import com.szzy.packages.entity.UserLoginInfo;
import com.szzy.packages.tool.TipsHttpError;

import android.app.Application;
import android.util.Log;
/**
 * Application ���ڴ��һЩȫ�ֱ���
 * @author mac
 *
 */
public class MApplication extends Application {

	private String user ; //�洢�û���
	private String password ;//�洢����
	
	public List<Box> listBox ;//��������
	
	public String lockId ;//������
	/****---------�ָ���--------****/
	
	private UserInfo userInfo ;//�洢�û���¼��Ϣ
	
	private UserLoginInfo loginInfo ;//�û���¼��Ϣ
	
	private List<BoxInfo> listBoxInfo ;//��������
	
	
	
	public List<BoxInfo> getListBoxInfo() {
		return listBoxInfo;
	}


	public void setListBoxInfo(List<BoxInfo> listBoxInfo) {
		this.listBoxInfo = listBoxInfo;
	}


	public UserLoginInfo getLoginInfo() {
		return loginInfo;
	}


	public void setLoginInfo(UserLoginInfo loginInfo) {
		this.loginInfo = loginInfo;
	}


	public UserInfo getUserInfo() {
		return userInfo;
	}


	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}


	public String getLockId() {
		return lockId;
	}


	public void setLockId(String lockId) {
		this.lockId = lockId;
	}


	public List<Box> getListBox() {
		return listBox;
	}


	public void setListBox(List<Box> listBox) {
		this.listBox = listBox;
	}


	public String getUser() {
		return user;
	}


	public void setUser(String user) {
		this.user = user;
	}

	

	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	@Override
	public void onCreate() {
		Log.e("MApplication", "MApplication");
		super.onCreate();
	}
}
