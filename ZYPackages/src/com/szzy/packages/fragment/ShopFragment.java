package com.szzy.packages.fragment;

import com.szzy.packages.R;
import com.szzy.packages.activity.MApplication;
import com.szzy.packages.activity.MipcaActivityCapture;
import com.szzy.packages.activity.PostInfoActivity;
import com.szzy.packages.activity.ShopActicity;
import com.szzy.packages.entity.PostBoxInfo;
import com.szzy.packages.http.HttpHelper;
import com.szzy.packages.http.QueryBoxCall;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
/**
 * 商店购物
 * @author mac
 *
 */
public class ShopFragment extends Fragment implements OnClickListener{
	private Context context ;
	private View rootView ;
	
	private ImageView imgSys ; //扫一扫
	
	
	private final int SCANNIN_LOCK_CODE = 11 ;
	private MApplication mApp ;
	private String lockCode = "" ;
	private HttpHelper httpHelper ;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = getActivity();
		mApp = (MApplication) context.getApplicationContext() ;
		httpHelper = new HttpHelper() ;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_shop, null);
		initView() ;
		return rootView;
	}
	
	private void initView(){
		imgSys = (ImageView) rootView.findViewById(R.id.imageView_frag_shop_lock) ;
		imgSys.setOnClickListener(this) ;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.e("onActivityResult", "requestCode = " + requestCode + "resultCode = " + resultCode);
		switch (requestCode) {
		case SCANNIN_LOCK_CODE://返回箱柜编号
			if(resultCode == Activity.RESULT_OK){
				Log.e("lock", ""+ mApp.getLockId()) ;
				lockCode = mApp.getLockId().trim();
				try{
					int lock = Integer.valueOf(lockCode) ;
					Intent intentToShop = new Intent(context, ShopActicity.class);
					startActivity(intentToShop) ;
					//查询箱子状态
//					httpHelper.queryBoxInfo(mApp.getUser(), mApp.getPassword(), lock, new QueryBoxCall() {
//					
//					@Override
//					public void call( PostBoxInfo boxInfo) {
//						//只要有数据返回就进入
////						if(boxInfo!= null){
////							mApp.setListBox( boxInfo.getListBox());
////						}
//						
//					}
//				});
				}catch(Exception e){
					Log.e("",e.toString());
				}
			}
				break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageView_frag_shop_lock://扫一扫
//			Intent intent = new Intent(context, ShopActicity.class);
//			startActivity(intent) ;
			
			Intent intentLock = new Intent(context, MipcaActivityCapture.class);
			intentLock.putExtra("mode", 1);
			intentLock.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivityForResult(intentLock, SCANNIN_LOCK_CODE);
			break;

		default:
			break;
		}
		
	}
}
