package com.szzy.packages.activity;

import java.util.regex.Pattern;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.szzy.packages.R;
/**
 * 注册界面
 * @author mac
 *
 */
public class RegisterActivity extends Activity {
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register) ;
		initView();
	}
	
	private void initView(){
		editTel = (EditText) findViewById(R.id.edit_register_tel);
		editLoginName = (EditText) findViewById(R.id.edit_register_tel);
		editRealName = (EditText) findViewById(R.id.edit_register_tel);
		editID = (EditText) findViewById(R.id.edit_register_tel);
		editEmail = (EditText) findViewById(R.id.edit_register_tel);
		editPassword = (EditText) findViewById(R.id.edit_register_tel);
		editEnterPassword = (EditText) findViewById(R.id.edit_register_tel);
		btnRegister = (Button) findViewById(R.id.button_register_register);
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
	
	
	
}
