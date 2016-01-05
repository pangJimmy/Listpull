package com.szzy.packages.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.szzy.packages.R;
import com.szzy.packages.adapter.MSelectBoxAdapter;
import com.szzy.packages.entity.Box;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
//选择投递柜
public class SelectBoxActivity extends Activity {

	private ImageView imgBack ; //返回
	private GridView gridViewBoxs ;//用于填充箱子
	private MApplication mApp ;
	private List<Box> listBox ;
//	private SimpleAdapter simpleAdapter ;
	private MSelectBoxAdapter mAdapter ;
	private List<Map<String, String>> listMap ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selectbox);
		mApp = (MApplication) getApplication();
		listBox = mApp.getListBox();
		initView();
	}
	
	private void initView(){
		imgBack = (ImageView) findViewById(R.id.imageView_select_box_back);
		gridViewBoxs = (GridView) findViewById(R.id.gridView_select_box);
		mAdapter = new MSelectBoxAdapter(this, listBox);
		gridViewBoxs.setAdapter(mAdapter);
		imgBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		gridViewBoxs.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Box box = listBox.get(position);
				if("0".equals(box.getBoxState())){
//					Toast.makeText(mApp, "可用", 0).show();
					Intent intent = new Intent();
					intent.putExtra("position", position);
					SelectBoxActivity.this.setResult(RESULT_OK, intent);
					finish();
				}else{
					Toast.makeText(mApp, "此箱不可用，请选择可投递的箱柜", 0).show();
				}
				
			}
		});
	}
}
