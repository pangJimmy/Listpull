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
 * 亲友寄存
 * @author mac
 *
 */
public class FamilyPostActivity extends MBaseActivity implements OnClickListener{
	private TextView tvTitle ;//标题
	private ImageView imgBack ; //返回键
	
	private EditText editLock ;//箱柜号
	private ImageView imgSys ;//扫一扫
	private Button btnSelectBox ; //选择箱号
	private Button btnOpenBox ; //开箱投递
	private boolean isSelectLock ;//是否扫描了箱柜
	private EditText editTel ;//收件人电话
	private EditText editLetter ;//备注留言,不知如何传递消息
	private String lockCode ;  //箱柜编号
	private String lockName ;  //箱柜名称
	private int postTel = 0 ;
	private final static int SCANNIN_LOCK_CODE = 11;   //扫描柜箱编号
	private String boxName  ; //箱门号
	private String boxId ; //箱门ID 
	private MApplication mApp ;
	private HttpHelper httpHelper ;
	private Handler mHandler = new Handler() ;
	
	private String systemid = null; //用户寄存柜编号参数
	
	private int serverTel = 0 ;
	private String telStr = null;  //取件人电话
	private String msg = null ;//用户寄存留言
	
	private Timer timerPost ;  //箱门开启定时器，当用户开箱之后，超时时间设定为60s，如果60s无操作，则重新开箱
	private TextView tvDialogTime ;//倒计时
	private int timeout = 60 ;//超时60秒
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
		tvTitle.setText("亲友寄存");
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
		//监听输入电话的位数
		editTel.addTextChangedListener(new TextWatcher() {
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
					Toast.makeText(mApp, "您已经输入了11位电话号码了", 0).show();
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
	
	private final int SELECT_BOX_REQUEST_CODE = 2 ;  //选择箱门
	private LockInfo lockInfo ; //箱门信息
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case SCANNIN_LOCK_CODE://返回箱柜编号
			if(resultCode == RESULT_OK){
				Log.e("lock", ""+ mApp.getLockId()) ;
				lockCode = mApp.getLockId().trim();
				super.createProgressDialog("正在获取箱柜信息") ;
				try{
//					int lock = Integer.valueOf(lockCode) ;
					//查询箱子状态
					httpHelper.getBoxInfo(mApp.getLockId(), mApp.getPassword(), lockCode, new HttpCallBack() {
						
						@Override
						public void call(final Object obj, final String err) {
							mHandler.post(new Runnable() {
								
								@Override
								public void run() {
									//取消progress
									FamilyPostActivity.super.closeDialog() ;
									TipsHttpError.toastError(mApp, err) ;
									lockInfo = (LockInfo) obj ;
									if(lockInfo != null){ //正确返回
										isSelectLock = true ; 
										systemid = lockInfo.getSystemid() ;
										//设置全局listBoxinfo
										mApp.setListBoxInfo(lockInfo.getListBox()) ;
										//显示柜名称
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
		case SELECT_BOX_REQUEST_CODE://选择快递箱号
			if(resultCode == RESULT_OK){
				int position = data.getIntExtra("position", 0);
				boxPosition = position ;
				//获取箱门名称
				boxName = mApp.getListBoxInfo().get(position).getBname();
				
				btnSelectBox.setText(boxName);
				//获取bcode用于用户寄存
				boxId = mApp.getListBoxInfo().get(position).getBcode();
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageButton_back://返回
			finish() ;
			break;
		case R.id.imageView_family_sys://扫一扫箱柜
			Intent intent = new Intent(FamilyPostActivity.this, MipcaActivityCapture.class);
			intent.putExtra("mode", 1);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivityForResult(intent, SCANNIN_LOCK_CODE);
			break; 
		case R.id.button_family_select_box://选择箱门
			//选择箱子之前要先判断下是否扫描了箱柜
			if(isSelectLock){
				Intent toSelect = new Intent(FamilyPostActivity.this, SelectBoxActivity.class);
				startActivityForResult(toSelect, SELECT_BOX_REQUEST_CODE);
			}else{
				Toast.makeText(getApplicationContext(), "请先扫描箱柜", 0).show() ;
			}
			break;
		case R.id.button_family_commit://开箱投递
//			Utils.tipsUnfinish(this) ;
			//当电话为空时，默认为寄存者的电话
			if(telStr == null || telStr.length() == 0){
				telStr = mApp.getUser() ;
			}else if(telStr.length() != 11){
				Toast.makeText(mApp, "请输入收件人11为电话号码", 0).show() ;
				return ; 
			}
			if(!isSelectLock){
				Toast.makeText(getApplicationContext(), "请先扫描箱柜", 0).show() ;
				return ;
			}
			if(boxId == null || boxId.length() < 1){
				Toast.makeText(mApp, "请选择快递柜箱子", 0).show();
				return;
			}
			msg = editLetter.getText().toString() ;
			//等待
			super.createProgressDialog("正在获取开箱信息");
			//寄存
			httpHelper.userPost(mApp.getUser(), mApp.getPassword(), systemid, boxId, telStr, msg , new HttpCallBack() {
				
				@Override
				public void call(final Object obj, final String err) {//此obj为systemid String 类型
					mHandler.post(new Runnable() {
						
						@Override
						public void run() {
							//取消等待
							FamilyPostActivity.super.closeDialog() ;
							//正确返回，弹出对话框是否保存，确认调用寄存保存指令，
							if(err.equals(TipsHttpError.OK)){
								final String sysid = (String) obj ;
								//弹出对话框
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
	
	private int boxPosition ;  //箱门位置
	
	Dialog mdialog ;//对话框
	//创建对话框
	private void createDialog(final String sysid){
		Builder builder = new Builder(this);
		LayoutInflater inflater = LayoutInflater.from(this);
		View view  = inflater.inflate(R.layout.dialog_user_express, null);
		builder.setView(view);
		tvDiaLock = (TextView) view.findViewById(R.id.textView_dialog_user_express_lock) ;
		tvDiaCompany = (TextView) view.findViewById(R.id.textView_dialog_user_express_post_company) ;
		tvDiaCompanyType = (TextView) view.findViewById(R.id.textView_dialog_user_express_post_company_type) ;
		tvDiaCompanyType.setText("收件人电话:");
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
		//计时器
		timerPost = new Timer();
		timerPost.schedule(new TimerTask() {
			
			@Override
			public void run() {
				if(timeout > 0){
					//刷新计时
					mHandler.post(new Runnable() {
						public void run() {
							tvDialogTime.setText("" + timeout);
						}
					});
					
				}else{
					//对话框取消,计时取消
					mHandler.post(new Runnable() {
						public void run() {
							//取消对话框
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

		builder.setPositiveButton("确认投递", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//用户寄存保存
				httpHelper.saveUserPost(mApp.getUser(), mApp.getPassword(), sysid, new HttpCallBack() {
					
					@Override
					public void call(Object obj, final String err) {
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								//保存成功
								if(err.equals(TipsHttpError.OK)){
									
									FamilyPostActivity.super.ToastInfo("寄存成功");
									Log.e("error--- ", "OK");
									editLetter.setText("");
									btnSelectBox.setText("点击选择箱子") ;
									//修改箱门状态,0正常，1箱门锁定， 3箱门故障
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
		builder.setNegativeButton("取消", new  DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				//取消计时
				timerPost.cancel();
				
			}
			
		});
		mdialog = builder.create() ;
		mdialog.show() ;
	}
}
