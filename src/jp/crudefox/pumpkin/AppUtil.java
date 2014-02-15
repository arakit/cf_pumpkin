package jp.crudefox.pumpkin;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import jp.crudefox.library.help.Helper;
import jp.crudefox.library.help.Helper.FileNameAndExtention;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera.Size;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import cf.u.android.camera.JNIUtility;


public class AppUtil {

//	public static final int NMI_STOWAGE = 1000;
//	public static final int NMI_ACCEPT_BLUETOOTH = 1001;




//	private static int mIssueWID = 100;
//	public static synchronized int issueWID(){
//		return mIssueWID++;
//	}

	//public static final String BOOKMARK_FILENAME_EXTENTION = "bookmark";

//    private static final UUID MY_UUID_SECURE =
//            UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    public static final UUID MY_UUID_SECURE =
            UUID.fromString("00001101-0000-1000-8080-0080779B34FC");



	//ルートアクティビティ用
//	public static final String ACTION_RESUME_ACTIVITY_ROOT = "jp.cf.android.minibrowser.ACTION_RESUME_ACTIVITY_ROOT";
//	public static final String ACTION_RESTORE_WINDOW = "jp.cf.android.minibrowser.ACTION_RESTORE_WINDOW";
//
//	public static final String ACTION_NEW_WINDOW = "jp.cf.android.minibrowser.ACTION_NEW_WINDOW";
//
//	public static final String EXTRA_RESTORE_WINDOW_ID = "restore_window_id";

	//サービス用
//	public static final String ACTION_FINISH_STOWAGE = "jp.cf.android.minibrowser.ACTION_FINISH_STOWAGE";
//	public static final String ACTION_START_STOWAGE = "jp.cf.android.minibrowser.ACTION_START_STOWAGE";
//	public static final String ACTION_SHOW_STOWAGE = "jp.cf.android.minibrowser.ACTION_SHOW_STOWAGE";
//	public static final String ACTION_HIDE_STOWAGE = "jp.cf.android.minibrowser.ACTION_HIDE_STOWAGE";
//	public static final String ACTION_STOWAGE_NOTIFY_MENU = "jp.cf.android.minibrowser.ACTION_STOWAGE_NOTIFY_MENU";
//	public static final String ACTION_START_VIDEO = "jp.cf.android.minibrowser.ACTION_START_VIDEO";
//	public static final String ACTION_ACCEPT_BLUETOOTH = "jp.cf.android.minibrowser.ACTION_ACCEPT_BLUETOOTH";
//	public static final String EXTRA_TO_SERVICE_INTENT = "intent_data";

//	public static final String ADMOB_KEY = "a14fe32e145ea52";

	static final String SIGNED_KEY = "308202bf308201a7a00302010202047a9780c4300d06092a864886f70d01010b05003010310e300c060355040313056368696565301e170d3132303532383130343030305a170d3337303532323130343030305a3010310e300c06035504031305636869656530820122300d06092a864886f70d01010105000382010f003082010a028201010082c3c93d50c6805a406254d9c7163c714e4b8e99d2f34169ec7f4e14fad347309742ac77c32f27bd92fa043a90ac26fe5a7f71898213a973a2ce6d9a455bce1be76b1aa3a41989e329fbb1370e21a78a376ee8e6295fbdd915c1f0e01da974912b4614f46c28c275c0f92fc8bde2ebc5eed16c7b6f29d222e2d5de14e674f945328dec3add6781e0ca8e4aff8b7535fc9dca6cfdcfe23d14526f9a8ff049bc168592b86a1aae865d24b7e617714909b77ce69a32373e2703e5882d9a06ed9ee04362490b9686fda9f6d823478991fdb9b432bf7162de8bb3c349d032de2798a0d4cffcf5ea8360827b1187fd8685b26019ab421a9234a347292cade0992de6b90203010001a321301f301d0603551d0e04160414fec47e8af6f3e134ad8c5cf833a0a97c35913ba4300d06092a864886f70d01010b050003820101000b671779687ec55f8a7d9174da3e462e6ab40334abd37ca15280c9293b435ef6787c6e455c8384586614206fa2f187052978e24fad7567c2098e7715f3c871097784c355b7dcb63c9019101f44b30ba9a6185a6c8b50949653ac82d6557e0a213c2b116d03f78901f9ce766335f5727b1c70d89cfaf28476e70b5ee62bfa4602eef19fe36bdedc2e034718982e5a5293d9f636c65f3a23fbefa5b5adaf0157670a09161cf75df06748ec38d288db99382b2e637e0f9f208ebd3b4aa1ec95eaaf1a7017a1debd456258b80a706ffbc0e72151fcccb8f683ad6285e9b38d4d691282b480b39147844d6fe6eec6dadeb9ed62f86838a6ccad4c639565f7c3a624a8";
    static final String SIGNED_KEY_D = "3082030d308201f5a00302010202042ddee27c300d06092a864886f70d01010b05003037310b30090603550406130255533110300e060355040a1307416e64726f6964311630140603550403130d416e64726f6964204465627567301e170d3131313032343136323332385a170d3431313031363136323332385a3037310b30090603550406130255533110300e060355040a1307416e64726f6964311630140603550403130d416e64726f696420446562756730820122300d06092a864886f70d01010105000382010f003082010a028201010094d2180705773eb0679798482bbabb61c640adcff9f85979d6b492a0c37b0d78e645540317dd6e524e717232372767f30d22d8db5fa8e335cfd6fe9023f5c578e497c86df40d48778006389a5cd23df2f63744ceda92115ba40d77de9ce6ebfd65771173ea2408c1cb417f77968ae37ec8a00f6dfec09733be10bac04c57fabbb4f1e6aee157cdc7cb5231d3cbffa057bd8f63d44733d8a8e7d9960c725fffc8a856c6e1dd32e92c21868231b1f52bd83688dcb3d386b6aed341ed869d60c9e57737f651924ad320b50d93eb5422ed84d9239c30284e4a0bbe0cc63e7fba78a4edced8fa535401b784d214078e1533472562cdf9ccae69348a44ba5d7e0e2b330203010001a321301f301d0603551d0e04160414ab0b9e6814030afa9e524e8cdd8e562350523127300d06092a864886f70d01010b050003820101005d0d6378952a76c1df9b969eba7824cffbb16180a0b6b4c7818c8bd161ffc1168791d225756e2c2d99a9d5f39ca413bfb2a2ee56cc84d35f7f349333d27432fa5edadc329ff125fbe1f011dafc29006bab3fb5808c7282cae17cc38fa27d81fba900562bc4918c0dc5ebdaf381bdb48c84b625b8c297cb196d9f421ca5f7717398effac44bae1a0edc10a2db6a156fac0a65d4a34dda90d63912d775a55fb7b7c2a143aa40934353e931a537ae55f65ccb54d5862a6d529164895098014259944473dfe1ae7e58cca0e05872fbdb6cce96b33d2ac98930ac69bbf5879000de33b46b906307c06b07de557e8bafd6de8a067ac2a4c46a68d18209ea81e2aa7fd5";

	public static String TAG = "cf_pumpkin";


	//コンパイル日
	public static final long COMPILE_TIME = makeDate(2013, 3, 15).getTime();
	//最初にPlayに公開した日
	public static final long PUBLISH_TIME = makeDate(2013, 3, 15).getTime();

	//year=cf.2012, month=1~12, day=1~30or31
	public static Date makeDate(int year,int month,int day){
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(year, month-1, day, 0, 0, 0);
		return cal.getTime();
	}



	public static void Log(String str){
		if(isDebug()) android.util.Log.d(TAG,str);
	}
	public static void Log(String str,Throwable tr){
		if(isDebug()) android.util.Log.d(TAG,str,tr);
	}

	public static boolean isDebug(){
		return true;
	}



	private static Boolean sIsCheckSigned;
	public static boolean checkSigned(Context context){
		if(sIsCheckSigned==null){
			if(isDebug()) sIsCheckSigned = Helper.checkSigned(context.getPackageManager(), context.getPackageName(), SIGNED_KEY_D);
			else sIsCheckSigned = Helper.checkSigned(context.getPackageManager(), context.getPackageName(), SIGNED_KEY);
		}
		return sIsCheckSigned;
	}

	public static boolean isOk_SDK_Gingerbread(){
		return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD;
	}
	public static boolean isOk_SDK_HoneyCombo(){
		return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB;
	}
	public static boolean isOk_SDK_JellyBean(){
		return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN;
	}
	public static boolean isOk_SDK_ICS(){
		return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH;
	}
	public static boolean isOk_SDK(int sdk_int){
		return android.os.Build.VERSION.SDK_INT >= sdk_int;
	}

	public static void deletePictFile(ContentResolver cr,File file){
		cr.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				MediaStore.Images.Media.DATA + "=?",
				new String[]{file.getAbsolutePath()}
				);

		file.delete();
	}

	//ギャラリーへ知らせる
	public static void scanFile(Context context,File file){
		MediaScannerConnection.scanFile(context,new String[]{file.getAbsolutePath()},new String[]{"image/jpeg"}, null);
	}
	public static void scanFiles(Context context,List<File> files){
		String[] arr = new String[files.size()];
		String[] arrm = new String[files.size()];
		for(int i=0;i<arr.length;i++){
			arr[i] = files.get(i).getAbsolutePath();
			arrm[i] = "image/jpeg";
		}
		MediaScannerConnection.scanFile(context,arr,arrm, null);
	}


	//ファイルへ保存
	public static File saveBitmap(Context context,Bitmap bmp,Date date,int quality,boolean scan){
		File file = FileManager.getTakePictureFile(date);
		if( saveBitmap(context, bmp, file, quality, scan) ){
			return file;
		}
		return null;
	}
	//ファイルへ保存
	public static boolean saveBitmap(Context context,Bitmap bmp,File file,int quality,boolean scan){
		try {
			if(file!=null){
				OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
				bmp.compress(Bitmap.CompressFormat.JPEG, quality, os);
				os.close();

				if(scan) scanFile(context, file);

				return true;
			}else{
				AppUtil.Log("保存場所が無い！");
			}

		} catch (Exception e) {
			e.printStackTrace();
			AppUtil.Log("失敗しました。"+e);
		}
		return false;
	}




	public static void decodeYUV420SP(int[] rgb, byte[] yuv420sp, int width, int height,int cameraDegree,int displayDegree,Point outSize){
		int diffDegree = (cameraDegree - displayDegree + 360 ) % 360;
		if(diffDegree==0) 			JNIUtility.decodeYUV420SP(rgb, yuv420sp, width, height, 0);
		else if(diffDegree==180)	JNIUtility.decodeYUV420SP(rgb, yuv420sp, width, height, 1);
		else if(diffDegree==90)		JNIUtility.decodeYUV420SPAndRotate(rgb, yuv420sp, width, height, 0);
		else if(diffDegree==270)	JNIUtility.decodeYUV420SPAndRotate(rgb, yuv420sp, width, height, 1);

		if(outSize!=null){
			if(diffDegree==0 || diffDegree==180) 		outSize.set(width, height);
			else if(diffDegree==90 || diffDegree==270)	outSize.set(height, width);
		}
	}


	public static Dialog showAboutAppWindow(Context context,boolean isFloating){
		try {
			final Context appc = context.getApplicationContext();
			java.text.DateFormat df = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM);
			PackageInfo pkginfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			boolean gb = AppUtil.isOk_SDK_Gingerbread();

			AlertDialog.Builder ab = new AlertDialog.Builder(context);
			ab.setTitle(context.getString(R.string.app_name));
			ab.setIcon(R.drawable.ic_launcher);
			ab.setPositiveButton(android.R.string.ok, null);
			final AlertDialog dialog = ab.create();
			View view = dialog.getLayoutInflater().inflate(R.layout.about_app_layout_2, null);
			TextView tv_version = (TextView) view.findViewById(R.id.about_version_text);
			TextView tv_updated = (TextView) view.findViewById(R.id.about_updated_text);
			TextView tv_installed = (TextView) view.findViewById(R.id.about_installed_text);
			TextView tv_compled = (TextView) view.findViewById(R.id.about_compiled_text);
			TextView tv_release = (TextView) view.findViewById(R.id.about_release_text);
			ImageButton tv_googleplay = (ImageButton) view.findViewById(R.id.about_googleplay);
			TextView tv_author = (TextView) view.findViewById(R.id.about_author_text);

			tv_version.setText(pkginfo.versionName);
			tv_updated.setText((gb?df.format(new Date(pkginfo.lastUpdateTime)):"---"));
			tv_installed.setText((gb?df.format(new Date(pkginfo.firstInstallTime)):"---"));
			tv_compled.setText(df.format(new Date(AppUtil.COMPILE_TIME)));
			tv_release.setText(df.format(new Date(AppUtil.PUBLISH_TIME)));

			//market://details?id="+pkginfo.packageName
			final Uri gp_address = Uri.parse("market://details?id="+pkginfo.packageName);
//			URLSpan urlspan = new URLSpan(gp_address.toString());
//			String gp_label = "→Google Play";
//			Spannable.Factory fac = Spannable.Factory.getInstance();
//			Spannable spa = fac.newSpannable(gp_label);
//			spa.setSpan(gp_address.toString(), 0, gp_label.length(), spa.getSpanFlags(gp_address.toString()));
//			tv_googleplay.setText(spa, TextView.BufferType.SPANNABLE);
//			//tv_googleplay.getsp
			tv_googleplay.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent();
					i.setAction(Intent.ACTION_VIEW);
					i.setData( gp_address );
					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					try{
						appc.startActivity(i);
						dialog.dismiss();
					}catch (Exception e) {}
				}
			});

			tv_author.setText("Created by CHiKARA.");

//			//tv.setAutoLinkMask(Linkify.ALL);
//			tv.setMovementMethod(LinkMovementMethod.getInstance());
//			tv.setText(Html.fromHtml(str));

			dialog.setView(view);
			if(isFloating){//さーびす
				Window window = dialog.getWindow();
				WindowManager.LayoutParams wlp = window.getAttributes();
				wlp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
				window.setAttributes(wlp);
			}
			dialog.show();
			return dialog;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}


	public static Size getMinimumSize(List<Size> sizes){
		if(sizes==null || sizes.size()==0) return null;
		int minArea = Integer.MAX_VALUE;
		Size minSize = null;
		for(Size e : sizes){
			int area = e.width * e.height;
			if(area<minArea){
				minArea = area;
				minSize = e;
			}
		}
		return minSize;
	}
	public static Size getMaximumSize(List<Size> sizes){
		if(sizes==null || sizes.size()==0) return null;
		int maxArea = Integer.MIN_VALUE;
		Size maxSize = null;
		for(Size e : sizes){
			int area = e.width * e.height;
			if(area>maxArea){
				maxArea = area;
				maxSize = e;
			}
		}
		return maxSize;
	}



	public static Size getAutoPrevieSize(List<Size> sizes,boolean isMini){
	     if (sizes == null) return null;
	     Size result = getAutoPrevieSize(sizes, 500, isMini);
	     if( result!=null ) return result;
	     result = getAutoPrevieSize(sizes, Integer.MAX_VALUE, isMini);
	     return result;
	}
	private static Size getAutoPrevieSize(List<Size> sizes,int limitMaxHeight,boolean isMini){

	     if (sizes == null) return null;

	     Size optimalSize = null;
	     double minDiff = Double.MAX_VALUE;
	     double minDiff2 = Double.MAX_VALUE;

	     for (Size size : sizes) {
	    	 if( size.height > limitMaxHeight ) continue;
	         double ratio = (double) size.width / size.height;
	         double diff =  - size.height;
	         if( isMini ) diff = -diff;
	         if (diff <= minDiff && ratio < minDiff2) {
	             optimalSize = size;
	             minDiff = diff;
	             minDiff2 = ratio;
	         }
	     }
	     return optimalSize;
	}



	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static long startDownload(Context context,String uri,String mimetype,File dir){
		DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
		Uri downloadUri = Uri.parse(uri);
		DownloadManager.Request r = new DownloadManager.Request(downloadUri);
		r.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
		String lastPath = downloadUri.getLastPathSegment();
		AppUtil.Log("lastPath="+lastPath);
		if(TextUtils.isEmpty(lastPath)) lastPath = "download";
		lastPath = findNewDownloadFileName(dir, lastPath);
		if(lastPath==null) lastPath = "download" + System.currentTimeMillis();
		File saveFile = new File(dir,lastPath);
		r.setDestinationUri(Uri.fromFile(saveFile));
		r.setTitle(lastPath);
		r.setVisibleInDownloadsUi(true);
		if(android.os.Build.VERSION.SDK_INT>=11)
			r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		else
			r.setShowRunningNotification(true);
		if(mimetype!=null) r.setMimeType(mimetype);
		return dm.enqueue(r);
	}

	public static String findNewDownloadFileName(File dir,String def){
		if(!dir.isDirectory()) return null;
		if(TextUtils.isEmpty(def)) return null;
		def = replaceFileName(def);
		if( !(new File(dir,def).exists()) ) return def;
		FileNameAndExtention fne = Helper.getFileNameAndExtention(def);
		for(int i=1;i<1000;i++){
			String cname = String.format("%s (%d)", fne.name, i);
			if(fne.ext!=null) cname += "." + fne.ext;
			File file = new File(dir,cname);
			if(file.exists()) continue;
			return cname;
		}
		return null;
	}

	private static String replaceFileName(String name){
		//< > : * ? " / \ |
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<name.length();i++){
			char c = name.charAt(i);
			switch(c){
			case '<':
			case '>':
			case ':':
			case '*':
			case '?':
			case '"':
			case '/':
			case '\\':
			case '|':
				sb.append(String.format("%%%02x", (int)c)); break;
			default:
				sb.append(c); break;
			}
		}
		return sb.toString();
	}


	public static boolean showIntentDialog(final Context context,final Intent intent){
//		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		final PackageManager pm = context.getPackageManager();
		final List<ResolveInfo> ri_list = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

		if( ri_list==null || ri_list.size()==0 ) return false;

		if( ri_list.size()==1 ){
			startActivityByResolve(context, ri_list.get(0), intent);
			return true;
		}

		Collections.sort(ri_list, new ResolveInfo.DisplayNameComparator(pm));

		final LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		AlertDialog.Builder ab = new AlertDialog.Builder(context);

		BaseAdapter adapter = new BaseAdapter() {
			public View getView(int position, View convertView, ViewGroup parent) {
				if(convertView==null){
					convertView = inf.inflate(R.layout.resolve_list_item, null);
				}
				ResolveInfo r = ri_list.get(position);
				ImageView iv = (ImageView) convertView.findViewById(android.R.id.icon);
				TextView tv1 = (TextView) convertView.findViewById(android.R.id.text1);

				iv.setImageDrawable(r.loadIcon(pm));

				CharSequence label = r.loadLabel(pm);
				tv1.setText(label!=null?label:"");

				return convertView;
			}
			public long getItemId(int position) {return position;}
			public Object getItem(int position) {return null;}
			public int getCount() {return ri_list.size();}
		};

		ab.setTitle("アプリの選択");

		ab.setAdapter(adapter, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				ResolveInfo r = ri_list.get(which);
				startActivityByResolve(context, r, intent);
			}
		});

		final AlertDialog dlg = ab.create();

		if(true){
			Window window = dlg.getWindow();
			WindowManager.LayoutParams wlp = window.getAttributes();
			wlp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
			window.setAttributes(wlp);
		}

		dlg.show();

		return true;
	}
	private static void startActivityByResolve(Context context,ResolveInfo r,Intent intent){
		Intent intent2 = new Intent(intent);
		intent2.setClassName(r.activityInfo.packageName, r.activityInfo.name);
		intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.getApplicationContext().startActivity(intent2);
	}


 	public static BitmapFactory.Options calcBitmapOption(InputStream is,int maxWidth,int maxHeight){
    	if(is!=null){
    		try {
    			BitmapFactory.Options opt = new BitmapFactory.Options();
    		    opt.inJustDecodeBounds = true;
    		    BitmapFactory.decodeStream(is, null, opt);
    		    is.close();
    		    float scale = Math.min((float)maxWidth / (float)opt.outWidth, (float)maxHeight / (float)opt.outHeight);
    		    opt.inJustDecodeBounds = false;
    		    opt.inPurgeable = true;
    		    if(scale<1.0f){
    		    	float bai = 1.0f / scale;
    		    	int i;
    		    	for(i=2;((float)i)<bai;i*=2);
    		    	opt.inSampleSize = i;
    		    }
    		    return opt;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	return null;
	}

 	public static Bitmap createBitmapByMax(Context context,Uri uri,int maxWidth,int maxHeight){
		Bitmap bmp = null;
    	if(uri!=null){
    		try {
    			Context c = context.getApplicationContext();
    			ContentResolver cr = c.getContentResolver();
    			InputStream is = cr.openInputStream(uri);
    			if(is!=null){
	    			BitmapFactory.Options opt = calcBitmapOption(is, maxWidth, maxHeight);
	    		    is.close();
	    		    //もう一度
	    			if(opt!=null){
	    				is = cr.openInputStream(uri);
	    				if(is!=null){
		    				bmp = BitmapFactory.decodeStream(is, null, opt);
		        			is.close();
	    				}
	    			}
    			}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	return bmp;
	}
 	public static Bitmap createBitmapByMax(File file,int maxWidth,int maxHeight){
		Bitmap bmp = null;
    	if(file!=null && file.exists() && file.isFile()){
    		try {
    			InputStream is = new FileInputStream(file);
    			BitmapFactory.Options opt = calcBitmapOption(is, maxWidth, maxHeight);
    		    is.close();
    		    //もう一度
    			if(opt!=null){
    				is = new FileInputStream(file);
    				bmp = BitmapFactory.decodeStream(is, null, opt);
    				is.close();
    			}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}catch (Exception e) {
				e.printStackTrace();
			}

    	}
    	return bmp;
	}
 	public static BitmapDrawable createBitmapDrawableByMax(File file,int maxWidth,int maxHeight){
 		Bitmap bmp = createBitmapByMax(file, maxWidth, maxHeight);
 		if(bmp!=null){
 			return new BitmapDrawable(bmp);
 		}
 		return null;
 	}


}