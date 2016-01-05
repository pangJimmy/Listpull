package com.szzy.packages.activity;

import java.io.IOException;
import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.mining.app.zxing.camera.CameraManager;
import com.mining.app.zxing.decoding.CaptureActivityHandler;
import com.mining.app.zxing.decoding.InactivityTimer;
import com.mining.app.zxing.view.ViewfinderView;
import com.szzy.packages.R;
import com.szzy.packages.http.HttpHelper;
import com.szzy.packages.http.OpenBoxCall;
import com.szzy.packages.tool.TipsHttpError;
/**
 * Initial the camera
 * @author Ryan.Tang
 */
public class MipcaActivityCapture extends Activity implements Callback {

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;

	
	private HttpHelper httpHelper ;
	private MApplication mApp ;
	private Handler mHandler = new Handler();
	private int mode = 0 ;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capture);
		//ViewUtil.addTopView(getApplicationContext(), this, R.string.scan_card);
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		mode = getIntent().getIntExtra("mode", 0);
		Log.e("ACTION", getIntent().getIntExtra("mode", 0)+ "");
		httpHelper = new HttpHelper();
		mApp = (MApplication) getApplication();
//		Button mButtonBack = (Button) findViewById(R.id.button_back);
//		mButtonBack.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				MipcaActivityCapture.this.finish();
//				
//			}
//		});
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}
	
	/**
	 * ´¦ÀíÉ¨Ãè½á¹û
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		 String resultString = result.getText();
		 Log.e("scan result ", resultString);
		if (resultString.equals("")) {
			Toast.makeText(MipcaActivityCapture.this, "Scan failed!", Toast.LENGTH_SHORT).show();
		}else {
			  ;
			
			if(mode == 1){//É¨ÃèÏä¹ñ±àºÅ
//				final Intent resultIntent = new Intent(MipcaActivityCapture.this, PostInfoActivity.class);
				
				resultString = resolveLockId(resultString) ;
				 Log.e("scan result222 ", resultString);
				final Intent resultIntent = new Intent();
				final Bundle bundle = new Bundle();
				bundle.putString("result", resultString);
				mApp.setLockId(resultString);
//				bundle.putString("result", resultString);
				//±£´æÏä¹ñ±àºÅ
//				mApp.setLockId(resultString);
//				startActivity(resultIntent);
				int lockCode = 0 ;
				try{
					lockCode = Integer.valueOf(resultString);
				}catch(Exception e){
					Toast.makeText(mApp, "Ïä¹ñ¶þÎ¬Âë´íÎó£¬ÇëÖØÐÂÉ¨Âë", 0).show();
					return ;
				}
				
				MipcaActivityCapture.this.setResult(RESULT_OK, resultIntent);
				MipcaActivityCapture.this.finish();
				//ÑéÖ¤
//				httpHelper.checkLock(mApp.getUser(), mApp.getPassword(), lockCode, new OpenBoxCall() {
//					
//					@Override
//					public void call(final String errorCode) {
//						Log.e("", ""+ errorCode);
//						
//						if("0".equals(errorCode)){
//							mHandler.post(new Runnable() {
//								
//								@Override
//								public void run() {
////									startActivity(resultIntent);
////									MipcaActivityCapture.this.finish();
//									MipcaActivityCapture.this.setResult(RESULT_OK, resultIntent);
//									MipcaActivityCapture.this.finish();
//								}
//							});
//						}else{
////							Toast.makeText(mApp, "err=" + errorCode, 0).show();
//							mHandler.post(new Runnable() {
//								
//								@Override
//								public void run() {
////									startActivity(resultIntent);
////									MipcaActivityCapture.this.finish();
//									//¸ø³öÌáÊ¾
//									TipsHttpError.toastError(mApp, errorCode);
//									MipcaActivityCapture.this.finish();
//								}
//							});
//						}
//						
//					}
//				});
			}else if(mode == 0){//¿ìµÝµ¥ºÅÉ¨Ãè
				Intent mIntent = new Intent();
				Bundle mBundle = new Bundle();
				mBundle.putString("barcode", resultString);
				mIntent.putExtras(mBundle);
				this.setResult(RESULT_OK, mIntent);
				MipcaActivityCapture.this.finish();
			}
		}
//		MipcaActivityCapture.this.finish();
	}
	
	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}
	
	//´¦ÀíÉ¨Ãè¹ñºÅ
	private String resolveLockId(String result){
		int index = result.indexOf("ID:");
		if(index > 0){
			String lockID = result.substring(index).replaceAll("\nNum.*", "").replaceAll("ID:", "");
			return lockID ;
		}
		
		return result ;
	}
	

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

}