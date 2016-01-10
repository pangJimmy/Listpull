package com.szzy.packages.http;

import java.net.URI;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import com.szzy.packages.entity.GetPackages;
import com.szzy.packages.entity.PostBoxInfo;
import com.szzy.packages.entity.PostPackages;
import com.szzy.packages.tool.TipsHttpError;

import android.os.Handler;
import android.util.Log;

/**
 * http������
 * @author mac
 *
 */
public class HttpHelper {
	/**��������ַ***////
	private final String BASE_URI = "http://192.168.1.51:8009/";//http://112.74.67.62:8008/     //szzhenyao.6655.la:8008
	/**���Ӳ���ָ��***/
	private final int CMD_Test = 1 ;
	/**�û���¼ָ��***/
	private final int CMD_LOGIN = 3 ;
	/**ȡ����ѯָ��***/
	private final int CMD_GET_PACKAGE_QUEERY = 3 ;
	/**Ͷ����ѯָ��***/
	private final int CMD_POST_QUEERY = 4 ;
	/**��֤Ͷ��ʹ��Ȩָ��***/
	private final int CMD_CHECK_POST_PERMISSION = 5 ;
	/**��֤ʹ��Ȩָ��***/
	private final int CMD_CHECK_USER_PERMISSION = 6 ;
	/**��ȡ�����豸����״ָ̬��***/
	private final int CMD_GET_BOX_STATE = 7 ;
	/**Ͷ����Ʒָ��***/
	private final int CMD_POSET = 8 ;
	/**ȡ����Ʒָ��***/
	private final int CMD_GET_PACKAGE = 9 ;
	/**��ȡ����Ͷ��Ȩָ��***/
	private final int CMD_GET_ALL_POST = 10 ;
	/**��ȡ����ʹ��Ȩָ��***/
	private final int CMD_GET_ALL_USE = 11 ;
	/**����ָ��***/
	private final int CMD_OPEN_BOX = 12 ;
	
	/****--------------------�ָ�xian----------------------****/
	/**ҵ����¼ģʽ***/
	public static final String LOGIN_MODE_USER = "1" ;
	/**Ͷ��Ա��¼ģʽ***/
	public static final String LOGIN_MODE_POSTER = "2" ;
	/**����Ա��¼ģʽ***/
	public static final String LOGIN_MODE_ADMIN = "3" ;
	/**����û��Ƿ����**/
	private final int CMD_HTTP_USER_EXIST = 1 ;
	/**ע���û�**/
	private final int CMD_HTTP_REGISTER_USER = 2 ;
	/**�û���¼**/
	private final int CMD_HTTP_LOGIN = 3 ;
	/**Ͷ���ż�**/
	private final int CMD_HTTP_POST_MAIL = 4 ;
	/**��������**/
	private final int CMD_HTTP_OPEN_MAIL_BOX = 5 ;
	/**Ͷ���**/
	private final int CMD_HTTP_POST_EXPRESS = 6 ;
	/**ȡ���**/
	private final int CMD_HTTP_GET_EXPRESS = 7 ;
	/**�û��Ĵ�**/
	private final int CMD_HTTP_USER_POST = 8;
	/**ȡ�Ĵ���Ʒ**/
	private final int CMD_HTTP_USER_GET = 9 ;
	/**����������**/
	private final int CMD_OTHER_ = 10 ;
	/**�������մ�**/
	private final int CMD_OTHER_GET = 11 ;
	/**Ͷ��ԱͶ�ݼ�¼��ѯ**/
	private final int CMD_HTTP_POSTER_REQUEST = 12 ;
	/**�û�ȡ����¼��ѯ**/
	private final int CMD_HTTP_USER_REQUEST = 13 ;
	/**�û��Ĵ��¼��ѯ**/
	private final int CMD_HTTP_USER_POST_REQUEST = 14 ;
	/**���Ͷ�ݱ���**/
	private final int CMD_HTTP_POSTER_POST_SAVE = 16 ;
	/**�Ĵ�Ͷ�ݱ���**/
	private final int CMD_HTTP_USER_POST_SAVE = 17 ;
	/**��ȡ������Ϣ**/
	private final int CMD_HTTP_GET_BOX_INFO = 18 ;
	
	
	
	
	public final static String SUCCESS = "0" ;  //���سɹ�
	
	public final static String LOGIN_ELSE = "1" ;
	
	private final Handler hander = new Handler();
	 private ExecutorService executorService = Executors.newFixedThreadPool(8); // �̶�5���߳���ִ������

	 /***
	  * ����û��Ƿ����
	  * @param user
	  * @param callback
	  */
	 public void checkUserExist(final String user, final HttpCallBack callback){
		 executorService.submit(new Runnable() {
			@Override
			public void run() {
				try{
					HttpClient client = new DefaultHttpClient();
					HttpGet httpget = new HttpGet();
					String request = BASE_URI + "?CMD=" + CMD_HTTP_USER_EXIST +"&username=" + user  ;
					LogInfo("checkUserExist  request = ", request) ;
					httpget.setURI(new URI(request));
					//�������ӳ�ʱΪ5S
					client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
					HttpResponse response = client.execute(httpget);
					if(response.getStatusLine().getStatusCode() == 200){
						String strResult = EntityUtils.toString(response.getEntity(),"UTF-8");
						if(strResult != null){
							LogInfo("checkUserExist  strResult = ", strResult) ;
							return ;
						}
					}
					//��������
					callback.call(null, TipsHttpError.ERROR_UNKNOW);
				}catch(Exception exception){
					Log.e("", exception.toString());
					//���糬ʱ�����������ӳ���
					callback.call(null, TipsHttpError.ERROR_NETWORK);
				}
			}
		});
	 }
	 
	 /**
	  * �û�ע��
	  * @param user  �û���(�ֻ���) �̶�����
	  * @param password ���룬��󳤶�20
	  * @param realName ��ʵ����(�ɿ�)
	  * @param idCard  ���֤(�ɿ�),���ȹ̶�18
	  * @param email  ע������(�ɿ�)����󳤶�50
	  * @param userType  ע������ 1��ҵ��  2��Ͷ��Ա   3������Ա
	  */
	 public void register(final String user, final String password, final String realName,
			 final String idCard, final String email, final String userType, final HttpCallBack callback){
		 executorService.submit(new Runnable() {
			@Override
			public void run() {
				try{
					HttpClient client = new DefaultHttpClient();
					HttpGet httpget = new HttpGet();
					String request = BASE_URI + "?CMD=" + CMD_HTTP_REGISTER_USER +"&username=" + user +
							"&userpass=" + password + "&realname=" + realName + "&idcard=" + idCard  + 
							"&email=" + email + "&usertype=" + userType;
					LogInfo("register  request = ", request) ;
					httpget.setURI(new URI(request));
					//�������ӳ�ʱΪ5S
					client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
					HttpResponse response = client.execute(httpget);
					if(response.getStatusLine().getStatusCode() == 200){
						String strResult = EntityUtils.toString(response.getEntity(),"UTF-8");
						if(strResult != null){
							callback.call(null, HttpUtil.getError(strResult));
							LogInfo("register  strResult = ", strResult) ;
							return ;
						}
					}
					//��������
					callback.call(null, TipsHttpError.ERROR_UNKNOW);
				}catch(Exception exception){
					Log.e("", exception.toString());
					//���糬ʱ�����������ӳ���
					callback.call(null, TipsHttpError.ERROR_NETWORK);
				}
			}
		});
	 }
	 
	 /**
	  * �û���¼
	  * @param user  �û���
	  * @param password ����
	  * @param userType  ��¼����
	  * @param callback
	  */
	 public void login(final String user, final String password,final String userType,
			 final HttpCallBack callback){
		 executorService.submit(new Runnable() {
			@Override
			public void run() {
				try{
					long firstTime = System.currentTimeMillis() ;
					HttpClient client = new DefaultHttpClient();
					HttpGet httpget = new HttpGet();
					String request = BASE_URI + "?CMD=" + CMD_HTTP_LOGIN +"&username=" + user + 
							"&userpass=" + password + "&usertype=" + userType;
					LogInfo("login  request = ", request) ;
					httpget.setURI(new URI(request));
					//�������ӳ�ʱΪ5S
					client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
					HttpResponse response = client.execute(httpget);
					if(response.getStatusLine().getStatusCode() == 200){
						String strResult = EntityUtils.toString(response.getEntity(),"UTF-8");
						if(strResult != null){
							LogInfo("login  strResult = ", strResult) ;
							if(System.currentTimeMillis() - firstTime > 8000){
								callback.call(null, TipsHttpError.ERROR_NETWORK);
								return ;
							}
							//�ص�
							callback.call(HttpUtil.resolveUserLogin(strResult), HttpUtil.getError(strResult));
							return ;
						}
					}
					//��������
					callback.call(null, TipsHttpError.ERROR_UNKNOW);
				}catch(Exception exception){
					Log.e("", exception.toString());
					//���糬ʱ�����������ӳ���
					callback.call(null, TipsHttpError.ERROR_NETWORK);
				}
			}
		});
	 }
	 
	 /**
	  * Ͷ���ż�
	  * @param user   �û���
	  * @param password  ����
	  * @param ecode  ����
	  * @param bcode  ����
	  * @param callback
	  */
	 public void postMail(final String user, final String password,final String ecode, 
			 final String bcode,  final HttpCallBack callback){
		 executorService.submit(new Runnable() {
			@Override
			public void run() {
				try{
					HttpClient client = new DefaultHttpClient();
					HttpGet httpget = new HttpGet();
					String request = BASE_URI + "?CMD=" + CMD_HTTP_POST_MAIL +"&username=" + user + 
							"&userpass=" + password + "&ecode=" + ecode + "&bcode=" + bcode;
					LogInfo("login  postMail = ", request) ;
					httpget.setURI(new URI(request));
					//�������ӳ�ʱΪ5S
					client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
					HttpResponse response = client.execute(httpget);
					if(response.getStatusLine().getStatusCode() == 200){
						String strResult = EntityUtils.toString(response.getEntity(),"UTF-8");
						if(strResult != null){
							LogInfo("postMail  strResult = ", strResult) ;
							return ;
						}
					}
					//��������
					callback.call(null, TipsHttpError.ERROR_UNKNOW);
				}catch(Exception exception){
					Log.e("", exception.toString());
					//���糬ʱ�����������ӳ���
					callback.call(null, TipsHttpError.ERROR_NETWORK);
				}
			}
		});		 
		 
	 }
	 
	 /**
	  * ��������
	  * @param user   �û���
	  * @param password ����
	  * @param ecode  ����
	  * @param bcode  ����
	  * @param callback
	  */
	 public void openMail(final String user, final String password,final String ecode, 
			 final String bcode,  final HttpCallBack callback){
		 executorService.submit(new Runnable() {
			@Override
			public void run() {
				try{
					HttpClient client = new DefaultHttpClient();
					HttpGet httpget = new HttpGet();
					String request = BASE_URI + "?CMD=" + CMD_HTTP_OPEN_MAIL_BOX +"&username=" + user + 
							"&userpass=" + password + "&ecode=" + ecode + "&bcode=" + bcode;
					LogInfo("openMail  postMail = ", request) ;
					httpget.setURI(new URI(request));
					//�������ӳ�ʱΪ5S
					client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
					HttpResponse response = client.execute(httpget);
					if(response.getStatusLine().getStatusCode() == 200){
						String strResult = EntityUtils.toString(response.getEntity(),"UTF-8");
						if(strResult != null){
							LogInfo("postMail  strResult = ", strResult) ;
							callback.call(null, HttpUtil.getError(strResult));
							return ;
						}
					}
					//��������
					callback.call(null, TipsHttpError.ERROR_UNKNOW);
				}catch(Exception exception){
					Log.e("", exception.toString());
					//���糬ʱ�����������ӳ���
					callback.call(null, TipsHttpError.ERROR_NETWORK);
				}
			}
		});	 
			 }
	 
	 /**
	  * Ͷ��Ա Ͷ���
	  * @param user  �û���
	  * @param password  ����
	  * @param ecode ����
	  * @param bcode ����
	  * @param getUserTel  ȡ�����ֻ��ţ��ɿգ�
	  * @param order  �������
	  * @param callback
	  */
	 public void postExpress(final String user, final String password,final String ecode, 
			 final String bcode,final String getUserTel,final String order,  final HttpCallBack callback){
		 executorService.submit(new Runnable() {
			@Override
			public void run() {
				try{
					HttpClient client = new DefaultHttpClient();
					HttpGet httpget = new HttpGet();
					String request = BASE_URI + "?CMD=" + CMD_HTTP_POST_EXPRESS +"&username=" + user + 
							"&userpass=" + password + "&ecode=" + ecode + "&bcode=" + bcode + 
							"&getuserphone=" + getUserTel + "&order=" + order;
					LogInfo("posterPost  request = ", request) ;
					httpget.setURI(new URI(request));
					//�������ӳ�ʱΪ5S
					client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
					HttpResponse response = client.execute(httpget);
					if(response.getStatusLine().getStatusCode() == 200){
						String strResult = EntityUtils.toString(response.getEntity(),"UTF-8");
						if(strResult != null){
							LogInfo("posterPost  strResult = ", strResult) ;
							return ;
						}
					}
					//��������
					callback.call(null, TipsHttpError.ERROR_UNKNOW);
				}catch(Exception exception){
					Log.e("", exception.toString());
					//���糬ʱ�����������ӳ���
					callback.call(null, TipsHttpError.ERROR_NETWORK);
				}
			}
		});	 	 
	 }
	 
	 /**
	  * ȡ���  
	  * @param user  �û���
	  * @param password  ����
	  * @param systemid��¼ϵͳ���
	  * @param mode   ȡ����ʽ  1.�ռ�������ȡ���� 2.Ͷ���˳�ʱȡ��
	  * @param callback
	  */
	 public void getExpress(final String user, final String password,final String systemid,
			 final int mode ,final HttpCallBack callback){
		 executorService.submit(new Runnable() {
			@Override
			public void run() {
				try{
					HttpClient client = new DefaultHttpClient();
					HttpGet httpget = new HttpGet();
					String request = BASE_URI + "?CMD=" + CMD_HTTP_GET_EXPRESS+"&username=" + user + 
							"&userpass=" + password + "&systemid=" + systemid + "&mode=" + mode;
					LogInfo("getExpress  request = ", request) ;
					httpget.setURI(new URI(request));
					//�������ӳ�ʱΪ5S
					client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
					HttpResponse response = client.execute(httpget);
					if(response.getStatusLine().getStatusCode() == 200){
						String strResult = EntityUtils.toString(response.getEntity(),"UTF-8");
						if(strResult != null){
							LogInfo("getExpress  strResult = ", strResult) ;
							callback.call(null, TipsHttpError.OK);
							 return ;
						}
					}
					//��������
					callback.call(null, TipsHttpError.ERROR_UNKNOW);
				}catch(Exception exception){
					Log.e("", exception.toString());
					//���糬ʱ�����������ӳ���
					callback.call(null, TipsHttpError.ERROR_NETWORK);
				}
			}
		});	
	 }
	 
	 /**
	  * �û��Ĵ�
	  * @param user  �û���
	  * @param password  ����
	  * @param ecode  ����
	  * @param bcode  ����
	  * @param getUserPhone  ȡ���˺���(�ɿգ� Ϊ��Ĭ��Ϊ�Լ�)
	  * @param callback
	  */
	 public void userPost(final String user, final String password, final String ecode , final String bcode,
			 final String getUserPhone, final String msg ,final HttpCallBack callback){
		 executorService.submit(new Runnable() {
			@Override
			public void run() {
				try{
					HttpClient client = new DefaultHttpClient();
					HttpGet httpget = new HttpGet();
					String request = BASE_URI + "?CMD=" + CMD_HTTP_USER_POST + "&username=" + user + 
							"&userpass=" + password + "&ecode=" + ecode + "&bcode=" + bcode + 
							"&getuserphone=" + getUserPhone + "&msg=" + msg;
					LogInfo("userPost  request = ", request) ;
					httpget.setURI(new URI(request));
					//�������ӳ�ʱΪ5S
					client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
					HttpResponse response = client.execute(httpget);
					if(response.getStatusLine().getStatusCode() == 200){
						String strResult = EntityUtils.toString(response.getEntity(),"UTF-8");
						if(strResult != null){
							LogInfo("userPost  strResult = ", strResult) ;
							callback.call(HttpUtil.resolveUserPost(strResult), HttpUtil.getError(strResult));
							 return ;
						}
					}
					//��������
					callback.call(null, TipsHttpError.ERROR_UNKNOW);
				}catch(Exception exception){
					Log.e("", exception.toString());
					//���糬ʱ�����������ӳ���
					callback.call(null, TipsHttpError.ERROR_NETWORK);
				}
			}
		});
	 }
	 
	 /**
	  * ȡ�Ĵ���Ʒ
	  * @param user   �û���
	  * @param password  ����
	  * @param systemid  ϵͳ��¼���
	  * @param callback
	  */
	 public void userGet(final String user, final String password,final String systemid,  final HttpCallBack callback){
		 executorService.submit(new Runnable() {
			@Override
			public void run() {
				try{
					HttpClient client = new DefaultHttpClient();
					HttpGet httpget = new HttpGet();
					String request = BASE_URI + "?CMD=" + CMD_HTTP_USER_GET+"&username=" + user + 
							"&userpass=" + password + "&systemid=" + systemid + "&mode=1";
					LogInfo("userGet  request = ", request) ;
					httpget.setURI(new URI(request));
					//�������ӳ�ʱΪ5S
					client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
					HttpResponse response = client.execute(httpget);
					if(response.getStatusLine().getStatusCode() == 200){
						String strResult = EntityUtils.toString(response.getEntity(),"UTF-8");
						if(strResult != null){
							LogInfo("userGet  strResult = ", strResult) ;
							//�ص�������
							callback.call(null, HttpUtil.getError(strResult));
							 return ;
						}
					}
					//��������
					callback.call(null, TipsHttpError.ERROR_UNKNOW);
				}catch(Exception exception){
					Log.e("", exception.toString());
					//���糬ʱ�����������ӳ���
					callback.call(null, TipsHttpError.ERROR_NETWORK);
				}
			}
		}); 
	 }
	 
	 /**
	  * Ͷ��ԱͶ�ݼ�¼��ѯ
	  * @param user			�û���
	  * @param password		����
	  * @param mode			��ѯģʽ  0.δȡ 1.��ȡ  2.����
	  * @param callback
	  * @return
	  */
	 public List<Object> queryPosterRecord(final String user, final String password,final int mode,  final HttpCallBack callback){
		 executorService.submit(new Runnable() {
			@Override
			public void run() {
				try{
					HttpClient client = new DefaultHttpClient();
					HttpGet httpget = new HttpGet();
					String request = BASE_URI + "?CMD=" + CMD_HTTP_POSTER_REQUEST+"&username=" + user + 
							"&userpass=" + password + "&mode=" + mode ;
					LogInfo("queryPosterRecord  request = ", request) ;
					httpget.setURI(new URI(request));
					//�������ӳ�ʱΪ5S
					client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
					HttpResponse response = client.execute(httpget);
					if(response.getStatusLine().getStatusCode() == 200){
						String strResult = EntityUtils.toString(response.getEntity(),"UTF-8");
						if(strResult != null){
							LogInfo("queryPosterRecord  strResult = ", strResult) ;
							
							 return ;
						}
					}
					//��������
					callback.call(null, TipsHttpError.ERROR_UNKNOW);
				}catch(Exception exception){
					Log.e("", exception.toString());
					//���糬ʱ�����������ӳ���
					callback.call(null, TipsHttpError.ERROR_NETWORK);
				}
			}
		}); 
		 return null ;
	 }
	 
	 /**
	  * �û�ȡ����¼��ѯ
	  * @param user			�û���
	  * @param password		����
	  * @param mode			��ѯģʽ��0.δȡ   1.��ȡ   2.����
	  * @param callback
	  * @return
	  */
	 public List<Object> queryUserGet(final String user, final String password,final int mode , final HttpCallBack callback){
		 executorService.submit(new Runnable() {
			@Override
			public void run() {
				try{
					HttpClient client = new DefaultHttpClient();
					HttpGet httpget = new HttpGet();
					String request = BASE_URI + "?CMD=" + CMD_HTTP_USER_REQUEST+"&username=" + user + 
							"&userpass=" + password + "&mode=" + mode ;
					LogInfo("queryUserGet  request = ", request) ;
					httpget.setURI(new URI(request));
					//�������ӳ�ʱΪ5S
					client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
					HttpResponse response = client.execute(httpget);
					if(response.getStatusLine().getStatusCode() == 200){
						String strResult = EntityUtils.toString(response.getEntity(),"UTF-8");
						if(strResult != null){
							LogInfo("queryUserGet  strResult = ", strResult) ;
							callback.call(HttpUtil.resolveUserGet(strResult), HttpUtil.getError(strResult));
							 return ;
						}
					}
					//��������
					callback.call(null, TipsHttpError.ERROR_UNKNOW);
				}catch(Exception exception){
					Log.e("", exception.toString());
					//���糬ʱ�����������ӳ���
					callback.call(null, TipsHttpError.ERROR_NETWORK);
				}
			}
		}); 
		 return null ;
	 }
	 
	 /**
	  * �û��Ĵ��¼��ѯ
	  * @param user			�û���
	  * @param password		����
	  * @param mode			��ѯģʽ��0.�ҼĴ��   1.�Ĵ���ҵ�   2.����
	  * @param callback
	  * @return
	  */
	 public List<Object> queryUserPostGet(final String user, final String password,final int mode , final HttpCallBack callback){
		 executorService.submit(new Runnable() {
			@Override
			public void run() {
				try{
					HttpClient client = new DefaultHttpClient();
					HttpGet httpget = new HttpGet();
					String request = BASE_URI + "?CMD=" + CMD_HTTP_USER_POST_REQUEST+"&username=" + user + 
							"&userpass=" + password + "&mode=" + mode ;
					LogInfo("queryUserPostGet  request = ", request) ;
					httpget.setURI(new URI(request));
					//�������ӳ�ʱΪ5S
					client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
					HttpResponse response = client.execute(httpget);
					if(response.getStatusLine().getStatusCode() == 200){
						String strResult = EntityUtils.toString(response.getEntity(),"UTF-8");
						if(strResult != null){
							LogInfo("queryUserPostGet  strResult = ", strResult) ;
							//�ص�������
							callback.call(HttpUtil.resolveUserPostRecord(strResult), HttpUtil.getError(strResult));
							 return ;
						}
					}
					//��������
					callback.call(null, TipsHttpError.ERROR_UNKNOW);
				}catch(Exception exception){
					Log.e("", exception.toString());
					//���糬ʱ�����������ӳ���
					callback.call(null, TipsHttpError.ERROR_NETWORK);
				}
			}
		}); 
		 return null ;
	 }
	 
	 /**
	  * ���Ͷ�ݱ��棬���Ա��ȷ��Ͷ��ʱ���͸�������
	  * @param user
	  * @param password
	  * @param systemid
	  * @param callback
	  */
	 public void savePosterPost(final String user, final String password,final String systemid , final HttpCallBack callback){
		 executorService.submit(new Runnable() {
			@Override
			public void run() {
				try{
					HttpClient client = new DefaultHttpClient();
					HttpGet httpget = new HttpGet();
					String request = BASE_URI + "?CMD=" + CMD_HTTP_POSTER_POST_SAVE+"&username=" + user + 
							"&userpass=" + password + "&systemid=" + systemid ;
					LogInfo("queryUserGet  request = ", request) ;
					httpget.setURI(new URI(request));
					//�������ӳ�ʱΪ5S
					client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
					HttpResponse response = client.execute(httpget);
					if(response.getStatusLine().getStatusCode() == 200){
						String strResult = EntityUtils.toString(response.getEntity(),"UTF-8");
						if(strResult != null){
							LogInfo("savePosterPost  strResult = ", strResult) ;
							callback.call(null, HttpUtil.getError(strResult));
							 return ;
						}
					}
					//��������
					callback.call(null, TipsHttpError.ERROR_UNKNOW);
				}catch(Exception exception){
					Log.e("", exception.toString());
					//���糬ʱ�����������ӳ���
					callback.call(null, TipsHttpError.ERROR_NETWORK);
				}
			}
		});  
	 }
	 
	 /**
	  * �Ĵ�Ͷ�ݱ���
	  * @param user
	  * @param password
	  * @param systemid
	  * @param callback
	  */
	 public void saveUserPost(final String user, final String password,final String systemid , final HttpCallBack callback){
		 executorService.submit(new Runnable() {
				@Override
				public void run() {
					try{
						HttpClient client = new DefaultHttpClient();
						HttpGet httpget = new HttpGet();
						String request = BASE_URI + "?CMD=" + CMD_HTTP_USER_POST_SAVE+"&username=" + user + 
								"&userpass=" + password + "&systemid=" + systemid ;
						LogInfo("queryUserGet  request = ", request) ;
						httpget.setURI(new URI(request));
						//�������ӳ�ʱΪ5S
						client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
						HttpResponse response = client.execute(httpget);
						if(response.getStatusLine().getStatusCode() == 200){
							String strResult = EntityUtils.toString(response.getEntity(),"UTF-8");
							if(strResult != null){
								LogInfo("savePosterPost  strResult = ", strResult) ;
								callback.call(null, HttpUtil.getError(strResult));
								 return ;
							}
						}
						//��������
						callback.call(null, TipsHttpError.ERROR_UNKNOW);
					}catch(Exception exception){
						Log.e("", exception.toString());
						//���糬ʱ�����������ӳ���
						callback.call(null, TipsHttpError.ERROR_NETWORK);
					}
				}
			});
	 }
	 
	 /**
	  * ��ȡ������Ϣ
	  * @param user
	  * @param password
	  * @param ecode  ����ϵĶ�ά��
	  * @param callback
	  */
	 public void getBoxInfo(final String user, final String password,final String ecode , final HttpCallBack callback) {
		 executorService.submit(new Runnable() {
				@Override
				public void run() {
					try{
						HttpClient client = new DefaultHttpClient();
						HttpGet httpget = new HttpGet();
						String request = BASE_URI + "?CMD=" + CMD_HTTP_GET_BOX_INFO+"&username=" + user + 
								"&userpass=" + password + "&ecode=" + ecode ;
						LogInfo("queryUserGet  request = ", request) ;
						httpget.setURI(new URI(request));
						//�������ӳ�ʱΪ5S
						client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
						HttpResponse response = client.execute(httpget);
						if(response.getStatusLine().getStatusCode() == 200){
							String strResult = EntityUtils.toString(response.getEntity(),"UTF-8");
							if(strResult != null){
								LogInfo("getBoxInfo  strResult = ", strResult) ;
								//�ص�������������Ϣ
								callback.call(HttpUtil.resolveGetBox(strResult), HttpUtil.getError(strResult));
								 return ;
							}
						}
						//��������
						callback.call(null, TipsHttpError.ERROR_UNKNOW);
					}catch(Exception exception){
						Log.e("", exception.toString());
						//���糬ʱ�����������ӳ���
						callback.call(null, TipsHttpError.ERROR_NETWORK);
					}
				}
			});
	 }
	 
/**********--------------------------�ָ���-----------------------------*************/	 
	 /**
	  * ��¼����
	  * @param user  �˺�
	  * @param password ����
	  * @param callback  �ص��ӿ�
	  */
	 public void login(final String user, final String password, final HttpCallBack callback){
		 executorService.submit(new Runnable() {
			@Override
			public void run() {
				try{
					HttpClient client = new DefaultHttpClient();
					HttpGet httpget = new HttpGet();
					String request = BASE_URI + "?CMD=" + CMD_LOGIN +"&spid=" + user + "&sppassword=" + password ;
					httpget.setURI(new URI(request));
					Log.e("Login resp", request) ;
					//�������ӳ�ʱΪ5S
					client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
					HttpResponse response = client.execute(httpget);
					if(response.getStatusLine().getStatusCode() == 200){
					     String strResult = EntityUtils.toString(response.getEntity(),"UTF-8");
					     Log.e("Login resp", strResult) ;
					     if(strResult != null && strResult.length() > 4){
					    		//������Error
				    		 String errStr = strResult.replaceAll("\\&.*", "");
				    		 String errValue = errStr.replaceAll("err=", "");
				    		 //�ص�
				    		 callback.call( HttpUtil.resolveLogin(strResult), errValue);
				    		 return ;
					     }
					}
					//��������
					callback.call(null, TipsHttpError.ERROR_UNKNOW);
				}catch(Exception exception){
					Log.e("", exception.toString());
//					handlerLoginPost("�������ӳ���������������µ�¼", callback);
					//256 �������Ӵ���
					callback.call(null, TipsHttpError.ERROR_NETWORK);
				}
				
			}
		});
	 }
	 
	 /**
	  * ��ѯȡ�����
	  * @param user
	  * @param password
	  * @param mode
	  * @param notget
	  */
	 public void queryPackage(final String user, final String password, final int mode, final HttpCallBack callback){
		 executorService.submit(new Runnable() {
			@Override
			public void run() {
				try{
					Log.e("Exception", "http get");
					HttpClient client = new DefaultHttpClient();
					HttpGet httpget = new HttpGet();
					httpget.setURI(new URI(BASE_URI + "?CMD=" + CMD_GET_PACKAGE_QUEERY +"&spid=" + user + "&sppassword=" + password + "&mode=" + mode));
					//�������ӳ�ʱΪ5S
					client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
					HttpResponse response = client.execute(httpget);
					HttpError error = new HttpError();
					if(response.getStatusLine().getStatusCode() == 200){
						//��������������
					     String strResult = EntityUtils.toString(response.getEntity(),"UTF-8"); 
					     //����������ȡ������
					     List<GetPackages> listGet = null ;
					     if(strResult != null && strResult.length() > 4){
				    		//������Error
				    		 String errStr = strResult.replaceAll("\\&.*", "");
				    		 String errValue = errStr.replaceAll("err=", "");
					    	 if(strResult.startsWith("err=0")){//���ؽ������ȷ����err=0&...
					    		 listGet = HttpUtil.resolveQueryData(strResult, new HttpError());
					    	 }
					    	 callback.call(listGet, errValue);
					    	 return ;
					     }
					}
					callback.call(null, "255");
				}catch(Exception exception){
					Log.e("", exception.toString());
					callback.call(null, "256");
				}
				
			}
		});
	 }
	 
	 
	 //��ѯ����״̬
	 public void queryBoxInfo(final String user, final String password, final int lockCode, final QueryBoxCall queryBox){
		 executorService.submit(new Runnable() {
			@Override
			public void run() {
				try{
					Log.e("Exception", "http get");
					HttpClient client = new DefaultHttpClient();
					HttpGet httpget = new HttpGet();
					httpget.setURI(new URI(BASE_URI + "?CMD=" + CMD_GET_BOX_STATE +"&spid=" + user + "&sppassword=" + password + "&lockcode=" + lockCode));
					//�������ӳ�ʱΪ5S
					client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
					HttpResponse response = client.execute(httpget);
					if(response.getStatusLine().getStatusCode() == 200){
					     String strResult = EntityUtils.toString(response.getEntity(),"UTF-8");
					     if(strResult != null && strResult.length() > 4){
					    	 Log.e("queryBoxInfo", strResult);
					    	 if(strResult.startsWith("err=0")){//���ؽ������ȷ����err=0&...
						    		//������Error
					    		 String errStr = strResult.replaceAll("\\&.*", "");
					    		 String errValue = errStr.replaceAll("err=", "");
					    		//������ʾ��
//					    		 TipsHttpError.toastError(errValue);
					    		 Log.e("queryBoxInfo", strResult);
					    		 PostBoxInfo boxInfo = HttpUtil.resolveQueryBox(strResult, new HttpError());
					    		 queryBox.call(boxInfo);
					    		 return ;
					    	 }
					     }
//					     handlerLoginPost(LOGIN_ELSE, callback);
					}
				}catch(Exception exception){
					Log.e("", exception.toString());
//					handlerLoginPost(exception.toString(), callback);
				}
				
			}
		});
	 }
	 
	 //����ָ��
	 public  void openBox(final String user, final String password, 
			 final int lockCode,final String boxId , final OpenBoxCall openBox){
		 executorService.submit(new Runnable() {
			@Override
			public void run() {
				try{
					Log.e("Exception", "http get");
					HttpClient client = new DefaultHttpClient();
					HttpGet httpget = new HttpGet();
					httpget.setURI(new URI(BASE_URI + "?CMD=" + CMD_OPEN_BOX +"&spid=" + user 
							+ "&sppassword=" + password + "&lockcode=" + lockCode + "&boxid=" + boxId));
					
					
					//�������ӳ�ʱΪ5S
					client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
					HttpResponse response = client.execute(httpget);
					if(response.getStatusLine().getStatusCode() == 200){
					     String strResult = EntityUtils.toString(response.getEntity(),"UTF-8");
					     if(strResult != null && strResult.length() > 4){
					    	 
					    	 if(strResult.startsWith("err=")){//���ؽ������ȷ����err=0&...
						    		//������Error
					    		 String errStr = strResult.replaceAll("\\&.*", "");
					    		 String errValue = errStr.replaceAll("err=", "");
					    		//������ʾ��
//					    		 TipsHttpError.toastError(errValue);
					    		 String error = strResult.replaceAll("err=", "");
					    		 openBox.call(error);
//					    		 Log.e("", strResult);
					    		 return ;
					    	 }
					     }
//					     handlerLoginPost(LOGIN_ELSE, callback);
					}
				}catch(Exception exception){
					Log.e("", exception.toString());
//					handlerLoginPost(exception.toString(), callback);
				}
				
			}
		});
	 }
	 
	 /**
	  * Ͷ����Ʒ
	  * @param user
	  * @param password
	  * @param lockCode
	  * @param boxId
	  * @param tel
	  * @param orderNo
	  * @param openBox
	  */
	 public void postPackages(final String user, final String password, 
			 final int lockCode,final String boxId ,final String tel, final String orderNo, final OpenBoxCall openBox){
		 executorService.submit(new Runnable() {
			@Override
			public void run() {
				try{
					Log.e("Exception", "http get");
					HttpClient client = new DefaultHttpClient();
					HttpGet httpget = new HttpGet();
					httpget.setURI(new URI(BASE_URI + "?CMD=" + CMD_POSET +"&spid=" + user 
							+ "&sppassword=" + password + "&lockcode=" + lockCode + "&boxid=" + boxId
							+"&tel=" + tel + "&orderno="+orderNo));
					//�������ӳ�ʱΪ5S
					client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
					HttpResponse response = client.execute(httpget);
					if(response.getStatusLine().getStatusCode() == 200){
					     String strResult = EntityUtils.toString(response.getEntity(),"UTF-8");
					     if(strResult != null && strResult.length() > 4){
					    	 
					    	 if(strResult.startsWith("err=")){//���ؽ������ȷ����err=0&...
					    		//������Error
					    		 String errStr = strResult.replaceAll("\\&.*", "");
					    		 String errValue = errStr.replaceAll("err=", "");
					    		//������ʾ��
//					    		 TipsHttpError.toastError(errValue);
					    		 openBox.call(errValue);
//					    		 Log.e("", strResult);
					    		 return ;
					    	 }
					     }
//					     handlerLoginPost(LOGIN_ELSE, callback);
					}
				}catch(Exception exception){
					Log.e("", exception.toString());
//					handlerLoginPost(exception.toString(), callback);
				}
				
			}
		});
	 }
	 
	 /**
	  * Ͷ����ѯ
	  * @param user
	  * @param password
	  * @param mode
	  */
	 public void queryPostPackages(final String user, final String password, final int mode, final HttpCallBack callback){
		 executorService.submit(new Runnable() {
			@Override
			public void run() {
				try{
					Log.e("Exception", "http get");
					HttpClient client = new DefaultHttpClient();
					HttpGet httpget = new HttpGet();
					httpget.setURI(new URI(BASE_URI + "?CMD=" + CMD_POST_QUEERY +"&spid=" + user + "&sppassword=" + password + "&mode=" + mode));
					//�������ӳ�ʱΪ5S
					client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
					HttpResponse response = client.execute(httpget);
					if(response.getStatusLine().getStatusCode() == 200){
					     String strResult = EntityUtils.toString(response.getEntity(),"UTF-8");
					     List<PostPackages> listPost = null ;
					     if(strResult != null && strResult.length() > 4){
					    	 //������������
				    		 String errStr = strResult.replaceAll("\\&.*", "");
				    		 String errValue = errStr.replaceAll("err=", "");
					    	 if(strResult.startsWith("err=0")){//���ؽ������ȷ����err=0&...
					    		 //��������
					    		 listPost = HttpUtil.resolveQueryPost(strResult, new HttpError()) ;
					    	 }
					    	 //�ص�
					    	 callback.call(listPost, errValue);
					    	 return ;
					     }
					}
					callback.call(null, "255");
				}catch(Exception exception){
					Log.e("", exception.toString());
					callback.call(null, "256");
//					handlerLoginPost(exception.toString(), callback);
				}
				
			}
		});
	 }
	 
	 /**
	  * ��֤����Ƿ����
	  * @param user
	  * @param password
	  * @param lock
	  */
	 public void checkLock(final String user, final String password, final int lockCode, final OpenBoxCall open){
		 executorService.submit(new Runnable() {
			@Override
			public void run() {
				try{
					Log.e("Exception", "http get");
					HttpClient client = new DefaultHttpClient();
					HttpGet httpget = new HttpGet();
					httpget.setURI(new URI(BASE_URI + "?CMD=" + CMD_CHECK_POST_PERMISSION +"&spid=" + user 
							+ "&sppassword=" + password + "&lockcode=" + lockCode));
					//�������ӳ�ʱΪ5S
					client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
					HttpResponse response = client.execute(httpget);
					if(response.getStatusLine().getStatusCode() == 200){
					     String strResult = EntityUtils.toString(response.getEntity(),"UTF-8");
					     if(strResult != null && strResult.length() > 4){
					    	 
					    	 if(strResult.startsWith("err=")){//���ؽ������ȷ����err=0&...
					    		 final String error = strResult.replaceAll("err=", "");
					    		 //������ʾ��
//					    		 new Handler().post(new Runnable() {
//									
//									@Override
//									public void run() {
//										TipsHttpError.toastError(error);
//										
//									}
//								});
					    		 
					    		 open.call(error);
//					    		 Log.e("", strResult);
					    		 return ;
					    	 }
					     }
//					     handlerLoginPost(LOGIN_ELSE, callback);
					}
				}catch(Exception exception){
					Log.e("", exception.toString());
//					handlerLoginPost(exception.toString(), callback);
				}
				
			}
		});
	 }
	 
	 /**
	  * ȡ��
	  * @param user
	  * @param password
	  * @param lockCode
	  * @param boxid
	  * @param mode
	  * @param open
	  */
	 public void getPackage(final String user, final String password, final int lockCode,final int boxid,final int mode ,final OpenBoxCall open){
		 executorService.submit(new Runnable() {
			@Override
			public void run() {
				try{
					Log.e("Exception", "http get");
					HttpClient client = new DefaultHttpClient();
					HttpGet httpget = new HttpGet();
					httpget.setURI(new URI(BASE_URI + "?CMD=" + CMD_GET_PACKAGE +"&spid=" + user 
							+ "&sppassword=" + password + "&lockcode=" + lockCode +"&boxid="+ boxid+"&mode="+ mode));
					//�������ӳ�ʱΪ5S
					client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
					HttpResponse response = client.execute(httpget);
					if(response.getStatusLine().getStatusCode() == 200){
					     String strResult = EntityUtils.toString(response.getEntity(),"UTF-8");
					     if(strResult != null && strResult.length() > 4){
					    	 
					    	 if(strResult.startsWith("err=")){//���ؽ������ȷ����err=0&...
					    		 Log.e("result", strResult);
					    		 final String error = strResult.replaceAll("err=", "");
//					    		 TipsHttpError.toastError(error);
					    		 open.call(error);
//					    		 Log.e("", strResult);
					    		 return ;
					    	 }
					     }
//					     handlerLoginPost(LOGIN_ELSE, callback);
					}
				}catch(Exception exception){
					Log.e("", exception.toString());
//					handlerLoginPost(exception.toString(), callback);
				}
				
			}
		});
	 }
	
	 /**
	  * ���͵�¼������Ϣ
	  * @param msg
	  * @param callback
	  */
	 private void handlerLoginPost(final String msg, final LoginCallBack callback){
		 hander.post(new Runnable() {
			@Override
			public void run() {
				callback.call(msg);
			}
		});
	 }

	 
	 /**
	  * Log��־��Ϣ
	  * @param tag
	  * @param info
	  */
	 private void LogInfo(String tag, String info){
		 Log.e(tag, info) ;
	 }
	
}
