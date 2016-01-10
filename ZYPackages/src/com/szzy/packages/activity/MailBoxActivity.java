package com.szzy.packages.activity;

import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.szzy.packages.R;
import com.szzy.packages.base.NormalBaseActivity;
import com.szzy.packages.entity.Box;
import com.szzy.packages.entity.MailBox;
import com.szzy.packages.http.HttpCallBack;
import com.szzy.packages.http.HttpHelper;
import com.szzy.packages.tool.TipsHttpError;
/**
 * ���俪��
 * @author mac
 *
 */
public class MailBoxActivity extends NormalBaseActivity {
	
	private MApplication mApp ;
	private ListView lvBox ;
	
	private List<MailBox> listBox ;//��������
	private HttpHelper httpHelper ;
	
	private Handler mHandler = new Handler() ;
//	private 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mail_box) ;
		setTitle("����");
		mApp = (MApplication) getApplication();
		listBox = mApp.getLoginInfo().getListBox() ;
		httpHelper = new HttpHelper() ;
		initView() ;
	}
	
	private void initView(){
		lvBox = (ListView) findViewById(R.id.listView_mail_box_data) ;
		if(listBox != null){
			lvBox.setAdapter(new BoxAdapter()) ;
		}
		
	}
	
	
	//�ڲ������ڲ�������Ľṹ
	private class BoxAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			
			return listBox.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return listBox.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder ;
			if(convertView == null){
				holder = new ViewHolder();
				convertView = LayoutInflater.from(MailBoxActivity.this).inflate(R.layout.listview_item_mail_box, null);
				holder.tvBox = (TextView) convertView.findViewById(R.id.textView_listview_item_mail_box) ;
				holder.btnOpen = (Button) convertView.findViewById(R.id.button_listview_item_mail_box) ;
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tvBox.setText(listBox.get(position).getMname()) ;
			holder.btnOpen.setOnClickListener(new OpenBox(position)) ;
			return convertView;
		}
		
		//Item����
		class ViewHolder {
			TextView tvBox ;
			Button btnOpen ;
		}
		
		class OpenBox implements OnClickListener{
			int position ;
			public OpenBox(int position){
				this.position = position ;
			}
			@Override
			public void onClick(View v) {
				//�ȴ���ʾ
				MailBoxActivity.super.createProgressDialog("���ڿ���") ;
				// ��ɿ������
				httpHelper.openMail(mApp.getUser(), mApp.getPassword(), listBox.get(position).getMecode(), 
						listBox.get(position).getMbcode(), new HttpCallBack() {
					
					@Override
					public void call(Object obj, final String err) {
						// TODO Auto-generated method stub
						mHandler.post(new Runnable() {
							
							@Override
							public void run() {
								MailBoxActivity.super.closeDialog() ;
								TipsHttpError.toastError(mApp, err) ;
								if(err.equals(TipsHttpError.OK)){
									TipsHttpError.toastNormal(mApp, "����ɹ�") ;
								}
							}
						});
						
					}
				});
			}
			
		}
		
		
	}
	
}
