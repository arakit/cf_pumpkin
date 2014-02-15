package cf.u.android.camera;

public class JNIUtility {


	
	public native static int decodeYUV420SP(int[] j_rgb, byte[] j_yuv420sp, int width, int height,int flag);
	public native static int decodeYUV420SPAndRotate(int[] j_rgb, byte[] j_yuv420sp, int width, int height,int flag);
	

	public native static int invertBitmap(int[] j_rgb,int width,int height);
	public native static int sepiaBitmap(int[] j_rgb,int width,int height);
	public native static int grayscaleBitmap(int[] j_rgb,int width,int height);
	public native static int brightnessBitmap(int[] j_rgb,int width,int height,int brightness);
	public native static int contrastBitmap(int[] j_rgb,int width,int height,float contrast,int brightness);
	public native static int flipBitmap(int[] j_dst,int[] j_src,int width,int height,int flip_h,int flip_v);
	//public native static int mirrorBitmap(int[] j_dst,int[] j_src,int width,int height,int mirror_h,int mirror_v);

	public static void mirrorBitmapNN(int[] rgb,int width,int height,int mirror_h,int mirror_v){
		int i,j;
		int x,y;
		final int w=width,h=height;
		final int w2=width/2,h2=height/2;

		if(mirror_h!=0 && mirror_v==0){
			for(j=0; j<h; j++){
				y = j;
				if(mirror_h==1){
					for(i=w2; i<w; i++){
						x = w2-(i-w2);
						rgb[w*j + i] = 0xff000000 | rgb[w*y + x];
					}
				}else{
					for(i=0; i<w2; i++){
						x = w-i-1;
						rgb[w*j + i] = 0xff000000 | rgb[w*y + x];
					}
				}
			}
		}
		else if(mirror_v!=0 && mirror_h==0){
			for(i=0; i<w; i++){
				x = i;
				if(mirror_v==1){
					for(j=h2; j<h; j++){
						y = h2-(j-h2);
						rgb[w*j + i] = 0xff000000 | rgb[w*y + x];
					}
				}else{
					for(j=0; j<h2; j++){
						y = h-j-1;
						rgb[w*j + i] = 0xff000000 | rgb[w*y + x];
					}
				}
			}
		}

	}
	
	
	
	public native static int createToyTable(short[] j_table, int width, int height);
	public native static int toyBitmap(int[] j_rgb,int width,int height,short[] j_table,float contrast,int brightness);
	
	
	public native static int fishEyeBitmap(int[] j_dst, int[] j_src, short[] j_table, int width, int height);
	public native static int createFishEyeTable(short[] j_table, int width, int height, int rx, int ry, int D);
	public native static int createTunnelTable(short[] j_table, int width, int height, int rx, int ry, int D);


	public native static int calcHistogram(int[] j_out,int j_size,int[] j_rgb,int width,int height);
	
	public static int calcHistogramNN(int[] out, int j_size, int[] rgb, int width, int height){
		final float waru3 = 1.0f/3.0f;
		final float m_weight = j_size / 256.0f;
		int i,j,index;
		int r,g,b,m;

		for(j=0, index=0; j<height; j++){
			for(i=0; i<width; i++, index++){

				r = ((rgb[index] & 0xff0000) >> 16);
				g = ((rgb[index] & 0x00ff00) >>  8);
				b = ((rgb[index] & 0x0000ff)      );
				m = (int)( (r + g + b) * waru3 );

				out[(int)(m*m_weight)]++;
			}
		}

		return 0;
	}	
	
//	public static int calcDynamicRange(int[] histogram,int size,int[] rgb,int width,int height){
//
//		int iki = width * height / 10000;
////		int maxrange = 0;
////		int maxvalue = 0;
//		int min_kido = 255;
//		int max_kido = 0;
//		for(int i=0;i<size;i++){
////			if( histogram[i] > maxvalue ){
////				maxvalue = histogram[i];
////				maxrange = i;
////			}
//			if(histogram[i]>iki){
//				if( i  >  max_kido )  max_kido = i;
//				if( i  <  min_kido )  min_kido = i;
//			}
//		}
//		max_kido = (int)( max_kido * (256.0/size) );
//		min_kido = (int)( min_kido * (256.0/size) );
//		
//		int target_max_kido = 255-32;
//		int target_min_kido = 32;
//		
////		int center = (int)( maxrange * (256.0/size) );
//		
//		final float waru3 = 1.0f/3.0f;
//		int index=0;
//		int r,g,b,m,c,s,m2;
//		float f,w,w2;
//
//		for(int j=0; j<height; j++){
//			for(int i=0; i<width; i++, index++){
//				//index = width*j + i;
//
//				r = ((rgb[index] & 0xff0000) >> 16);
//				g = ((rgb[index] & 0x00ff00) >>  8);
//				b = ((rgb[index] & 0x0000ff)      );
//				m = (int)( (r + g + b) * waru3 );
////				f = (64 - Math.abs( m-center )) / 64.0f;
////				if(f<0) f = 0;
////				c = (int)( (m-center)*(0.5f+f*f) );
////				c += center*2;
//				
////				m2 = (target_max_kido-target_min_kido) * (m - min_kido) / (max_kido-min_kido) + target_min_kido;
////				w = m2 / (float) m;
////				
////				r = (int)( w * r );
////				g = (int)( w * g );
////				b = (int)( w * b );
//				
//				w = (target_max_kido-target_min_kido) / (float)(max_kido-min_kido);
//				
//				r = (int)( (r - min_kido) * w + target_min_kido );
//				g = (int)( (g - min_kido) * w + target_min_kido );
//				b = (int)( (b - min_kido) * w + target_min_kido );
//				
//
////				r += c;
////				g += c;
////				b += c;
//
//				if(r>255) r=255; else if(r<0) r = 0;
//				if(g>255) g=255; else if(g<0) g = 0;
//				if(b>255) b=255; else if(b<0) b = 0;
//
//				rgb[index] = 0xff000000 | r<<16 | g<<8 | b;
//			}
//		}
//
//		return 0;
//	}
	
	

    static {
        System.loadLibrary("camera-jni");
    }
	
}
