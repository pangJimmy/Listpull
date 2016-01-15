package com.szzy.packages.activity;

import java.util.Timer;
import java.util.TimerTask;

import com.szzy.packages.R;
import com.szzy.packages.base.MBaseActivity;
import com.szzy.packages.entity.LockInfo;
import com.szzy.packages.entity.PostBoxInfo;
import com.szzy.packages.http.HttpCallBack;
import com.szzy.packages.http.HttpHelper;
import com.szzy.packages.http.OpenBoxCall;
import com.szzy.packages.http.QueryBoxCall;
import com.szzy.packages.tool.TipsHttpError;
import com.szzy.packages.tool.Utils;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * ���ѼĴ�
 * @author mac
 *
 */
public class FamilyPostActivity extends MBaseActivity implements OnClickListener{
	private TextView tvTitle ;//����
	private ImageView imgBack ; //���ؼ�
	
	private EditText editLock ;//����
	private ImageView imgSys ;//ɨһɨ
	private Button btnSelectBox ; //ѡ�����
	private Button btnOpenBox ; //����Ͷ��
	private boolean isSelectLock ;//�Ƿ�ɨ�������
	private EditText editTel ;//�ռ��˵绰
	private EditText editLetter ;//��ע����,��֪��δ�����Ϣ
	private String lockCode ;  //�����
	private String lockName ;  //�������
	private int postTel = 0 ;
	private final static int SCANNIN_LOCK_CODE = 11;   //ɨ�������
	private String boxName  ; //���ź�
	private String boxId ; //����ID 
	private MApplication mApp ;
	private HttpHelper httpHelper ;
	private Handler mHandler = new Handler() ;
	
	private String systemid = null; //�û��Ĵ���Ų���
	
	private int serverTel = 0 ;
	private String telStr = null;  //ȡ���˵绰
	private String msg = null ;//�û��Ĵ�����
	
	private Timer timerPost ;  //���ſ�����ʱ�������û�����֮�󣬳�ʱʱ���趨Ϊ60s�����60s�޲����������¿���
	private TextView tvDialogTime ;//����ʱ
	private int timeout = 60 ;//��ʱ60��
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_family_post) ;
		mApp = (MApplication) getApplication() ;
		httpHelper = new HttpHelper() ;
		minitView() ;
	}
	
	private void minitView(){
		tvTitle = (TextView) findViewById(R.id.textView_title) ;
		tvTitle.setText("���ѼĴ�");
		imgBack = (ImageView) findViewById(R.id.imageButton_back) ;
		editTel = (EditText) findViewById(R.id.editText_family_tel) ;
		editLetter = (EditText) findViewById(R.id.editText_family_letter) ;
		editLock = (EditText) findViewById(R.id.editText_family_lock) ;
		imgSys = (ImageView) findViewById(R.id.imageView_family_sys) ;
		btnSelectBox = (Button) findViewById(R.id.button_family_select_box) ;
		btnOpenBox = (Button) findViewById(R.id.button_family_commit) ;
		
		imgSys.setOnClickListener(this);
		btnSelectBox.setOnClickListener(this);
		btnOpenBox.setOnClickListener(this);
		//��������绰��λ��
		editTel.addTextChangedListener(new TextWatcher() {
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
					Toast.makeText(mApp, "���Ѿ�������11λ�绰������", 0).show();
					s.delete(editStart - 1, editEnd);
					int tempSelection = editStart;
					editTel.setText(s);
//					telStr = s.toString() ;
					editTel.setSelection(tempSelection);
				}
				telStr = editTel.getText().toString();
			}
		});
		btnSelectBox.setOnClickListener(this) ;
		btnOpenBox.setOnClickListener(this) ;
		imgBack.setOnClickListener(this) ;
	}
	
	private final int SELECT_BOX_REQUEST_CODE = 2 ;  //ѡ������
	private LockInfo lockInfo ; //������Ϣ
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case SCANNIN_LOCK_CODE://���������
			if(resultCode == RESULT_OK){
				Log.e("lock", ""+ mApp.getLockId()) ;
				lockCode = mApp.getLockId().trim();
				super.createProgressDialog("���ڻ�ȡ�����Ϣ") ;
				try{
//					int lock = Integer.valueOf(lockCode) ;
					//��ѯ����״̬
					httpHelper.getBoxInfo(mApp.getLockId(), mApp.getPassword(), lockCode, new HttpCallBack() {
						
						@Override
						public void call(final Object obj, final String err) {
							mHandler.post(new Runnable() {
								
								@Override
								public void run() {
									//ȡ��progress
									FamilyPostActivity.super.closeDialog() ;
									TipsHttpError.toastError(mApp, err) ;
									lockInfo = (LockInfo) obj ;
									if(lockInfo != null){ //��ȷ����
										isSelectLock = true ; 
										systemid = lockInfo.getSystemid() ;
										//����ȫ��listBoxinfo
										mApp.setListBoxInfo(lockInfo.getListBox()) ;
										//��ʾ������
										editLock.setText(lockInfo.getEname()) ;
									}
								}
							}) ;
							
						}
					});
				}catch(Exception e){
					Log.e("",e.toString());
				}
				
			}			
			break ;
		case SELECT_BOX_REQUEST_CODE://ѡ�������
			if(resultCode == RESULT_OK){
				int position = data.getIntExtra("position", 0);
				boxPosition = position ;
				//��ȡ��������
				boxName = mApp.getListBoxInfo().get(position).getBname();
				
				btnSelectBox.setText(boxName);
				//��ȡbcode�����û��Ĵ�
				boxId = mApp.getListBoxInfo().get(position).getBcode();
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageButton_back://����
			finish() ;
			break;
		case R.id.imageView_family_sys://ɨһɨ���
			Intent intent = new Intent(FamilyPostActivity.this, MipcaActivityCapture.class);
			intent.putExtra("mode", 1);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivityForResult(intent, SCANNIN_LOCK_CODE);
			break; 
		case R.id.button_family_select_box://ѡ������
			//ѡ������֮ǰҪ���ж����Ƿ�ɨ�������
			if(isSelectLock){
				Intent toSelect = new Intent(FamilyPostActivity.this, SelectBoxActivity.class);
				startActivityForResult(toSelect, SELECT_BOX_REQUEST_CODE);
			}else{
				Toast.makeText(getApplicationContext(), "����ɨ�����", 0).show() ;
			}
			break;
		case R.id.button_family_commit://����Ͷ��
//			Utils.tipsUnfinish(this) ;
			//���绰Ϊ��ʱ��Ĭ��Ϊ�Ĵ��ߵĵ绰
			if(telStr == null || telStr.length() == 0){
				telStr = mApp.getUser() ;
			}else if(telStr.length() != 11){
				Toast.makeText(mApp, "�������ռ���11Ϊ�绰����", 0).show() ;
				return ; 
			}
			if(!isSelectLock){
				Toast.makeText(getApplicationContext(), "����ɨ�����", 0).show() ;
				return ;
			}
			if(boxId == null || boxId.length() < 1){
				Toast.makeText(mApp, "��ѡ���ݹ�����", 0).show();
				return;
			}
			msg = editLetter.getText().toString() ;
			//�ȴ�
			super.createProgressDialog("���ڻ�ȡ������Ϣ");
			//�Ĵ�
			httpHelper.userPost(mApp.getUser(), mApp.getPassword(), systemid, boxId, telStr, msg , new HttpCallBack() {
				
				@Override
				public void call(final Object obj, final String err) {//��objΪsystemid String ����
					mHandler.post(new Runnable() {
						
						@Override
						public void run() {
							//ȡ���ȴ�
							FamilyPostActivity.super.closeDialog() ;
							//��ȷ���أ������Ի����Ƿ񱣴棬ȷ�ϵ��üĴ汣��ָ�
							if(err.equals(TipsHttpError.OK)){
								final String sysid = (String) obj ;
								//�����Ի���
								createDialog(sysid) ;
							}else{
								TipsHttpError.toastError(mApp, err) ;
							}
							
						}
					});
				}
			});
			break;

		default:
			break;
		}
		
	}
	
	private TextView tvDiaLock ;
	private TextView tvDiaCompany  ;
	private TextView tvDiaCompanyType ;
	private TextView tvDiaBox ;
	
	private int boxPosition ;  //����λ��
	
	Dialog mdialog ;//�Ի���
	//�����Ի���
	private void createDialog(final String sysid){
		Builder builder = new Builder(this);
		LayoutInflater inflater = LayoutInflater.from(this);
		View view  = inflater.inflate(R.layout.dialog_user_express, null);
		builder.setView(view);
		tvDiaLock = (TextView) view.findViewById(R.id.textView_dialog_user_express_lock) ;
		tvDiaCompany = (TextView) view.findViewById(R.id.textView_dialog_user_express_post_company) ;
		tvDiaCompanyType = (TextView) view.findViewById(R.id.textView_dialog_user_express_post_company_type) ;
		tvDiaCompanyType.setText("�ռ��˵绰:");
		tvDiaBox = (TextView) view.findViewById(R.id.textView_dialog_user_express_boxname) ;
		tvDiaLock.setText(lockName);
		tvDiaCompany.setText(telStr);
		tvDiaBox.setText(boxName);
		tvDialogTime = (TextView) view.findViewById(R.id.textView_dialog_time) ;
		if(timerPost != null){
			timerPost.cancel() ;
			timerPost = null ;
		}
		timeout = 60 ;
		//��ʱ��
		timerPost = new Timer();
		timerPost.schedule(new TimerTask() {
			
			@Override
			public void run() {
				if(timeout > 0){
					//ˢ�¼�ʱ
					mHandler.post(new Runnable() {
						public void run() {
							tvDialogTime.setText("" + timeout);
						}
					});
					
				}else{
					//�Ի���ȡ��,��ʱȡ��
					mHandler.post(new Runnable() {
						public void run() {
							//ȡ���Ի���
							if(mdialog != null){
								mdialog.dismiss() ;
							}
							
						}
					});
					
					timerPost.cancel() ;
				}
				timeout-- ;
			}
		}, 100	, 1000) ;

		builder.setPositiveButton("ȷ��Ͷ��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//�û��Ĵ汣��
				httpHelper.saveUserPost(mApp.getUser(), mApp.getPassword(), sysid, new HttpCallBack() {
					
					@Override
					public void call(Object obj, final String err) {
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								//����ɹ�
								if(err.equals(TipsHttpError.OK)){
									
									FamilyPostActivity.super.ToastInfo("�Ĵ�ɹ�");
									Log.e("error--- ", "OK");
									editLetter.setText("");
									btnSelectBox.setText("���ѡ������") ;
									//�޸�����״̬,0������1���������� 3���Ź���
									mApp.getListBoxInfo().get(boxPosition).setBstate("1") ;
								}else{
									TipsHttpError.toastError(mApp, err)  ;
								}
							}
						}) ;
						
					}
				}) ;
				
			}
		});
		builder.setNegativeButton("ȡ��", new  DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				//ȡ����ʱ
				timerPost.cancel();
				
			}
			
		});
		mdialog = builder.create() ;
		mdialog.show() ;
	}
}
