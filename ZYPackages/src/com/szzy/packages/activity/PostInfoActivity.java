package com.szzy.packages.activity;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Layout;
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

import com.szzy.packages.R;
import com.szzy.packages.base.MBaseActivity;
import com.szzy.packages.entity.PostBoxInfo;
import com.szzy.packages.http.HttpHelper;
import com.szzy.packages.http.OpenBoxCall;
import com.szzy.packages.http.QueryBoxCall;
import com.szzy.packages.tool.TipsHttpError;

//填写投递信息
public class PostInfoActivity extends Activity implements OnClickListener{
	private final static int SCANNIN_ORDER_CODE = 1;  //扫描快递单
	
	private final static int SCANNIN_LOCK_CODE = 11;   //扫描柜箱编号
	private EditText editOrder ;
	private EditText editLock ;
	private ImageView imgSysOrder ;//扫一扫快递单号
	private ImageView imgSysLock ;//扫一扫柜门号
	private EditText editTel ;
	private Button btnSelectBox ;
	private Button btnOpenBox ;//开箱投递
	private ImageView imgBack ;//返回
	
	private MApplication mApp ;
	private HttpHelper httpHelper ;//HTTP 请求处理
	private Handler mHandler = new Handler();
	
	private String lockCode = null;//快递柜编号
	private String orderNo = null;//快递单号
	private String tel = null;//收件人电话
	private String boxId = null;//快递箱号
	
	private boolean openBox = false ;//开箱标志
	private boolean selectFlag = false ;//是否选择箱柜，作为开箱的标志
	private String lockName = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_postinfo);
		super.onCreate(savedInstanceState);
//		Intent dataIntent = getIntent();
//		String result = dataIntent.getStringExtra("result");
		mApp = (MApplication) getApplication();
		lockCode = mApp.getLockId();
//		String result = mApp.getLockId();
		httpHelper = new HttpHelper();
		Log.e("", "" +lockCode);
		
		initView();

	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	public void initView() {
		editOrder = (EditText) findViewById(R.id.editText_post_order);
		imgSysOrder = (ImageView) findViewById(R.id.imageView_post_order_sys);
		imgSysLock = (ImageView) findViewById(R.id.imageView_post_lock_sys) ;
		editTel = (EditText) findViewById(R.id.editText_post_recv_tel);
		btnSelectBox = (Button) findViewById(R.id.button_post_select_box);
		editLock = (EditText) findViewById(R.id.editText_post_lock);
		btnOpenBox = (Button) findViewById(R.id.button_post_commit);
		imgBack = (ImageView) findViewById(R.id.imageButton_postinfo_back);
		
		imgBack.setOnClickListener(this);
		btnOpenBox.setOnClickListener(this);
		imgSysOrder.setOnClickListener(this);
		btnSelectBox.setOnClickListener(this);
		imgSysLock.setOnClickListener(this) ;
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
					editTel.setSelection(tempSelection);
				}
				
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.e("onActivityResult", "requestCode = " + requestCode + "resultCode = " + resultCode);
		switch (requestCode) {
		case SCANNIN_LOCK_CODE://返回箱柜编号
			if(resultCode == RESULT_OK){
				Log.e("lock", ""+ mApp.getLockId()) ;
				lockCode = mApp.getLockId().trim();
				try{
					int lock = Integer.valueOf(lockCode) ;
					//查询箱子状态
					httpHelper.queryBoxInfo(mApp.getUser(), mApp.getPassword(), lock, new QueryBoxCall() {
					
					@Override
					public void call( PostBoxInfo boxInfo) {
						if(boxInfo!= null){
							mApp.setListBox( boxInfo.getListBox());
							 lockName = boxInfo.getLockName();
							 selectFlag = true ; 
							//更新柜名称
							mHandler.post(new Runnable() {
								
								@Override
								public void run() {
									editLock.setText(lockName);
								}
							});
						}
						
					}
				});
				}catch(Exception e){
					Log.e("",e.toString());
				}
				
				//查询箱子状态
//				httpHelper.queryBoxInfo(mApp.getUser(), mApp.getPassword(), lock, new QueryBoxCall() {
//					
//					@Override
//					public void call( PostBoxInfo boxInfo) {
//						if(boxInfo!= null){
//							mApp.setListBox( boxInfo.getListBox());
//							 lockName = boxInfo.getLockName();
//							//更新柜名称
//							mHandler.post(new Runnable() {
//								
//								@Override
//								public void run() {
//									editLock.setText(lockName);
//								}
//							});
//						}
//						
//					}
//				});
			}
			break;
		case SCANNIN_ORDER_CODE://返回扫描结果
			if(resultCode == RESULT_OK){
				String orderId = data.getExtras().getString("barcode");
				if(orderId != null){
					editOrder.setText(orderId);
				}
			}
			break;
		case SELECT_BOX_REQUEST_CODE://选择快递箱号
			if(resultCode == RESULT_OK){
				int position = data.getIntExtra("position", 0);
				boxName = mApp.getListBox().get(position).getBoxName();
				btnSelectBox.setText(boxName);
				boxId = mApp.getListBox().get(position).getBoxid();
			}
			break;

		default:
			break;
		}
		
	}
	

	private final int SELECT_BOX_REQUEST_CODE = 2 ;
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageButton_postinfo_back:
			finish();
			break;
		case R.id.imageView_post_order_sys://扫描快递单
			Intent intent = new Intent(PostInfoActivity.this, MipcaActivityCapture.class);
			intent.putExtra("mode", 0);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivityForResult(intent, SCANNIN_ORDER_CODE);
			break;
		case R.id.imageView_post_lock_sys://扫描快递柜
			Intent intentLock = new Intent(PostInfoActivity.this, MipcaActivityCapture.class);
			intentLock.putExtra("mode", 1);
			intentLock.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivityForResult(intentLock, SCANNIN_LOCK_CODE);
			break;
		case R.id.button_post_select_box://选择箱号
			//选择箱子之前要先判断下是否扫描了箱柜
			if(selectFlag){
				Intent toSelect = new Intent(PostInfoActivity.this, SelectBoxActivity.class);
				startActivityForResult(toSelect, SELECT_BOX_REQUEST_CODE);
			}else{
				Toast.makeText(mApp, "请先扫描箱柜编号后，再操作", 0).show();
			}
			break;
		case R.id.button_post_commit:
			orderNo = editOrder.getText().toString().trim();
			tel = editTel.getText().toString().trim();
			if(orderNo == null || orderNo.length() < 1){
				Toast.makeText(mApp, "请输入快递单号", 0).show();
				return;
			}
			if(tel == null || tel.length() < 1){
				Toast.makeText(mApp, "请输入收件人电话", 0).show();
				return;
			}
			if(boxId == null || boxId.length() < 1){
				Toast.makeText(mApp, "请选择快递柜箱子", 0).show();
				return;
			}
			//开箱
//			if(!openBox){
//				openBox = true ;
//				
				httpHelper.openBox(mApp.getUser(), mApp.getPassword(), Integer.valueOf(lockCode), boxId, new OpenBoxCall() {
					
					@Override
					public void call(final String errorCode) {
						Log.e("errorCode----", errorCode);
						mHandler.post(new Runnable() {
							
							@Override
							public void run() {
								if("0".equals(errorCode)){
									createDialog();
								}
								TipsHttpError.toastError(mApp, errorCode);
								
							}
						});
						
						
					}
				});
//			}
			break;

		default:
			break;
		}
	}
	
	String boxName = "";
	//创建确认对话框
	private void createDialog(){
		Builder builder = new Builder(this);
		LayoutInflater inflater = LayoutInflater.from(this);
		View view  = inflater.inflate(R.layout.dialog_post_info, null);
		builder.setView(view);
		TextView tvDialogLock = (TextView) view.findViewById(R.id.textView_dialog_lockname);
		TextView tvDialogOrder = (TextView) view.findViewById(R.id.textView_dialog_orderno);
		TextView tvDialogTel = (TextView) view.findViewById(R.id.textView_dialog_tel);
		TextView tvDialogBoxName = (TextView) view.findViewById(R.id.textView_dialog_boxname);
		tvDialogLock.setText(lockName);
		tvDialogOrder.setText(orderNo);
		tvDialogTel.setText(tel);
		tvDialogBoxName.setText(boxName);
		builder.setPositiveButton("确认投递", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				openBox = false ;
				//提交
				httpHelper.postPackages(mApp.getUser(), mApp.getPassword(),
						Integer.valueOf(lockCode), boxId, tel, orderNo, new OpenBoxCall() {
					
					@Override
					public void call(String errorCode) {
						Log.e("55", errorCode);
						if("0".equals(errorCode.trim())){
							mHandler.post(new Runnable() {
								
								@Override
								public void run() {
									Toast.makeText(mApp, "投递成功", 0).show();
									openBox = false ;
									editOrder.setText("");
									editTel.setText("");
									btnSelectBox.setText("点击选择箱子");
									//更新箱子状态
									httpHelper.queryBoxInfo(mApp.getUser(), mApp.getPassword(), Integer.valueOf(lockCode), new QueryBoxCall() {
										
										@Override
										public void call( PostBoxInfo boxInfo) {
											if(boxInfo!= null){
												mApp.setListBox( boxInfo.getListBox());
												 lockName = boxInfo.getLockName();
												//更新柜名称
												mHandler.post(new Runnable() {
													
													@Override
													public void run() {
														editLock.setText(lockName);
													}
												});
											}
											
										}
									});
								}
							});
						}
						
					}
				});
				
			}
		});
		builder.setNegativeButton("取消", null);
		builder.create().show();
		
	}
	
}
