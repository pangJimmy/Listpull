package com.szzy.packages.fragment;

import java.util.ArrayList;
import java.util.List;

import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.szzy.packages.R;
import com.szzy.packages.activity.MApplication;
import com.szzy.packages.activity.MipcaActivityCapture;
import com.szzy.packages.activity.PostInfoActivity;
import com.szzy.packages.adapter.NotGetPackageListViewAdapter;
import com.szzy.packages.adapter.GetPackageQueryAdapter;
import com.szzy.packages.entity.GetPackages;
import com.szzy.packages.entity.PostPackages;
import com.szzy.packages.http.HttpCallBack;
import com.szzy.packages.http.HttpHelper;
import com.szzy.packages.http.QueryPostPackageCall;
import com.szzy.packages.tool.TipsHttpError;
import com.szzy.packages.view.MyListView;
import com.szzy.packages.view.MyListView.OnRefreshListener;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PostFragment extends Fragment implements OnClickListener {

	private Context context;
	private View rootView;

	private TextView tvTitleNotRecv;// δǩ��
	private TextView tvTitleRecv;// ��ǩ��
	private TextView tvTitleNotRecvBar;// δǩ��bar
	private TextView tvTitleRecvBar;// ��ǩ��bar

	private PullToRefreshLayout ptrl ;//����ˢ�¿ؼ�
	private ListView mListView ;
//	private MyListView mListView;// listView
	private int mode = 1;// ģʽ��1Ϊδǩ��ģʽ��2Ϊ��ǩ��ģʽ
	private HttpHelper httpHelper;// HTTP
	private MApplication mApp;
	private ImageView imgSaoyiSao;// ɨһɨ

	private List<PostPackages> notListPost = new ArrayList<PostPackages>();// δǩ��
	private List<PostPackages> listPost = new ArrayList<PostPackages>();
	private List<PostPackages> listData = new ArrayList<PostPackages>();
	GetPackageQueryAdapter adapter;// ������

	private Handler mHandler = new Handler();
	private MyReceiver myReceiver;// �㲥�����ߣ����ڽ���ˢ����Ϣ
	
	private boolean first = true ;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = getActivity();
		mApp = (MApplication) getActivity().getApplication();
		httpHelper = new HttpHelper();

		myReceiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("update");
		context.registerReceiver(myReceiver, filter);

	}

	@Override
	public void onDestroy() {
		context.unregisterReceiver(myReceiver);
		super.onDestroy();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_post, null);
		initView();
		return rootView;
	}

//
	private void initView() {

		Log.e("Mode ", "mode = " + mode);
		imgSaoyiSao = (ImageView) rootView.findViewById(R.id.img_frag_saoyisao);
		ptrl = (PullToRefreshLayout) rootView.findViewById(R.id.listView_frag_post_data);
		mListView = (ListView) ptrl.getPullableView();
		if(mode == 1){
//			adapter = new PostPackageListViewAdapter(mApp,context, notListPost);
		}else{
//			adapter = new PostPackageListViewAdapter(mApp,context, listPost);
		}
		ptrl.setOnPullListener(new PullToRefreshLayout.OnPullListener() {
			
			@Override
			public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
				new Handler() {
					@Override
					public void handleMessage(Message msg) {
						// ˢ�������Ŷ��
						ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
						Log.e("onRefresh", "refreshFinish++");
					}
				}.sendEmptyMessageDelayed(0, 5000);
				//����ˢ��
				httpHelper.queryPostPackages(mApp.getUser(),mApp.getPassword(), mode, new HttpCallBack() {
					
					@Override
					public void call(Object obj, String err) {
						//���󷵻�
						if(!"0".equals(err)){
							TipsHttpError.toastError(context, err);
							return; 
						}
						if(mode == 1){
							notListPost = (List<PostPackages>) obj;
						}else{
							//��ǩ�յ�����
							listPost = new ArrayList<PostPackages>();
							List<PostPackages> listTemp = (List<PostPackages>) obj;
							for(PostPackages post : listTemp){
								listPost.add(post);
							}
						}
						
						new AsyncTask<Void, Void, Void>() {
						protected Void doInBackground(
								Void... params) {

							// data.add("ˢ�º���ӵ�����");
							Log.e("onRefresh", "onRefresh");
							return null;
						}

						@Override
						protected void onPostExecute(Void result) {
							Log.e("onPostExecute", "onPostExecute");
							Toast.makeText(context, "���ݸ��³ɹ�", 0)
									.show();
//							adapter = new PostPackageListViewAdapter(
//									mApp, context, listData);
//							if(mode == 1){
//								adapter = new PostPackageListViewAdapter(
//										mApp, context, notListPost);
//							}else{
//								adapter = new PostPackageListViewAdapter(
//										mApp, context, listPost);
//							}
							mListView.setAdapter(adapter);
							//����ˢ��
							ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
						}

					}.execute();
						
					}
				});
				
			}
			
			@Override
			public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
				//����ˢ��
				new Handler().post(new Runnable() {
					
					@Override
					public void run() {
						ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
					}
				});
				
			}
		});
		mListView.setAdapter(adapter);
		tvTitleNotRecv = (TextView) rootView
				.findViewById(R.id.textView_post_not_receive);
		tvTitleRecv = (TextView) rootView
				.findViewById(R.id.textView_post_received);
		tvTitleNotRecvBar = (TextView) rootView
				.findViewById(R.id.textView_post_not_receive_bar);
		tvTitleRecvBar = (TextView) rootView
				.findViewById(R.id.textView_post_received_bar);
		if(first){
			first = false ;
			httpHelper.queryPostPackages(mApp.getUser(), mApp.getPassword(), 1, new HttpCallBack() {
				
				@Override
				public void call(Object obj, String err) {
					//���󷵻�
					if(!"0".equals(err)){
						TipsHttpError.toastError(context, err);
						return; 
					}
					notListPost = (List<PostPackages>) obj ;
					mHandler.post(new Runnable() {

						@Override
						public void run() {
//							adapter = new PostPackageListViewAdapter(mApp,
//									context, notListPost);
							mListView.setAdapter(adapter);
						}
					});
				}
			});
			
			httpHelper.queryPostPackages(mApp.getUser(),mApp.getPassword(), 2, new HttpCallBack() {
				
				@Override
				public void call(Object obj, String err) {
					//���󷵻�
					if(!"0".equals(err)){
						TipsHttpError.toastError(context, err);
						return; 
					}
					//��ǩ�յ�����
					listPost = new ArrayList<PostPackages>();
					List<PostPackages> listTemp = (List<PostPackages>) obj;
					for(PostPackages post : listTemp){
						listPost.add(post);
					}
				}
			});
			
//			httpHelper.queryPostPackages(mApp.getUser(),
//			mApp.getPassword(), 2, new QueryPostPackageCall() {
//
//				@Override
//				public void call(List<PostPackages> listPost) {
//						listData = new ArrayList<PostPackages>();
//						if (listPost != null) {
//							for (PostPackages post : listPost) {
//								if (!post.getState().equals("0")) {
//									listData.add(post);
//								}
//							}
//							PostFragment.this.listPost = listData;
//						}
//					new AsyncTask<Void, Void, Void>() {
//						protected Void doInBackground(
//								Void... params) {
//
//							// data.add("ˢ�º���ӵ�����");
//							Log.e("onRefresh", "onRefresh");
//							return null;
//						}
//
//						@Override
//						protected void onPostExecute(Void result) {
//							Log.e("onPostExecute", "onPostExecute");
//							Toast.makeText(context, "���ݸ��³ɹ�", 0)
//									.show();
////							adapter = new PostPackageListViewAdapter(
////									mApp, context, listData);
////							mListView.setAdapter(adapter);
//							//����ˢ��
//							ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
//						}
//
//					}.execute();
//				}
//			});
		}
		// ˢ��Ͷ����Ϣ


		
//		//������һ�ε�״̬
		if(mode == 1){
			tvTitleNotRecvBar.setBackgroundColor(context.getResources()
					.getColor(R.color.title_font_bg));
			tvTitleRecvBar.setBackgroundColor(context.getResources().getColor(
					R.color.title_bg));
//			adapter = new PostPackageListViewAdapter(mApp,context, notListPost);
			mListView.setAdapter(adapter);
		}else{
			tvTitleNotRecvBar.setBackgroundColor(context.getResources()
					.getColor(R.color.title_bg));
			tvTitleRecvBar.setBackgroundColor(context.getResources().getColor(
					R.color.title_font_bg));
		}
		tvTitleNotRecv.setOnClickListener(this);
		tvTitleRecv.setOnClickListener(this);
		imgSaoyiSao.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.textView_post_not_receive://δǩ�ձ�ǩ
			tvTitleNotRecvBar.setBackgroundColor(context.getResources()
					.getColor(R.color.title_font_bg));
			tvTitleRecvBar.setBackgroundColor(context.getResources().getColor(
					R.color.title_bg));
			mode = 1;
			listData = notListPost;
//			adapter = new PostPackageListViewAdapter(mApp, context, notListPost);
			mListView.setAdapter(adapter);
			break;
		case R.id.textView_post_received://��ǩ�ձ�ǩ
			tvTitleNotRecvBar.setBackgroundColor(context.getResources()
					.getColor(R.color.title_bg));
			tvTitleRecvBar.setBackgroundColor(context.getResources().getColor(
					R.color.title_font_bg));
			mode = 2;
			listData = listPost;
//			adapter = new PostPackageListViewAdapter(mApp, context, listPost);
			mListView.setAdapter(adapter);
			break;
		case R.id.img_frag_saoyisao://Ͷ��
//			Intent intent = new Intent();
//			intent.setClass(context, MipcaActivityCapture.class);
//			intent.putExtra("mode", 1);// ɨ����
//			startActivity(intent);
			Intent intent = new Intent();
			intent.setClass(context, PostInfoActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}

	}

	// �㲥������,����ˢ����Ϣ
	private class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.e("onReceive", "onReceive");
			// ˢ��Ͷ����Ϣ
			httpHelper.queryPostPackages(mApp.getUser(), mApp.getPassword(), 1, new HttpCallBack() {
				
				@Override
				public void call(Object obj, String err) {
					notListPost = (List<PostPackages>) obj ;
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							if (notListPost == null
									|| notListPost.isEmpty()) {
								notListPost = new ArrayList<PostPackages>();
							}
//							adapter = new PostPackageListViewAdapter(
//									mApp, getActivity(), notListPost);
							mListView.setAdapter(adapter);
						}
					});
				}
			});
		}

	}


}
