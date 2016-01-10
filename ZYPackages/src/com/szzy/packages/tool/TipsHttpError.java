package com.szzy.packages.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

//用于给出HTTP错误提示
public class TipsHttpError {


//	public static final String ERROR_AUTHORITY = "1";  //无权操作
//	public static final String ERROR_GETUSE = "2";   //投递人账号无效
//	public static final String ERROR_BOX_USE = "3"; //箱门已被占用
//	public static final String ERROR_BOX_ERROR = "4";	//柜号或箱号错误
//	public static final String ERROR_RECORD = "5";	//无此投递记录
//	public static final String ERROR_USER = "6";	//用户不存在
//	public static final String ERROR_CMD = "7";		//命令错误
//	public static final String ERROR_8 = "8";
//	public static final String ERROR_9 = "9";
//	public static final String ERROR_10 = "10";

//	
//	public static final String ERROR_TIMEOUT = "-1";  //设备超时无响应;
//	public static final String ERROR_2_ = "-2";
//	public static final String ERROR_3_ = "-3";
//	public static final String ERROR_4_ = "-4";
//	public static final String ERROR_OFF = "-5";//设备不在线;
//	public static Context context = null ;
	public static final String OK = "0" ;//正确返回
	private final static String ERROR_DEV_TIMEOUT = "-1";
	private final static  String ERROR_RECV_DATA = "-2";
	private final static  String ERROR_DEV_RESOLVE = "-3";
	private final static  String ERROR_SYS_BISS = "-4";
	private final static  String ERROR_DEV_DISCONN = "-5";
	private final static  String ERROR_BOX_NOT_USE = "1";
	private final static  String ERROR_CMD = "2";
	private final static  String ERROR_USER_NOT = "3";
	private final static  String ERROR_NOT_PRIMISSION = "4";
	private final static  String ERROR_BOX_TYPE = "5";
	private final static  String ERROR_BOX_IS_USED = "6";
	private final static  String ERROR_RECORD = "7";
	private final static  String ERROR_LOCK_ID = "8";
	private final static  String ERROR_ACCOUNT_TYPE = "10";
	private final static  String ERROR_PASSWORD = "11";
	private final static  String ERROR_IDCARD = "12";
	private final static  String ERROR_EMAIL = "13";
	private final static  String ERROR_ACCOUNT_EXIST = "14";
	private final static  String ERROR_ACCOUNT_NOT_EXIST = "15";
	
	private final static  String ERROR_USER_OR_PASS = "20";
	private final static  String ERROR_USER_NOT_AUTH = "21";
	private final static  String ERROR_BOX_SELECT = "31";
	private final static  String ERROR_RECEIVER = "60";
	
	public static final String ERROR_UNKNOW = "255";  //未知错误;
	public static final String ERROR_NETWORK = "256";  //网络连接出错
	
	/***取件状态：未取***/
	public static final String PACKAGER_STATE_NOT_GET = "1"; //未取
	/***取件状态：已取***/
	public static final String PACKAGER_STATE_GET = "2"; //已取
	/***取件方式：收件人正常取件***/
	public static final String GET_PACKAGER_MODE_NORMAL = "2"; 
	/***取件方式：投件人超时取件***/
	public static final String GET_PACKAGER_MODE_TIMEOUT = "2"; 
	
	
	private static Map<String, String> map1 = new HashMap<String, String>();
	static{
		
		map1.put(ERROR_DEV_TIMEOUT, "设备超时无响应");
		map1.put(ERROR_RECV_DATA, "数据接收错误");
		map1.put(ERROR_DEV_RESOLVE, "设备处理错误");
		map1.put(ERROR_SYS_BISS, "当前操作员较多,系统繁忙");
		map1.put(ERROR_DEV_DISCONN, "设备已断开连接");
		map1.put(ERROR_BOX_NOT_USE, "箱门故障,暂停使用");
		map1.put(ERROR_CMD, "命令错误");
		map1.put(ERROR_USER_NOT, "投递人账号无效");
		map1.put(ERROR_NOT_PRIMISSION, "无权操作");
		map1.put(ERROR_BOX_TYPE, "箱门种类选择错误");
		map1.put(ERROR_BOX_IS_USED, "此箱已被使用");
		map1.put(ERROR_RECORD, "记录号错误");
		map1.put(ERROR_LOCK_ID, "柜号错误");
		map1.put(ERROR_ACCOUNT_TYPE, "账号格式错误");
		map1.put(ERROR_PASSWORD, "密码格式错误");
		map1.put(ERROR_IDCARD, "身份证错误");
		map1.put(ERROR_EMAIL, "邮箱格式错误");
		map1.put(ERROR_ACCOUNT_EXIST, "账号已存在");
		map1.put(ERROR_ACCOUNT_NOT_EXIST, "账号不存在");
		map1.put(ERROR_USER_OR_PASS, "用户名或密码错误");
		map1.put(ERROR_USER_NOT_AUTH, "账号未认证,请联系管理员认证");
		map1.put(ERROR_BOX_SELECT, "箱门选择错误");
		map1.put(ERROR_RECEIVER, "收件人号码无效");
		map1.put(ERROR_UNKNOW, "未知错误");
		map1.put(ERROR_NETWORK, "网络连接出错");
		
	}
	
	
	/**
	 * 正常提示
	 * @param context
	 * @param info
	 */
	public static void toastNormal(Context context ,String info){
		Toast.makeText(context, info, Toast.LENGTH_SHORT).show() ;
	}

	/**
	 * 0	正确
-1	设备超时无响应
-2	数据接收错误
-3	设备处理错误
-4	当前操作员较多,系统繁忙
-5	设备已断开连接
1	箱门故障,暂停使用
2	命令错误
3	投递人账号无效
4	无权操作
5	箱门种类选择错误
6	此箱已被使用
7	记录号错误
8	柜号错误
10	账号格式错误
11	密码格式错误
12	身份证错误
13	邮箱格式错误
14	账号已存在
15	账号不存在
20	用户名或密码错误
21	账号未认证,请联系管理员认证
31	箱门选择错误
60	收件人号码无效
	 */
	/**
	 * 给出http请求时报出的错误
	 * @param errorCode
	 */
	public static void toastError(final Context context ,final String errorCode){
		Handler handler = new Handler();
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				String msg = "";
				if(map1.containsKey(errorCode)){
					msg = "err=" + errorCode + map1.get(errorCode);
				}else{
					//正确返回不做处理
					if(errorCode.equals(OK)){
						return ;
					}else{
						msg = "err=" + errorCode + "未知错误";
					}
					
				}
//				switch (errorCode) {
//				case "0":
//					return;
//				case ERROR_AUTHORITY:
//					msg = "err=" + errorCode + "无权限操作";
//					break;
//				case ERROR_GETUSE:
//					msg = "err=" + errorCode + "投递人账号无效";
//					break;
//				case ERROR_BOX_USE:
//					msg = "err=" + errorCode + "箱门已被占用";
//					break;
//				case ERROR_BOX_ERROR:
//					msg = "err=" + errorCode + "柜号或箱号错误";
//					break;
//				case ERROR_RECORD:
//					msg = "err=" + errorCode + "无此投递记录";
//					break;
//				case ERROR_USER:
//					msg = "err=" + errorCode + "用户不存在";
//					break;
//				case ERROR_CMD:
//					msg = "err=" + errorCode + "命令错误";
//					break;
//				case ERROR_8:
////					msg = "err=" + errorCode + "箱门已被占用";
//					break;
//				case ERROR_9:
////					msg = "err=" + errorCode + "投递手机号码无使用权限";
//					break;
//				case ERROR_10:
////					msg = "err=" + errorCode + "无此投递记录";
//					break;
//				case ERROR_UNKNOW:
//					msg = "err=" + errorCode + "未知错误";
//					break;
//				case ERROR_NETWORK:
//					msg =  "网络连接出错，请检查网络";
//					break;
//				case ERROR_TIMEOUT:
//					msg = "err=" + errorCode + "设备超时无响应";
//					break ;
//				case ERROR_2_:
//					msg = "err=" + errorCode + "数据接收错误";
//					break ;
//				case ERROR_3_:
//					msg = "err=" + errorCode + "设备处理错误";
//					break ;
//				case ERROR_4_:
//					msg = "err=" + errorCode + "当前操作员较多，系统繁忙";
//					break ;
//				case ERROR_OFF:
//					msg = "err=" + errorCode + "设备已断开连接";
//					break ;
//
//				default:
//					msg = "err=" + errorCode + "未知错误";
//					break ;
//				}
				Toast.makeText(context, msg, 0).show();
			}
		});
	}
}
