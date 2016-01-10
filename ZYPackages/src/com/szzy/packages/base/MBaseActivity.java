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
 * �����࣬��Ҫ�ṩһЩactivity�й��õķ���
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
	 *��ʼ��UI�ؼ�
	 */
	public void initView(){
		
	}
	
	long exitSytemTime = 0;
	/**
	 * �����η��ؼ��˳�����
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
	 * ��ӡLOG.E��Ϣ
	 * @param tag
	 * @param msg
	 */
	public void printLog_E(String tag , String msg){
		Log.e(tag, msg);
	}
	/**
	 * ��ӡLOG.I��Ϣ
	 * @param tag
	 * @param msg
	 */
	public void printLog_i(String tag , String msg){
		Log.i(tag, msg);
	}
	/**
	 * ��ӡLOG.D��Ϣ
	 * @param tag
	 * @param msg
	 */
	public void printLog_d(String tag , String msg){
		Log.d(tag, msg);
	}
	
	
	@Override
	public void onClick(View v) {
		
		
	}
	
	/**
	 * ��ʾ��Ϣ
	 * @param info
	 */
	public void ToastInfo(String info){
		Toast.makeText(this, info, Toast.LENGTH_SHORT).show() ;
	}
	
	public Dialog dialog ;  //�Ի���
	
	/**
	 * �رնԻ���
	 */
	public void closeDialog(){
		if(dialog != null){
			dialog.cancel() ;
		}
	}
	
	/**
	 * �����ȴ��Ի���
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
        wmParams.format = PixelFormat.TRANSPARENT;  //����ȫ͸��  
//        wmParams.format = PixelFormat.TRANSLUCENT;  //���ݰ�͸��  
        wmParams.alpha=0.8f;    //����͸���ȣ�1.0��� 
		dialog.show() ;
		
	}

}
