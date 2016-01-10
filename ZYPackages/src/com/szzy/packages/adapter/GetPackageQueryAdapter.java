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

	private LayoutInflater inflater ;//ע����
//	private List<PostPackages> listPackages ;
	private List<UserGetRecord> listRecord ;//��listRecord����listPackages
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
			//ע�벼���ļ�
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
		 String begtime = listRecord.get(position).getBegtime(); // ���ʱ��
		 String ename = listRecord.get(position).getEname(); // ������
		 String bname = listRecord.get(position).getBname(); // ������
		 String postman = listRecord.get(position).getPostman(); // ȡ�����ֻ���
		 String order = listRecord.get(position).getOrder(); // ������� �� �ɿ�
		 String endtime = listRecord.get(position).getEndtime(); // ȡ��ʱ�䣬�ɿ�
		 String getstyle = listRecord.get(position).getGetstyle(); // ȡ�����ǣ�(δȡ��0)
		 String state = listRecord.get(position).getState(); // �Ĵ�״̬(0δȡ��1��ȡ)
		//����������item, PACKAGER_STATE_NOT_GET����״̬δȡ
		if(state != null && state.equals(TipsHttpError.PACKAGER_STATE_NOT_GET)){
			holder.tvState.setText("δǩ��");
			holder.tvOrder.setText(order);
			holder.tvPostDate.setText(begtime);
			holder.tvAddress.setText(ename);
			holder.tvTel.setText(postman);
			holder.tvBoxid.setText("��ţ�" + bname);
			holder.tvPoster.setText(postman);
			holder.btnGet.setVisibility(View.VISIBLE);
			holder.tvGetTime.setVisibility(View.GONE) ;
			holder.btnGet.setOnClickListener(new MyonClick(position));
		}else if(state != null){
			holder.tvState.setText("��ǩ��");
			holder.tvOrder.setText(order);
			holder.tvPostDate.setText(begtime);
			holder.tvAddress.setText(ename);
			holder.tvTel.setText(postman);
			holder.tvBoxid.setText("��ţ�" + bname);
			holder.tvPoster.setText(postman);
			holder.tvGetTime.setText(endtime);
			holder.btnGet.setVisibility(View.GONE);
		}
		
		return convertView;
	}
	//ȡ����ť����
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
	
	//ȡ���Ի���,ȡ��ʱ��systemidȥȡ
	private void createDialog(final int position){
		Builder builder = new Builder(context);
		builder.setMessage("ȷ���Ƿ�ȡ����");
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
//				int lock = Integer.valueOf(listRecord.get(position).getLockCode());
//				int boxid = Integer.valueOf(listRecord.get(position).getBoxId());
				//ȡ��ʱ��systemidȥȡ
				String systemid = listRecord.get(position).getSystemid() ;
				httpHelper.getExpress(mApp.getUser(), mApp.getPassword(), systemid, 
						Integer.valueOf(TipsHttpError.GET_PACKAGER_MODE_NORMAL), new HttpCallBack(){

							@Override
							public void call(Object obj, final String err) {
								//���ؿ�����
								mHandler.post(new Runnable() {
									
									@Override
									public void run() {
										if(err.equals(TipsHttpError.OK)){
											//���͹㲥
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
		builder.setNegativeButton("ȡ��", null);
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
