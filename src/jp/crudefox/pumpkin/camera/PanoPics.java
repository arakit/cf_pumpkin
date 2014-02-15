package jp.crudefox.pumpkin.camera;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import jp.crudefox.library.help.Helper;
import jp.crudefox.pumpkin.AppUtil;
import android.content.Context;
import android.graphics.Bitmap;




public class PanoPics {
	
//	public static class TakeFileNameInfo{
//		public int year;
//		public int month;
//		public int day;
//		public int hour;
//		public int minute;
//		public int second;
//		public int millsec;
//		public String extention;
//		
//		public void getTime(Calendar cal){
//			cal.clear();
//			cal.set(Calendar.YEAR, year);
//			cal.set(Calendar.MONTH, month);
//			cal.set(Calendar.DAY_OF_MONTH, day);
//			cal.set(Calendar.HOUR_OF_DAY, hour);
//			cal.set(Calendar.MINUTE, minute);
//			cal.set(Calendar.SECOND, second);
//			cal.set(Calendar.MILLISECOND, millsec);
//		}
//	}
	
	
	private static String FORMAT_PANORAMA_FOLDERNAME = 
			"pano-%04d-%02d-%02d-%02d%02d%02d-%03d";
	public static File getPanpramaFolderFile(File dir,Date date){
		if(dir==null) return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return new File(dir,String.format(FORMAT_PANORAMA_FOLDERNAME, 
				cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,cal.get(Calendar.DATE), 
				cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE),cal.get(Calendar.SECOND),
				cal.get(Calendar.MILLISECOND)
				));
	}
	
	private static String FORMAT_SRCPICT_FILENAME = 
			"pict-%03d.%s";
	public static File getSrcPictFile(File dir,int azim,String ext){
		if(dir==null) return null;
		return new File(dir,String.format(FORMAT_SRCPICT_FILENAME, 
				Helper.to0to360ByDegree(azim),
				ext
				));
	}
	

	private final Context mContext;
	private final File mParentDir;
	private final Date mDate;
	
	private File mDir;
	
	public PanoPics(Context context,File dir,Date date){
		if(dir==null || date==null) throw new IllegalArgumentException();
		mParentDir = dir;
		mDate = date;
		mContext = context;
	}
	
	public boolean makeFolder(){
		File file = getPanpramaFolderFile(mParentDir, mDate);
		if( file==null ) return false;
		if( !file.exists() ) file.mkdir();
		if( file.isDirectory() && file.exists() ){
			mDir = file;
			return true;
		}
		return false;
	}
	public boolean deleteFolder(){
		if(mDir==null) return false;
		return Helper.deleteDirOrFile(mDir);
	}
	

	public File saveBmp(Bitmap bmp,int azim){
		if(mDir==null) return null;
		File file = getSrcPictFile(mDir, azim, "jpg");
		boolean ret = AppUtil.saveBitmap(mContext, bmp, file, 100, false);
		if(ret) return file;
		return null;
	}
	
	
	
	private Panoramer mPanoramer;
	
	public void addMosaic(int[] data,int linesize){
		if( mPanoramer == null ) {
			mPanoramer = new Panoramer(linesize);
		}
		mPanoramer.addMosaic(data, linesize);
	}
	
	
	
	
	
}
