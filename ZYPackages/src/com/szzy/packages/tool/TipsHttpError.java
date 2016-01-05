package com.szzy.packages.tool;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

//用于给出HTTP错误提示
public class TipsHttpError {

	/**
	 * #define Http_err_unknown	0xFF			//未知错误;
#define Http_err_OK			0x00			//正确,无错误
#define Http_err_authority	0x01			//无权操作
#define Http_err_GetUse		0x02			//投递人账号无效
#define Http_err_BoxUse		0x03			//箱门已被占用
#define Http_err_BoxErr		0x04			//柜号或箱号错误
#define Http_err_Record		0x05			//无此投递记录
#define Http_err_user		0x06			//用户不存在
#define Http_err_cmd		0x07			//命令错误
#define Http_err_Off		-5	//设备不在线;
#define Http_err_Timeout	-1	//设备超时无响应;
	 */
	public static final String OK = "0" ;//正确返回
	public static final String ERROR_AUTHORITY = "1";  //无权操作
	public static final String ERROR_GETUSE = "2";   //投递人账号无效
	public static final String ERROR_BOX_USE = "3"; //箱门已被占用
	public static final String ERROR_BOX_ERROR = "4";	//柜号或箱号错误
	public static final String ERROR_RECORD = "5";	//无此投递记录
	public static final String ERROR_USER = "6";	//用户不存在
	public static final String ERROR_CMD = "7";		//命令错误
	public static final String ERROR_8 = "8";
	public static final String ERROR_9 = "9";
	public static final String ERROR_10 = "10";
	public static final String ERROR_UNKNOW = "255";  //未知错误;
	public static final String ERROR_NETWORK = "256";  //网络连接出错
	
	public static final String ERROR_TIMEOUT = "-1";  //设备超时无响应;
	public static final String ERROR_2_ = "-2";
	public static final String ERROR_3_ = "-3";
	public static final String ERROR_4_ = "-4";
	public static final String ERROR_OFF = "-5";//设备不在线;
//	public static Context context = null ;
	
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
				switch (errorCode) {
				case "0":
					return;
				case ERROR_AUTHORITY:
					msg = "err=" + errorCode + "无权限操作";
					break;
				case ERROR_GETUSE:
					msg = "err=" + errorCode + "投递人账号无效";
					break;
				case ERROR_BOX_USE:
					msg = "err=" + errorCode + "箱门已被占用";
					break;
				case ERROR_BOX_ERROR:
					msg = "err=" + errorCode + "柜号或箱号错误";
					break;
				case ERROR_RECORD:
					msg = "err=" + errorCode + "无此投递记录";
					break;
				case ERROR_USER:
					msg = "err=" + errorCode + "用户不存在";
					break;
				case ERROR_CMD:
					msg = "err=" + errorCode + "命令错误";
					break;
				case ERROR_8:
//					msg = "err=" + errorCode + "箱门已被占用";
					break;
				case ERROR_9:
//					msg = "err=" + errorCode + "投递手机号码无使用权限";
					break;
				case ERROR_10:
//					msg = "err=" + errorCode + "无此投递记录";
					break;
				case ERROR_UNKNOW:
					msg = "err=" + errorCode + "未知错误";
					break;
				case ERROR_NETWORK:
					msg =  "网络连接出错，请检查网络";
					break;
				case ERROR_TIMEOUT:
					msg = "err=" + errorCode + "设备超时无响应";
					break ;
				case ERROR_2_:
					msg = "err=" + errorCode + "数据接收错误";
					break ;
				case ERROR_3_:
					msg = "err=" + errorCode + "设备处理错误";
					break ;
				case ERROR_4_:
					msg = "err=" + errorCode + "当前操作员较多，系统繁忙";
					break ;
				case ERROR_OFF:
					msg = "err=" + errorCode + "设备已断开连接";
					break ;

				default:
					msg = "err=" + errorCode + "未知错误";
					break ;
				}
				Toast.makeText(context, msg, 0).show();
			}
		});
	}
}
