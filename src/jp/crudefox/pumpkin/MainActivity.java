package jp.crudefox.pumpkin;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import jp.crudefox.library.help.Helper;
import jp.crudefox.pumpkin.camera.CFCamera;
import jp.crudefox.pumpkin.camera.CFCamera.CameraType;
import jp.crudefox.pumpkin.camera.PanoPano;
import jp.crudefox.pumpkin.camera.SensorValue;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.Toast;
import cf.android.ar.d3.Circle;
import cf.android.ar.d3.Cube;
import cf.android.ar.d3.Line;
import cf.android.ar.d3.PumpBall;
import cf.android.ar.d3.Text;



public class MainActivity extends Activity {


	Handler mHandler = new Handler();

	CFCamera mCamera;

    SensorManager mSensorManager;
    Sensor mMagSensor;
    Sensor mAccSensor;

    SensorValue mSensorValue;

//    int miOrientation;
//    int mDispOrientaiton;

    float mDensity;

    int mDisplayDegree;

    ViewGroup mPreviewContainer;

	CameraPreview mPreview;
	CameraOverlay mOverlay;
	CameraOverlayView mOverlayView;

	//Size mPreviewSize;
	Size mReservedPreviewSize;



	View mShutterBtn;


	private PanoPano mPano;


	private boolean mIsShutterBtnPressed = false;

	private boolean mIsAutoFocusing = false;


	private Canvas mCanvas;

    private Bitmap mDrawBitmap;
	private Bitmap mPreviewBitmap;
	private Bitmap mSaveBitmap;
	private int[] mDataBuf;
	private int[] mDataBuf2;
	private short[] mShortArrTable;
	//private short[] mDynamicRangeBuf;
	boolean mIsNewShortArrTable = false;


	private int[] getDataBuf(int size){
		if(mDataBuf==null) return mDataBuf = new int[size];
		if(mDataBuf.length!=size) return mDataBuf = new int[size];
		return mDataBuf;
	}
//	private int[] getDataBuf2(int size){
//		if(mDataBuf2==null) return mDataBuf2 = new int[size];
//		if(mDataBuf2.length!=size) return mDataBuf2 = new int[size];
//		return mDataBuf2;
//	}
//	private short[] getShortArrTable(int size){
//		if(mShortArrTable==null){ mIsNewShortArrTable=true; return mShortArrTable= new short[size]; }
//		if(mShortArrTable.length!=size){ mIsNewShortArrTable=true; return mShortArrTable = new short[size]; }
//		return mShortArrTable;
//	}
	private static Bitmap getBitmap(Bitmap bmp,int width,int height){
		if(bmp==null) return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		if(bmp.getWidth()!=width || bmp.getHeight()!=height){
			bmp.recycle();
			return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		}
		return bmp;
	}
	private Bitmap getPreviewBitmap(int width,int height){
		return mPreviewBitmap = getBitmap(mPreviewBitmap, width, height);
	}
	private Bitmap getSaveBitmap(int width,int height){
		return mSaveBitmap = getBitmap(mSaveBitmap, width, height);
	}
	private Bitmap getDrawBitmap(int width,int height){
		return mDrawBitmap = getBitmap(mDrawBitmap, width, height);
	}
	private Bitmap getDrawBitmap(){
		return mDrawBitmap;
	}
	private void cleanBuf(){
		if(mPreviewBitmap!=null) {
			mPreviewBitmap.recycle(); mPreviewBitmap = null;
		}
		if(mSaveBitmap!=null){
			mSaveBitmap.recycle(); mSaveBitmap = null;
		}
		if(mDrawBitmap!=null){
			mDrawBitmap.recycle(); mDrawBitmap = null;
		}
		mDataBuf = null;
		mDataBuf2 = null;
		mShortArrTable = null; mIsNewShortArrTable = false;
		//mDynamicRangeBuf = null;
	}



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Resources res = getResources();

		Context appc = getApplicationContext();
		Context context = this;

		mDensity = res.getDisplayMetrics().density;

		mSensorValue = new SensorValue();

		mCamera = new CFCamera();

		mCanvas = new Canvas();

		mPano = new PanoPano();

		mDisplayDegree = Helper.getDisplayDegree(this);

		mPreviewContainer = (FrameLayout) findViewById(R.id.preview_container);
		mShutterBtn = findViewById(R.id.shutter);



		mShutterBtn.setOnTouchListener(new View.OnTouchListener() {
			boolean mmIsDown = false;
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				int action = event.getActionMasked();

				if(action==MotionEvent.ACTION_DOWN){
					if(!mmIsDown){
						mmIsDown = true;
						toast("down");
						onShutterButtonDown();
					}
				}
				else if(action==MotionEvent.ACTION_UP || action==MotionEvent.ACTION_CANCEL){
					if(mmIsDown){
						mmIsDown = false;
						toast("up");
						onShutterButtonUp();
					}
				}

				return true;
			}
		});






		mPreview = new CameraPreview(context);
		mOverlay = new CameraOverlay(context);
		mOverlayView = new CameraOverlayView(context);

        //LayoutParams lp;
        FrameLayout.LayoutParams flp;
        flp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mOverlay.setLayoutParams(flp);

        flp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mPreview.setLayoutParams(flp);

        flp = new FrameLayout.LayoutParams((int)(256*mDensity), (int)(256*mDensity));
        flp.gravity = Gravity.CENTER;
        mOverlayView.setLayoutParams(flp);

        mOverlay.setZOrderMediaOverlay(true);

		mPreviewContainer.addView(mPreview);
		mPreviewContainer.addView(mOverlay);
		mPreviewContainer.addView(mOverlayView);

		//mPreviewContainer.addView(mOv);


//        mOverlay.setZOrderMediaOverlay(true);
//        mPreview.setZOrderMediaOverlay(true);
//        layout_Preview.bringToFront();
        mOverlay.setZOrderOnTop(true);


	}


	private void onShutterButtonDown(){
//		if(mMode==TakePictMode.PRESSING_CONTINUOUS){
//			mIsPressingContinuousShooting = true;
//		}
		mIsShutterBtnPressed = true;

		takePicture();
	}
	private void onShutterButtonUp(){
//		if(mMode==TakePictMode.PRESSING_CONTINUOUS){
//			mIsPressingContinuousShooting = false;
//		}
		mIsShutterBtnPressed = false;
	}
	private void onShutterButtonClick(){
//		if(mIsIntervalContinuousShooting){
//			finishRepeatContinuesShooting();
//		}else{
//			if(mMode==TakePictMode.QUICK){
//				takePicture();
//			}else if(mMode==TakePictMode.INTERVAL_CONTINUOUS){
//				startRepeatContinuesShooting();
//			}
//		}
	}
	private void onLongShutterButton(){
//		if(mMode==TakePictMode.QUICK){
//			mIsAutoFocusTakePict=true;
//			autoFocus();
//		}
	}


	private void strat(){
		//
	}

    //写真を撮る
    private void takePicture(){
		//mCamera.takePicture(null,null,mPictureCallback);

    	if(mCamera.isOpened() && mCamera.isPreviewing() && !mIsAutoFocusing &&mPrevCallBackNum<2){
    		mPrevCallBackNum++;
    		//mOneShotNum++;
    		//mIsOneShot = true;
    		//mShutteredNum++;
    		//mCameraM.setPreviewCallback(mPreviewCallback);
    		//mCamera.setOneShotPreviewCallback(mPreviewCallback);
			//if(!mIsContinuesShooting) mHandler.post(mRun_OverlayInvalidate);

    		mCamera.addCallbackBuffer();
    	}
    }

    private synchronized void startRepeatContinuesShooting(){
//    	if(!mIsIntervalContinuousShooting){
//	    	if(mCamera.isOpened() && mCamera.isPreviewing() && !mIsAutoFocusing){
//	    		mIsIntervalContinuousShooting = true;
//	    		mHandler.post(mRun_RepeartContinuesTakePict);
//	    		mes_post(getString(R.string.start_shooting));
//	    	}
//    	}
    }
    private synchronized void finishRepeatContinuesShooting(){
//    	if(mIsIntervalContinuousShooting){
//    		mIsIntervalContinuousShooting = false;
//    		mHandler.removeCallbacks(mRun_RepeartContinuesTakePict);
//    		mes_post(getString(R.string.finish_shooting));
//    	}
    }


	@Override
	protected void onPause() {
		super.onPause();


	}

	@Override
	protected void onResume() {
		super.onResume();


	}

	@Override
	protected void onStart() {
		super.onStart();
		if(!mCamera.isOpened()){
			int camId = CFCamera.findCameraId(CameraType.Back);
			if(camId==-1) camId = 0;
			mCamera.openCamera(camId);
		}
		//if(mCamera.isOpened()){
			Configuration config = getResources().getConfiguration();
			mSensorValue.isDiffDigree = config.orientation == Configuration.ORIENTATION_PORTRAIT;//mCamera.isDiffRotateRect(mDisplayDegree);
		//}
		initSensor();
		//startMyLoop();
	}

	@Override
	protected void onStop() {
		super.onStop();
		//stopMyLoop();
		if(mCamera.isOpened()){
			mCamera.releaseCamera();
		}
		releaseSensor();
		//cleanBuf();
	}


    //センサー初期化
    public void initSensor(){

    	mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

    	// センサの取得
        List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        // センサマネージャへリスナーを登録(implements SensorEventListenerにより、thisで登録する)
        for (Sensor sensor : sensors) {

            if( sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
                mSensorManager.registerListener(mSendorLis, sensor, SensorManager.SENSOR_DELAY_UI);
                mMagSensor = sensor;
            }

            if( sensor.getType() == Sensor.TYPE_ACCELEROMETER){
                mSensorManager.registerListener(mSendorLis, sensor, SensorManager.SENSOR_DELAY_UI);
                mAccSensor = sensor;
            }
        }

    }
    //センサー解放
    public void releaseSensor(){
    	//センサーマネージャのリスナ登録破棄
        if (mMagSensor!=null || mAccSensor!=null) {
            mSensorManager.unregisterListener(mSendorLis);
            mMagSensor = null;
            mAccSensor = null;
        }
    }


    //センサー
    private SensorEventListener mSendorLis = new SensorEventListener() {
		public void onSensorChanged(SensorEvent event) {
			mSensorValue.onSensorChanged(event);
			int roll = (int)  mSensorValue.getRollDegree() ;
			int pitch = (int)  mSensorValue.getPitchDegree() ;
			int azim = (int)Helper.to0to360ByDegree( mSensorValue.getAzimuthDegree() );
			//AppUtil.Log("方角="+azim+" roll="+roll+" pitch="+pitch);
		}
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}
	};

	//プレビューを取得して変換して
	private int[] toRGBFromPreview(byte[] data){

		try {
			final int cam_degree = mCamera.getCameraDegree();
			final int width = mCamera.getPreviewSize().width; // プレビューの幅
			final int height = mCamera.getPreviewSize().height; // プレビューの高さ
			int[] rgb = getDataBuf(width*height); // ARGB8888の画素の配列

			Point size = new Point();
			AppUtil.decodeYUV420SP(rgb, data, width, height, cam_degree, mDisplayDegree, size);

			return rgb;
		}
		catch (OutOfMemoryError e) {
			e.printStackTrace();
			//mes_post(getString(R.string.out_of_memory_error)+"\n"+e);
		}
		catch (Exception e) {
			e.printStackTrace();
			//mes_post(getString(R.string.error)+"\n"+e);
		}

		return null;
	}


	//プレビューを取得して変換して
	private Bitmap toBitmapFromRGB(int[] rgb){
		try {
			final int width = mCamera.getPreviewSize().width; // プレビューの幅
			final int height = mCamera.getPreviewSize().height; // プレビューの高さ
			int[] rgb2 = null ;
			short[] shortArrTable=null;
			int dest_width,dest_height;
			if(mCamera.isDiffRotateRect(mDisplayDegree)){
				dest_height = width;
				dest_width = height;
			}else{
				dest_width = width;
				dest_height = height;
			}


			Bitmap bmp = getPreviewBitmap(dest_width, dest_height);
			//timetime = System.currentTimeMillis();
			bmp.setPixels(rgb, 0, dest_width, 0, 0, dest_width, dest_height); // 変換した画素からビットマップにセット
			//long setp_time = System.currentTimeMillis()-timetime;

			//AppUtility.Log("effect="+effect_time+"; setPixels="+setp_time);

			return bmp;

		}catch (OutOfMemoryError e) {
			e.printStackTrace();
			//mes_post(getString(R.string.out_of_memory_error)+"\n"+e);
		}
		catch (Exception e) {
			e.printStackTrace();
			//mes_post(getString(R.string.error)+"\n"+e);
		}

		return null;
	}


	boolean mIsRunningPreviewCallback = false;
	int mPrevCallbackCallTotalCount = 0;
	int mPrevCallBackNum;
	boolean mIsTakingPict = false;

    //プレビューを取得
    final PreviewCallback mPreviewCallback = new PreviewCallback() {
		public void onPreviewFrame(final byte[] data, Camera camera) {

			//AppUtil.Log("preview ");

			mIsRunningPreviewCallback = true;

			mPrevCallbackCallTotalCount++;

			if(mCamera==null){ mIsRunningPreviewCallback = false; return ; }
			if(!mCamera.isOpened() || !mCamera.isPreviewing()){
				mCamera.endCallbackBuffer(data);
				mIsRunningPreviewCallback = false;
				return ;
			}

			//if(AppUtility.isDebug()) AppUtility.Log("PreviewCallback "+data);

			//
			mCamera.addCallbackBuffer();

			//FPS計算
//			long nowTime = System.currentTimeMillis();
//			if(mPrevCallbackCount==0){
//				mPreviewCallbackFPSStartTime = nowTime;
//				mPrevCallbackCount = 1;
//			}
//			else if(nowTime-mPreviewCallbackFPSStartTime>1000){
//				mFPS = mPrevCallbackCount;
//				//if(AppUtility.isDebug()) AppUtility.Log("FPS="+mFPS);
//				mPrevCallbackCount = 0;
//			}else{
//				mPrevCallbackCount++;
//			}

//			mCameraM.endCallbackBuffer(data);
//			if(true) return ;

			//変換処理
			long timetime = System.currentTimeMillis();
			int rgb[] = toRGBFromPreview(data);

			mCamera.endCallbackBuffer(data);

			if(rgb == null){
				mIsRunningPreviewCallback = false;
				return ;
			}
			Bitmap bmp = toBitmapFromRGB(rgb);
			//long pretobimp_time = (System.currentTimeMillis()-timetime);
			if(bmp==null){
				mIsRunningPreviewCallback = false;
				return ;
			}

			float h_vd = mCamera.getHorizontalViewAngle();
			float v_vd = mCamera.getVerticalViewAngle();

			//拡大処理
			timetime = System.currentTimeMillis();
//			Bitmap dBmp = getDrawBitmap(bmp.getWidth(),bmp.getHeight());
//			mCanvas.setBitmap(dBmp);

//			boolean isSave = mZoomScale!=1.0f || mCameraM.isFrontFace();
//			if(isSave) mCanvas.save();
//			if(mCameraM.isFrontFace()){
//				mCanvas.rotate(180, bmp.getWidth()*0.5f, bmp.getHeight()*0.5f);
//			}
//			if(mZoomScale!=1.0f){
//				mCanvas.scale(mZoomScale, mZoomScale, bmp.getWidth()*0.5f, bmp.getHeight()*0.5f);
//			}
			//BitmapDrawable bd = new BitmapDrawable(bmp);
			//bd.setBounds(0,0,(int)(bmp.getWidth()*mZoomScale),(int)(bmp.getHeight()*mZoomScale));
			//bd.draw(mCanvas);

			//mCanvas.drawBitmap(bmp, 0,0, null);

//			if(mReportBmp==null){
//				mCanvas.drawBitmap(bmp, 0,0, null);
//			}else{
//				mCanvas.drawBitmap(mReportBmp, 0,0, null);
//			}
//			if(isSave) mCanvas.restore();
			//long sc_time = (System.currentTimeMillis()-timetime);

			//描画
			timetime = System.currentTimeMillis();
			//mPreview.doDraw();
			//long dodraw_time = System.currentTimeMillis()-timetime;
			//if(AppUtility.isDebug()) AppUtility.Log("preToBmp="+pretobimp_time+"; scale="+sc_time+"; doDraw="+dodraw_time);

//			if(mIsPressingContinuousShooting){
//				mPrevCallBackNum = 1;
//			}

			SensorValue sv = mSensorValue;

			mPano.draw(bmp,
					Helper.to0to360ByDegree(sv.getAzimuthDegree()),
					//sv.getAzimuthDegree(),
					sv.getPitchDegree(),
					h_vd, v_vd,
					sv.getRollDegree());
			
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					mOverlayView.invalidate();
				}
			});

			Bitmap dBmp = mPano.getBitmap();

			if(mPrevCallBackNum==0){
				//何もしない
			}else if(!mIsTakingPict){
				mIsTakingPict = true;
				final Bitmap svBmp = getSaveBitmap(dBmp.getWidth(), dBmp.getHeight());

				timetime = System.currentTimeMillis();
				Canvas cv = new Canvas(svBmp);
				cv.drawBitmap(dBmp, 0, 0, null);
				if(AppUtil.isDebug()) AppUtil.Log("bitmap copy ="+(System.currentTimeMillis()-timetime));

	        	Thread th = new Thread(new Runnable() {
					public void run() {
						if(svBmp!=null){
							postToast("保存...");
							saveBitmap(svBmp);
						}
						mIsTakingPict = false;
					}
				});
	        	th.start();
				mPrevCallBackNum--;
			}

//			mCameraM.addCallbackBuffer();

			mIsRunningPreviewCallback = false;
//            mCameraM.setOneShotPreviewCallback(mPreviewCallback);

		}
	};



	//プレビューを取得して変換してファイルへ保存
	private boolean saveBitmap(Bitmap bmp){

		try {
			Bitmap thumb;
			File file;

			synchronized (bmp) {
				int jpeg_quality = 90;
				int width = bmp.getWidth();
				int height = bmp.getHeight();
				float scale = Helper.calcFitSizeScale(width,height, 48*mDensity,48*mDensity);
				//サムネイル生成
				thumb = Bitmap.createScaledBitmap(bmp,(int)(width*scale),(int)(height*scale),true);

				//保存先
				file = FileManager.getTakePictureFile(new Date());
				if(file==null){
					postToast("保存できませんでした。");
					return false;
				}

				//ギャラリー
				if(thumb!=null){
					//addGalleyImage_post(thumb,file);
				}

				//保存
				long timetime = System.currentTimeMillis();
				if( !AppUtil.saveBitmap(this,bmp, file, jpeg_quality, true) ){
					file = null;
				}
				AppUtil.Log("AppUtility.saveBitmap="+(System.currentTimeMillis()-timetime));

				if(file==null){
					postToast("保存できませんでした。");
					return false;
				}
			}

			postToast("保存しました。");

			return true;


		}catch (OutOfMemoryError e) {
			e.printStackTrace();
			postToast("メモリ不足"+"\n"+e);
			return false;
		}
		catch (Exception e) {
			e.printStackTrace();
			postToast("エラー"+"\n"+e);
			return false;
		}
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	private boolean startCameraPreview(){
		if(!mCamera.isOpened()) return false;	//起動してないので無視
		if(mCamera.isPreviewing()) return false;//すでに動いている
		if(mReservedPreviewSize==null) return false;//サイズが決められてな
		//if(!mCamera.hasPreviewDisplayOrTexture()) return ;
		//if( !mTextureView.isAvailable() ) return false;	//まだプレビューできない
		if( !mPreview.isSurfaceCreated() ) return false;	//まだプレビューできない
//		SurfaceTexture st = mTextureView.getSurfaceTexture();
//		if( st == null ) return false;//まだプレビューできない。
//		if( !mCamera.setPreviewTexture(st) ) return false;//プレビューできない。
		//mCamera.startPreview();
		mCamera.setPreviewCallbackWithBuffer(mPreviewCallback);
		int degree = mDisplayDegree;
		mCamera.setOrientationByDisplayDegree(degree);
		mCamera.setPreviewSize(mReservedPreviewSize);
		mCamera.startPreview();
		setOverlayDisplaySize(
				mPreview.getWidth(),
				mPreview.getHeight(),
				false
				//mCamera.isDiffRotateRect(degree)
				);
		return true;
	}
	private void stopCameraPreview(){
		if(!mCamera.isOpened()) return;
		if(!mCamera.isPreviewing()) return;
		mCamera.stopPreview();
//		if(mIsAutoFocusing){
//			mIsAutoFocusing = false;
//			mCamera.cancelAutoFocus();
//		}
//		mIsTakingFinePicture = false;
//		mIsTakingPreview = false;
		mCamera.setPreviewCallbackWithBuffer(null);
		mCamera.endCallbackBufferAll();
	}


	private void setOverlayDisplaySize(int width,int height,boolean inverse_aspect){
		//
		FrameLayout.LayoutParams flp = (FrameLayout.LayoutParams) mOverlay.getLayoutParams();
		if(inverse_aspect){
			flp.width = height;
			flp.height = width;
		}else{
			flp.width = width;
			flp.height = height;
		}
		mOverlay.setLayoutParams(flp);

		//
//		FrameLayout.LayoutParams flp = (FrameLayout.LayoutParams) mOverlay.getLayoutParams();
//		if(inverse_aspect){
//			flp.width = height;
//			flp.height = width;
//		}else{
//			flp.width = width;
//			flp.height = height;
//		}
//		mOverlay.setLayoutParams(flp);
	}




    //プレビュー描画サーフェイス
	class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
        private SurfaceHolder mHolder;


        public CameraPreview(Context context) {
            super(context);
            mHolder = getHolder();
            mHolder.addCallback(this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        public boolean isSurfaceCreated(){
        	return mHolder!=null;
        }


        public void surfaceCreated(SurfaceHolder holder) {
        	Log.d("cf","surfaceCreated");
        	if(mCamera!=null){
        		try {
					mCamera.setPreviewDisplay(holder);
				} catch (Exception e) {
					e.printStackTrace();
					toast("カメラでプレビューできませんでした。");
				}
        	}
        }


        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        	Log.d("cf","surfaceChenged");
        	if(mCamera!=null){
        		stopCameraPreview();
//	            Camera.Parameters params = mCamera.getParameters();
//	            params.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
//	            mCamera.setPreviewSize(mPreviewSize);
	            requestLayout();
//	            mCamera.setParameters(params);
	            startCameraPreview();
        	}
        	Log.d("cf","surfaceChanged - end");
        }


        public void surfaceDestroyed(SurfaceHolder holder) {
        	Log.d("cf","surfaceDestroyed");
        	if(mCamera!=null){
        		stopCameraPreview();
        	}
        }

        @Override
    	public boolean onTouchEvent(MotionEvent me) {
    		if(me.getAction()==MotionEvent.ACTION_DOWN) {
    			Log.d("cf","onTouchEvent - start");
    			Log.d("cf","onTouchEvent - end");
    		}
    		return true;
    	}

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        	Log.d("cf","onMeasure");

        	List<Size> supsize = mCamera.getSupportedPreviewSizes();
            if (supsize != null) {
//            	final int mode_w = MeasureSpec.getMode(widthMeasureSpec);
//            	final int mode_h = MeasureSpec.getMode(heightMeasureSpec);
            	final int size_w = MeasureSpec.getSize(widthMeasureSpec);
            	final int size_h = MeasureSpec.getSize(heightMeasureSpec);
            	int width,height;
            	Size prev_size;
            	if(mCamera.isDiffRotateRect(mDisplayDegree)){
            		prev_size = getFitPreviewSize(supsize, size_h, size_w);
            		width = prev_size.height;
            		height = prev_size.width;
            	}else{
            		prev_size = getFitPreviewSize(supsize, size_w, size_h);
            		width = prev_size.width;
            		height = prev_size.height;
            	}

            	AppUtil.Log("size="+prev_size.width+","+prev_size.height);

            	float scale = Helper.calcFitSizeScale(new PointF(width,height), new PointF(size_w,size_h));
            	setMeasuredDimension((int)(width*scale), (int)(height*scale));

            	AppUtil.Log("scale="+scale);

            	mReservedPreviewSize = prev_size;

            }else{
	            final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
	            final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
	            setMeasuredDimension(width, height);
            }
        }

    }



    //オーバレイ
	private class CameraOverlay extends GLSurfaceView implements Renderer{

		Cube mCube = new Cube();
		Circle mCircle = new Circle();
		Line mRing = Line.createRing(32, 1, 0,0,0);
		Line mLine = Line.createLine(
				new float[]{
						0,0,0,
						0,1,0},
				new float[]{
						1,1,1,1,
						0.5f,0.5f,0.5f,1
				});
		Text[] mAzmText = new Text[]{new Text(),new Text(),new Text(),new Text()};

		PumpBall mBall;

		int mWidth;
		int mHeight;

		public CameraOverlay(Context context) {
			super(context);
			setEGLConfigChooser(8, 8, 8, 8, 16, 0);
	        getHolder().setFormat( PixelFormat.TRANSLUCENT );
			setRenderer(this);
		}

		public void myDraw(Canvas canvas) {
			super.draw(canvas);
			int w = getWidth();
			int h = getHeight();
			RectF rc = new RectF(0,0,w,h);
			float cx = rc.centerX();
			float cy = rc.centerY();
			float maxsize = Math.max(w, h);
			float maxsize_half = maxsize*0.5f;

			float roll_d = (float)Math.toDegrees( mSensorValue.getRoll() );
			float pitch_d = (float)Math.toDegrees( mSensorValue.getPitch() );

			Paint paint = new Paint();
			paint.setStrokeWidth(1);

			canvas.save();
			canvas.rotate((float)-roll_d,cx,cy);

			paint.setColor(Color.RED);
			canvas.drawLine(cx-maxsize_half, cy, cx+maxsize_half, cy, paint);
			paint.setColor(Color.BLUE);
			canvas.drawLine(cx, cy-maxsize_half, cx, cy+maxsize_half, paint);

			canvas.translate(0, (h*0.5f) * -(pitch_d/90.0f) );
			paint.setColor(Color.GREEN);
			canvas.drawLine(cx-maxsize_half, cy, cx+maxsize_half, cy, paint);

			canvas.restore();

		}

		public void onDrawFrame(GL10 gl) {
//			Log.d("cf","onDrawFrame(GL10 gl)");

//			gl.glClearColor(1,1,1,0);
			//gl.glEnable(GL10.GL_DEPTH_TEST);
			//gl.glDepthMask(true); // 深度バッファ書き込み有効

			float roll = mSensorValue.getRoll();
			float pitch = mSensorValue.getPitch();
			float azimuth = mSensorValue.getAzimuth();
			float roll_d = (float)Math.toDegrees(roll);
			float pitch_d = (float)Math.toDegrees(pitch);
			float azimuth_d = (float)Math.toDegrees(azimuth);

			//Log.d("cf",String.format("%.1f",azimuth_d));

			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

			float lookAtX = (float)(Math.cos(azimuth) * Math.cos(-pitch)) * 1;
			float lookAtZ = (float)(Math.sin(azimuth) * Math.cos(-pitch)) * 1;
			float lookAtY = (float)(Math.sin(-pitch)) * 1;
//			float lookAtX = 0;//(float)(Math.cos(azimuth) * Math.cos(-pitch));
//			float lookAtZ = -3;//(float)(Math.sin(azimuth) * Math.cos(-pitch));
//			float lookAtY = 0;//(float)(Math.sin(-pitch));//(float)(Math.sin(-pitch));

//			float UpX = (float)(Math.cos(roll+Math.PI/2.0f));
//			float UpZ = 0;
//			float UpY = (float)(Math.sin(roll+Math.PI/2.0f));
			float UpX = 0;
			float UpZ = 0;
			float UpY = 1;

		    gl.glMatrixMode(GL10.GL_PROJECTION);
		    gl.glLoadIdentity();
//		    gl.glRotatef(roll_d, 0, 0, 1);
		    GLU.gluPerspective(gl, 45f,(float) mWidth / mHeight, 1f, 100f);
		    gl.glRotatef(roll_d, 0, 0, 1);
			GLU.gluLookAt(gl, 0, 0, 0,      lookAtX,lookAtY,lookAtZ,    UpX,UpY,UpZ);

		    gl.glMatrixMode(GL10.GL_MODELVIEW);
		    gl.glLoadIdentity();

//		    gl.glEnable(GL10.GL_LIGHTING);
//		  	gl.glEnable(GL10.GL_LIGHT0);

		    //ﾃｸｽﾁｬ有効
		    gl.glEnable(GL10.GL_TEXTURE_2D);
		    //色配列なし
		    gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
		    gl.glColor4f(1,1,1,0.5f);

//		    //文字描画
//		    for(int i=0;i<4;i++){
//			    gl.glLoadIdentity();
//			    gl.glRotatef(i*90, 0,1,0);
//			    gl.glTranslatef(0,0,10);
//			    mAzmText[i].draw(gl);
//		    }


		    //テクスチャ無効化
		    gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		    gl.glBindTexture( GL10.GL_TEXTURE_2D, 0 );
		    gl.glDisable(GL10.GL_TEXTURE_2D);
		    gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		    gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

		    gl.glLoadIdentity();
		    //gl.glTranslatef(0,0,10);
		    gl.glScalef(10,10,10);
		    //mBall.draw(gl);


		    //テクスチャ無効化
		    gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		    gl.glBindTexture( GL10.GL_TEXTURE_2D, 0 );
		    gl.glDisable(GL10.GL_TEXTURE_2D);

		    //色無し
		    gl.glDisableClientState(GL10.GL_COLOR_ARRAY);

		    //地面
		    gl.glLineWidth(2);
			gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
			float[] hsv = new float[3];
		    for(int i=1;i<=20;i++){
		    	hsv[0] = 240.0f - (i * 240.0f / 20);
		    	hsv[1] = 1;
		    	hsv[2] = 0.75f;
		    	int rgb = Color.HSVToColor(hsv);
				gl.glColor4f( Color.red(rgb)/255.0f, Color.green(rgb)/255.0f, Color.blue(rgb)/255.0f, 1.0f);
			    gl.glLoadIdentity();
			    gl.glTranslatef(0, -1, 0);
			    gl.glScalef(i,i,i);
			    gl.glRotatef(90, 1, 0, 0);
			    mRing.draw(gl,false);
		    }

		    gl.glLineWidth(2);
			gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
		    for(int i=0;i<16;i++){
				if(i/4==0) 		gl.glColor4f( 1,0,0, 1.0f);
				else if(i/4==1)	gl.glColor4f( 0,1,0, 1.0f);
				else if(i/4==2)	gl.glColor4f( 0,0,1, 1.0f);
				else			gl.glColor4f( 1,1,1, 1.0f);
			    gl.glLoadIdentity();
			    gl.glTranslatef(0, -1, 0);
			    gl.glRotatef(i*90/4, 0, 1, 0);
			    gl.glScalef(20,20,20);
			    gl.glRotatef(90, 1, 0, 0);
			    mLine.draw(gl,false);
		    }

//		    gl.glLineWidth(2);
//			gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
//			gl.glColor4f( 0,1,0, 1.0f);
//
//		    gl.glLoadIdentity();
//		    gl.glTranslatef(0, -2, 0);
//		    gl.glRotatef(0, 0, 1, 0);
//		    gl.glScalef(20,20,20);
//		    gl.glRotatef(90, 1, 0, 0);
//		    mLine.draw(gl,true);

		}


		public void onSurfaceChanged(GL10 gl, int width, int height) {
			gl.glViewport(0, 0, width, height);

			mWidth = width;
			mHeight = height;

//		    gl.glMatrixMode(GL10.GL_PROJECTION);
//		    gl.glLoadIdentity();
//		    GLU.gluPerspective(gl, 45f,(float) width / height, 1f, 50f);
		}

		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			  gl.glEnable(GL10.GL_DEPTH_TEST);
			  gl.glDepthFunc(GL10.GL_LEQUAL);
			  gl.glEnable(GL10.GL_TEXTURE_2D);

//			  mAzmText[0].set(gl, "南", Color.RED, 20, 64);
//			  mAzmText[1].set(gl, "東", Color.RED, 20, 64);
//			  mAzmText[2].set(gl, "北", Color.RED, 20, 64);
//			  mAzmText[3].set(gl, "西", Color.RED, 20, 64);

			 mBall = new PumpBall(1.0f, 3, 1.0f, 0.0f, 0.0f);
		}

	}


	private class CameraOverlayView extends View{
		
		Paint mmPaint;

		public CameraOverlayView(Context context) {
			super(context);
			
			mmPaint = new Paint();
			mmPaint.setStrokeWidth(5);
			mmPaint.setStyle(Paint.Style.STROKE);
			mmPaint.setColor(Color.RED);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);

			CFCamera cam = mCamera;
			if(cam==null) return ;

			float hva = cam.getHorizontalViewAngle();
			float vva = cam.getVerticalViewAngle();

			SensorValue sv = mSensorValue;
			float hv = Helper.to0to360ByDegree( sv.getAzimuthDegree() );
			float vv = sv.getPitch();
			
			//hv = 0;
			//vv = 0;
			

			canvas.save();
			canvas.translate(getWidth()/2.0f, getHeight()/2.0f);
			canvas.scale(256.0f/90.0f, 256.0f/90.0f);
			canvas.scale(mDensity, mDensity);
			
			mPano.drawToCanvas(canvas, hv, vv);

			canvas.restore();
			
			
			canvas.drawRect(0,0,getWidth(), getHeight(), mmPaint);
		}



		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);



		}




	}




		   //プレビューサイズの計算
	 private Size getFitPreviewSize(List<Size> sizes, int w, int h) {

	 	//if(mDecidedPrevieSize!=null) return mDecidedPrevieSize;

	 	return AppUtil.getAutoPrevieSize(sizes, false);

	 }



 	Toast mToast;
 	public void toast(String str){
 		if(mToast==null){
 			mToast = Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT);
 		}else{
 			mToast.setText(str);
 		}
 		mToast.show();
 	}

 	public void postToast(final String str){
 		mHandler.post(new Runnable() {
			@Override
			public void run() {
				toast(str);
			}
		});
 	}




}
