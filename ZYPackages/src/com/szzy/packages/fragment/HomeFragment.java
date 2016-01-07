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
import com.szzy.packages.activity.MailBoxActivity;
import com.szzy.packages.activity.SearchPackageActivity;
import com.szzy.packages.activity.ShowBoxActivity;
import com.szzy.packages.activity.UserPostActivity;
import com.szzy.packages.adapter.HomeMenuGridAdapter;
import com.szzy.packages.entity.MailBox;
//��ҳ
public class HomeFragment extends Fragment implements OnClickListener{
	private Context context ;
	private View rootView ;
	
	private ViewPager vpHome ;//���ҳ
	private int[] imgs ;
	private List<ImageView> listImgView ;//ͼƬ����
	private List<View> dots ;  //ͼƬ�ϵĵ�
	private LinearLayout layoutDot ;
	private int oldPosition ;//��¼��һ����
	private int currentItem ;//��ǰͼƬ
	private ScheduledExecutorService scheule ; //��ʱ��
	private MyVPadapter adapter ; //��ҳ���������
	//��ѯ����
	private TextView tvSearch ;
	private RelativeLayout layoutSearch ;
	//�˵����ؼ�
	private GridView gridMenu ;//������
	//ͼ����ʾ˳�򣺸�����ȡ�����ļ���Ͷ�ݡ��̵ꡢ����
	private int[] imgMenus = new int[]{ R.drawable.home_ic_get_package,
			R.drawable.home_ic_user_post,
			R.drawable.home_ic_shop,R.drawable.home_ic_hd,R.drawable.home_ic_get_package,R.drawable.home_ic_user_post}; //ͼ��
	private String[] strMenus = new String[] { "ȡ��", "�Ĵ�",  "�̵�", "����","����", "չʾ��"};
	private List<Map<String, Object>> listMenu ;  //�˵�������
	private HomeMenuGridAdapter gridAdapter ;//��ҳ�˵���������
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
		//��ʼ�����ҳ
		imgs = new int[]{
			R.drawable.home_view_1,	
			R.drawable.home_view_2,
			R.drawable.home_view_3
		};
		layoutDot = (LinearLayout) rootView.findViewById(R.id.layout_home_dot) ;
		vpHome = (ViewPager) rootView.findViewById(R.id.viewpager_home) ;
		listImgView = new ArrayList<ImageView>();
		dots = new ArrayList<View>();
		//���ѡ���
		
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
		//��ʼ����һ��
		dots.get(0).setBackgroundResource(R.drawable.dot_select) ;
		adapter = new MyVPadapter() ;
		vpHome.setAdapter(adapter) ;
		//���ü���
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
		//�����˵���
		tvSearch = (TextView) rootView.findViewById(R.id.textView_home_search) ;
		layoutSearch = (RelativeLayout) rootView.findViewById(R.id.layout_home_search) ;
		//��������
		tvSearch.setOnClickListener(this);
		layoutSearch.setOnClickListener(this) ;
		//���ܲ˵�
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
		//�����˵�item����¼�
		gridMenu.setOnItemClickListener(new GridClick());
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		scheule = Executors.newSingleThreadScheduledExecutor() ;
		//ÿ�������л�һ��ͼƬ
		scheule.scheduleWithFixedDelay(new ViewChangeTask(), 2, 5, TimeUnit.SECONDS);
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//ҳ����תʱ�رչ�涯̬
		if(scheule != null){
			scheule.shutdown() ;
		}
	}
	
	//����л��߳�
	private class ViewChangeTask implements Runnable{

		@Override
		public void run() {
			currentItem = (currentItem + 1) %imgs.length;
			mHandler.obtainMessage().sendToTarget() ;
			
		}
		
	}
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			//���������Ϣ
			vpHome.setCurrentItem(currentItem);
		};
	};
	
	//viewpager������
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
		case R.id.textView_home_search://��������
			Intent intentSearch = new Intent(context, SearchPackageActivity.class) ;
			startActivity(intentSearch);
			break ;
		default:
			break;
		}
		
	}
	
	
	private final String getPackage = "ȡ��" ;
	private final String userPost  = "�Ĵ�" ;
	private final String shop = "�̵�" ;
	private final String hudong = "����" ;
	private final String mailBox = "����" ;
	private final String showBox = "չʾ��";
	/**
	 * �˵������¼�����
	 * @author mac
	 * R.drawable.home_ic_get_package,
			R.drawable.home_ic_user_post,R.drawable.home_ic_postor_post,
			R.drawable.home_ic_shop,R.drawable.home_ic_hd
	 */
	private class GridClick implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			//strMenus = new String[] { "ȡ��", "�Ĵ�",  "�̵�", "����","����"};
			switch (strMenus[position]) {
//			case R.drawable.home_ic_fj://����
//				tipsUnfinish() ;
//				break;
			case getPackage://ȡ�����û���Ͷ��Ա�����Բ�����
				Intent iToGetPackage = new Intent(context, GetPackageActivity.class);
				startActivity(iToGetPackage) ;
				break;
			case userPost://ҵ���ļ�
				Intent iToUserPost = new Intent(context, UserPostActivity.class);
				startActivity(iToUserPost) ;
				break;
//			case R.drawable.home_ic_postor_post://Ͷ��
//				Intent itoPost = new Intent(context, PostInfoActivity.class);
//				startActivity(itoPost) ;
//				tipsUnfinish() ;
//				break;
			case shop://����
				//���͹㲥
				Intent toBraud = new Intent();
				toBraud.setAction("toshop") ;
				context.sendBroadcast(toBraud) ;
				break;
			case hudong://����
				tipsUnfinish();
				break;
			case mailBox://����
				Intent toMailBox = new Intent(getActivity(), MailBoxActivity.class) ;
				startActivity(toMailBox) ;
				break;
			case showBox://չʾ��
				Intent toShowBox = new Intent(getActivity(), ShowBoxActivity.class) ;
				startActivity(toShowBox) ;
				break ;
			default:
				break;
			}
			
		}
		
	}
	
	//������ʱδʵ����ʾ
	private void tipsUnfinish(){
		Toast.makeText(context, "�ù������ڽ����У������ڴ�", 0).show() ;
	}
	
}
