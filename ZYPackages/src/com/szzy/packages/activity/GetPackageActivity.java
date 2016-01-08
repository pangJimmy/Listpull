package com.szzy.packages.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.PullableListView;
import com.szzy.packages.R;
import com.szzy.packages.adapter.GetPackageQueryAdapter;
import com.szzy.packages.adapter.NotGetPackageListViewAdapter;
import com.szzy.packages.entity.GetPackages;
import com.szzy.packages.entity.PostPackages;
import com.szzy.packages.entity.UserGetRecord;
import com.szzy.packages.http.HttpCallBack;
import com.szzy.packages.http.HttpHelper;
import com.szzy.packages.tool.TipsHttpError;
/**
 * 取件签收界面
 * @author mac
 *
 */
public class GetPackageActivity extends Activity implements OnClickListener{
	private TextView tvTitleNotRecv;// 未签收
	private TextView tvTitleRecv;// 已签收
	private TextView tvTitleNotRecvBar;// 未签收bar
	private TextView tvTitleRecvBar;// 已签收bar
	
	private ImageView imgBack ;//返回
	
	private PullToRefreshLayout ptrl;// 下拉刷新控件
	private PullableListView listViewData;
//	private NotGetPackageListViewAdapter adapter;// listview适配器
	
	
	private HttpHelper httpHelper;
	private MApplication mApp;
	
	private List<PostPackages> listPost = new ArrayList<PostPackages>(); //投件查询记录
	private List<GetPackages> listGet = new ArrayList<GetPackages>();  //取件查询记录
	
	private NotGetPackageListViewAdapter getAdapter ;//取件查询适配器
	
	
	private GetPackageQueryAdapter adapter ;//查询数据适配器
	private int MODE_ALL = 0;// 查询所有，包括未取和已经取

	private boolean first = true; // 是否首次进入程序

	private MyReceiver mReceiver; // 广播接收者，用于更新取件后数据的刷新
	private final int MSG_UPDATE = 1010 ;//更新listview信息
	
	private List<UserGetRecord> listRecord = null ; //用户记录
	private Handler mHandler = new Handler(){// 消息处理器，用于给出http错误提示
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_UPDATE://更新listview信息
//				synchronized (listViewData) {
//					Toast.makeText(GetPackageActivity.this, "数据更新成功", 0).show();
//					if(MODE_ALL == 1){
////						listViewData.setAdapter(postAdapter);
//					}else{
//						listViewData.setAdapter(getAdapter);
//					}
//				}
				break;

			default:
				break;
			}
		};
	}; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_package) ;
		mApp = (MApplication) getApplication();
		httpHelper = new HttpHelper() ;
		mReceiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("update");
		registerReceiver(mReceiver, filter);
		initView() ;
	}
	
	@Override
	protected void onDestroy() {
		unregisterReceiver(mReceiver);
		super.onDestroy();
	}
	//初始化UI 
	private void initView(){
		imgBack = (ImageView) findViewById(R.id.imageButton_back) ;
		tvTitleNotRecv = (TextView)findViewById(R.id.textView_get_not_receive);
		tvTitleNotRecv.setText("寄件记录");
		tvTitleRecv = (TextView) findViewById(R.id.textView_get_received);
		tvTitleRecv.setText("取件记录");
		tvTitleNotRecvBar = (TextView) findViewById(R.id.textView_get_not_receive_bar);
		tvTitleRecvBar = (TextView) findViewById(R.id.textView_get_received_bar);
		ptrl = (PullToRefreshLayout) findViewById(R.id.listView_frag_get_data);
		listViewData = (PullableListView) ptrl.getPullableView();
		//不可上拉刷新
		ptrl.setPullUpEnable(false) ;
		// 下拉监听
		ptrl.setOnPullListener(new PullToRefreshLayout.OnPullListener() {

			@Override
			public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
//				sendHttp();//发送http请求
				refreshRecord() ;
			}

			@Override
			public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
				new Handler().post(new Runnable() {

					@Override
					public void run() {
						ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);

					}
				});

			}
		});
		//自动刷新
		ptrl.autoRefresh() ;
		tvTitleNotRecv.setOnClickListener(this) ;
		tvTitleRecv.setOnClickListener(this) ;
		imgBack.setOnClickListener(this) ;
	}
	
	//发送http请求更新listview
	private synchronized void sendHttp(){
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageButton_back://返回
			finish() ;
			break;
		case R.id.textView_get_not_receive:
			break;
		case R.id.textView_get_received:
			tvTitleNotRecvBar.setBackgroundColor(this.getResources()
					.getColor(R.color.title_bg));
			tvTitleRecvBar.setBackgroundColor(this.getResources().getColor(
					R.color.title_font_bg));
//			MODE_ALL = 2;
			// listData = listGet ;
			getAdapter = new NotGetPackageListViewAdapter(mApp, this, listGet);
			listViewData.setAdapter(getAdapter);
			break;

		default:
			break;
		}
		
	}
	
	private boolean isrefreshRecord = false ;
	/**
	 * 刷新查询请求
	 */
	private  void refreshRecord(){
		if(!isrefreshRecord){
			isrefreshRecord = true ;
			//取件记录
			httpHelper.queryUserGet(mApp.getUser(), mApp.getPassword(), MODE_ALL, new HttpCallBack() {
				
				@Override
				public void call(Object obj, final String errorCode) {
					listRecord = (List<UserGetRecord>) obj ;
					mHandler.post(new Runnable() {
						
						@Override
						public void run() {
							//取消刷新
//							ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
							ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
							
							//提示错误信息
							TipsHttpError.toastError(mApp, errorCode);
							//刷新ListView
							if(listRecord != null){
								adapter = new GetPackageQueryAdapter(mApp, GetPackageActivity.this, listRecord);
								listViewData.setAdapter(adapter);
							}
							isrefreshRecord = false ;
						}
					});
					
				}
			});
		}
	}
	
	//广播接受者,用于刷新信息
	private class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.e("onReceive", "onReceive");
			// 刷新投递信息
			sendHttp() ;
		}

	}
}
