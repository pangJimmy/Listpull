package com.szzy.packages.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.szzy.packages.R;

/**
 * Ê×Ò³GridViewÊÊÅäÆ÷
 * @author mac
 *
 */
public class HomeMenuGridAdapter extends BaseAdapter {
	private Context context ;
	private List<Map<String, Object>> listData ;

	public HomeMenuGridAdapter(Context context, List<Map<String, Object>> listData){
		this.context = context ;
		this.listData = listData ;
	}
	
	@Override
	public int getCount() {
		
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHold hold = null ;
		if(convertView == null){
			hold = new ViewHold() ;
			convertView = LayoutInflater.from(context).inflate(R.layout.home_menu_gridview_item, null);
			hold.img = (ImageView) convertView.findViewById(R.id.imageView_home_menu_img) ;
			hold.tv = (TextView) convertView.findViewById(R.id.textView_home_menu_tv) ;
			convertView.setTag(hold) ;
		}else{
			hold = (ViewHold) convertView.getTag() ;
		}
		//Ìî³äÊý¾Ý
		hold.img.setImageResource((Integer)listData.get(position).get("menuImg"));
		hold.tv.setText((String)listData.get(position).get("menuItem")) ;
		return convertView;
	}
	
	private class ViewHold{
		ImageView img ;
		TextView tv ;
	}

}
