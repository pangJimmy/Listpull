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

import com.szzy.packages.R;
import com.szzy.packages.base.NormalBaseActivity;
import com.szzy.packages.entity.ShowBox;
import com.szzy.packages.http.HttpCallBack;
import com.szzy.packages.http.HttpHelper;
import com.szzy.packages.tool.TipsHttpError;
/**
 * 展示柜
 * @author mac
 *
 */
public class ShowBoxActivity extends NormalBaseActivity {

	private MApplication mApp ;
	private ListView lvBox ;
	
	private List<ShowBox> listBox ;//信箱数量
	private HttpHelper httpHelper ;
	
	private Handler mHandler = new Handler() ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_box) ;
		setTitle("展示柜");
		mApp = (MApplication) getApplication();
		listBox = mApp.getLoginInfo().getListShowBox();
		httpHelper = new HttpHelper() ;
		initView() ;
	}
	private void initView() {
		lvBox = (ListView) findViewById(R.id.listView_show_box_data) ;
		if(listBox != null){
			lvBox.setAdapter(new BoxAdapter()) ;
		}
		
	}
	
	//内部类用于布局信箱的结构
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
				convertView = LayoutInflater.from(ShowBoxActivity.this).inflate(R.layout.listview_item_mail_box, null);
				holder.tvBox = (TextView) convertView.findViewById(R.id.textView_listview_item_mail_box) ;
				holder.btnOpen = (Button) convertView.findViewById(R.id.button_listview_item_mail_box) ;
				holder.tvBoxType = (TextView) convertView.findViewById(R.id.textView_listview_item_mail_box_type) ;
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tvBoxType.setText("展示柜");
			holder.tvBox.setText(listBox.get(position).getMname()) ;
			holder.btnOpen.setOnClickListener(new OpenBox(position)) ;
			return convertView;
		}
		
		//Item布局
		class ViewHolder {
			TextView tvBoxType ;
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
				//等待提示
				ShowBoxActivity.super.createProgressDialog("正在开箱") ;
				// 完成开箱操作
				httpHelper.openMail(mApp.getUser(), mApp.getPassword(), listBox.get(position).getMecode(), 
						listBox.get(position).getMbcode(), new HttpCallBack() {
					
					@Override
					public void call(Object obj, final String err) {
						// TODO Auto-generated method stub
						mHandler.post(new Runnable() {
							
							@Override
							public void run() {
								ShowBoxActivity.super.closeDialog() ;
								TipsHttpError.toastError(mApp, err) ;
								
							}
						});
						
					}
				});
			}
			
		}
		
		
	}
	
}
