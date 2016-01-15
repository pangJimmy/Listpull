package com.szzy.packages.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.szzy.packages.R;
import com.szzy.packages.entity.VersionInfo;
import com.szzy.packages.tool.TipsHttpError;

/**
 * 软件升级管理
 * @author mac
 *
 */
public class UpdateManager {
	
    /* 下载中 */  
    private static final int DOWNLOAD = 1;  
    /* 下载结束 */  
    private static final int DOWNLOAD_FINISH = 2;  
    /* 获取版本号 */ 
    private static final int GET_VERSION = 3 ;
    /* 保存解析的XML信息 */  
//    HashMap<String, String> mHashMap;  
    /* 下载保存路径 */  
    private String mSavePath;  
    /* 记录进度条数量 */  
    private int progress;  
    /* 是否取消更新 */  
    private boolean cancelUpdate = false;  
  
    private Context mContext;  
    /* 更新进度条 */  
    private ProgressBar mProgress;  
    private Dialog mDownloadDialog; 
    
    
    private HttpHelper httpHelper ;
    
    private String sysVersion = null;//服务器上的版本
    private String url = null ;//下载APK的url
    
    
    
    private Handler mHandler = new Handler()  
    {  
        public void handleMessage(Message msg)  
        {  
            switch (msg.what)  
            {  
            // 正在下载  
            case DOWNLOAD:  
                // 设置进度条位置  
                mProgress.setProgress(progress);  
                break;  
            case DOWNLOAD_FINISH:  
                // 安装文件  
                installApk();  
                break;  
            case GET_VERSION:
            	//获取版本号，是否更新
            	if(sysVersion != null){
            		//版本不一致提示是否更新
            		if(!sysVersion.equals(getVersionName(mContext))){
            			showNoticeDialog();
            		}
            	}
            	break; 
            default:  
                break;  
            }  
        };  
    }; 

	public UpdateManager(Context context){
		this.mContext = context ;
		httpHelper = new HttpHelper() ;
		//向服务器发请求
    	httpHelper.getVersion(new HttpCallBack() {
			
			@Override
			public void call(Object obj, String err) {
				//从服务器上成功获取版本号
					if(err.equals(TipsHttpError.OK)){
						Message msg = new Message() ;
						VersionInfo info = (VersionInfo) obj ;
						sysVersion = info.getVersionName() ;
						url = info.getUrl() ;
						msg.what = GET_VERSION ;
						mHandler.sendMessage(msg) ;
					}
			}
		});
	}
	
	/** 
     * 显示软件更新对话框 
     */  
    private void showNoticeDialog(){
    	// 构造对话框  
        AlertDialog.Builder builder = new Builder(mContext);  
        builder.setTitle("软件更新");  
        builder.setMessage("发现新版本");  
        // 更新  
        builder.setPositiveButton("现在更新", new OnClickListener()  
        {  
            @Override  
            public void onClick(DialogInterface dialog, int which)  
            {  
                dialog.dismiss();  
                // 显示下载对话框  
                showDownloadDialog();  
            }  
        });  
        // 稍后更新  
        builder.setNegativeButton("稍后更新", new OnClickListener()  
        {  
            @Override  
            public void onClick(DialogInterface dialog, int which)  
            {  
                dialog.dismiss();  
            }  
        });  
        Dialog noticeDialog = builder.create();  
        noticeDialog.show();
    }
	
    /** 
     * 显示软件下载对话框 
     */  
    private void showDownloadDialog() {
    	// 构造软件下载对话框  
        AlertDialog.Builder builder = new Builder(mContext);  
        builder.setTitle("正在下载");  
        // 给下载对话框增加进度条  
        final LayoutInflater inflater = LayoutInflater.from(mContext);  
        View v = inflater.inflate(R.layout.softupdate_progress, null);  
        mProgress = (ProgressBar) v.findViewById(R.id.update_progress);  
        builder.setView(v);  
        // 取消更新  
        builder.setNegativeButton("取消", new OnClickListener()  
        {  
            @Override  
            public void onClick(DialogInterface dialog, int which)  
            {  
                dialog.dismiss();  
                // 设置取消状态  
                cancelUpdate = true;  
            }  
        });  
        mDownloadDialog = builder.create();  
        mDownloadDialog.show();  
        // 现在文件  
        downloadApk(); 
    }
    
    /** 
     * 下载apk文件 
     */  
    private void downloadApk()  
    {  
        // 启动新线程下载软件  
        new downloadApkThread().start();  
    } 
    
    /**
     * 下载线程
     * @author mac
     *
     */
    private class downloadApkThread extends Thread{
    	@Override
    	public void run() {
    		try  
            {  
                // 判断SD卡是否存在，并且是否具有读写权限  
//                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))  
                {  
                    // 获得存储卡的路径  
                    String sdpath = "/mnt/sdcard/";  
                    mSavePath = sdpath + "download";  
                    URL url = new URL(UpdateManager.this.url);  
                    // 创建连接  
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
                    conn.connect();  
                    // 获取文件大小  
                    int length = conn.getContentLength();  
                    // 创建输入流  
                    InputStream is = conn.getInputStream();  
  
                    File file = new File(mSavePath);  
                    // 判断文件目录是否存在  
                    if (!file.exists())  
                    {  
                        file.mkdir();  
                    }  
                    File apkFile = new File(mSavePath, "zyposter.apk");  
                    FileOutputStream fos = new FileOutputStream(apkFile);  
                    int count = 0;  
                    // 缓存  
                    byte buf[] = new byte[1024];  
                    // 写入到文件中  
                    do  
                    {  
                        int numread = is.read(buf);  
                        count += numread;  
                        // 计算进度条位置  
                        progress = (int) (((float) count / length) * 100);  
                        // 更新进度  
                        mHandler.sendEmptyMessage(DOWNLOAD);  
                        if (numread <= 0)  
                        {  
                            // 下载完成  
                            mHandler.sendEmptyMessage(DOWNLOAD_FINISH);  
                            break;  
                        }  
                        // 写入文件  
                        fos.write(buf, 0, numread);  
                    } while (!cancelUpdate);// 点击取消就停止下载.  
                    fos.close();  
                    is.close();  
                }  
            } catch (MalformedURLException e)  
            {  
                e.printStackTrace();  
            } catch (IOException e)  
            {  
                e.printStackTrace();  
            }  
            // 取消下载对话框显示  
            mDownloadDialog.dismiss();  
  
    	}
    }
    
	/** 
	 * 获取软件版本号 
	 *  
	 * @param context 
	 * @return 
	 */  
	private int getVersionCode(Context context)  
	{  
	    int versionCode = 0;  
	    try  
	    {  
	        // 获取软件版本号，对应AndroidManifest.xml下android:versionCode  
	        versionCode = context.getPackageManager().getPackageInfo("com.szzy.packagesposter", 0).versionCode;  
	    } catch (NameNotFoundException e)  
	    {  
	        e.printStackTrace();  
	    }  
	    return versionCode;  
	} 
	
	/** 
	 * 获取软件版本名称
	 *  
	 * @param context 
	 * @return 
	 */  
	private String getVersionName(Context context)  
	{  
	    String versionName = "";  
	    try  
	    {  
	        // 获取软件版本号，对应AndroidManifest.xml下android:versionCode  
	    	versionName = context.getPackageManager().getPackageInfo("com.szzy.packagesposter", 0).versionName;  
	    } catch (NameNotFoundException e)  
	    {  
	        e.printStackTrace();  
	    }  
	    return versionName;  
	} 
	
	/** 
     * 安装APK文件 
     */  
    private void installApk()  
    {  
        File apkfile = new File(mSavePath, "zyposter.apk");  
        if (!apkfile.exists())  
        {  
            return;  
        }  
        // 通过Intent安装APK文件  
        Intent i = new Intent(Intent.ACTION_VIEW);  
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");  
        mContext.startActivity(i);  
    } 
}
