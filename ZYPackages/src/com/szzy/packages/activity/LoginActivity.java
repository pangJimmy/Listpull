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
import com.szzy.packages.http.UpdateManager;
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
 * ��¼���棬��¼��ע���û�
 * @author mac
 *
 */
public class LoginActivity extends MBaseActivity {
	
	private EditText editUser ;       	//�˺�
	private EditText editPassword ;		//����
	private Button buttonLogin ;		//��¼��ť
	private CheckBox checkRemember ;	//��ס���븴ѡ��

	private boolean isRemember = false ;
	
	private MApplication mAppli ;
	private Handler mHandler = new Handler();  //���������߳��и���UI

	private TextView tvRegister ;//ע��

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_login);
		super.onCreate(savedInstanceState);
		mAppli = (MApplication) getApplication();
		//���汾�Ƿ����
		new UpdateManager(this) ;
	}
	
	@Override
	public void initView() {
		editUser = (EditText) findViewById(R.id.editText_login_username);
		editPassword = (EditText) findViewById(R.id.editText_login_password) ;
		checkRemember = (CheckBox) findViewById(R.id.checkBox_login_remember) ;
		buttonLogin = (Button) findViewById(R.id.button_login_login) ;
		
		tvRegister = (TextView) findViewById(R.id.textView_register);
		tvRegister.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//�»���
		
		//�û�ע��
		tvRegister.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//��ת����¼����
				Intent toRegister = new Intent(LoginActivity.this, RegisterActivity.class);
				startActivity(toRegister);
				
			}
		});
		
		//�����¼
		buttonLogin.setOnClickListener(this);
		SharedPreferences shared = getSharedPreferences("remember_user", Context.MODE_PRIVATE);
		isRemember = shared.getBoolean("isRemember", false);
		if(isRemember){
			//��ȡ��һ�ε��˺ź�����
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
		case R.id.button_login_login://�����¼
			final String userStr = editUser.getText().toString().trim();
			final String passwordStr = editPassword.getText().toString().trim() ;
			if(userStr == null || userStr.length() == 0){
				Toast.makeText(getApplicationContext(), "�������û���", 0).show();
				return ;
			}
			if(passwordStr == null || passwordStr.length() == 0){
				Toast.makeText(getApplicationContext(), "����������", 0).show();
				return ;
			}
			
			if(isRemember){//��
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
			//��ʾ�Ի���
			super.createProgressDialog("���ڵ�½��...") ;
			//ҵ����¼ģʽ
			httphelper.login(userStr, passwordStr, HttpHelper.LOGIN_MODE_USER, new HttpCallBack() {
				
				@Override
				public void call(final Object obj, final String err) {
					//�رնԻ���
					LoginActivity.super.closeDialog() ;
					//��Ҫ�����߳�������
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							if(err.equals(TipsHttpError.OK) && obj != null){//��¼�ɹ�
								//�洢��¼��Ϣ
								mAppli.setLoginInfo((UserLoginInfo) obj) ;
								mAppli.setUser(userStr);
								mAppli.setPassword(passwordStr);
								
								//������ȷ��¼
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
			//��¼����
//			httphelper.login(userStr, passwordStr, new HttpCallBack() {
//				
//				@Override
//				public void call(final Object obj, final String err) {
//					//��¼�ɹ�
//					if(err.equals(HttpHelper.SUCCESS)){
//						//�������߳�������
//						mHandler.post(new Runnable() {
//							@Override
//							public void run() {
//								//�����û���Ϣ
//								mAppli.setUser(userStr);
//								mAppli.setPassword(passwordStr);
//								mAppli.setUserInfo((UserInfo) obj) ;
//								Toast.makeText(getApplicationContext(), "��¼�ɹ�", 0).show();
//								//������ȷ��¼
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
	
	//��ס�û���
	private void saveUser(String user){
		SharedPreferences shared = getSharedPreferences("user", Context.MODE_PRIVATE);
		Editor editor = shared.edit();
		editor.putString("user", user);
		editor.commit();
	}
	
	//��ס����
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
