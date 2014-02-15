package jp.crudefox.pumpkin.camera;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.crudefox.pumpkin.AppUtil;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.view.SurfaceHolder;


public class CFCamera {


	static class CallbackBuffer{
		boolean isUsing;
		byte[] buffer;

		public void newBuffer(int framesize){
	    	if(buffer==null) buffer = new byte[framesize];
	    	else if(framesize!=buffer.length){
	    		buffer = null;
	    		System.gc();
	    		try{
	    			buffer = new byte[framesize];
	    		}catch (OutOfMemoryError e) {
	    			AppUtil.Log("CallbackBuffer.newBufferでエラー",e);
				}
	    	}
		}
		public void release(){
			buffer = null;
			isUsing = false;
		}
	}

    private Camera mCamera;
    private int mCameraId;
    //private Size mPreviewSize;
    private List<Size> mSupportedPreviewSizes;
    private List<Size> mSupportedPictureSizes;
    private List<String> mSupportedSceneModes;
    private List<String> mSupportedWhiteBalance;
    private List<String> mSupportedColorEffects;
    private List<String> mSupportedFlashModes;
    private List<String> mSupportedFocusModes;
    private String mSupportedFlashOn;
    private boolean mIsSupportedAutoFocus;
    private boolean mIsSmoothZoomSupported;
    private boolean mIsZoomSupported;
    private List<Integer> mZoomRatios;
    private int mMinExposure;
    private int mMaxExposure;
    private float mExposureStep;

    private float mHorizontalViewDegree;
    private float mVerticalViewDegree;

    private int mBitPerPixel;

    private CallbackBuffer[] mCallbackBuffers =
    		new CallbackBuffer[]{ new CallbackBuffer(), new CallbackBuffer() };

    private SurfaceHolder mPreviewDisplay;
    private SurfaceTexture mPreviewDisplayTexture;

    private int mCameraDegree;
    private boolean mIsFrontFace;



    private boolean mIsPreviewing=false;

    public boolean isOpened(){
    	return mCamera!=null;
    }
    boolean isFrontFace(){
    	return mIsFrontFace;
    }
    public boolean isSupportedFlashON(){
    	return mSupportedFlashOn!=null;
    }
    public boolean isSupportedWhiteBalance(){
    	return mSupportedWhiteBalance!=null;
    }
    public boolean isSupportedColorEffects(){
    	return mSupportedColorEffects!=null;
    }
    public boolean isSupportedSceneModes(){
    	return mSupportedSceneModes!=null;
    }
    public boolean isSupportedAutoFocus(){
    	return mIsSupportedAutoFocus;
    }
    public boolean isPreviewing(){
    	return mIsPreviewing;
    }
   public boolean hasPreviewDisplayOrTexture(){
    	return mPreviewDisplay!=null || mPreviewDisplayTexture!=null;
    }

    boolean isSmoothZoomSupported(){
    	return mIsSmoothZoomSupported;
    }
    public boolean isZoomSupported(){
    	return mIsZoomSupported;
    }

    public int getCameraDegree(){
    	return mCameraDegree;
    }
    public boolean isDiffRotateRect(int degree){
    	int difdegree = (mCameraDegree - degree + 360) % 360;
    	boolean result = difdegree!=0 && difdegree!=180;
    	//AppUtility.Log("isDiffRotateRect "+result);
    	return result;
    }
//    boolean isPortrait(){
//
//    	return miOrientation == Configuration.ORIENTATION_PORTRAIT;
//    }

    public List<String> getSupportedWhiteBalance(){
    	return mSupportedWhiteBalance;
    }
    public List<String> getSupportedColorEffects(){
    	return mSupportedColorEffects;
    }
    public List<String> getSupportedSceneModes(){
    	return mSupportedSceneModes;
    }
    public List<Camera.Size> getSupportedPreviewSizes(){
    	return mSupportedPreviewSizes;
    }

    public int getMaxExposure(){
    	return mMaxExposure;
    }
    public int getMinExposure(){
    	return mMinExposure;
    }
    public float getExposureStep(){
    	return mExposureStep;
    }
    public int getExposure(){
    	return mCamera.getParameters().getExposureCompensation();
    }

    public float getHorizontalViewAngle(){
    	return mHorizontalViewDegree;
    }
    public float getVerticalViewAngle(){
    	return mVerticalViewDegree;
    }


    public boolean setExposure(int value){
    	Camera.Parameters params = mCamera.getParameters();
    	params.setExposureCompensation(value);
    	return setParameters(params);
    }
    public boolean setSceneMode(String mode){
		Camera.Parameters params = mCamera.getParameters();
		params.setSceneMode(mode);
		return setParameters(params);
    }
    public boolean setColorEffect(String mode){
		Camera.Parameters params = mCamera.getParameters();
		params.setColorEffect(mode);
		return setParameters(params);
    }
    public boolean setWhiteBalance(String mode){
		Camera.Parameters params = mCamera.getParameters();
		params.setWhiteBalance(mode);
		return setParameters(params);
    }
    void setPictureSize(Size size){
		Camera.Parameters params = mCamera.getParameters();
		params.setPictureSize(size.width, size.height);
		setParameters(params);

		updateSupportInfo();
    }
    public void setPreviewSize(Size size){
    	//boolean prev = mIsPreview;
    	//if(prev) startPreview(false);
		Camera.Parameters params = mCamera.getParameters();
		//mPreviewSize = size;
		params.setPreviewSize(size.width, size.height);
		mCamera.setParameters(params);
		//if(prev) startPreview(true);
		updateSupportInfo();
    }

    private boolean setParameters(Camera.Parameters params){
    	if(mCamera!=null){
    		try {
				mCamera.setParameters(params);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	return false;
    }

    private void updateSupportInfo(){
    	updateSupportInfo(mCamera.getParameters());
    }
    private void updateSupportInfo(Camera.Parameters params){
    	//Camera.Parameters params = mCamera.getParameters();
		mZoomRatios = params.getZoomRatios();

    	mHorizontalViewDegree = params.getHorizontalViewAngle();
    	mVerticalViewDegree = params.getVerticalViewAngle();

    	AppUtil.Log("HorizontalViewDegree = " + mHorizontalViewDegree);
    	AppUtil.Log("VerticalViewDegree = " + mVerticalViewDegree);
    }



    public boolean setPreviewDisplay(SurfaceHolder holder){
    	if(mCamera==null) return false;
    	try {
			mCamera.setPreviewDisplay(holder);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
    	mPreviewDisplay = holder;
    	return true;
    }

    public boolean setPreviewTexture(SurfaceTexture surfaceTexture){
    	if(mCamera==null) return false;
    	try {
			mCamera.setPreviewTexture(surfaceTexture);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
    	mPreviewDisplayTexture = surfaceTexture;
    	return true;
    }

    /*
		Added in API level 5
		Triggers an asynchronous image capture. The camera service will initiate a series of callbacks to the application as the image capture progresses. The shutter callback occurs after the image is captured. This can be used to trigger a sound to let the user know that image has been captured. The raw callback occurs when the raw image data is available (NOTE: the data will be null if there is no raw image callback buffer available or the raw image callback buffer is not large enough to hold the raw image). The postview callback occurs when a scaled, fully processed postview image is available (NOTE: not all hardware supports this). The jpeg callback occurs when the compressed image is available. If the application does not need a particular callback, a null can be passed instead of a callback method.

		This method is only valid when preview is active (after startPreview()). Preview will be stopped after the image is taken; callers must call startPreview() again if they want to re-start preview or take more pictures. This should not be called between start() and stop().

		After calling this method, you must not call startPreview() or take another picture until the JPEG callback has returned.

		Parameters
		shutter  the callback for image capture moment, or null
		raw  the callback for raw (uncompressed) image data, or null
		postview  callback with postview image data, may be null
		jpeg  the callback for JPEG image data, or null

     */
    public void takePicture(ShutterCallback shutter,PictureCallback raw,PictureCallback postview,PictureCallback jpeg){
    	if(mCamera==null) return ;
    	mCamera.takePicture(shutter, raw, postview, jpeg);
    }

    public void autoFocus(AutoFocusCallback cb){
    	mCamera.autoFocus(cb);
    }
    public void cancelAutoFocus(){
    	if(mCamera==null) return ;
    	mCamera.cancelAutoFocus();
    }
//    void setPreviewCallback(PreviewCallback cb){
//    	mCamera.setPreviewCallback(cb);
//    }
    public void setPreviewCallbackWithBuffer(PreviewCallback cb){
    	mCamera.setPreviewCallbackWithBuffer(cb);
    }
//    void setOneShotPreviewCallback(PreviewCallback cb){
//    	mCamera.setOneShotPreviewCallback(cb);
//    }

    public boolean existRunningBuffer(){
    	for(CallbackBuffer cb : mCallbackBuffers){
    		if( cb.isUsing ) return true;
    	}
    	return false;
    }
    public void endCallbackBuffer(byte[] buffer){
    	if(buffer==null) return ;
    	for(CallbackBuffer cb : mCallbackBuffers){
    		if( cb.buffer == buffer ) cb.isUsing = false;
    	}
    }
    public void endCallbackBufferAll(){
    	for(CallbackBuffer cb : mCallbackBuffers){
    		cb.isUsing = false;
    	}
    }
    private CallbackBuffer findNotUsingCallbackBuffer(){
    	for(CallbackBuffer cb : mCallbackBuffers){
    		if( !cb.isUsing ) return cb;
    	}
    	return null;
    }
    private void releaseCallbackBuffer(){
    	for(CallbackBuffer cb : mCallbackBuffers){
    		cb.release();
    	}
    }

    public boolean addCallbackBuffer(){
    	if(mCamera==null) return false;
    	Size size = getPreviewSize();
    	if(size==null) return false;
    	int framesize = size.width*size.height*mBitPerPixel/8;

    	CallbackBuffer cb = findNotUsingCallbackBuffer();
    	if(cb==null) return false;
    	cb.newBuffer(framesize);
    	if(cb.buffer==null) return false;

    	cb.isUsing = true;
    	mCamera.addCallbackBuffer(cb.buffer);
    	return true;
    }

    public Size getPreviewSize(){
    	if(mCamera==null) return null;
    	return mCamera.getParameters().getPreviewSize();
    }

    public Camera.Parameters getParameters(){
    	return mCamera.getParameters();
    }

    public void setFlashLight(boolean on){
		Camera.Parameters params = mCamera.getParameters();
		if( on ){
			if(mSupportedFlashOn!=null)
				params.setFlashMode(mSupportedFlashOn);
		}else{
			params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
		}
		mCamera.setParameters(params);
    }


    public static int getNumberOfCameras(){
    	if( !AppUtil.isOk_SDK_Gingerbread() ) return 1;
//    	return 0;
    	return Camera.getNumberOfCameras();
    }


    public int getCameraId(){
    	return mCameraId;
    }

    public enum CameraType{
    	Back,
    	Front,
    	Unkown,
    }
    public static CameraType getCameraType(int cameraId){
    	if( !AppUtil.isOk_SDK_Gingerbread() ) return CameraType.Back;
    	android.hardware.Camera.CameraInfo cameraInfo = new android.hardware.Camera.CameraInfo();
    	Camera.getCameraInfo(cameraId, cameraInfo);
    	switch(cameraInfo.facing){
    	case android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK:
    		return CameraType.Back;
    	case android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT:
    		return CameraType.Front;
    	default:
    		return CameraType.Unkown;
    	}
    }

    public static int findCameraId(CameraType type){
    	if( !AppUtil.isOk_SDK_Gingerbread() ) return -1;
    	//android.hardware.Camera.CameraInfo cameraInfo = new android.hardware.Camera.CameraInfo();
    	int num = getNumberOfCameras();
    	for(int i=0;i<num;i++){
    		CameraType cur_type = getCameraType(i);
    		if(cur_type==type) return i;
    	}
    	return -1;
    }

	//カメラの起動と初期化
    public boolean openCamera(int cameraId){

    	if(isOpened()) return false;

    	try{
//    		int cameraId = -1;
//    		if(front) cameraId = getFrontCameraId();
//    		else cameraId = getBackCameraId();
    		if(AppUtil.isOk_SDK_Gingerbread()){
	    	    if(cameraId!=-1){
	    	    	mCameraId = cameraId;
	        		mCamera = Camera.open(cameraId);
	    	    }
    		}else{
    			mCameraId = 0;
        		mCamera = Camera.open();
    		}
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    	if(mCamera==null){
    		return false;
    	}

    	if(AppUtil.isOk_SDK_Gingerbread()){
	    	android.hardware.Camera.CameraInfo camInfo = new android.hardware.Camera.CameraInfo();
	    	Camera.getCameraInfo(cameraId, camInfo);
	    	if( camInfo.facing == android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT ){
	    		mIsFrontFace = true;
	    	}else{
	    		mIsFrontFace = false;
	    	}
	    	mCameraDegree = camInfo.orientation;
	    	AppUtil.Log("回転="+camInfo.orientation);
    	}else{
    		mIsFrontFace = false;
    		mCameraDegree = 90;
    	}

    	mPreviewDisplay = null;
    	mPreviewDisplayTexture = null;

    	//向きの設定
    	//setOrientation2(orientation);

    	//パラメータ取得
    	Camera.Parameters params = mCamera.getParameters();


    	//
//    	params.setPreviewFormat(17);
//    	mCamera.setParameters(params);

//    	try {
//			mCamera.setPreviewDisplay(null);
//    		mCamera.setPreviewTexture(null);
//    		SurfaceTexture s = new ;
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}

    	//写真サイズを最小に設定
    	List<Size> pSizesList = params.getSupportedPictureSizes();
    	if(pSizesList!=null && pSizesList.size()>1){
	    	Size pSize = AppUtil.getMinimumSize(pSizesList);
	    	params.setPictureSize(pSize.width, pSize.height);
	    	mCamera.setParameters(params);
	    	params = mCamera.getParameters();
    	}



    	//サポート情報取得
    	mSupportedPreviewSizes = params.getSupportedPreviewSizes();
    	mSupportedSceneModes = params.getSupportedSceneModes();
    	mSupportedWhiteBalance = params.getSupportedWhiteBalance();
    	mSupportedColorEffects = params.getSupportedColorEffects();
    	mSupportedFlashModes = params.getSupportedFlashModes();
    	mSupportedPictureSizes = params.getSupportedPictureSizes();
    	mSupportedFocusModes = params.getSupportedFocusModes();
    	mIsSmoothZoomSupported = params.isSmoothZoomSupported();
    	mIsZoomSupported = params.isZoomSupported();

    	mZoomRatios = params.getZoomRatios();
//    	mHorizontalViewDegree = params.getHorizontalViewAngle();
//    	mVerticalViewDegree = params.getVerticalViewAngle();
    	updateSupportInfo(params);


    	mMaxExposure = params.getMaxExposureCompensation();
    	mMinExposure = params.getMinExposureCompensation();

    	try{
    		mExposureStep = params.getExposureCompensationStep();
    	}catch (Exception e) {
    		mExposureStep = 1.0f;
		}
    	if(mExposureStep == 0.0f) mExposureStep = 1.0f;

//    	List<Size> psList = params.getSupportedPictureSizes();
//    	if(psList!=null){
//    		for(Size e : psList){
//    			AppUtility.Log("pict size "+e.width+";"+e.height);
//    		}
//    	}

    	//Size lastps = psList.get(psList.size()-1);
    	//params.setPictureSize(lastps.width,lastps.height);
    	//
    	//mCamera.setParameters(params);
    	//params = mCamera.getParameters();

    	Size psize = params.getPictureSize();
    	AppUtil.Log("pictSize width="+psize.width+"; height="+psize.height);

    	List<Integer> zoomList = mZoomRatios;
    	if(zoomList!=null){
    		for(int e : zoomList){
    			AppUtil.Log("zoom "+e);
    		}
    	}else{
    		AppUtil.Log("zoomList is null");
    	}


    	//フラッシュライト常時点灯の確認
    	boolean has_torch=false;
    	boolean has_on=false;
    	if(mSupportedFlashModes!=null){
	    	for(String e : mSupportedFlashModes){
	    		if(e.equals(Camera.Parameters.FLASH_MODE_TORCH)){
	    			has_torch = true;
	    		}else if(e.equals(Camera.Parameters.FLASH_MODE_ON)){
	    			has_on = true;
	    		}
	    	}
    	}
    	if(has_torch) 	mSupportedFlashOn = Camera.Parameters.FLASH_MODE_TORCH;
    	else if(has_on)	mSupportedFlashOn = Camera.Parameters.FLASH_MODE_ON;
    	else			mSupportedFlashOn = null;

    	//フォーカスモードを
//    	for(String e: mSupportedFocusModes){
//    		AppUtility.Log("focus="+e);
//    	}
//    	AppUtility.Log("now focus="+params.getFocusMode());
    	if(mSupportedFocusModes!=null)
    		mIsSupportedAutoFocus = mSupportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO);
    	else mIsSupportedAutoFocus = false;

//    	mPreviewSize = params.getPreviewSize();
//    	params.setPreviewFormat(ImageFormat.YV12);
//    	mCamera.setParameters(params);



    	int imgformat = params.getPreviewFormat();
    	AppUtil.Log("PreviewFormat="+imgformat);
    	mBitPerPixel = ImageFormat.getBitsPerPixel(imgformat);


    	//AppUtility.Log("flatten"+params.flatten());
    	//params.getM


    	return true;
    }

    //カメラのお片づけ
    public void releaseCamera(){
    	if(mCamera!=null){
    		stopPreview();
    		mCamera.release();
    		mCamera = null;
    		releaseCallbackBuffer();
    		mPreviewDisplay = null;
    		mPreviewDisplayTexture = null;
    	}
    }



    public void setOptimalPictureSize(){
    	if(mCamera==null) return ;
    	//パラメータ取得
    	Camera.Parameters params = mCamera.getParameters();
    	if(true){
	    	//写真サイズを最小に設定
	    	List<Size> pSizesList = params.getSupportedPictureSizes();
	    	if(pSizesList!=null && pSizesList.size()>1){
		    	Size pSize = AppUtil.getMinimumSize(pSizesList);
		    	params.setPictureSize(pSize.width, pSize.height);
		    	mCamera.setParameters(params);
		    	params = mCamera.getParameters();
	    	}
    	}
//    	else{
//        	Size curPrevSize = params.getPreviewSize();
//	    	List<Size> pSizesList = params.getSupportedPictureSizes();
//    		float curAspect = curPrevSize.width / (float) curPrevSize.height;
//    		float min_dif_aspect = Float.MAX_VALUE;
//    		Size resultSize = null;
//	    	if(pSizesList!=null && pSizesList.size()>1){
//	    		for(Size pSize : pSizesList){
//	    			float aspect = pSize.width / (float) pSize.height;
//	    			float dif_aspect = Math.abs(aspect-curAspect);
//		    		AppUtility.Log("PictureSize="+pSize.width+","+pSize.height+"; aspect="+aspect);
//	    			if(dif_aspect<min_dif_aspect){
//	    				resultSize = pSize;
//	    				min_dif_aspect = dif_aspect;
//	    			}
//	    		}
//	    	}
//	    	if(resultSize!=null){
//	    		params.setPictureSize(resultSize.width, resultSize.height);
//	    		AppUtility.Log("optimapPictureSize="+resultSize.width+","+resultSize.height);
//	    	}
//    	}
    	mCamera.setParameters(params);
    }

	public void setOrientationByDisplayDegree(int displayDegree){
    	//向きの設定
        if( mCamera!=null ){
        	if((displayDegree%90)!=0) throw new IllegalArgumentException("displatDegree can only multiple of 90 degree.");
        	//boolean prev = mIsPreview;
        	//if(prev) startPreview(false);
            int result;
            if (mIsFrontFace) {
                result = (mCameraDegree + displayDegree) % 360;
                result = (360 - result) % 360;  // compensate the mirror
            } else {  // back-facing
                result = (mCameraDegree - displayDegree + 360) % 360;
            }
        	mCamera.setDisplayOrientation(result);
	        //if(prev) startPreview(true);
        }
	}

	public void startPreview(){
		if(mCamera==null) return;
		mCamera.startPreview();
		//mCamera.setOneShotPreviewCallback(cb)PreviewCallback(mPreviewCallback);
		mIsPreviewing = true;
	}
	public void stopPreview(){
		if(mCamera==null) return;
		mCamera.setOneShotPreviewCallback(null);
		mCamera.setPreviewCallback(null);
		mCamera.setPreviewCallbackWithBuffer(null);
		mCamera.stopPreview();
		for(CallbackBuffer cb : mCallbackBuffers){
			cb.isUsing = false;
		}
		mIsPreviewing = false;
	}

	public void progressZoom(int progress){
		if(mCamera!=null){
		 	Camera.Parameters params = mCamera.getParameters();
			int max = params.getMaxZoom();
			int zoom = params.getZoom() + progress;
			if(zoom>max) zoom = max; else if(zoom<0) zoom = 0;
			params.setZoom(zoom);
			AppUtil.Log("max="+max+"; zoom="+zoom);
			mCamera.setParameters(params);
		}
	}
	public int getZoom(){
		if(mCamera!=null){
		 	return mCamera.getParameters().getZoom();
		}
		return -1;
	}
	public int getZoomScale(){
		if(mCamera!=null && mZoomRatios!=null && mZoomRatios.size()>0){
		 	int iZoom = mCamera.getParameters().getZoom();
		 	return mZoomRatios.get(iZoom);
		}
		return 100;
	}

	public List<Integer> getZoomRatios(){
		return mZoomRatios;
	}
	public int getZoomLevelLength(){
		if(mZoomRatios!=null){
		 	return mZoomRatios.size();
		}
		return 0;
	}
	public int getMaxZoom(){
		if(mCamera!=null){
		 	return mCamera.getParameters().getMaxZoom();
		}
		return -1;
	}
	public boolean setZoom(int value){
		if(mCamera!=null){
			try {
			 	Camera.Parameters params = mCamera.getParameters();
			 	params.setZoom(value);
			 	mCamera.setParameters(params);
			 	return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	public void startSmoothZoom(int value){
		if(mCamera!=null){
			mCamera.startSmoothZoom(value);
		}
	}
	public void stopSmoothZoom(){
		if(mCamera!=null){
			mCamera.stopSmoothZoom();
		}
	}


	public List<String> getSplitFlatten(){
		if(mCamera==null) return null;
		Camera.Parameters params = mCamera.getParameters();
		String flatten = params.flatten();
		String[] arr = flatten.split(";");
		ArrayList<String> list = new ArrayList<String>();
		for(String e : arr){
			list.add(e);
		}
		return list;
	}


}
