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
 * ע�����
 * @author mac
 *
 */
public class RegisterActivity extends Activity implements OnClickListener{
	/**
	 * �û�ע�������
	 * u_tel		�û��ֻ�
	 * u_loginName 	��¼��
	 * u_name		��ʵ����
	 * u_IDcard 	���֤��
	 * u_Email		����
	 * u_password	��¼����
	 */
	private EditText editTel ;//�ֻ���
	private EditText editLoginName ;//��¼��
	private EditText editRealName ;//��ʵ����
	private EditText editID ;//���֤��
	private EditText editEmail ;//�����
	private EditText editPassword ;//��¼����
	private EditText editEnterPassword ;//ȷ������
	private Button btnRegister ;//ע��
	
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
		//�绰���Ӽ���
		editTel.addTextChangedListener(telWatcher);
		//���֤�ż���
		editID.addTextChangedListener(idWatcher) ;
		
	}
	
	//�����ֻ�������λ��
	private TextWatcher telWatcher = new TextWatcher() {
		 private CharSequence temp;//����ǰ���ı�  
	       private int editStart;//��꿪ʼλ��  
	       private int editEnd;//������λ��  
	       private final int charMaxNum = 11; //����ı���
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
				Toast.makeText(RegisterActivity.this, "���Ѿ�������11λ�绰������", 0).show();
				s.delete(editStart - 1, editEnd);
				int tempSelection = editStart;
				editTel.setText(s);
				editTel.setSelection(tempSelection);
			}
			
		}
	};
	
	//�������֤��
	private TextWatcher idWatcher = new TextWatcher() {
		private CharSequence temp;//����ǰ���ı�  
	       private int editStart;//��꿪ʼλ��  
	       private int editEnd;//������λ��  
	       private final int charMaxNum = 18; //����ı���
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
				Toast.makeText(RegisterActivity.this, "���Ѿ�������18λ���֤����", 0).show();
				s.delete(editStart - 1, editEnd);
				int tempSelection = editStart;
				editID.setText(s);
				editID.setSelection(tempSelection);
			}
			
		}
	};
	
	/**
	 * ����ƥ�����֤��
	 */
	private boolean checkID(String id){
		//�ж����֤��,ǰ17λһ��Ϊ���֣����һλ������x����X
		String rex = "[0-9]+[x?X?]";
		Pattern p = Pattern.compile(rex);
		return p.matcher(id).matches() ;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_register_register://ע��
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
	//ע��
	private void register(){
		tel = editTel.getText().toString().trim() ;
		realName = editRealName.getText().toString().trim() ;
		idCard = editID.getText().toString().trim() ;
		email = editEmail.getText().toString().trim() ;
		password = editPassword.getText().toString().trim() ;
		repassword = editEnterPassword.getText().toString().trim() ;
		String hexName ;
		//�ж�
		if(!isTel(tel)){
			tips("������11λ�ֻ���");
			return ;
		}
		//��ʵ����
		if(realName == null || realName.length() == 0){
			realName = "";
			hexName = "";
		}else{
			hexName = "";
			try {
				byte[] nameByte = realName.getBytes("GBK") ;
				hexName = Tools.Bytes2HexString(nameByte, nameByte.length) ;
				if(!isRealName(hexName)){
					tips("��ʵ�������ȹ���");
					return ;
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		//���֤
		if(idCard == null || idCard.length() == 0){
			idCard = "" ;
		}else{
			if(!isId(idCard)){
				tips("���֤����");
				return ;
			}
		}
		//����
		if(email == null || email.length() == 0){
			email = "";
		}else{
			if(!isEmail(email)){
				tips("���䳤�ȹ���");
				return ;
			}
		}
		//����,���ж��Ƿ��ȷ������һ��
		if(password != null && password.length() > 0){
			if(repassword != null){
				if(!password.equals(repassword)){
					tips("���벻һ��");
					return ;
				}else{
					if(!isPassword(password)){
						tips("�������");
						return ;
					}
				}
			}else{
				tips("������ȷ������");
				return ;
			}
		}else{
			tips("����������");
			return ;
		}
		//ע��
		httpHelper.register(tel, password, hexName, idCard, email, userType, new HttpCallBack() {
			
			@Override
			public void call(Object obj, final String err) {
				mHandler.post(new Runnable() {
					
					@Override
					public void run() {
						if(err.equals("0")){
							
							Intent intent = new Intent(RegisterActivity.this, LoginActivity.class) ;
							startActivity(intent) ;
							tips("ע��ɹ�");
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
