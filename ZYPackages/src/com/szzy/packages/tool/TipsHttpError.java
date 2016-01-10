package com.szzy.packages.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

//���ڸ���HTTP������ʾ
public class TipsHttpError {


//	public static final String ERROR_AUTHORITY = "1";  //��Ȩ����
//	public static final String ERROR_GETUSE = "2";   //Ͷ�����˺���Ч
//	public static final String ERROR_BOX_USE = "3"; //�����ѱ�ռ��
//	public static final String ERROR_BOX_ERROR = "4";	//��Ż���Ŵ���
//	public static final String ERROR_RECORD = "5";	//�޴�Ͷ�ݼ�¼
//	public static final String ERROR_USER = "6";	//�û�������
//	public static final String ERROR_CMD = "7";		//�������
//	public static final String ERROR_8 = "8";
//	public static final String ERROR_9 = "9";
//	public static final String ERROR_10 = "10";

//	
//	public static final String ERROR_TIMEOUT = "-1";  //�豸��ʱ����Ӧ;
//	public static final String ERROR_2_ = "-2";
//	public static final String ERROR_3_ = "-3";
//	public static final String ERROR_4_ = "-4";
//	public static final String ERROR_OFF = "-5";//�豸������;
//	public static Context context = null ;
	public static final String OK = "0" ;//��ȷ����
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
	
	public static final String ERROR_UNKNOW = "255";  //δ֪����;
	public static final String ERROR_NETWORK = "256";  //�������ӳ���
	
	/***ȡ��״̬��δȡ***/
	public static final String PACKAGER_STATE_NOT_GET = "1"; //δȡ
	/***ȡ��״̬����ȡ***/
	public static final String PACKAGER_STATE_GET = "2"; //��ȡ
	/***ȡ����ʽ���ռ�������ȡ��***/
	public static final String GET_PACKAGER_MODE_NORMAL = "2"; 
	/***ȡ����ʽ��Ͷ���˳�ʱȡ��***/
	public static final String GET_PACKAGER_MODE_TIMEOUT = "2"; 
	
	
	private static Map<String, String> map1 = new HashMap<String, String>();
	static{
		
		map1.put(ERROR_DEV_TIMEOUT, "�豸��ʱ����Ӧ");
		map1.put(ERROR_RECV_DATA, "���ݽ��մ���");
		map1.put(ERROR_DEV_RESOLVE, "�豸�������");
		map1.put(ERROR_SYS_BISS, "��ǰ����Ա�϶�,ϵͳ��æ");
		map1.put(ERROR_DEV_DISCONN, "�豸�ѶϿ�����");
		map1.put(ERROR_BOX_NOT_USE, "���Ź���,��ͣʹ��");
		map1.put(ERROR_CMD, "�������");
		map1.put(ERROR_USER_NOT, "Ͷ�����˺���Ч");
		map1.put(ERROR_NOT_PRIMISSION, "��Ȩ����");
		map1.put(ERROR_BOX_TYPE, "��������ѡ�����");
		map1.put(ERROR_BOX_IS_USED, "�����ѱ�ʹ��");
		map1.put(ERROR_RECORD, "��¼�Ŵ���");
		map1.put(ERROR_LOCK_ID, "��Ŵ���");
		map1.put(ERROR_ACCOUNT_TYPE, "�˺Ÿ�ʽ����");
		map1.put(ERROR_PASSWORD, "�����ʽ����");
		map1.put(ERROR_IDCARD, "���֤����");
		map1.put(ERROR_EMAIL, "�����ʽ����");
		map1.put(ERROR_ACCOUNT_EXIST, "�˺��Ѵ���");
		map1.put(ERROR_ACCOUNT_NOT_EXIST, "�˺Ų�����");
		map1.put(ERROR_USER_OR_PASS, "�û������������");
		map1.put(ERROR_USER_NOT_AUTH, "�˺�δ��֤,����ϵ����Ա��֤");
		map1.put(ERROR_BOX_SELECT, "����ѡ�����");
		map1.put(ERROR_RECEIVER, "�ռ��˺�����Ч");
		map1.put(ERROR_UNKNOW, "δ֪����");
		map1.put(ERROR_NETWORK, "�������ӳ���");
		
	}
	
	
	/**
	 * ������ʾ
	 * @param context
	 * @param info
	 */
	public static void toastNormal(Context context ,String info){
		Toast.makeText(context, info, Toast.LENGTH_SHORT).show() ;
	}

	/**
	 * 0	��ȷ
-1	�豸��ʱ����Ӧ
-2	���ݽ��մ���
-3	�豸�������
-4	��ǰ����Ա�϶�,ϵͳ��æ
-5	�豸�ѶϿ�����
1	���Ź���,��ͣʹ��
2	�������
3	Ͷ�����˺���Ч
4	��Ȩ����
5	��������ѡ�����
6	�����ѱ�ʹ��
7	��¼�Ŵ���
8	��Ŵ���
10	�˺Ÿ�ʽ����
11	�����ʽ����
12	���֤����
13	�����ʽ����
14	�˺��Ѵ���
15	�˺Ų�����
20	�û������������
21	�˺�δ��֤,����ϵ����Ա��֤
31	����ѡ�����
60	�ռ��˺�����Ч
	 */
	/**
	 * ����http����ʱ�����Ĵ���
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
					//��ȷ���ز�������
					if(errorCode.equals(OK)){
						return ;
					}else{
						msg = "err=" + errorCode + "δ֪����";
					}
					
				}
//				switch (errorCode) {
//				case "0":
//					return;
//				case ERROR_AUTHORITY:
//					msg = "err=" + errorCode + "��Ȩ�޲���";
//					break;
//				case ERROR_GETUSE:
//					msg = "err=" + errorCode + "Ͷ�����˺���Ч";
//					break;
//				case ERROR_BOX_USE:
//					msg = "err=" + errorCode + "�����ѱ�ռ��";
//					break;
//				case ERROR_BOX_ERROR:
//					msg = "err=" + errorCode + "��Ż���Ŵ���";
//					break;
//				case ERROR_RECORD:
//					msg = "err=" + errorCode + "�޴�Ͷ�ݼ�¼";
//					break;
//				case ERROR_USER:
//					msg = "err=" + errorCode + "�û�������";
//					break;
//				case ERROR_CMD:
//					msg = "err=" + errorCode + "�������";
//					break;
//				case ERROR_8:
////					msg = "err=" + errorCode + "�����ѱ�ռ��";
//					break;
//				case ERROR_9:
////					msg = "err=" + errorCode + "Ͷ���ֻ�������ʹ��Ȩ��";
//					break;
//				case ERROR_10:
////					msg = "err=" + errorCode + "�޴�Ͷ�ݼ�¼";
//					break;
//				case ERROR_UNKNOW:
//					msg = "err=" + errorCode + "δ֪����";
//					break;
//				case ERROR_NETWORK:
//					msg =  "�������ӳ�����������";
//					break;
//				case ERROR_TIMEOUT:
//					msg = "err=" + errorCode + "�豸��ʱ����Ӧ";
//					break ;
//				case ERROR_2_:
//					msg = "err=" + errorCode + "���ݽ��մ���";
//					break ;
//				case ERROR_3_:
//					msg = "err=" + errorCode + "�豸�������";
//					break ;
//				case ERROR_4_:
//					msg = "err=" + errorCode + "��ǰ����Ա�϶࣬ϵͳ��æ";
//					break ;
//				case ERROR_OFF:
//					msg = "err=" + errorCode + "�豸�ѶϿ�����";
//					break ;
//
//				default:
//					msg = "err=" + errorCode + "δ֪����";
//					break ;
//				}
				Toast.makeText(context, msg, 0).show();
			}
		});
	}
}
