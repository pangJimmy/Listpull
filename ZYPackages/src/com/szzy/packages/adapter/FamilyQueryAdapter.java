package com.szzy.packages.adapter;

import java.util.List;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.szzy.packages.R;
import com.szzy.packages.activity.MApplication;
import com.szzy.packages.entity.UserPostRecord;
import com.szzy.packages.http.HttpCallBack;
import com.szzy.packages.http.HttpHelper;
import com.szzy.packages.tool.TipsHttpError;
/**
 * 寄存取件适配器
 * @author mac
 *
 */
public class FamilyQueryAdapter extends BaseAdapter {

	List<UserPostRecord> listRecord ;
	
	private LayoutInflater inflater ;//注入器
	private Context context ;
	private MApplication mApp ;
	private HttpHelper httpHelper ;
	private Handler mHandler = new Handler();
	
	public FamilyQueryAdapter(MApplication mApp, Context context,List<UserPostRecord> listRecord){
		inflater = LayoutInflater.from(context);
		this.listRecord = listRecord ;
		this.context = context ;
		this.mApp = mApp;
		httpHelper = new HttpHelper();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listRecord.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listRecord.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder() ;
			convertView = inflater.inflate(R.layout.listview_item_data_get_family_post, null) ;
			holder.tvBegTime = (TextView) convertView.findViewById(R.id.textView_listview_item_get_family_begtime) ;  //存放时间
			holder.tvEname  = (TextView) convertView.findViewById(R.id.textView_listview_item_get_family_ename) ;//柜名称
			holder.tvBname = (TextView) convertView.findViewById(R.id.textView_listview_item_get_family_boxname) ;//箱名称
			holder.tvSetMen  = (TextView) convertView.findViewById(R.id.textView_listview_item_get_family_post) ;//寄存者
			holder.tvGetMen  = (TextView) convertView.findViewById(R.id.textView_listview_item_get_family_geter) ;//取件人
			holder.tvEndTime  = (TextView) convertView.findViewById(R.id.textView_listview_item_get_family_gettime) ;//取件时间
			holder.tvState  = (TextView) convertView.findViewById(R.id.textView_listview_item_get_family_state) ;//包裹状态
			holder.btnGet  = (Button) convertView.findViewById(R.id.button_listview_item_get_family_get_package) ;//取件
			convertView.setTag(holder) ;
		}else{
			holder = (ViewHolder) convertView.getTag() ;
		}
		//数据匹配填充
		UserPostRecord record = listRecord.get(position) ;
		if(record.getState().equals("2")){//取件
			holder.tvBegTime.setText(record.getBegtime()) ;  //存放时间
			holder.tvEname.setText(record.getEname()) ;//柜名称
			holder.tvBname.setText(record.getBname()) ;//箱名称
			holder.tvSetMen.setText(record.getSetman());//寄存者
			holder.tvGetMen.setText(record.getGetman());//取件人
			holder.tvEndTime.setVisibility(View.VISIBLE) ;//取件时间
			holder.tvEndTime.setText(record.getEndtime()) ;
			holder.tvState.setText("已取") ;//包裹状态
			holder.btnGet.setVisibility(View.GONE) ;//取件
			
		}else{
			holder.tvBegTime.setText(record.getBegtime()) ;  //存放时间
			holder.tvEname.setText(record.getEname()) ;//柜名称
			holder.tvBname.setText(record.getBname()) ;//箱名称
			holder.tvSetMen.setText(record.getSetman());//寄存者
			holder.tvGetMen.setText(record.getGetman());//取件人
			holder.tvEndTime.setText(record.getEndtime()) ;//取件时间
			holder.tvState.setText("未取") ;//包裹状态
			holder.btnGet.setVisibility(View.VISIBLE) ;//取件
			holder.tvEndTime.setVisibility(View.GONE) ;//取件时间
			holder.btnGet.setOnClickListener( new MyClick(position, record.getSystemid())) ;
		}
		return convertView;
	}
	
	
	class MyClick implements OnClickListener{
		int position ;
		String systemid ;
		public MyClick(int postion, String systemid){
			this.position = position ;
			this.systemid = systemid ;
		}
		
		@Override
		public void onClick(View v) {
			//弹出对话框
			createDialog(position , systemid) ;
		}
		
	}
	
	
	class ViewHolder {
		TextView tvBegTime ;  //存放时间
		TextView tvEname  ;//柜名称
		TextView tvBname ;//箱名称
		TextView tvSetMen ;//寄存者
		TextView tvGetMen ;//取件人
		TextView tvEndTime ;//取件时间
		TextView tvState ;//包裹状态
		Button btnGet ;//取件
		
	}

	//取件对话框,取件时用systemid去取
	private void createDialog(final int position, final String systemid){
		Builder builder = new Builder(context);
		builder.setMessage("确认是否取件？");
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//取件时用systemid去取
//				String systemid = listRecord.get(position).getSystemid() ;
				httpHelper.userGet(mApp.getUser(), mApp.getPassword(), systemid, new HttpCallBack(){

							@Override
							public void call(Object obj, final String err) {
								//返回开箱结果
								mHandler.post(new Runnable() {
									
									@Override
									public void run() {
										if(err.equals(TipsHttpError.OK)){
											//发送广播
											Intent intent = new Intent();
											intent.setAction("update");
											context.sendBroadcast(intent);
										}else{
											TipsHttpError.toastError(mApp, err);
										}
										
									}
								});
								
							}
				
				});
			}
		});
		builder.setNegativeButton("取消", null);
		builder.create().show();
	}
}
