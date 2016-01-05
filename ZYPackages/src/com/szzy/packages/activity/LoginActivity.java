package com.szzy.packages.activity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.szzy.packages.R;
import com.szzy.packages.base.MBaseActivity;
import com.szzy.packages.entity.GetPackages;
import com.szzy.packages.entity.UserInfo;
import com.szzy.packages.entity.UserLoginInfo;
import com.szzy.packages.http.HttpCallBack;
import com.szzy.packages.http.HttpHelper;
import com.szzy.packages.http.LoginCallBack;
import com.szzy.packages.http.QueryNotGetPackageCall;
import com.szzy.packages.tool.TipsHttpError;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 登录界面，登录和注册用户
 * @author mac
 *
 */
public class LoginActivity extends MBaseActivity {
	
	private EditText editUser ;       	//账号
	private EditText editPassword ;		//密码
	private Button buttonLogin ;		//登录按钮
	private CheckBox checkRemember ;	//记住密码复选框

	private boolean isRemember = false ;
	
	private MApplication mAppli ;
	private Handler mHandler = new Handler();  //用于在子线程中更新UI

	private TextView tvRegister ;//注册

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_login);
		super.onCreate(savedInstanceState);
		mAppli = (MApplication) getApplication();
	}
	
	@Override
	public void initView() {
		editUser = (EditText) findViewById(R.id.editText_login_username);
		editPassword = (EditText) findViewById(R.id.editText_login_password) ;
		checkRemember = (CheckBox) findViewById(R.id.checkBox_login_remember) ;
		buttonLogin = (Button) findViewById(R.id.button_login_login) ;
		
		tvRegister = (TextView) findViewById(R.id.textView_register);
		tvRegister.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
		
		//用户注册
		tvRegister.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//跳转到登录界面
				Intent toRegister = new Intent(LoginActivity.this, RegisterActivity.class);
				startActivity(toRegister);
				
			}
		});
		
		//点击登录
		buttonLogin.setOnClickListener(this);
		SharedPreferences shared = getSharedPreferences("remember_user", Context.MODE_PRIVATE);
		isRemember = shared.getBoolean("isRemember", false);
		if(isRemember){
			//读取上一次的账号和密码
			editUser.setText(getUser());
			editPassword.setText(getPassword());
		}
		
		checkRemember.setChecked(isRemember);
		checkRemember.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				isRemember = isChecked;
				SharedPreferences shared = getSharedPreferences("remember_user", Context.MODE_PRIVATE);
				Editor editor = shared.edit();
				editor.putBoolean("isRemember", isRemember);
				editor.commit();
			}
		});
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		return super.twoPressExit(keyCode, event);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_login_login://点击登录
			final String userStr = editUser.getText().toString().trim();
			final String passwordStr = editPassword.getText().toString().trim() ;
			if(userStr == null || userStr.length() == 0){
				Toast.makeText(getApplicationContext(), "请输入用户名", 0).show();
				return ;
			}
			if(passwordStr == null || passwordStr.length() == 0){
				Toast.makeText(getApplicationContext(), "请输入密码", 0).show();
				return ;
			}
			
			if(isRemember){//将
				saveUser(userStr);
				savePassword(passwordStr);
			}
			HttpHelper httphelper = new HttpHelper();
//			//
//			httphelper.queryPackage(userStr, passwordStr, 1, new QueryNotGetPackageCall() {
//				
//				@Override
//				public void call(NotGetPackages notget) {
//					
//					
//				}
//			});
			//业主登录模式
			httphelper.login(userStr, passwordStr, HttpHelper.LOGIN_MODE_USER, new HttpCallBack() {
				
				@Override
				public void call(final Object obj, final String err) {
					//需要在主线程中运行
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							if(err.equals(TipsHttpError.OK) && obj != null){//登录成功
								//存储登录信息
								mAppli.setLoginInfo((UserLoginInfo) obj) ;
								mAppli.setUser(userStr);
								mAppli.setPassword(passwordStr);
								//处理正确登录
								Intent intent = new Intent(LoginActivity.this, MainActivity.class);
								startActivity(intent);
								finish();
							}else{
								TipsHttpError.toastError(mAppli, err);
							}
							
						}
						
					});

						
				}
			});
			//登录返回
//			httphelper.login(userStr, passwordStr, new HttpCallBack() {
//				
//				@Override
//				public void call(final Object obj, final String err) {
//					//登录成功
//					if(err.equals(HttpHelper.SUCCESS)){
//						//需在主线程中运行
//						mHandler.post(new Runnable() {
//							@Override
//							public void run() {
//								//保存用户信息
//								mAppli.setUser(userStr);
//								mAppli.setPassword(passwordStr);
//								mAppli.setUserInfo((UserInfo) obj) ;
//								Toast.makeText(getApplicationContext(), "登录成功", 0).show();
//								//处理正确登录
//								Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//								startActivity(intent);
//								finish();
//								
//							}
//						});
//
//					}else{
//						mHandler.post(new Runnable() {
//							
//							@Override
//							public void run() {
//								TipsHttpError.toastError(mAppli, err);
//								
//							}
//						});
//						
//					}
//					
//				}
//			});
			break;

		default:
			break;
		}
	}
	
	//记住用户名
	private void saveUser(String user){
		SharedPreferences shared = getSharedPreferences("user", Context.MODE_PRIVATE);
		Editor editor = shared.edit();
		editor.putString("user", user);
		editor.commit();
	}
	
	//记住密码
	private void savePassword(String password){
		SharedPreferences shared = getSharedPreferences("user", Context.MODE_PRIVATE);
		Editor editor = shared.edit();
		editor.putString("password", password);
		editor.commit();
	}
	
	private String getUser(){
		SharedPreferences shared = getSharedPreferences("user", Context.MODE_PRIVATE);
		String user = shared.getString("user", "zhenyao");
		return user;
	}
	
	private String getPassword(){
		SharedPreferences shared = getSharedPreferences("user", Context.MODE_PRIVATE);
		String password = shared.getString("password", "888888");
		return password;
		
	}
	
	
}
