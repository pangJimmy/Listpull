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
 * �Ĵ�ȡ��������
 * @author mac
 *
 */
public class FamilyQueryAdapter extends BaseAdapter {

	List<UserPostRecord> listRecord ;
	
	private LayoutInflater inflater ;//ע����
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
			holder.tvBegTime = (TextView) convertView.findViewById(R.id.textView_listview_item_get_family_begtime) ;  //���ʱ��
			holder.tvEname  = (TextView) convertView.findViewById(R.id.textView_listview_item_get_family_ename) ;//������
			holder.tvBname = (TextView) convertView.findViewById(R.id.textView_listview_item_get_family_boxname) ;//������
			holder.tvSetMen  = (TextView) convertView.findViewById(R.id.textView_listview_item_get_family_post) ;//�Ĵ���
			holder.tvGetMen  = (TextView) convertView.findViewById(R.id.textView_listview_item_get_family_geter) ;//ȡ����
			holder.tvEndTime  = (TextView) convertView.findViewById(R.id.textView_listview_item_get_family_gettime) ;//ȡ��ʱ��
			holder.tvState  = (TextView) convertView.findViewById(R.id.textView_listview_item_get_family_state) ;//����״̬
			holder.btnGet  = (Button) convertView.findViewById(R.id.button_listview_item_get_family_get_package) ;//ȡ��
			convertView.setTag(holder) ;
		}else{
			holder = (ViewHolder) convertView.getTag() ;
		}
		//����ƥ�����
		UserPostRecord record = listRecord.get(position) ;
		if(record.getState().equals("2")){//ȡ��
			holder.tvBegTime.setText(record.getBegtime()) ;  //���ʱ��
			holder.tvEname.setText(record.getEname()) ;//������
			holder.tvBname.setText(record.getBname()) ;//������
			holder.tvSetMen.setText(record.getSetman());//�Ĵ���
			holder.tvGetMen.setText(record.getGetman());//ȡ����
			holder.tvEndTime.setVisibility(View.VISIBLE) ;//ȡ��ʱ��
			holder.tvEndTime.setText(record.getEndtime()) ;
			holder.tvState.setText("��ȡ") ;//����״̬
			holder.btnGet.setVisibility(View.GONE) ;//ȡ��
			
		}else{
			holder.tvBegTime.setText(record.getBegtime()) ;  //���ʱ��
			holder.tvEname.setText(record.getEname()) ;//������
			holder.tvBname.setText(record.getBname()) ;//������
			holder.tvSetMen.setText(record.getSetman());//�Ĵ���
			holder.tvGetMen.setText(record.getGetman());//ȡ����
			holder.tvEndTime.setText(record.getEndtime()) ;//ȡ��ʱ��
			holder.tvState.setText("δȡ") ;//����״̬
			holder.btnGet.setVisibility(View.VISIBLE) ;//ȡ��
			holder.tvEndTime.setVisibility(View.GONE) ;//ȡ��ʱ��
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
			//�����Ի���
			createDialog(position , systemid) ;
		}
		
	}
	
	
	class ViewHolder {
		TextView tvBegTime ;  //���ʱ��
		TextView tvEname  ;//������
		TextView tvBname ;//������
		TextView tvSetMen ;//�Ĵ���
		TextView tvGetMen ;//ȡ����
		TextView tvEndTime ;//ȡ��ʱ��
		TextView tvState ;//����״̬
		Button btnGet ;//ȡ��
		
	}

	//ȡ���Ի���,ȡ��ʱ��systemidȥȡ
	private void createDialog(final int position, final String systemid){
		Builder builder = new Builder(context);
		builder.setMessage("ȷ���Ƿ�ȡ����");
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//ȡ��ʱ��systemidȥȡ
//				String systemid = listRecord.get(position).getSystemid() ;
				httpHelper.userGet(mApp.getUser(), mApp.getPassword(), systemid, new HttpCallBack(){

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
}
