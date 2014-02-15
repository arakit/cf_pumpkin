package jp.crudefox.pumpkin;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

public class FileManager {


	public final static String APP_DATA_DIR = "CF_PUMPKIN";


	public static boolean isSD(){
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			return true;
		}
		return false;
	}
	public static File getPublicDownloadDir(){
		File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		AppUtil.Log("file = "+dir.getAbsolutePath());
		return dir;
	}



	public static File getRootDir(){
		if(!isSD()) return null;
		File sd = Environment.getExternalStorageDirectory();
		File root = new File(sd.getAbsolutePath(),APP_DATA_DIR);
		if(!root.exists()) root.mkdir();
		if(!root.exists()) return null;
		return root;
	}
	public static File getDataDir(){
		return getDir("data");
	}
	public static File getPictDir(){
		return getDir( APP_DATA_DIR.toLowerCase(Locale.ENGLISH) + "_" + "pict");
	}
	public static File getStitchDir(){
		return getDir("stitch");
	}





	private static String FORMAT_TAKEPICT_FILENAME =
			"%04d-%02d-%02d-%02d%02d%02d-%03d.jpg";
	public static File getTakePictureFile(Date date){
		File dir = getPictDir();
		if(dir==null) return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return new File(dir,String.format(FORMAT_TAKEPICT_FILENAME,
				cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,cal.get(Calendar.DATE),
				cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE),cal.get(Calendar.SECOND),
				cal.get(Calendar.MILLISECOND)
				));
	}


	private static Pattern PATTERN_TAKEPICT_FILENAME =
			Pattern.compile("^([0-9]{4})-([0-9]{2})-([0-9]{2})-([0-9]{2})([0-9]{2})([0-9]{2})-([0-9]{3})\\.(.+)$");
	public static boolean getTakePictureDateFromFileName(String name,TakeFileNameInfo out){
		if(TextUtils.isEmpty(name)) return false;
		Matcher mat = PATTERN_TAKEPICT_FILENAME.matcher(name);
		if(!mat.find()) return false;
		if(out!=null){
			out.year = Integer.valueOf( mat.group(1) );
			out.month = Integer.valueOf( mat.group(2) );
			out.day = Integer.valueOf( mat.group(3) );
			out.hour = Integer.valueOf( mat.group(4) );
			out.minute = Integer.valueOf( mat.group(5) );
			out.second = Integer.valueOf( mat.group(6) );
			out.millsec = Integer.valueOf( mat.group(7) );
			out.extention = mat.group(8) ;
		}
		return true;
	}

	public static class TakeFileNameInfo{
		public int year;
		public int month;
		public int day;
		public int hour;
		public int minute;
		public int second;
		public int millsec;
		public String extention;

		public void getTime(Calendar cal){
			cal.clear();
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.MONTH, month);
			cal.set(Calendar.DAY_OF_MONTH, day);
			cal.set(Calendar.HOUR_OF_DAY, hour);
			cal.set(Calendar.MINUTE, minute);
			cal.set(Calendar.SECOND, second);
			cal.set(Calendar.MILLISECOND, millsec);
		}
	}




//	public static File getDefaultBookmarksExternalDir(){
//		return getDir("bookmarks");
//	}
//	public static File getDefaultBookmarksInternalDir(Context context){
//		return getLocalDir(context, "bookmarks");
//	}




//	private static final SimpleDateFormat BATTERY_DATA_FILENAME_SDF = new SimpleDateFormat("yyyyMMdd");
//	private static final Pattern BATTERY_DATA_FILENAME_PATTERN = Pattern.compile("^([0-9]{4})([0-9]{2})([0-9]{2})$");
//
//	public static Date getDateByBatteryDataFile(File file){
//		String name = file.getName();
//		Matcher mat = BATTERY_DATA_FILENAME_PATTERN.matcher(name);
//		if(mat.find()){
//			Calendar cal = Calendar.getInstance();
//			cal.clear();
//			int yyyy = Integer.parseInt( mat.group(1) );
//			int MM = Integer.parseInt( mat.group(2) );
//			int dd = Integer.parseInt( mat.group(3) );
//			cal.set(yyyy, MM-1, dd);
//			return cal.getTime();
//		}
//		return null;
//	}
//
//
//	public static File[] getBatteryDataListExistFilesByLocal(Context context){
//		File dir = context.getFilesDir();
//		if(dir==null) return null;
//		FileFilter filter = new FileFilter() {
//			public boolean accept(File pathname) {
//				String name = pathname.getName();
//				Matcher mat = BATTERY_DATA_FILENAME_PATTERN.matcher(name);
//				return mat.find();
//			}
//		};
//		return dir.listFiles(filter);
//	}
//	public static File[] getBatteryDataListExistFilesBySD(){
//		File dir = getDataDir();
//		if(dir==null) return null;
//		FileFilter filter = new FileFilter() {
//			public boolean accept(File pathname) {
//				String name = pathname.getName();
//				Matcher mat = BATTERY_DATA_FILENAME_PATTERN.matcher(name);
//				return mat.find();
//			}
//		};
//		return dir.listFiles(filter);
//	}
//
//	public static File getBatteryDataListFileByLocal(Context context,Date date){
//		File dir = context.getFilesDir();
//		if(dir!=null){
//			SimpleDateFormat sdf = BATTERY_DATA_FILENAME_SDF;
//			return new File(dir,sdf.format(date));
//		}
//		return null;
//	}
//
//	public static File getBatteryDataListFileBySD(Date date){
//		File dir = getDataDir();
//		if(dir!=null){
//			SimpleDateFormat sdf = BATTERY_DATA_FILENAME_SDF;
//			return new File(dir,sdf.format(date));
//		}
//		return null;
//	}

	public static File getLocalDir(Context context,String dirname){
		File ldir = context.getFilesDir();
		if(ldir==null) return null;
		File dir = new File(ldir.getAbsolutePath(),dirname);
		if(!dir.exists()) dir.mkdir();
		if(!dir.exists()) return null;
		return dir;
	}

	private static File getDir(String dirname){
		File root = getRootDir();
		if(root==null) return null;
		File dir = new File(root.getAbsolutePath(),dirname);
		if(!dir.exists()) dir.mkdir();
		if(!dir.exists()) return null;
		return dir;
	}

}
