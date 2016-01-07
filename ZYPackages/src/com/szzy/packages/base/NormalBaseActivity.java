package com.szzy.packages.base;

import com.szzy.packages.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 基本界面,公用函数，公用界面
 * @author mac
 *
 */
public abstract class NormalBaseActivity extends Activity implements OnClickListener{

	private TextView tvTitle ;  //标题
	private ImageView imgBack ; //返回
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
	}
	
	public void setTitle(String title){
		tvTitle = (TextView) findViewById(R.id.textView_title) ;
		//设置标题
		tvTitle.setText(title) ;
		imgBack = (ImageView) findViewById(R.id.imageButton_back) ;
		imgBack.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageButton_back:
			finish() ;
			break;

		default:
			break;
		}
		
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
