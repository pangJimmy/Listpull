package com.szzy.packages.activity;

import java.util.List;

import com.szzy.packages.entity.Box;
import com.szzy.packages.entity.PostBoxInfo;
import com.szzy.packages.entity.UserInfo;
import com.szzy.packages.entity.UserLoginInfo;
import com.szzy.packages.tool.TipsHttpError;

import android.app.Application;
import android.util.Log;
/**
 * Application 用于存放一些全局变量
 * @author mac
 *
 */
public class MApplication extends Application {

	private String user ; //存储用户名
	private String password ;//存储密码
	
	public List<Box> listBox ;//箱子数量
	
	public String lockId ;//箱柜编码
	
	private UserInfo userInfo ;//存储用户登录信息
	
	private UserLoginInfo loginInfo ;//用户登录信息
	
	
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
