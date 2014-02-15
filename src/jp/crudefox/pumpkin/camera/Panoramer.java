package jp.crudefox.pumpkin.camera;

import java.util.Arrays;

import jp.crudefox.pumpkin.AppUtil;


public class Panoramer {
	
	private int[] mData;
	
	
	private int mLineSize;
	private int mLineNum;

	
	public  Panoramer(int linesize){
		mLineSize = linesize;
	}
	
	
	public int[] getData(){
		return mData;
	}
	public int getDataLineNum(){
		return mLineNum;
	}
	public int getDataLineSize(){
		return mLineSize;
	}
	
	
	public void addMosaic(int[] data,int a_linesize){
		if(mData==null){
			mData = new int[mLineSize*1500];
			copyFrom(0, data, a_linesize, (a_linesize-mLineSize)/2, 0);
			mLineNum = data.length / a_linesize;
			return ;
		}
		
		int[] mospos = new int[2];
		if( !calcMosaicPosition(data, a_linesize, mospos) ) return ;

		int combine_line = mospos[0];
		if(combine_line<0) return ;
		
		final int union_num = UNION_LINE;
		int data_linenum = data.length / a_linesize;
		int combine_line2 = mLineNum - combine_line - union_num;
		
		if(combine_line2<0) return ;
		if(mLineNum-union_num<0) return ;

		copyFrom(mLineNum-union_num, data, a_linesize, mospos[1], combine_line2);
		mLineNum = combine_line + data_linenum;
		
	}
	
	private void copyFrom(int insert_line,int[] data,int data_first_line){
		int linenum = data.length/mLineSize;
		linenum -= data_first_line;
		for(int i=0;i<linenum;i++){
			for(int j=0;j<mLineSize;j++){
				mData[(i+insert_line)*mLineSize+j] =
						0xff000000 | data[(i+data_first_line)*mLineSize+j];
			}
		}		
	}
	private void copyFrom(int insert_line,int[] data,int data_linesize,int x_first,int data_first_line){
		int linenum = data.length/data_linesize;
		linenum -= data_first_line;
		for(int i=0;i<linenum;i++){
			for(int j=0;j<mLineSize;j++){
				mData[(i+insert_line)*mLineSize+j] = 
						0xff000000 | data[(i+data_first_line)*data_linesize+(j+x_first)];
			}
		}
	}
	
//	private int getLineNum(){
//		if(mData==null) return 0;
//		return mData.length / mLineSize;
//	}
	
	private final static int CALC_LINE = 50;
	private final static int UNION_LINE = 100;
	
	private boolean calcMosaicPosition(int[] data,int a_linesize,int[] result){
		if(mData==null) return false;
		
		int calcline = mLineNum-CALC_LINE;
		if(calcline<0) calcline = 0;
		int calclinenum = mLineNum - calcline;
		
		long min_dif = Long.MAX_VALUE;
		int min_dif_line = -1;
		int min_dif_x = -1;
		
		int a_linenum = data.length / a_linesize;
		for(int a_i=0;a_i<a_linenum-calclinenum;a_i++){
			for(int a_j=0;a_j<=a_linesize-mLineSize;a_j++){

				long dif = 0;
				
				for(int i=0;i<calclinenum;i++){
					for(int j=0;j<mLineSize;j++){
						
						int index = (i+calcline)*mLineSize + j;
						int a_index = (a_i+i)*a_linesize + (a_j+j);
						
						int r =( mData[index] >> 16 ) & 0xff;
						int g =( mData[index] >> 8 ) & 0xff;
						int b =( mData[index]      ) & 0xff;
						
						int a_r =( data[a_index] >> 16 ) & 0xff;
						int a_g =( data[a_index] >> 8 ) & 0xff;
						int a_b =( data[a_index]      ) & 0xff;
						
						dif += Math.abs(a_r - r);
						dif += Math.abs(a_g - g);
						dif += Math.abs(a_b - b);
						
					}
				}
				
				if(dif < min_dif){
					min_dif = dif;
					min_dif_line = calcline - a_i;
					min_dif_x = a_j;
				}				
				
			}
			
		}
		
		int dif_ave = (int)( min_dif / (calclinenum * mLineSize) );
		
		AppUtil.Log("min_dif_line="+min_dif_line+" dif_ave="+dif_ave);
		
		if(dif_ave>=100) return false;

		result[0] = min_dif_line;
		result[1] = min_dif_x;
		
		return true;
	}
	

	static class Histog{
		final int[] arr = new int[32];
		
		public void calc(int[] data,int width,int x,int y,int num){
			Arrays.fill(arr, 0);
			for(int i=0;i<num;i++){
				for(int j=0;j<num;j++){
					int val = data[i*width+j];
					int r = (val >> 16) & 0xff;
					int g = (val >> 8) & 0xff;
					int b = (val >> 0) & 0xff;
					int ave = (r+g+b)/3;
					arr[ave/16]++;
				}
			}
		}
	}
	
	
	
	private void calcSIFT(int[] data,int data_linesize){
		int data_linenum = data.length / data_linesize;
		
		Histog[] histogs = new Histog[4];
		
		final int SIZE = 20;

		histogs[0] = new Histog();
		histogs[0].calc(data, data_linesize, 0, 0, SIZE);
		
		histogs[1] = new Histog();
		histogs[1].calc(data, data_linesize, data_linesize-SIZE, 0, SIZE);
		
		histogs[2] = new Histog();
		histogs[2].calc(data, data_linesize, 0, data_linenum-SIZE, SIZE);

		histogs[3] = new Histog();
		histogs[3].calc(data, data_linesize, data_linesize-SIZE, data_linenum-SIZE, SIZE);
		
//		for(int i=0;i<histogs.length;i++){
//			histogs[i].cal
//		}
		
	}
	


}
