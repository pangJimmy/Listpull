package com.szzy.packages.adapter;

import java.util.List;

import com.szzy.packages.R;
import com.szzy.packages.entity.Box;
import com.szzy.packages.entity.BoxInfo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MSelectBoxAdapter extends BaseAdapter {

	private Context context ;
	private  List<BoxInfo> listBox ;
	private LayoutInflater inflater ;
	public MSelectBoxAdapter(Context context, List<BoxInfo> listBox){
		this.context = context ;
		this.listBox = listBox ;
		this.inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
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
		ViewHolder holder = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.gridview_item_box, null);
			holder = new ViewHolder();
			holder.layout = (LinearLayout) convertView.findViewById(R.id.layout_grid_item);
			holder.tvBoxId = (TextView) convertView.findViewById(R.id.textView_grid_item_boxid);
			holder.tvBoxType = (TextView) convertView.findViewById(R.id.textView_grid_item_boxtype);
			holder.tvBoxState = (TextView) convertView.findViewById(R.id.textView_grid_item_boxstate);
			convertView.setTag(holder);
			
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		//�������
		holder.tvBoxId.setText(listBox.get(position).getBname());
		int typeInt = Integer.valueOf(listBox.get(position).getBtype());
		switch (typeInt) {
		case 1:
			holder.tvBoxType.setText("��ݹ񣨴�");
			break;
		case 2:
			holder.tvBoxType.setText("��ݹ��У�");
			break;
		case 3:
			holder.tvBoxType.setText("��ݹ�С��");
			break;
		case 4:
			holder.tvBoxType.setText("�ű���");
			break;
		case 5:
			holder.tvBoxType.setText("չ����");
			break;

		default:
			break;
		}
//		Log.e("state", listBox.get(position).getBoxState().trim());
		if("0".equals(listBox.get(position).getBstate().trim())){
			holder.tvBoxState.setText("��Ͷ��");
			holder.layout.setBackgroundResource(R.drawable.corners_bg_select);
		}else if("1".equals(listBox.get(position).getBstate().trim())){
			holder.tvBoxState.setText("��ռ��");
			holder.layout.setBackgroundResource(R.drawable.corners_background);
		}else if("2".equals(listBox.get(position).getBstate().trim())){
			holder.tvBoxState.setText("���Ź���");
			holder.layout.setBackgroundResource(R.drawable.corners_background);
		}
		
		
		
		return convertView;
	}
	
	private class ViewHolder{
		LinearLayout layout ;
		TextView tvBoxId ;
		TextView tvBoxType ;
		TextView tvBoxState;
		
	}

}
