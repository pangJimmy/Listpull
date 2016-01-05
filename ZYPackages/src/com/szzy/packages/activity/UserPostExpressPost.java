package com.szzy.packages.activity;

import com.szzy.packages.R;
import com.szzy.packages.entity.PostBoxInfo;
import com.szzy.packages.http.HttpHelper;
import com.szzy.packages.http.OpenBoxCall;
import com.szzy.packages.http.QueryBoxCall;
import com.szzy.packages.tool.TipsHttpError;
import com.szzy.packages.tool.Utils;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 业主快递寄存
 * @author mac
 *
 */
public class UserPostExpressPost extends Activity implements OnClickListener{

	private TextView tvTitle ;  //标题
	private ImageView imgBack ; //返回
	
	private EditText editLock ;//箱柜号
	private ImageView imgSys ;//扫一扫
	private TextView tvCompany ; //快递公司
	private GridView gridCompany ;//快递公司图标选择
	//邮政、顺丰、德邦、ups 、韵达、速尔、申通、联邦、中通
	private int[] imgs = new int[]{R.drawable.post_zgyz, R.drawable.post_sf, 
			R.drawable.post_debang, R.drawable.post_ups, 
			R.drawable.post_yunda, R.drawable.post_suer,
			R.drawable.post_sto, R.drawable.post_fedex, 
			R.drawable.post_zto};
	private Button btnSelectBox ; //选择箱号
	private Button btnOpenBox ; //开箱投递
	
	private PostCompanyAdapter adapter ;
	
	private HttpHelper httpHelper ;//HTTP 请求处理
	private Handler mHandler = new Handler();
	
	private boolean isSelectLock ;//是否扫描了箱柜
	private final static int SCANNIN_LOCK_CODE = 11;   //扫描柜箱编号
	
	private MApplication mApp ;
	private String lockCode ;  //箱柜编号
	private String lockName ;  //箱柜名称
	private int postTel = 0 ;
	
	private String boxName  ; //箱门号
	private String boxId ; //箱门ID 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_express_post) ;
		mApp = (MApplication) getApplication() ;
		httpHelper = new HttpHelper();
		initView() ;
	}
	
	private void initView(){
		tvTitle = (TextView) findViewById(R.id.textView_title) ;
		//设置标题
		tvTitle.setText("快递待收") ;
		imgBack = (ImageView) findViewById(R.id.imageButton_back) ;
		imgBack.setOnClickListener(this) ;
		//添加快递公司
		gridCompany = (GridView) findViewById(R.id.gridView_user_post_company) ;
		adapter = new PostCompanyAdapter() ;
		gridCompany.setAdapter(adapter);
		
		editLock = (EditText) findViewById(R.id.editText_user_express_post_lock) ;
		imgSys = (ImageView) findViewById(R.id.imageView_user_express_post_lock_sys) ;
		btnSelectBox = (Button) findViewById(R.id.button_user_express_post_select_box) ;
		btnOpenBox = (Button) findViewById(R.id.button_user_express_commit) ;
		tvCompany = (TextView) findViewById(R.id.textView_user_express_company) ;
		//监听点击
		imgSys.setOnClickListener(this);
		btnSelectBox.setOnClickListener(this);
		btnOpenBox.setOnClickListener(this);
		gridCompany.setOnItemClickListener(new GridItemClick());
		
	}
	
	private final int SELECT_BOX_REQUEST_CODE = 2 ;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
							 isSelectLock = true ; 
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
			break ;
		case SELECT_BOX_REQUEST_CODE://选择快递箱号
			if(resultCode == RESULT_OK){
				int position = data.getIntExtra("position", 0);
				boxName = mApp.getListBox().get(position).getBoxName();
				btnSelectBox.setText(boxName);
				boxId = mApp.getListBox().get(position).getBoxid();
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	//快递公司点击图标选择
	private class GridItemClick implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			//电话从1开始到9
			postTel = position + 1 ;
			switch (position) {
			case 0:
				tvCompany.setText("中国邮政");
				break;
			case 1:
				tvCompany.setText("顺丰速运");
				break;
			case 2:
				tvCompany.setText("德邦物流");
				break;
			case 3:
				tvCompany.setText("UPS");
				break;
			case 4:
				tvCompany.setText("韵达快递");
				break;
			case 5:
				tvCompany.setText("速尔快递");
				break;
			case 6:
				tvCompany.setText("申通快递");
				break;
			case 7:
				tvCompany.setText("联邦快递");
				break;
			case 8:
				tvCompany.setText("中通快递");
				break;
				

			default:
				break;
			}
			
		}
		
	}
	
	//gridView适配器
	private class PostCompanyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return imgs.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return imgs[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHold hold = null ;
			if(convertView == null){
				hold = new ViewHold() ;
				convertView = LayoutInflater.from(UserPostExpressPost.this).inflate(R.layout.gridview_item_post_company, null);
				hold.img = (ImageView) convertView.findViewById(R.id.imageView_gridview_item_post_company) ;
				convertView.setTag(hold) ;
			}else{
				hold = (ViewHold) convertView.getTag() ;
			}
			//填充图片
			hold.img.setImageResource(imgs[position]) ;
			return convertView;
		}
		
		private class ViewHold{
			ImageView img ;
		}
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageButton_back://返回
			finish() ;
			break;
		case R.id.imageView_user_express_post_lock_sys://扫一扫
			Intent intent = new Intent(UserPostExpressPost.this, MipcaActivityCapture.class);
			intent.putExtra("mode", 1);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivityForResult(intent, SCANNIN_LOCK_CODE);
			break;
		case R.id.button_user_express_post_select_box://选择箱门
			//选择箱子之前要先判断下是否扫描了箱柜
			if(isSelectLock){
				Intent toSelect = new Intent(UserPostExpressPost.this, SelectBoxActivity.class);
				startActivityForResult(toSelect, SELECT_BOX_REQUEST_CODE);
			}else{
				Toast.makeText(getApplicationContext(), "请先扫描箱柜", 0).show() ;
			}
			break;
		case R.id.button_user_express_commit://开箱投递
			Utils.tipsUnfinish(this) ;
//			if(postTel == 0){
//				Toast.makeText(mApp, "请选择快递公司", 0).show() ;
//				return ; 
//			}
//			if(!isSelectLock){
//				Toast.makeText(getApplicationContext(), "请先扫描箱柜", 0).show() ;
//				return ;
//			}
//			if(boxId == null || boxId.length() < 1){
//				Toast.makeText(mApp, "请选择快递柜箱子", 0).show();
//				return;
//			}
//			httpHelper.openBox(mApp.getUser(), mApp.getPassword(), Integer.valueOf(lockCode), boxId, new OpenBoxCall() {
//				
//				@Override
//				public void call(final String errorCode) {
//					Log.e("errorCode----", errorCode);
//					mHandler.post(new Runnable() {
//						
//						@Override
//						public void run() {
//							if("0".equals(errorCode)){
//								createDialog();
//							}
//							TipsHttpError.toastError(mApp, errorCode);
//							
//						}
//					});
//					
//					
//				}
//			});
			
			break;

		default:
			break;
		}
		
	}
	
	private TextView tvDiaLock ;
	private TextView tvDiaCompany  ;
	private TextView tvDiaBox ;
	
	//创建对话框
	private void createDialog(){
		Builder builder = new Builder(this);
		LayoutInflater inflater = LayoutInflater.from(this);
		View view  = inflater.inflate(R.layout.dialog_user_express, null);
		builder.setView(view);
		tvDiaLock = (TextView) view.findViewById(R.id.textView_dialog_user_express_lock) ;
		tvDiaCompany = (TextView) view.findViewById(R.id.textView_dialog_user_express_post_company) ;
		tvDiaBox = (TextView) view.findViewById(R.id.textView_dialog_user_express_boxname) ;
		tvDiaLock.setText(lockName);
		tvDiaCompany.setText(tvCompany.getText().toString());
		tvDiaBox.setText(boxName);
		builder.setPositiveButton("确认投递", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.e("TEL", postTel + "") ;
				String tel = "0000000000" + postTel ;
				//提交
				httpHelper.postPackages(mApp.getUser(), mApp.getPassword(),
						Integer.valueOf(lockCode), boxId, tel, "", new OpenBoxCall() {
					
					@Override
					public void call(String errorCode) {
						Log.e("55", errorCode);
						if("0".equals(errorCode.trim())){
							mHandler.post(new Runnable() {
								
								@Override
								public void run() {
									Toast.makeText(mApp, "投递成功", 0).show();
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
		builder.setNegativeButton("取消", null) ;
		builder.create().show() ;
	}
}
