package com.szzy.packages.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

/**
 * Õº∆¨  ≈‰∆˜
 * @author mac
 *
 */
public class ImgAdapter extends BaseAdapter {
	
	private Context _context;
	private List<Integer> imgList; 
	public ImgAdapter(Context context,List<Integer> imgList ) {
		_context = context;
		this.imgList=imgList;
	}

	public int getCount() {
		return Integer.MAX_VALUE;
	}

	public Object getItem(int position) {

		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			ImageView imageView = new ImageView(_context);
			imageView.setAdjustViewBounds(true);
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setLayoutParams(new Gallery.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			convertView = imageView;
			viewHolder.imageView = (ImageView) convertView;
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.imageView.setImageResource(imgList.get(position % imgList.size()));
		return convertView;
	}

	private static class ViewHolder {
		ImageView imageView;
	}
}
