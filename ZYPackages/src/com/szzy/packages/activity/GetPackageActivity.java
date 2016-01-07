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
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.szzy.packages.R;
import com.szzy.packages.adapter.NotGetPackageListViewAdapter;
import com.szzy.packages.adapter.PostPackageListViewAdapter;
import com.szzy.packages.entity.GetPackages;
import com.szzy.packages.entity.MailBox;
import com.szzy.packages.entity.PostPackages;
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
	private ListView listViewData;
	private NotGetPackageListViewAdapter adapter;// listview������
	
	
	private HttpHelper httpHelper;
	private MApplication mApp;
	
	private List<PostPackages> listPost = new ArrayList<PostPackages>(); //Ͷ����ѯ��¼
	private List<GetPackages> listGet = new ArrayList<GetPackages>();  //ȡ����ѯ��¼
	
	private PostPackageListViewAdapter postAdapter ;// Ͷ����ѯ������
	private NotGetPackageListViewAdapter getAdapter ;//ȡ����ѯ������
	private int MODE_ALL = 0;// ��ѯ���У�����δȡ���Ѿ�ȡ

	private boolean first = true; // �Ƿ��״ν������

	private MyReceiver mReceiver; // �㲥�����ߣ����ڸ���ȡ�������ݵ�ˢ��
	private final int MSG_UPDATE = 1010 ;//����listview��Ϣ
	
	
	private Handler mHandler = new Handler(){// ��Ϣ�����������ڸ���http������ʾ
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_UPDATE://����listview��Ϣ
				synchronized (listViewData) {
					Toast.makeText(GetPackageActivity.this, "���ݸ��³ɹ�", 0).show();
					if(MODE_ALL == 1){
						listViewData.setAdapter(postAdapter);
					}else{
						listViewData.setAdapter(getAdapter);
					}
				}
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
		listViewData = (ListView) ptrl.getPullableView();
		
		//ȡ����¼
		httpHelper.queryUserGet(mApp.getUser(), mApp.getPassword(), MODE_ALL, new HttpCallBack() {
			
			@Override
			public void call(Object obj, String err) {
				
				
			}
		});
//		httpHelper.queryPackage(mApp.getUser(), mApp.getPassword(), 0, new HttpCallBack() {
//			
//			@Override
//			public void call(Object obj, String err) {
//				//���󷵻�
//				if(!"0".equals(err)){
//					TipsHttpError.toastError(mApp, err);
//					return; 
//				}
//				listGet = (List<GetPackages>) obj ;
//				mHandler.post(new Runnable() {
//
//					@Override
//					public void run() {
//						ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
//						//��ȡ
//						getAdapter = new NotGetPackageListViewAdapter(mApp, GetPackageActivity.this, listGet);
////						postAdapter = new PostPackageListViewAdapter(mApp, GetPackageActivity.this, listPost);
//						Message msg = new Message() ;
//						msg.what = MSG_UPDATE;
//						mHandler.sendMessage(msg) ;
//					}
//				});
//				
//			}
//		});
		// ��������
		ptrl.setOnPullListener(new PullToRefreshLayout.OnPullListener() {

			@Override
			public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
				sendHttp();//����http����
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

		tvTitleNotRecv.setOnClickListener(this) ;
		tvTitleRecv.setOnClickListener(this) ;
		imgBack.setOnClickListener(this) ;
	}
	
	//����http�������listview
	private synchronized void sendHttp(){
//		if(MODE_ALL == 1){
//			//Ͷ�ݼ�¼
//			httpHelper.queryPostPackages(mApp.getUser(), mApp.getPassword(), 0, new HttpCallBack() {
//				
//				@Override
//				public void call(Object obj, String err) {
//					//���󷵻�
//					if(!"0".equals(err)){
//						TipsHttpError.toastError(mApp, err);
//						return; 
//					}
////					notListPost = (List<PostPackages>) obj ;
//					listPost = (List<PostPackages>) obj ;
//					
//					mHandler.post(new Runnable() {
//
//						@Override
//						public void run() {
//							ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
//							//��ȡ
//							postAdapter = new PostPackageListViewAdapter(mApp, GetPackageActivity.this, listPost);
//							Message msg = new Message() ;
//							msg.what = MSG_UPDATE;
//							mHandler.sendMessage(msg) ;
//						}
//					});
//				}
//			});
//		}else{
//			//ȡ����¼
//			httpHelper.queryPackage(mApp.getUser(), mApp.getPassword(), 0, new HttpCallBack() {
//				
//				@Override
//				public void call(Object obj, String err) {
//					//���󷵻�
//					if(!"0".equals(err)){
//						TipsHttpError.toastError(mApp, err);
//						return; 
//					}
//					listGet = (List<GetPackages>) obj ;
//					mHandler.post(new Runnable() {
//
//						@Override
//						public void run() {
//							ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
//							//��ȡ
//							getAdapter = new NotGetPackageListViewAdapter(mApp, GetPackageActivity.this, listGet);
////							postAdapter = new PostPackageListViewAdapter(mApp, GetPackageActivity.this, listPost);
//							Message msg = new Message() ;
//							msg.what = MSG_UPDATE;
//							mHandler.sendMessage(msg) ;
//						}
//					});
//				}
//			});
//		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageButton_back://����
			finish() ;
			break;
		case R.id.textView_get_not_receive:
//			tvTitleNotRecvBar.setBackgroundColor(this.getResources()
//					.getColor(R.color.title_font_bg));
//			tvTitleRecvBar.setBackgroundColor(this.getResources().getColor(
//					R.color.title_bg));
//			mode = 1;
//			// listData = listNotGet ;
//			postAdapter = new PostPackageListViewAdapter(mApp, this,
//					listPost);
//			listViewData.setAdapter(postAdapter);
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
	
//	/**
//	 * ����������
//	 * @author mac
//	 *
//	 */
//	private class MailBoxAdapter extends BaseAdapter{
//
//		@Override
//		public int getCount() {
//			
//			return listMailBox.size();
//		}
//
//		@Override
//		public Object getItem(int position) {
//			
//			return listMailBox.get(position);
//		}
//
//		@Override
//		public long getItemId(int position) {
//			// TODO Auto-generated method stub
//			return position;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			ViewHolder holder = null ;
//			//�������
//			if(convertView == null){
//				holder = new ViewHolder() ;
//				convertView = LayoutInflater.from(GetPackageActivity.this).inflate(R.layout.listview_item_mail_box, null);
//				holder.tvBoxName = (TextView) convertView.findViewById(R.id.textView_listview_item_mail_box) ;
//				holder.btnOpen = (Button) convertView.findViewById(R.id.button_listview_item_mail_box) ;
//				convertView.setTag(holder);
//			}else{
//				holder = (ViewHolder) convertView.getTag() ;
//			}
//			holder.tvBoxName.setText(listMailBox.get(position).getMname()) ;
//			holder.btnOpen.setOnClickListener(new Myclick(position)) ;
//			return convertView;
//		}
//		
//		//������ʱ�洢����
//		class ViewHolder {
//			TextView tvBoxName ;
//			Button btnOpen ;
//		}
//		//�������俪��
//		class Myclick implements OnClickListener{
//			int position ;
//			public Myclick(int position){
//				this.position = position ;
//			}
//			@Override
//			public void onClick(View v) {
//				//������俪��Ĳ���
//				Log.e("item", "item---" + position) ;
//			}
//			
//		}
//		
//	}
	
	
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
