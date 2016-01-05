package com.szzy.packages.fragment;

import com.szzy.packages.R;
import com.szzy.packages.activity.MApplication;
import com.szzy.packages.entity.UserInfo;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class UserFragment extends Fragment {
	private Context context ;
	private View rootView ;
	
	private TextView tvLoginName ;
	private TextView tvTel ;
	private TextView tvRealName  ;
	private TextView tvIdCard ;
	private TextView tvEmail ;
	private TextView tvUserPermission ;
	private Button btnExit ;
	
	private MApplication mApp ;
	private UserInfo userInfo ;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = getActivity() ;
		mApp = (MApplication) context.getApplicationContext() ;
		userInfo = mApp.getUserInfo() ;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_user, null);
		initView() ;
		return rootView;
	}
	
	private void initView(){
		tvLoginName = (TextView) rootView.findViewById(R.id.textView_user_login_name) ;
		tvTel = (TextView) rootView.findViewById(R.id.textView_user_tel) ;
		tvRealName = (TextView) rootView.findViewById(R.id.textView_user_real_name) ;
		tvIdCard = (TextView) rootView.findViewById(R.id.textView_user_id_card) ;
		tvEmail = (TextView) rootView.findViewById(R.id.textView_user_email) ;
		tvUserPermission = (TextView) rootView.findViewById(R.id.textView_user_permision) ;
		btnExit = (Button) rootView.findViewById(R.id.button_user_exit) ;

//		tvLoginName.setText("" + userInfo.getLoginName()) ;
//		tvTel.setText("" + userInfo.getTel()) ;
//		tvRealName.setText("" + userInfo.getUserName()) ;
//		tvIdCard.setText("" + userInfo.getIdCard()) ;
//		tvEmail.setText("" + userInfo.getEmail()) ;
//		if("1".equals(userInfo.getState())){
//			tvUserPermission.setText("已认证") ;
//		}else{
//			tvUserPermission.setText("未认证，请核实身份") ;
//		}
//		tvUserPermission.setText(userInfo.get) ;

	}
}
