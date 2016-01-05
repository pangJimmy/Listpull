package com.szzy.packages.base;

import com.szzy.packages.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

/**
 * 基础类，主要提供一些activity中公用的方法
 * @author mac
 *
 */
public abstract class MBaseActivity extends Activity implements OnClickListener{
	
	@Override
	protected  void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
	}
	/**
	 *初始化UI控件
	 */
	public void initView(){
		
	}
	
	long exitSytemTime = 0;
	/**
	 * 按两次返回键退出程序
	 * @param keyCode
	 * @param event
	 * @return
	 */
	public boolean twoPressExit(int keyCode, KeyEvent event){
		if(keyCode == KeyEvent.KEYCODE_BACK){  
	        if(System.currentTimeMillis() - exitSytemTime > 2000){  
	            Toast.makeText(getApplicationContext(), R.string.again_exit_system, Toast.LENGTH_SHORT).show();  
	            exitSytemTime = System.currentTimeMillis();  
	            return true;  
	        }else{  
	            finish();  
	        }  
	          
	    }
		return false;
	}
	
	/**
	 * 打印LOG.E信息
	 * @param tag
	 * @param msg
	 */
	public void printLog_E(String tag , String msg){
		Log.e(tag, msg);
	}
	/**
	 * 打印LOG.I信息
	 * @param tag
	 * @param msg
	 */
	public void printLog_i(String tag , String msg){
		Log.i(tag, msg);
	}
	/**
	 * 打印LOG.D信息
	 * @param tag
	 * @param msg
	 */
	public void printLog_d(String tag , String msg){
		Log.d(tag, msg);
	}
	
	
	@Override
	public void onClick(View v) {
		
		
	}

}
