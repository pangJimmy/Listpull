package com.szzy.packages.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.szzy.packages.R;
import com.szzy.packages.base.MBaseActivity;
import com.szzy.packages.fragment.GetPackageFragment;
import com.szzy.packages.fragment.HomeFragment;
import com.szzy.packages.fragment.PackageFragment;
import com.szzy.packages.fragment.PostFragment;
import com.szzy.packages.fragment.ShopFragment;
import com.szzy.packages.fragment.UserFragment;
/**
 * 主界面
 * @author mac
 *
 */
public class MainActivity extends MBaseActivity{

	private RelativeLayout layout ;
	private Button btnHome ;
	private Button btnGet ;
	private Button btnPost ;
	private Button btnUser ;
	
	private FragmentManager fragManager ;
	private FragmentTransaction fragTran;
	
	private Fragment  getPackageFrag ; //取件界面
	private Fragment homeFrag ;//主界面
	private Fragment postFrag ;//投递界面
	private Fragment userFrag ;//用户界面
	private Fragment shopFrag ;//购物界面
	
	private Fragment packageFrag ;//信包界面
	
	private MyReceiver mReceiver ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setContentView(R.layout.activity_main);
		super.onCreate(savedInstanceState);
		fragManager = getFragmentManager();
		getPackageFrag = new GetPackageFragment();
		homeFrag = new HomeFragment();
		postFrag = new PostFragment() ;
		userFrag = new UserFragment() ;
		shopFrag = new ShopFragment() ;
		packageFrag = new PackageFragment() ;
		repleaceFragment(homeFrag);//默认为主界面
		mReceiver = new MyReceiver() ;
		IntentFilter filter = new IntentFilter();
		filter.addAction("toshop");
		registerReceiver(mReceiver, filter);
		
	}
	
	@Override
	protected void onDestroy() {
		unregisterReceiver(mReceiver);
		super.onDestroy();
	}
	
	@Override
	public void initView() {
		btnHome = (Button) findViewById(R.id.tab_home);
		btnGet = (Button) findViewById(R.id.tab_getpackage);
		btnPost = (Button) findViewById(R.id.tab_post);
		btnUser = (Button) findViewById(R.id.tab_user);
		
		btnHome.setOnClickListener(this);
		btnGet.setOnClickListener(this);
		btnPost.setOnClickListener(this);
		btnUser.setOnClickListener(this);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		return super.twoPressExit(keyCode, event);//按两次返回键，退出程序
	}
	
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tab_home:
			repleaceFragment(homeFrag);
			btnHome.setBackgroundResource(R.drawable.tab_home_selected);
			btnGet.setBackgroundResource(R.drawable.tab_post_normal);
			btnPost.setBackgroundResource(R.drawable.tab_shop_normal);
			btnUser.setBackgroundResource(R.drawable.tab_user_normal);
			break;
		case R.id.tab_getpackage:
//			repleaceFragment(getPackageFrag);//暂时注释掉这段代码
			repleaceFragment(packageFrag);
			btnHome.setBackgroundResource(R.drawable.tab_home_normal);
			btnGet.setBackgroundResource(R.drawable.tab_post_selected);
			btnPost.setBackgroundResource(R.drawable.tab_shop_normal);
			btnUser.setBackgroundResource(R.drawable.tab_user_normal);
			break;
		case R.id.tab_post:
//			repleaceFragment(postFrag);//将投件界面替换成购物界面
			repleaceFragment(shopFrag) ;
			btnHome.setBackgroundResource(R.drawable.tab_home_normal);
			btnGet.setBackgroundResource(R.drawable.tab_post_normal);
			btnPost.setBackgroundResource(R.drawable.tab_shop_selected);
			btnUser.setBackgroundResource(R.drawable.tab_user_normal);
			break;
		case R.id.tab_user:
			repleaceFragment(userFrag);
			btnHome.setBackgroundResource(R.drawable.tab_home_normal);
			btnGet.setBackgroundResource(R.drawable.tab_post_normal);
			btnPost.setBackgroundResource(R.drawable.tab_shop_normal);
			btnUser.setBackgroundResource(R.drawable.tab_user_selected);
			break;

		default:
			break;
		}
	}
	
	//切换Fragment
	private void repleaceFragment(Fragment fragment){
		fragTran = fragManager.beginTransaction();
		fragTran.replace(R.id.layout_fragment, fragment);
		fragTran.commit();
	}
	
	private class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
//			Log.e("MyReceiver  ---- ", "BroadcastReceiver");
			//跳转到商店
			repleaceFragment(shopFrag) ;
			btnHome.setBackgroundResource(R.drawable.tab_home_normal);
			btnGet.setBackgroundResource(R.drawable.tab_post_normal);
			btnPost.setBackgroundResource(R.drawable.tab_shop_selected);
			btnUser.setBackgroundResource(R.drawable.tab_user_normal);
			
		}
		
	}
}
