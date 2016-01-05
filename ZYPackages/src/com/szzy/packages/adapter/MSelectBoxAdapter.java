package com.szzy.packages.adapter;

import java.util.List;

import com.szzy.packages.R;
import com.szzy.packages.entity.Box;

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
	private  List<Box> listBox ;
	private LayoutInflater inflater ;
	public MSelectBoxAdapter(Context context, List<Box> listBox){
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
		//添加数据
		holder.tvBoxId.setText(listBox.get(position).getBoxName());
		int typeInt = Integer.valueOf(listBox.get(position).getBoxType());
		switch (typeInt) {
		case 1:
			holder.tvBoxType.setText("快递柜（大）");
			break;
		case 2:
			holder.tvBoxType.setText("快递柜（中）");
			break;
		case 3:
			holder.tvBoxType.setText("快递柜（小）");
			break;
		case 4:
			holder.tvBoxType.setText("信报柜");
			break;
		case 5:
			holder.tvBoxType.setText("展览柜");
			break;

		default:
			break;
		}
//		Log.e("state", listBox.get(position).getBoxState().trim());
		if("0".equals(listBox.get(position).getBoxState().trim())){
			holder.tvBoxState.setText("可投递");
			holder.layout.setBackgroundResource(R.drawable.corners_bg_select);
		}else{
			holder.tvBoxState.setText("已占用");
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
