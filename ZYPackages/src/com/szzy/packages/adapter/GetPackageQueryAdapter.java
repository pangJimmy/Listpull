package com.szzy.packages.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.szzy.packages.R;
import com.szzy.packages.activity.MApplication;
import com.szzy.packages.entity.PostPackages;
import com.szzy.packages.entity.UserGetRecord;
import com.szzy.packages.http.HttpCallBack;
import com.szzy.packages.http.HttpHelper;
import com.szzy.packages.http.OpenBoxCall;
import com.szzy.packages.tool.TipsHttpError;

public class GetPackageQueryAdapter extends BaseAdapter {

	private LayoutInflater inflater ;//注入器
//	private List<PostPackages> listPackages ;
	private List<UserGetRecord> listRecord ;//用listRecord代替listPackages
	private Context context ;
	private MApplication mApp ;
	private HttpHelper httpHelper ;
	private Handler mHandler = new Handler();
	
	public GetPackageQueryAdapter(MApplication mApp, Context context,List<UserGetRecord> listPost){
		inflater = LayoutInflater.from(context);
		this.listRecord = listPost ;
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
			holder = new ViewHolder();
			//注入布局文件
			convertView = inflater.inflate(R.layout.listview_item_data_post, null);
			holder.tvOrder = (TextView) convertView.findViewById(R.id.textView_listview_item_post_orderid);
			holder.tvPostDate = (TextView) convertView.findViewById(R.id.textView_listview_item_post_post_time);
			holder.tvAddress = (TextView) convertView.findViewById(R.id.textView_listview_item_post_address);
			holder.tvState = (TextView) convertView.findViewById(R.id.textView_listview_item_post_state);
			holder.imgOrder = (ImageView) convertView.findViewById(R.id.imageView_listview_item_post_ogo);
			holder.tvBoxid = (TextView) convertView.findViewById(R.id.textView_listview_item_post_boxid);
			holder.tvGetTime = (TextView) convertView.findViewById(R.id.textView_listview_item_post_gettime);
			holder.tvTel = (TextView) convertView.findViewById(R.id.textView_listview_item_post_tel);
			holder.tvPoster = (TextView) convertView.findViewById(R.id.textView_listview_item_post_poster);
			holder.btnGet = (Button) convertView.findViewById(R.id.button_listview_item_post_get_package);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		 String begtime = listRecord.get(position).getBegtime(); // 存放时间
		 String ename = listRecord.get(position).getEname(); // 柜名称
		 String bname = listRecord.get(position).getBname(); // 箱名称
		 String postman = listRecord.get(position).getPostman(); // 取件人手机号
		 String order = listRecord.get(position).getOrder(); // 订单编号 ， 可空
		 String endtime = listRecord.get(position).getEndtime(); // 取件时间，可空
		 String getstyle = listRecord.get(position).getGetstyle(); // 取件凡是，(未取是0)
		 String state = listRecord.get(position).getState(); // 寄存状态(0未取，1已取)
		//将数据填入item, PACKAGER_STATE_NOT_GET包裹状态未取
		if(state != null && state.equals(TipsHttpError.PACKAGER_STATE_NOT_GET)){
			holder.tvState.setText("未签收");
			holder.tvOrder.setText(order);
			holder.tvPostDate.setText(begtime);
			holder.tvAddress.setText(ename);
			holder.tvTel.setText(postman);
			holder.tvBoxid.setText("箱号：" + bname);
			holder.tvPoster.setText(postman);
			holder.btnGet.setVisibility(View.VISIBLE);
			holder.tvGetTime.setVisibility(View.GONE) ;
			holder.btnGet.setOnClickListener(new MyonClick(position));
		}else if(state != null){
			holder.tvState.setText("已签收");
			holder.tvOrder.setText(order);
			holder.tvPostDate.setText(begtime);
			holder.tvAddress.setText(ename);
			holder.tvTel.setText(postman);
			holder.tvBoxid.setText("箱号：" + bname);
			holder.tvPoster.setText(postman);
			holder.tvGetTime.setText(endtime);
			holder.btnGet.setVisibility(View.GONE);
		}
		
		return convertView;
	}
	//取件按钮监听
	private class MyonClick implements OnClickListener{
		private int position ;
		public MyonClick(int position){
			this.position = position;
		}
		@Override
		public void onClick(View v) {
			createDialog(position);
		}
		
	}
	
	//取件对话框,取件时用systemid去取
	private void createDialog(final int position){
		Builder builder = new Builder(context);
		builder.setMessage("确认是否取件？");
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
//				int lock = Integer.valueOf(listRecord.get(position).getLockCode());
//				int boxid = Integer.valueOf(listRecord.get(position).getBoxId());
				//取件时用systemid去取
				String systemid = listRecord.get(position).getSystemid() ;
				httpHelper.getExpress(mApp.getUser(), mApp.getPassword(), systemid, 
						Integer.valueOf(TipsHttpError.GET_PACKAGER_MODE_NORMAL), new HttpCallBack(){

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

	private class ViewHolder {
		TextView tvOrder ;
		TextView tvPostDate ;
		TextView tvAddress ;
		TextView tvState ;
		ImageView imgOrder ;
		TextView tvBoxid ;
		TextView tvGetTime ;
		TextView tvTel;
		TextView tvPoster;
		Button btnGet ;
		
	}
}
