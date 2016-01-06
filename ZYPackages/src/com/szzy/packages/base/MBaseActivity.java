package com.szzy.packages.base;

import com.szzy.packages.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.TextView;
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
	
	public Dialog dialog ;  //对话框
	
	/**
	 * 关闭对话框
	 */
	public void closeDialog(){
		if(dialog != null){
			dialog.cancel() ;
		}
	}
	
	/**
	 * 创建等待对话框
	 * @param content
	 */
	public void createProgressDialog(String content){
		Builder builder = new Builder(this, AlertDialog.THEME_HOLO_LIGHT) ;
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_progress, null) ;
		TextView text = (TextView) view.findViewById(R.id.textView_progress_dialog) ;
		text.setText(content) ;
		builder.setView(view);
		dialog = builder.create() ;
		WindowManager.LayoutParams wmParams = dialog.getWindow().getAttributes();  
        wmParams.format = PixelFormat.TRANSPARENT;  //内容全透明  
//        wmParams.format = PixelFormat.TRANSLUCENT;  //内容半透明  
        wmParams.alpha=0.8f;    //调节透明度，1.0最大 
		dialog.show() ;
		
	}

}
