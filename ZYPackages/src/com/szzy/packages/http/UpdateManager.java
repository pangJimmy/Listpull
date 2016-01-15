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
 * �����������
 * @author mac
 *
 */
public class UpdateManager {
	
    /* ������ */  
    private static final int DOWNLOAD = 1;  
    /* ���ؽ��� */  
    private static final int DOWNLOAD_FINISH = 2;  
    /* ��ȡ�汾�� */ 
    private static final int GET_VERSION = 3 ;
    /* ���������XML��Ϣ */  
//    HashMap<String, String> mHashMap;  
    /* ���ر���·�� */  
    private String mSavePath;  
    /* ��¼���������� */  
    private int progress;  
    /* �Ƿ�ȡ������ */  
    private boolean cancelUpdate = false;  
  
    private Context mContext;  
    /* ���½����� */  
    private ProgressBar mProgress;  
    private Dialog mDownloadDialog; 
    
    
    private HttpHelper httpHelper ;
    
    private String sysVersion = null;//�������ϵİ汾
    private String url = null ;//����APK��url
    
    
    
    private Handler mHandler = new Handler()  
    {  
        public void handleMessage(Message msg)  
        {  
            switch (msg.what)  
            {  
            // ��������  
            case DOWNLOAD:  
                // ���ý�����λ��  
                mProgress.setProgress(progress);  
                break;  
            case DOWNLOAD_FINISH:  
                // ��װ�ļ�  
                installApk();  
                break;  
            case GET_VERSION:
            	//��ȡ�汾�ţ��Ƿ����
            	if(sysVersion != null){
            		//�汾��һ����ʾ�Ƿ����
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
		//�������������
    	httpHelper.getVersion(new HttpCallBack() {
			
			@Override
			public void call(Object obj, String err) {
				//�ӷ������ϳɹ���ȡ�汾��
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
     * ��ʾ������¶Ի��� 
     */  
    private void showNoticeDialog(){
    	// ����Ի���  
        AlertDialog.Builder builder = new Builder(mContext);  
        builder.setTitle("�������");  
        builder.setMessage("�����°汾");  
        // ����  
        builder.setPositiveButton("���ڸ���", new OnClickListener()  
        {  
            @Override  
            public void onClick(DialogInterface dialog, int which)  
            {  
                dialog.dismiss();  
                // ��ʾ���ضԻ���  
                showDownloadDialog();  
            }  
        });  
        // �Ժ����  
        builder.setNegativeButton("�Ժ����", new OnClickListener()  
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
     * ��ʾ������ضԻ��� 
     */  
    private void showDownloadDialog() {
    	// ����������ضԻ���  
        AlertDialog.Builder builder = new Builder(mContext);  
        builder.setTitle("��������");  
        // �����ضԻ������ӽ�����  
        final LayoutInflater inflater = LayoutInflater.from(mContext);  
        View v = inflater.inflate(R.layout.softupdate_progress, null);  
        mProgress = (ProgressBar) v.findViewById(R.id.update_progress);  
        builder.setView(v);  
        // ȡ������  
        builder.setNegativeButton("ȡ��", new OnClickListener()  
        {  
            @Override  
            public void onClick(DialogInterface dialog, int which)  
            {  
                dialog.dismiss();  
                // ����ȡ��״̬  
                cancelUpdate = true;  
            }  
        });  
        mDownloadDialog = builder.create();  
        mDownloadDialog.show();  
        // �����ļ�  
        downloadApk(); 
    }
    
    /** 
     * ����apk�ļ� 
     */  
    private void downloadApk()  
    {  
        // �������߳��������  
        new downloadApkThread().start();  
    } 
    
    /**
     * �����߳�
     * @author mac
     *
     */
    private class downloadApkThread extends Thread{
    	@Override
    	public void run() {
    		try  
            {  
                // �ж�SD���Ƿ���ڣ������Ƿ���ж�дȨ��  
//                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))  
                {  
                    // ��ô洢����·��  
                    String sdpath = "/mnt/sdcard/";  
                    mSavePath = sdpath + "download";  
                    URL url = new URL(UpdateManager.this.url);  
                    // ��������  
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
                    conn.connect();  
                    // ��ȡ�ļ���С  
                    int length = conn.getContentLength();  
                    // ����������  
                    InputStream is = conn.getInputStream();  
  
                    File file = new File(mSavePath);  
                    // �ж��ļ�Ŀ¼�Ƿ����  
                    if (!file.exists())  
                    {  
                        file.mkdir();  
                    }  
                    File apkFile = new File(mSavePath, "zyposter.apk");  
                    FileOutputStream fos = new FileOutputStream(apkFile);  
                    int count = 0;  
                    // ����  
                    byte buf[] = new byte[1024];  
                    // д�뵽�ļ���  
                    do  
                    {  
                        int numread = is.read(buf);  
                        count += numread;  
                        // ���������λ��  
                        progress = (int) (((float) count / length) * 100);  
                        // ���½���  
                        mHandler.sendEmptyMessage(DOWNLOAD);  
                        if (numread <= 0)  
                        {  
                            // �������  
                            mHandler.sendEmptyMessage(DOWNLOAD_FINISH);  
                            break;  
                        }  
                        // д���ļ�  
                        fos.write(buf, 0, numread);  
                    } while (!cancelUpdate);// ���ȡ����ֹͣ����.  
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
            // ȡ�����ضԻ�����ʾ  
            mDownloadDialog.dismiss();  
  
    	}
    }
    
	/** 
	 * ��ȡ����汾�� 
	 *  
	 * @param context 
	 * @return 
	 */  
	private int getVersionCode(Context context)  
	{  
	    int versionCode = 0;  
	    try  
	    {  
	        // ��ȡ����汾�ţ���ӦAndroidManifest.xml��android:versionCode  
	        versionCode = context.getPackageManager().getPackageInfo("com.szzy.packagesposter", 0).versionCode;  
	    } catch (NameNotFoundException e)  
	    {  
	        e.printStackTrace();  
	    }  
	    return versionCode;  
	} 
	
	/** 
	 * ��ȡ����汾����
	 *  
	 * @param context 
	 * @return 
	 */  
	private String getVersionName(Context context)  
	{  
	    String versionName = "";  
	    try  
	    {  
	        // ��ȡ����汾�ţ���ӦAndroidManifest.xml��android:versionCode  
	    	versionName = context.getPackageManager().getPackageInfo("com.szzy.packagesposter", 0).versionName;  
	    } catch (NameNotFoundException e)  
	    {  
	        e.printStackTrace();  
	    }  
	    return versionName;  
	} 
	
	/** 
     * ��װAPK�ļ� 
     */  
    private void installApk()  
    {  
        File apkfile = new File(mSavePath, "zyposter.apk");  
        if (!apkfile.exists())  
        {  
            return;  
        }  
        // ͨ��Intent��װAPK�ļ�  
        Intent i = new Intent(Intent.ACTION_VIEW);  
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");  
        mContext.startActivity(i);  
    } 
}
