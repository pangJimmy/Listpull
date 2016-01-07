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
 * ��������,���ú��������ý���
 * @author mac
 *
 */
public abstract class NormalBaseActivity extends Activity implements OnClickListener{

	private TextView tvTitle ;  //����
	private ImageView imgBack ; //����
	
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
		//���ñ���
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
