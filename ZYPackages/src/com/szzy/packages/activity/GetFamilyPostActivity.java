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
 * �Ĵ�ȡ��
 * @author mac
 *
 */
public class GetFamilyPostActivity extends NormalBaseActivity {

	private PullToRefreshLayout ptrl;// ����ˢ�¿ؼ�
	private PullableListView listViewData;  //listview������ʾ����
	private HttpHelper httpHelper;
	private MApplication mApp;  //ȡȫ�ֱ���
	
	private Handler mHandler = new Handler();//��Ϣ������
	private MyReceiver mReceiver; // �㲥�����ߣ����ڸ���ȡ�������ݵ�ˢ��
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_get_family_post) ;
		//���ñ���
		super.setTitle("�Ĵ�ȡ��");
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
	//��ʼ��UI
	private void initView() {
		//�����ؼ�
		ptrl = (PullToRefreshLayout) findViewById(R.id.listView_get_family_post_data);
		listViewData = (PullableListView) ptrl.getPullableView();
		//��������ˢ��
		ptrl.setPullUpEnable(false) ;
		// ��������
		ptrl.setOnPullListener(new PullToRefreshLayout.OnPullListener() {

			@Override
			public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
				//ˢ�¼�¼
				refreshRecord() ;
				
			}

			@Override
			public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
				// TODO Auto-generated method stub
				
			}
					
		});
		//�Զ�ˢ��
		ptrl.autoRefresh() ;
	}
	
	private boolean isrefreshRecord = false ;//��ֹ��δ����ʱ����η���ͬ��������
	private static int QUERY_ALL = 2 ;  //0δ�ҼĴ�ģ�1�Ĵ���ҵģ�2 ����
	private List<UserPostRecord> listRecord ;
	//ˢ�¼�¼
	protected void refreshRecord() {
		if(!isrefreshRecord){
			isrefreshRecord = true ;
			//�������󣬲�ѯ�Ĵ��¼,��ѯģʽΪ��ѯ����
			httpHelper.queryUserPostGet(mApp.getUser(), mApp.getPassword(), QUERY_ALL, new HttpCallBack() {
				
				@Override
				public void call(final Object obj, final String err) {
					isrefreshRecord = false ;
					mHandler.post(new Runnable() {
						
						@Override
						public void run() {
							//ȡ��ˢ��
							ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
							//��ѯ�ɹ�
							if(err.equals(TipsHttpError.OK)){
								listRecord = (List<UserPostRecord>) obj ;
								//�м�¼
								if(listRecord != null && !listRecord.isEmpty()){
									listViewData.setAdapter(new FamilyQueryAdapter(mApp, GetFamilyPostActivity.this, listRecord));
								}else{
									//�޼�¼Ӧ����
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
	
	//�㲥������,����ˢ����Ϣ
	private class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.e("onReceive", "onReceive");
			// ˢ��Ͷ����Ϣ
			refreshRecord();
		}

	}

}
