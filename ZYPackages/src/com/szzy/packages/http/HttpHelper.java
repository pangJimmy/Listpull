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
 * http操作类
 * @author mac
 *
 */
public class HttpHelper {
	/**服务器地址***////
	private final String BASE_URI = "http://192.168.1.51:8009/";//http://112.74.67.62:8008/     //szzhenyao.6655.la:8008
	/**连接测试指令***/
	private final int CMD_Test = 1 ;
	/**用户登录指令***/
	private final int CMD_LOGIN = 3 ;
	/**取件查询指令***/
	private final int CMD_GET_PACKAGE_QUEERY = 3 ;
	/**投件查询指令***/
	private final int CMD_POST_QUEERY = 4 ;
	/**验证投递使用权指令***/
	private final int CMD_CHECK_POST_PERMISSION = 5 ;
	/**验证使用权指令***/
	private final int CMD_CHECK_USER_PERMISSION = 6 ;
	/**获取操作设备箱子状态指令***/
	private final int CMD_GET_BOX_STATE = 7 ;
	/**投递物品指令***/
	private final int CMD_POSET = 8 ;
	/**取出物品指令***/
	private final int CMD_GET_PACKAGE = 9 ;
	/**获取所有投递权指令***/
	private final int CMD_GET_ALL_POST = 10 ;
	/**获取所有使用权指令***/
	private final int CMD_GET_ALL_USE = 11 ;
	/**开箱指令***/
	private final int CMD_OPEN_BOX = 12 ;
	
	/****--------------------分割xian----------------------****/
	/**业主登录模式***/
	public static final String LOGIN_MODE_USER = "1" ;
	/**投递员登录模式***/
	public static final String LOGIN_MODE_POSTER = "2" ;
	/**管理员登录模式***/
	public static final String LOGIN_MODE_ADMIN = "3" ;
	/**检测用户是否存在**/
	private final int CMD_HTTP_USER_EXIST = 1 ;
	/**注册用户**/
	private final int CMD_HTTP_REGISTER_USER = 2 ;
	/**用户登录**/
	private final int CMD_HTTP_LOGIN = 3 ;
	/**投递信件**/
	private final int CMD_HTTP_POST_MAIL = 4 ;
	/**开启信箱**/
	private final int CMD_HTTP_OPEN_MAIL_BOX = 5 ;
	/**投快递**/
	private final int CMD_HTTP_POST_EXPRESS = 6 ;
	/**取快递**/
	private final int CMD_HTTP_GET_EXPRESS = 7 ;
	/**用户寄存**/
	private final int CMD_HTTP_USER_POST = 8;
	/**取寄存物品**/
	private final int CMD_HTTP_USER_GET = 9 ;
	/**第三方申请**/
	private final int CMD_OTHER_ = 10 ;
	/**第三方收贷**/
	private final int CMD_OTHER_GET = 11 ;
	/**投递员投递记录查询**/
	private final int CMD_HTTP_POSTER_REQUEST = 12 ;
	/**用户取件记录查询**/
	private final int CMD_HTTP_USER_REQUEST = 13 ;
	/**用户寄存记录查询**/
	private final int CMD_HTTP_USER_POST_REQUEST = 14 ;
	/**快递投递保存**/
	private final int CMD_HTTP_POSTER_POST_SAVE = 16 ;
	/**寄存投递保存**/
	private final int CMD_HTTP_USER_POST_SAVE = 17 ;
	/**获取箱门信息**/
	private final int CMD_HTTP_GET_BOX_INFO = 18 ;
	
	
	
	
	public final static String SUCCESS = "0" ;  //返回成功
	
	public final static String LOGIN_ELSE = "1" ;
	
	private final Handler hander = new Handler();
	 private ExecutorService executorService = Executors.newFixedThreadPool(8); // 固定5个线程来执行任务

	 /***
	  * 检测用户是否存在
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
					//设置连接超时为5S
					client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
					HttpResponse response = client.execute(httpget);
					if(response.getStatusLine().getStatusCode() == 200){
						String strResult = EntityUtils.toString(response.getEntity(),"UTF-8");
						if(strResult != null){
							LogInfo("checkUserExist  strResult = ", strResult) ;
							return ;
						}
					}
					//其他错误
					callback.call(null, TipsHttpError.ERROR_UNKNOW);
				}catch(Exception exception){
					Log.e("", exception.toString());
					//网络超时或者网络连接出错
					callback.call(null, TipsHttpError.ERROR_NETWORK);
				}
			}
		});
	 }
	 
	 /**
	  * 用户注册
	  * @param user  用户名(手机号) 固定长度
	  * @param password 密码，最大长度20
	  * @param realName 真实姓名(可空)
	  * @param idCard  身份证(可空),长度固定18
	  * @param email  注册邮箱(可空)，最大长度50
	  * @param userType  注册类型 1：业主  2：投递员   3：管理员
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
					//设置连接超时为5S
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
					//其他错误
					callback.call(null, TipsHttpError.ERROR_UNKNOW);
				}catch(Exception exception){
					Log.e("", exception.toString());
					//网络超时或者网络连接出错
					callback.call(null, TipsHttpError.ERROR_NETWORK);
				}
			}
		});
	 }
	 
	 /**
	  * 用户登录
	  * @param user  用户名
	  * @param password 密码
	  * @param userType  登录类型
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
					//设置连接超时为5S
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
							//回调
							callback.call(HttpUtil.resolveUserLogin(strResult), HttpUtil.getError(strResult));
							return ;
						}
					}
					//其他错误
					callback.call(null, TipsHttpError.ERROR_UNKNOW);
				}catch(Exception exception){
					Log.e("", exception.toString());
					//网络超时或者网络连接出错
					callback.call(null, TipsHttpError.ERROR_NETWORK);
				}
			}
		});
	 }
	 
	 /**
	  * 投递信件
	  * @param user   用户名
	  * @param password  密码
	  * @param ecode  柜编号
	  * @param bcode  箱编号
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
					//设置连接超时为5S
					client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
					HttpResponse response = client.execute(httpget);
					if(response.getStatusLine().getStatusCode() == 200){
						String strResult = EntityUtils.toString(response.getEntity(),"UTF-8");
						if(strResult != null){
							LogInfo("postMail  strResult = ", strResult) ;
							return ;
						}
					}
					//其他错误
					callback.call(null, TipsHttpError.ERROR_UNKNOW);
				}catch(Exception exception){
					Log.e("", exception.toString());
					//网络超时或者网络连接出错
					callback.call(null, TipsHttpError.ERROR_NETWORK);
				}
			}
		});		 
		 
	 }
	 
	 /**
	  * 开启信箱
	  * @param user   用户名
	  * @param password 密码
	  * @param ecode  柜编号
	  * @param bcode  箱编号
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
					//设置连接超时为5S
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
					//其他错误
					callback.call(null, TipsHttpError.ERROR_UNKNOW);
				}catch(Exception exception){
					Log.e("", exception.toString());
					//网络超时或者网络连接出错
					callback.call(null, TipsHttpError.ERROR_NETWORK);
				}
			}
		});	 
			 }
	 
	 /**
	  * 投递员 投快递
	  * @param user  用户名
	  * @param password  密码
	  * @param ecode 柜编号
	  * @param bcode 箱编号
	  * @param getUserTel  取件人手机号（可空）
	  * @param order  订单编号
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
					//设置连接超时为5S
					client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
					HttpResponse response = client.execute(httpget);
					if(response.getStatusLine().getStatusCode() == 200){
						String strResult = EntityUtils.toString(response.getEntity(),"UTF-8");
						if(strResult != null){
							LogInfo("posterPost  strResult = ", strResult) ;
							return ;
						}
					}
					//其他错误
					callback.call(null, TipsHttpError.ERROR_UNKNOW);
				}catch(Exception exception){
					Log.e("", exception.toString());
					//网络超时或者网络连接出错
					callback.call(null, TipsHttpError.ERROR_NETWORK);
				}
			}
		});	 	 
	 }
	 
	 /**
	  * 取快递  
	  * @param user  用户名
	  * @param password  密码
	  * @param systemid记录系统编号
	  * @param mode   取件方式  1.收件人正常取件， 2.投件人超时取件
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
					//设置连接超时为5S
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
					//其他错误
					callback.call(null, TipsHttpError.ERROR_UNKNOW);
				}catch(Exception exception){
					Log.e("", exception.toString());
					//网络超时或者网络连接出错
					callback.call(null, TipsHttpError.ERROR_NETWORK);
				}
			}
		});	
	 }
	 
	 /**
	  * 用户寄存
	  * @param user  用户名
	  * @param password  密码
	  * @param ecode  柜编号
	  * @param bcode  箱编号
	  * @param getUserPhone  取件人号码(可空， 为空默认为自己)
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
					//设置连接超时为5S
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
					//其他错误
					callback.call(null, TipsHttpError.ERROR_UNKNOW);
				}catch(Exception exception){
					Log.e("", exception.toString());
					//网络超时或者网络连接出错
					callback.call(null, TipsHttpError.ERROR_NETWORK);
				}
			}
		});
	 }
	 
	 /**
	  * 取寄存物品
	  * @param user   用户名
	  * @param password  密码
	  * @param systemid  系统记录编号
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
					//设置连接超时为5S
					client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
					HttpResponse response = client.execute(httpget);
					if(response.getStatusLine().getStatusCode() == 200){
						String strResult = EntityUtils.toString(response.getEntity(),"UTF-8");
						if(strResult != null){
							LogInfo("userGet  strResult = ", strResult) ;
							//回调给界面
							callback.call(null, HttpUtil.getError(strResult));
							 return ;
						}
					}
					//其他错误
					callback.call(null, TipsHttpError.ERROR_UNKNOW);
				}catch(Exception exception){
					Log.e("", exception.toString());
					//网络超时或者网络连接出错
					callback.call(null, TipsHttpError.ERROR_NETWORK);
				}
			}
		}); 
	 }
	 
	 /**
	  * 投递员投递记录查询
	  * @param user			用户名
	  * @param password		密码
	  * @param mode			查询模式  0.未取 1.已取  2.所有
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
					//设置连接超时为5S
					client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
					HttpResponse response = client.execute(httpget);
					if(response.getStatusLine().getStatusCode() == 200){
						String strResult = EntityUtils.toString(response.getEntity(),"UTF-8");
						if(strResult != null){
							LogInfo("queryPosterRecord  strResult = ", strResult) ;
							
							 return ;
						}
					}
					//其他错误
					callback.call(null, TipsHttpError.ERROR_UNKNOW);
				}catch(Exception exception){
					Log.e("", exception.toString());
					//网络超时或者网络连接出错
					callback.call(null, TipsHttpError.ERROR_NETWORK);
				}
			}
		}); 
		 return null ;
	 }
	 
	 /**
	  * 用户取件记录查询
	  * @param user			用户名
	  * @param password		密码
	  * @param mode			查询模式：0.未取   1.已取   2.所有
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
					//设置连接超时为5S
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
					//其他错误
					callback.call(null, TipsHttpError.ERROR_UNKNOW);
				}catch(Exception exception){
					Log.e("", exception.toString());
					//网络超时或者网络连接出错
					callback.call(null, TipsHttpError.ERROR_NETWORK);
				}
			}
		}); 
		 return null ;
	 }
	 
	 /**
	  * 用户寄存记录查询
	  * @param user			用户名
	  * @param password		密码
	  * @param mode			查询模式：0.我寄存的   1.寄存给我的   2.所有
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
					//设置连接超时为5S
					client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
					HttpResponse response = client.execute(httpget);
					if(response.getStatusLine().getStatusCode() == 200){
						String strResult = EntityUtils.toString(response.getEntity(),"UTF-8");
						if(strResult != null){
							LogInfo("queryUserPostGet  strResult = ", strResult) ;
							//回调给界面
							callback.call(HttpUtil.resolveUserPostRecord(strResult), HttpUtil.getError(strResult));
							 return ;
						}
					}
					//其他错误
					callback.call(null, TipsHttpError.ERROR_UNKNOW);
				}catch(Exception exception){
					Log.e("", exception.toString());
					//网络超时或者网络连接出错
					callback.call(null, TipsHttpError.ERROR_NETWORK);
				}
			}
		}); 
		 return null ;
	 }
	 
	 /**
	  * 快递投递保存，快递员在确认投递时发送给服务器
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
					//设置连接超时为5S
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
					//其他错误
					callback.call(null, TipsHttpError.ERROR_UNKNOW);
				}catch(Exception exception){
					Log.e("", exception.toString());
					//网络超时或者网络连接出错
					callback.call(null, TipsHttpError.ERROR_NETWORK);
				}
			}
		});  
	 }
	 
	 /**
	  * 寄存投递保存
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
						//设置连接超时为5S
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
						//其他错误
						callback.call(null, TipsHttpError.ERROR_UNKNOW);
					}catch(Exception exception){
						Log.e("", exception.toString());
						//网络超时或者网络连接出错
						callback.call(null, TipsHttpError.ERROR_NETWORK);
					}
				}
			});
	 }
	 
	 /**
	  * 获取箱门信息
	  * @param user
	  * @param password
	  * @param ecode  箱柜上的二维码
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
						//设置连接超时为5S
						client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
						HttpResponse response = client.execute(httpget);
						if(response.getStatusLine().getStatusCode() == 200){
							String strResult = EntityUtils.toString(response.getEntity(),"UTF-8");
							if(strResult != null){
								LogInfo("getBoxInfo  strResult = ", strResult) ;
								//回调，返回箱门信息
								callback.call(HttpUtil.resolveGetBox(strResult), HttpUtil.getError(strResult));
								 return ;
							}
						}
						//其他错误
						callback.call(null, TipsHttpError.ERROR_UNKNOW);
					}catch(Exception exception){
						Log.e("", exception.toString());
						//网络超时或者网络连接出错
						callback.call(null, TipsHttpError.ERROR_NETWORK);
					}
				}
			});
	 }
	 
/**********--------------------------分割线-----------------------------*************/	 
	 /**
	  * 登录调用
	  * @param user  账号
	  * @param password 密码
	  * @param callback  回调接口
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
					//设置连接超时为5S
					client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
					HttpResponse response = client.execute(httpget);
					if(response.getStatusLine().getStatusCode() == 200){
					     String strResult = EntityUtils.toString(response.getEntity(),"UTF-8");
					     Log.e("Login resp", strResult) ;
					     if(strResult != null && strResult.length() > 4){
					    		//解析出Error
				    		 String errStr = strResult.replaceAll("\\&.*", "");
				    		 String errValue = errStr.replaceAll("err=", "");
				    		 //回调
				    		 callback.call( HttpUtil.resolveLogin(strResult), errValue);
				    		 return ;
					     }
					}
					//其他错误
					callback.call(null, TipsHttpError.ERROR_UNKNOW);
				}catch(Exception exception){
					Log.e("", exception.toString());
//					handlerLoginPost("网络连接出错，请检查网络后，重新登录", callback);
					//256 网络连接错误
					callback.call(null, TipsHttpError.ERROR_NETWORK);
				}
				
			}
		});
	 }
	 
	 /**
	  * 查询取件快递
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
					//设置连接超时为5S
					client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
					HttpResponse response = client.execute(httpget);
					HttpError error = new HttpError();
					if(response.getStatusLine().getStatusCode() == 200){
						//服务器返回数据
					     String strResult = EntityUtils.toString(response.getEntity(),"UTF-8"); 
					     //解析出来的取件数据
					     List<GetPackages> listGet = null ;
					     if(strResult != null && strResult.length() > 4){
				    		//解析出Error
				    		 String errStr = strResult.replaceAll("\\&.*", "");
				    		 String errValue = errStr.replaceAll("err=", "");
					    	 if(strResult.startsWith("err=0")){//返回结果，正确返回err=0&...
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
	 
	 
	 //查询箱子状态
	 public void queryBoxInfo(final String user, final String password, final int lockCode, final QueryBoxCall queryBox){
		 executorService.submit(new Runnable() {
			@Override
			public void run() {
				try{
					Log.e("Exception", "http get");
					HttpClient client = new DefaultHttpClient();
					HttpGet httpget = new HttpGet();
					httpget.setURI(new URI(BASE_URI + "?CMD=" + CMD_GET_BOX_STATE +"&spid=" + user + "&sppassword=" + password + "&lockcode=" + lockCode));
					//设置连接超时为5S
					client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
					HttpResponse response = client.execute(httpget);
					if(response.getStatusLine().getStatusCode() == 200){
					     String strResult = EntityUtils.toString(response.getEntity(),"UTF-8");
					     if(strResult != null && strResult.length() > 4){
					    	 Log.e("queryBoxInfo", strResult);
					    	 if(strResult.startsWith("err=0")){//返回结果，正确返回err=0&...
						    		//解析出Error
					    		 String errStr = strResult.replaceAll("\\&.*", "");
					    		 String errValue = errStr.replaceAll("err=", "");
					    		//给出提示框
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
	 
	 //开箱指令
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
					
					
					//设置连接超时为5S
					client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
					HttpResponse response = client.execute(httpget);
					if(response.getStatusLine().getStatusCode() == 200){
					     String strResult = EntityUtils.toString(response.getEntity(),"UTF-8");
					     if(strResult != null && strResult.length() > 4){
					    	 
					    	 if(strResult.startsWith("err=")){//返回结果，正确返回err=0&...
						    		//解析出Error
					    		 String errStr = strResult.replaceAll("\\&.*", "");
					    		 String errValue = errStr.replaceAll("err=", "");
					    		//给出提示框
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
	  * 投递物品
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
					//设置连接超时为5S
					client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
					HttpResponse response = client.execute(httpget);
					if(response.getStatusLine().getStatusCode() == 200){
					     String strResult = EntityUtils.toString(response.getEntity(),"UTF-8");
					     if(strResult != null && strResult.length() > 4){
					    	 
					    	 if(strResult.startsWith("err=")){//返回结果，正确返回err=0&...
					    		//解析出Error
					    		 String errStr = strResult.replaceAll("\\&.*", "");
					    		 String errValue = errStr.replaceAll("err=", "");
					    		//给出提示框
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
	  * 投件查询
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
					//设置连接超时为5S
					client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
					HttpResponse response = client.execute(httpget);
					if(response.getStatusLine().getStatusCode() == 200){
					     String strResult = EntityUtils.toString(response.getEntity(),"UTF-8");
					     List<PostPackages> listPost = null ;
					     if(strResult != null && strResult.length() > 4){
					    	 //解析出错误码
				    		 String errStr = strResult.replaceAll("\\&.*", "");
				    		 String errValue = errStr.replaceAll("err=", "");
					    	 if(strResult.startsWith("err=0")){//返回结果，正确返回err=0&...
					    		 //解析数据
					    		 listPost = HttpUtil.resolveQueryPost(strResult, new HttpError()) ;
					    	 }
					    	 //回调
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
	  * 验证箱柜是否可用
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
					//设置连接超时为5S
					client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
					HttpResponse response = client.execute(httpget);
					if(response.getStatusLine().getStatusCode() == 200){
					     String strResult = EntityUtils.toString(response.getEntity(),"UTF-8");
					     if(strResult != null && strResult.length() > 4){
					    	 
					    	 if(strResult.startsWith("err=")){//返回结果，正确返回err=0&...
					    		 final String error = strResult.replaceAll("err=", "");
					    		 //给出提示框
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
	  * 取件
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
					//设置连接超时为5S
					client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
					HttpResponse response = client.execute(httpget);
					if(response.getStatusLine().getStatusCode() == 200){
					     String strResult = EntityUtils.toString(response.getEntity(),"UTF-8");
					     if(strResult != null && strResult.length() > 4){
					    	 
					    	 if(strResult.startsWith("err=")){//返回结果，正确返回err=0&...
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
	  * 发送登录返回信息
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
	  * Log日志信息
	  * @param tag
	  * @param info
	  */
	 private void LogInfo(String tag, String info){
		 Log.e(tag, info) ;
	 }
	
}
