package com.szzy.packages.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.szzy.packages.R;
import com.szzy.packages.tool.Utils;
/**
 * 业主寄存界面
 * @author mac
 *
 */
public class UserPostActivity extends Activity implements OnClickListener{
	private TextView tvTitle ;  //标题
	private ImageView imgBack ;  //返回键
	private ListView listMenu ; 
	//菜单主标题
	private String[] strMenus = new String[]{"快递待收", "服务上门", "亲友寄存" , "寄存取件"} ;
	//菜单副标题
	private String[] strSubs = new String[]{"我要发哪家快递", "我要洗衣、洗车", "我寄你拿，妈妈再也不担心我没带钥匙了" ,"我寄你拿，妈妈再也不担心我没带钥匙了"};
	
	private List<Map<String, Object>> listData ;   //数据
	private ListViewUPadapter adapter ; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_post) ;
		initView() ;
	}
	//初始化UI 
	private void initView(){
		tvTitle = (TextView) findViewById(R.id.textView_title) ;
		//设置标题
		tvTitle.setText("业主寄存") ;
		imgBack = (ImageView) findViewById(R.id.imageButton_back) ;
		imgBack.setOnClickListener(this) ;
		listMenu = (ListView) findViewById(R.id.listView_user_post) ;
		listData = new ArrayList<Map<String, Object>>();
		//添加菜单数据
		for(int i = 0; i < strMenus.length; i++){
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("textMenu", strMenus[i]);
			map.put("textSub", strSubs[i]);
			listData.add(map);
		}
		adapter = new ListViewUPadapter() ;
		listMenu.setAdapter(adapter) ;
		//监听Item点击事件
		listMenu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent() ;
				switch (position) {
				case 0://快递待收
					intent.setClass(UserPostActivity.this, UserPostExpressPost.class) ;
					startActivity(intent) ;
					break;
				case 1://服务上门
					intent.setClass(UserPostActivity.this, OtherServiceCompanyActivity.class) ;
					startActivity(intent) ;
//					Utils.tipsUnfinish(UserPostActivity.this) ;
					break;
				case 2://亲友寄存
					intent.setClass(UserPostActivity.this, FamilyPostActivity.class) ;
					startActivity(intent) ;
//					Utils.tipsUnfinish(UserPostActivity.this) ;
					break;
				case 3://寄存取件
					intent.setClass(UserPostActivity.this, GetFamilyPostActivity.class) ;
					startActivity(intent) ;
//					Utils.tipsUnfinish(UserPostActivity.this) ;
					break;

				default:
					break;
				}
				
			}
		});
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageButton_back://返回
			finish() ;
			break;

		default:
			break;
		}
		
	}
	
	//listView适配器
	private class ListViewUPadapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
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
				//注入布局
				convertView = LayoutInflater.from(UserPostActivity.this).inflate(R.layout.listview_item_package_menu, null);
//				hold.img = (ImageView) convertView.findViewById(R.id.imageView_listview_item_package_menu) ;
				hold.tvMenu = (TextView) convertView.findViewById(R.id.textView_listview_item_package_menu) ;
				hold.tvSub = (TextView) convertView.findViewById(R.id.textView_listview_item_package_menu_sub) ;
				convertView.setTag(hold) ;
			}else{
				hold = (ViewHold) convertView.getTag() ;
			}
			//添加数据
//			hold.img.setImageResource((Integer)listData.get(position).get("imgMenu")) ;
			hold.tvMenu.setText((String) listData.get(position).get("textMenu")) ;
			hold.tvSub.setText((String) listData.get(position).get("textSub")) ;
			return convertView;
		}
		
		private class ViewHold{
			TextView tvSub ; //菜单副标题
			TextView tvMenu ;//菜单主标题
		}
		
	}
}
