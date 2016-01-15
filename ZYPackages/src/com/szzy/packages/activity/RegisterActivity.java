package com.szzy.packages.activity;

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.szzy.packages.R;
import com.szzy.packages.http.HttpCallBack;
import com.szzy.packages.http.HttpError;
import com.szzy.packages.http.HttpHelper;
import com.szzy.packages.tool.TipsHttpError;
import com.szzy.packages.tool.Tools;
/**
 * 注册界面
 * @author mac
 *
 */
public class RegisterActivity extends Activity implements OnClickListener{
	/**
	 * 用户注册参数：
	 * u_tel		用户手机
	 * u_loginName 	登录名
	 * u_name		真实姓名
	 * u_IDcard 	身份证号
	 * u_Email		邮箱
	 * u_password	登录密码
	 */
	private EditText editTel ;//手机号
	private EditText editLoginName ;//登录号
	private EditText editRealName ;//真实姓名
	private EditText editID ;//身份证号
	private EditText editEmail ;//邮箱号
	private EditText editPassword ;//登录密码
	private EditText editEnterPassword ;//确认密码
	private Button btnRegister ;//注册
	
	private HttpHelper httpHelper ;//http
	
	private String tel ;
	private String  loginName ;
	private String realName ;
	private String idCard ;
	private String email ;
	private String password ;
	private String repassword ;
	private String userType = "1" ;
	
	private Handler mHandler  = new Handler() ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register) ;
		initView();
		httpHelper = new HttpHelper() ;
	}
	
	private void initView(){
		editTel = (EditText) findViewById(R.id.edit_register_tel);
		editLoginName = (EditText) findViewById(R.id.edit_register_login_name);
		editRealName = (EditText) findViewById(R.id.edit_register_real_name);
		editID = (EditText) findViewById(R.id.edit_register_id);
		editEmail = (EditText) findViewById(R.id.edit_register_email);
		editPassword = (EditText) findViewById(R.id.edit_register_upassword);
		editEnterPassword = (EditText) findViewById(R.id.edit_register_enter_password);
		btnRegister = (Button) findViewById(R.id.button_register_register);
		
//		editLoginName.setVisibility(View.GONE) ;
		btnRegister.setOnClickListener(this) ;
		//电话增加监听
		editTel.addTextChangedListener(telWatcher);
		//身份证号监听
		editID.addTextChangedListener(idWatcher) ;
		
	}
	
	//监听手机号输入位数
	private TextWatcher telWatcher = new TextWatcher() {
		 private CharSequence temp;//监听前的文本  
	       private int editStart;//光标开始位置  
	       private int editEnd;//光标结束位置  
	       private final int charMaxNum = 11; //最大文本数
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			temp = s;
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			editStart = editTel.getSelectionStart();
			editEnd = editTel.getSelectionEnd() ;
			if (temp.length() > charMaxNum) { 
				Toast.makeText(RegisterActivity.this, "您已经输入了11位电话号码了", 0).show();
				s.delete(editStart - 1, editEnd);
				int tempSelection = editStart;
				editTel.setText(s);
				editTel.setSelection(tempSelection);
			}
			
		}
	};
	
	//监听身份证号
	private TextWatcher idWatcher = new TextWatcher() {
		private CharSequence temp;//监听前的文本  
	       private int editStart;//光标开始位置  
	       private int editEnd;//光标结束位置  
	       private final int charMaxNum = 18; //最大文本数
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			temp = s;
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			editStart = editID.getSelectionStart();
			editEnd = editID.getSelectionEnd() ;
			if (temp.length() > charMaxNum) { 
				Toast.makeText(RegisterActivity.this, "您已经输入了18位身份证号了", 0).show();
				s.delete(editStart - 1, editEnd);
				int tempSelection = editStart;
				editID.setText(s);
				editID.setSelection(tempSelection);
			}
			
		}
	};
	
	/**
	 * 正则匹配身份证号
	 */
	private boolean checkID(String id){
		//判断身份证号,前17位一定为数字，最后一位可能是x或者X
		String rex = "[0-9]+[x?X?]";
		Pattern p = Pattern.compile(rex);
		return p.matcher(id).matches() ;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_register_register://注册
			register() ;
			break;

		default:
			break;
		}
		
	}
	
	/**
	 * 	private String tel ;
	private String  loginName ;
	private String realName ;
	private String idCard ;
	private String email ;
	private String password ;
	private String repassword ;
	 */
	//注册
	private void register(){
		tel = editTel.getText().toString().trim() ;
		realName = editRealName.getText().toString().trim() ;
		idCard = editID.getText().toString().trim() ;
		email = editEmail.getText().toString().trim() ;
		password = editPassword.getText().toString().trim() ;
		repassword = editEnterPassword.getText().toString().trim() ;
		String hexName ;
		//判断
		if(!isTel(tel)){
			tips("请输入11位手机号");
			return ;
		}
		//真实姓名
		if(realName == null || realName.length() == 0){
			realName = "";
			hexName = "";
		}else{
			hexName = "";
			try {
				byte[] nameByte = realName.getBytes("GBK") ;
				hexName = Tools.Bytes2HexString(nameByte, nameByte.length) ;
				if(!isRealName(hexName)){
					tips("真实姓名长度过长");
					return ;
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		//身份证
		if(idCard == null || idCard.length() == 0){
			idCard = "" ;
		}else{
			if(!isId(idCard)){
				tips("身份证错误");
				return ;
			}
		}
		//邮箱
		if(email == null || email.length() == 0){
			email = "";
		}else{
			if(!isEmail(email)){
				tips("邮箱长度过长");
				return ;
			}
		}
		//密码,先判断是否和确认密码一致
		if(password != null && password.length() > 0){
			if(repassword != null){
				if(!password.equals(repassword)){
					tips("密码不一致");
					return ;
				}else{
					if(!isPassword(password)){
						tips("密码过长");
						return ;
					}
				}
			}else{
				tips("请输入确认密码");
				return ;
			}
		}else{
			tips("请输入密码");
			return ;
		}
		//注册
		httpHelper.register(tel, password, hexName, idCard, email, userType, new HttpCallBack() {
			
			@Override
			public void call(Object obj, final String err) {
				mHandler.post(new Runnable() {
					
					@Override
					public void run() {
						if(err.equals("0")){
							
							Intent intent = new Intent(RegisterActivity.this, LoginActivity.class) ;
							startActivity(intent) ;
							tips("注册成功");
							finish() ;
						}else{
							TipsHttpError.toastError(getApplicationContext(), err) ;
						}
						
						
					}
				});
				
				
			}
		});
		
	}
	
	/**
	 * 
	 * @param tel
	 * @return
	 */
	private boolean isTel(String tel){
		if(tel.length() != 11){
			return false ;
		}
		return true ;
	}
	private boolean isRealName(String hexName){
		if(hexName.length() > 40){
			return false ;
		}
		return true ;
	}
	
	private boolean isId(String id){
		if(id.length() != 18){
			return false ;
		}
		return true ;
	}
	
	private boolean isEmail(String email){
		if(email.length() > 50){
			return false ;
		}
		return true ;
	}
	
	private boolean isPassword(String password){
		if(password.length() > 20){
			return false ;
		}
		return true ;
	}
	
	private void tips(String info){
		Toast.makeText(getApplicationContext(), info, Toast.LENGTH_LONG).show() ;
	}
}
