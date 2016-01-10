package com.szzy.packages.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.szzy.packages.R;
import com.szzy.packages.adapter.MSelectBoxAdapter;
import com.szzy.packages.entity.Box;
import com.szzy.packages.entity.BoxInfo;

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
/**
 * ѡ��Ͷ�ݹ�
 * @author mac
 *
 */
public class SelectBoxActivity extends Activity {

	private ImageView imgBack ; //����
	private GridView gridViewBoxs ;//�����������
	private MApplication mApp ;
//	private List<Box> listBox ;
	private List<BoxInfo> listBox ;
//	private SimpleAdapter simpleAdapter ;
	private MSelectBoxAdapter mAdapter ;
	private List<Map<String, String>> listMap ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selectbox);
		mApp = (MApplication) getApplication();
		//��ȡȫ������
		listBox = mApp.getListBoxInfo();
		initView();
	}
	
	//��ʼ��UI
	private void initView(){
		imgBack = (ImageView) findViewById(R.id.imageView_select_box_back);
		gridViewBoxs = (GridView) findViewById(R.id.gridView_select_box);
		mAdapter = new MSelectBoxAdapter(this, listBox);
		gridViewBoxs.setAdapter(mAdapter);
		//����
		imgBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		//�������ŵ���¼�
		gridViewBoxs.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				Box box = listBox.get(position);
				BoxInfo box = listBox.get(position) ;
				if("0".equals(box.getBstate())){
//					Toast.makeText(mApp, "����", 0).show();
					Intent intent = new Intent();
					intent.putExtra("position", position);
					SelectBoxActivity.this.setResult(RESULT_OK, intent);
					finish();
				}else{
					Toast.makeText(mApp, "���䲻���ã���ѡ���Ͷ�ݵ����", 0).show();
				}
				
			}
		});
	}
}
