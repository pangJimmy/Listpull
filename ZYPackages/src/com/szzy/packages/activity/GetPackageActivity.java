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
 * ȡ��ǩ�ս���
 * @author mac
 *
 */
public class GetPackageActivity extends Activity implements OnClickListener{
	private TextView tvTitleNotRecv;// δǩ��
	private TextView tvTitleRecv;// ��ǩ��
	private TextView tvTitleNotRecvBar;// δǩ��bar
	private TextView tvTitleRecvBar;// ��ǩ��bar
	
	private ImageView imgBack ;//����
	
	private PullToRefreshLayout ptrl;// ����ˢ�¿ؼ�
	private PullableListView listViewData;
//	private NotGetPackageListViewAdapter adapter;// listview������
	
	
	private HttpHelper httpHelper;
	private MApplication mApp;
	
	private List<PostPackages> listPost = new ArrayList<PostPackages>(); //Ͷ����ѯ��¼
	private List<GetPackages> listGet = new ArrayList<GetPackages>();  //ȡ����ѯ��¼
	
	private NotGetPackageListViewAdapter getAdapter ;//ȡ����ѯ������
	
	
	private GetPackageQueryAdapter adapter ;//��ѯ����������
	private int MODE_ALL = 0;// ��ѯ���У�����δȡ���Ѿ�ȡ

	private boolean first = true; // �Ƿ��״ν������

	private MyReceiver mReceiver; // �㲥�����ߣ����ڸ���ȡ�������ݵ�ˢ��
	private final int MSG_UPDATE = 1010 ;//����listview��Ϣ
	
	private List<UserGetRecord> listRecord = null ; //�û���¼
	private Handler mHandler = new Handler(){// ��Ϣ�����������ڸ���http������ʾ
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_UPDATE://����listview��Ϣ
//				synchronized (listViewData) {
//					Toast.makeText(GetPackageActivity.this, "���ݸ��³ɹ�", 0).show();
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
	//��ʼ��UI 
	private void initView(){
		imgBack = (ImageView) findViewById(R.id.imageButton_back) ;
		tvTitleNotRecv = (TextView)findViewById(R.id.textView_get_not_receive);
		tvTitleNotRecv.setText("�ļ���¼");
		tvTitleRecv = (TextView) findViewById(R.id.textView_get_received);
		tvTitleRecv.setText("ȡ����¼");
		tvTitleNotRecvBar = (TextView) findViewById(R.id.textView_get_not_receive_bar);
		tvTitleRecvBar = (TextView) findViewById(R.id.textView_get_received_bar);
		ptrl = (PullToRefreshLayout) findViewById(R.id.listView_frag_get_data);
		listViewData = (PullableListView) ptrl.getPullableView();
		//��������ˢ��
		ptrl.setPullUpEnable(false) ;
		// ��������
		ptrl.setOnPullListener(new PullToRefreshLayout.OnPullListener() {

			@Override
			public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
//				sendHttp();//����http����
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
		//�Զ�ˢ��
		ptrl.autoRefresh() ;
		tvTitleNotRecv.setOnClickListener(this) ;
		tvTitleRecv.setOnClickListener(this) ;
		imgBack.setOnClickListener(this) ;
	}
	
	//����http�������listview
	private synchronized void sendHttp(){
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageButton_back://����
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
	 * ˢ�²�ѯ����
	 */
	private  void refreshRecord(){
		if(!isrefreshRecord){
			isrefreshRecord = true ;
			//ȡ����¼
			httpHelper.queryUserGet(mApp.getUser(), mApp.getPassword(), MODE_ALL, new HttpCallBack() {
				
				@Override
				public void call(Object obj, final String errorCode) {
					listRecord = (List<UserGetRecord>) obj ;
					mHandler.post(new Runnable() {
						
						@Override
						public void run() {
							//ȡ��ˢ��
//							ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
							ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
							
							//��ʾ������Ϣ
							TipsHttpError.toastError(mApp, errorCode);
							//ˢ��ListView
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
	
	//�㲥������,����ˢ����Ϣ
	private class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.e("onReceive", "onReceive");
			// ˢ��Ͷ����Ϣ
			sendHttp() ;
		}

	}
}
