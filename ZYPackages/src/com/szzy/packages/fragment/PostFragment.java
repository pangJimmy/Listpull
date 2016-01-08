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

	private TextView tvTitleNotRecv;// 未签收
	private TextView tvTitleRecv;// 已签收
	private TextView tvTitleNotRecvBar;// 未签收bar
	private TextView tvTitleRecvBar;// 已签收bar

	private PullToRefreshLayout ptrl ;//下拉刷新控件
	private ListView mListView ;
//	private MyListView mListView;// listView
	private int mode = 1;// 模式，1为未签收模式，2为已签收模式
	private HttpHelper httpHelper;// HTTP
	private MApplication mApp;
	private ImageView imgSaoyiSao;// 扫一扫

	private List<PostPackages> notListPost = new ArrayList<PostPackages>();// 未签收
	private List<PostPackages> listPost = new ArrayList<PostPackages>();
	private List<PostPackages> listData = new ArrayList<PostPackages>();
	GetPackageQueryAdapter adapter;// 适配器

	private Handler mHandler = new Handler();
	private MyReceiver myReceiver;// 广播接受者，用于接收刷新信息
	
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
						// 刷新完毕了哦！
						ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
						Log.e("onRefresh", "refreshFinish++");
					}
				}.sendEmptyMessageDelayed(0, 5000);
				//下拉刷新
				httpHelper.queryPostPackages(mApp.getUser(),mApp.getPassword(), mode, new HttpCallBack() {
					
					@Override
					public void call(Object obj, String err) {
						//错误返回
						if(!"0".equals(err)){
							TipsHttpError.toastError(context, err);
							return; 
						}
						if(mode == 1){
							notListPost = (List<PostPackages>) obj;
						}else{
							//已签收的数据
							listPost = new ArrayList<PostPackages>();
							List<PostPackages> listTemp = (List<PostPackages>) obj;
							for(PostPackages post : listTemp){
								listPost.add(post);
							}
						}
						
						new AsyncTask<Void, Void, Void>() {
						protected Void doInBackground(
								Void... params) {

							// data.add("刷新后添加的内容");
							Log.e("onRefresh", "onRefresh");
							return null;
						}

						@Override
						protected void onPostExecute(Void result) {
							Log.e("onPostExecute", "onPostExecute");
							Toast.makeText(context, "数据更新成功", 0)
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
							//结束刷新
							ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
						}

					}.execute();
						
					}
				});
				
			}
			
			@Override
			public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
				//上啦刷新
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
					//错误返回
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
					//错误返回
					if(!"0".equals(err)){
						TipsHttpError.toastError(context, err);
						return; 
					}
					//已签收的数据
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
//							// data.add("刷新后添加的内容");
//							Log.e("onRefresh", "onRefresh");
//							return null;
//						}
//
//						@Override
//						protected void onPostExecute(Void result) {
//							Log.e("onPostExecute", "onPostExecute");
//							Toast.makeText(context, "数据更新成功", 0)
//									.show();
////							adapter = new PostPackageListViewAdapter(
////									mApp, context, listData);
////							mListView.setAdapter(adapter);
//							//结束刷新
//							ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
//						}
//
//					}.execute();
//				}
//			});
		}
		// 刷新投递信息


		
//		//保存上一次的状态
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
		case R.id.textView_post_not_receive://未签收标签
			tvTitleNotRecvBar.setBackgroundColor(context.getResources()
					.getColor(R.color.title_font_bg));
			tvTitleRecvBar.setBackgroundColor(context.getResources().getColor(
					R.color.title_bg));
			mode = 1;
			listData = notListPost;
//			adapter = new PostPackageListViewAdapter(mApp, context, notListPost);
			mListView.setAdapter(adapter);
			break;
		case R.id.textView_post_received://已签收标签
			tvTitleNotRecvBar.setBackgroundColor(context.getResources()
					.getColor(R.color.title_bg));
			tvTitleRecvBar.setBackgroundColor(context.getResources().getColor(
					R.color.title_font_bg));
			mode = 2;
			listData = listPost;
//			adapter = new PostPackageListViewAdapter(mApp, context, listPost);
			mListView.setAdapter(adapter);
			break;
		case R.id.img_frag_saoyisao://投件
//			Intent intent = new Intent();
//			intent.setClass(context, MipcaActivityCapture.class);
//			intent.putExtra("mode", 1);// 扫箱柜号
//			startActivity(intent);
			Intent intent = new Intent();
			intent.setClass(context, PostInfoActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}

	}

	// 广播接受者,接收刷新信息
	private class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.e("onReceive", "onReceive");
			// 刷新投递信息
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
