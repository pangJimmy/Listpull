package com.szzy.packages.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.szzy.packages.R;
import com.szzy.packages.activity.GetPackageActivity;
import com.szzy.packages.activity.SearchPackageActivity;
import com.szzy.packages.activity.UserPostActivity;
import com.szzy.packages.adapter.HomeMenuGridAdapter;
//主页
public class HomeFragment extends Fragment implements OnClickListener{
	private Context context ;
	private View rootView ;
	
	private ViewPager vpHome ;//广告页
	private int[] imgs ;
	private List<ImageView> listImgView ;//图片容器
	private List<View> dots ;  //图片上的点
	private LinearLayout layoutDot ;
	private int oldPosition ;//记录上一个点
	private int currentItem ;//当前图片
	private ScheduledExecutorService scheule ; //定时器
	private MyVPadapter adapter ; //首页广告适配器
	//查询界面
	private TextView tvSearch ;
	private RelativeLayout layoutSearch ;
	//菜单主控件
	private GridView gridMenu ;//主布局
	//图标显示顺序：附近、取件、寄件、投递、商店、互动
	private int[] imgMenus = new int[]{ R.drawable.home_ic_get_package,
			R.drawable.home_ic_user_post,
			R.drawable.home_ic_shop,R.drawable.home_ic_hd}; //图标
	private String[] strMenus = new String[] { "取件", "寄存",  "商店", "互动"};
	private List<Map<String, Object>> listMenu ;  //菜单项数据
	private HomeMenuGridAdapter gridAdapter ;//首页菜单项适配器
//	private String[] 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = getActivity() ;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_home, null);
		initView() ;
		return rootView;
	}

	private void initView() {
		//初始化广告页
		imgs = new int[]{
			R.drawable.home_view_1,	
			R.drawable.home_view_2,
			R.drawable.home_view_3
		};
		layoutDot = (LinearLayout) rootView.findViewById(R.id.layout_home_dot) ;
		vpHome = (ViewPager) rootView.findViewById(R.id.viewpager_home) ;
		listImgView = new ArrayList<ImageView>();
		dots = new ArrayList<View>();
		//添加选择点
		
		for(int i = 0; i < imgs.length ; i++){
			ImageView iv  = new ImageView(context);
			iv.setBackgroundResource(imgs[i]);
			View v = new View(context) ;
			v.setBackgroundResource(R.drawable.dot_normal) ;
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(8,8) ;
			params.setMargins(2, 2, 2, 2);
			v.setLayoutParams(params);
			
			dots.add(v);
			layoutDot.addView(v) ;
			listImgView.add(iv) ;
		}
		//初始化第一项
		dots.get(0).setBackgroundResource(R.drawable.dot_select) ;
		adapter = new MyVPadapter() ;
		vpHome.setAdapter(adapter) ;
		//设置监听
		vpHome.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				dots.get(position).setBackgroundResource(R.drawable.dot_select) ;
				dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal) ;
				
				oldPosition = position ;
				currentItem = position ;
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		//搜索运单号
		tvSearch = (TextView) rootView.findViewById(R.id.textView_home_search) ;
		layoutSearch = (RelativeLayout) rootView.findViewById(R.id.layout_home_search) ;
		//监听搜索
		tvSearch.setOnClickListener(this);
		layoutSearch.setOnClickListener(this) ;
		//功能菜单
		listMenu = new ArrayList<Map<String, Object>>();
		for(int i = 0 ; i < imgMenus.length; i++ ){
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("menuImg", imgMenus[i]);
			map.put("menuItem", strMenus[i]);
			listMenu.add(map);
		}
		gridAdapter = new HomeMenuGridAdapter(context, listMenu) ;
		gridMenu = (GridView) rootView.findViewById(R.id.gridView_home_menu) ;
		gridMenu.setAdapter(gridAdapter) ;
		//监听菜单item点击事件
		gridMenu.setOnItemClickListener(new GridClick());
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		scheule = Executors.newSingleThreadScheduledExecutor() ;
		//每隔两秒切换一张图片
		scheule.scheduleWithFixedDelay(new ViewChangeTask(), 2, 5, TimeUnit.SECONDS);
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//页面跳转时关闭广告动态
		if(scheule != null){
			scheule.shutdown() ;
		}
	}
	
	//广告切换线程
	private class ViewChangeTask implements Runnable{

		@Override
		public void run() {
			currentItem = (currentItem + 1) %imgs.length;
			mHandler.obtainMessage().sendToTarget() ;
			
		}
		
	}
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			//处理更新消息
			vpHome.setCurrentItem(currentItem);
		};
	};
	
	//viewpager适配器
	private class MyVPadapter extends PagerAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listImgView.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView(listImgView.get(position));
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			container.addView(listImgView.get(position));
			return listImgView.get(position) ;
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.textView_home_search://搜索功能
			Intent intentSearch = new Intent(context, SearchPackageActivity.class) ;
			startActivity(intentSearch);
			break ;
		default:
			break;
		}
		
	}
	
	/**
	 * 菜单项点击事件监听
	 * @author mac
	 * R.drawable.home_ic_get_package,
			R.drawable.home_ic_user_post,R.drawable.home_ic_postor_post,
			R.drawable.home_ic_shop,R.drawable.home_ic_hd
	 */
	private class GridClick implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			switch (imgMenus[position]) {
			case R.drawable.home_ic_fj://附近
				tipsUnfinish() ;
				break;
			case R.drawable.home_ic_get_package://取件（用户和投递员都可以操作）
				Intent iToGetPackage = new Intent(context, GetPackageActivity.class);
				startActivity(iToGetPackage) ;
				break;
			case R.drawable.home_ic_user_post://业主寄件
				Intent iToUserPost = new Intent(context, UserPostActivity.class);
				startActivity(iToUserPost) ;
				break;
			case R.drawable.home_ic_postor_post://投递
//				Intent itoPost = new Intent(context, PostInfoActivity.class);
//				startActivity(itoPost) ;
				tipsUnfinish() ;
				break;
			case R.drawable.home_ic_shop://购物
				//发送广播
				Intent toBraud = new Intent();
				toBraud.setAction("toshop") ;
				context.sendBroadcast(toBraud) ;
				break;
			case R.drawable.home_ic_hd://互动
				tipsUnfinish();
				break;

			default:
				break;
			}
			
		}
		
	}
	
	//功能暂时未实现提示
	private void tipsUnfinish(){
		Toast.makeText(context, "该功能正在建设中，敬请期待", 0).show() ;
	}
	
}
