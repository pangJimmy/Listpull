package com.szzy.packages.activity;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.PullableListView;
import com.szzy.packages.R;
import com.szzy.packages.adapter.FamilyQueryAdapter;
import com.szzy.packages.base.NormalBaseActivity;
import com.szzy.packages.entity.UserPostRecord;
import com.szzy.packages.http.HttpCallBack;
import com.szzy.packages.http.HttpHelper;
import com.szzy.packages.tool.TipsHttpError;
/**
 * 寄存取件
 * @author mac
 *
 */
public class GetFamilyPostActivity extends NormalBaseActivity {

	private PullToRefreshLayout ptrl;// 下拉刷新控件
	private PullableListView listViewData;  //listview用于显示数据
	private HttpHelper httpHelper;
	private MApplication mApp;  //取全局变量
	
	private Handler mHandler = new Handler();//消息处理器
	private MyReceiver mReceiver; // 广播接收者，用于更新取件后数据的刷新
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_get_family_post) ;
		//设置标题
		super.setTitle("寄存取件");
		mApp = (MApplication) getApplication();
		httpHelper = new HttpHelper() ;
		mReceiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("update");
		registerReceiver(mReceiver, filter);
		initView();
	}
	
	@Override
	protected void onDestroy() {
		unregisterReceiver(mReceiver);
		super.onDestroy();
	}
	//初始化UI
	private void initView() {
		//下拉控件
		ptrl = (PullToRefreshLayout) findViewById(R.id.listView_get_family_post_data);
		listViewData = (PullableListView) ptrl.getPullableView();
		//不可上拉刷新
		ptrl.setPullUpEnable(false) ;
		// 下拉监听
		ptrl.setOnPullListener(new PullToRefreshLayout.OnPullListener() {

			@Override
			public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
				//刷新记录
				refreshRecord() ;
				
			}

			@Override
			public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
				// TODO Auto-generated method stub
				
			}
					
		});
		//自动刷新
		ptrl.autoRefresh() ;
	}
	
	private boolean isrefreshRecord = false ;//防止在未返回时，多次发送同样的请求
	private static int QUERY_ALL = 2 ;  //0未我寄存的，1寄存给我的，2 所有
	private List<UserPostRecord> listRecord ;
	//刷新记录
	protected void refreshRecord() {
		if(!isrefreshRecord){
			isrefreshRecord = true ;
			//发送请求，查询寄存记录,查询模式为查询所有
			httpHelper.queryUserPostGet(mApp.getUser(), mApp.getPassword(), QUERY_ALL, new HttpCallBack() {
				
				@Override
				public void call(final Object obj, final String err) {
					isrefreshRecord = false ;
					mHandler.post(new Runnable() {
						
						@Override
						public void run() {
							//取消刷新
							ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
							//查询成功
							if(err.equals(TipsHttpError.OK)){
								listRecord = (List<UserPostRecord>) obj ;
								//有记录
								if(listRecord != null && !listRecord.isEmpty()){
									listViewData.setAdapter(new FamilyQueryAdapter(mApp, GetFamilyPostActivity.this, listRecord));
								}else{
									//无记录应处理
								}
							}else{
								TipsHttpError.toastError(mApp, err) ;
							}
							
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
			refreshRecord();
		}

	}

}
