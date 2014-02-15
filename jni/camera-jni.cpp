
#include <string.h>
#include <jni.h>
#include <math.h>


extern "C" jint
Java_cf_u_android_camera_JNIUtility_decodeYUV420SP( JNIEnv* env,
                                                  jclass thiz,
                                                  jintArray j_rgb, jbyteArray j_yuv420sp, jint width, jint height,jint flag
                                                  )
{

	int frameSize = width * height;
	int i,j,yp,uvp,y,y1192,r,g,b,u,v;

//	unsigned char *yuv420sp = (unsigned char *) env->GetPrimitiveArrayCritical(j_yuv420sp,NULL);//GetByteArrayElements(jSrc, &b);
//	jint *rgb = (jint *)env->GetPrimitiveArrayCritical(j_rgb,NULL);//GetIntArrayElements(jDst, &b);
	unsigned char *yuv420sp = (unsigned char *) env->GetByteArrayElements(j_yuv420sp,NULL);
	jint *rgb = (jint *)env->GetIntArrayElements(j_rgb,NULL);

	for (j = 0, yp = 0; j < height; j++ ) {
		uvp = frameSize + (j >> 1) * width; u = 0; v = 0;
		for (i = 0; i < width; i++, yp++) {
			y = (0xff & ((int) yuv420sp[yp])) - 16;
			if (y < 0) y = 0;
			if ((i & 1) == 0) {
				v = (0xff & yuv420sp[uvp++]) - 128;
				u = (0xff & yuv420sp[uvp++]) - 128;
			}
			y1192 = 1192 * y;
			r = (y1192 + 1634 * v);
			g = (y1192 - 833 * v - 400 * u);
			b = (y1192 + 2066 * u);
			if (r < 0) r = 0; else if (r > 262143) r = 262143;
			if (g < 0) g = 0; else if (g > 262143) g = 262143;
			if (b < 0) b = 0; else if (b > 262143) b = 262143;
			rgb[flag==0?yp:frameSize-yp-1] = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
		}
	}

//	env->ReleasePrimitiveArrayCritical(j_yuv420sp, yuv420sp,0);//ReleaseByteArrayElements(jSrc, (jbyte *)aSrc, 0);
//	env->ReleasePrimitiveArrayCritical(j_rgb, rgb, 0);//ReleaseIntArrayElements(jDst, aDst, 0);
	env->ReleaseByteArrayElements(j_yuv420sp, (jbyte *)yuv420sp, JNI_ABORT);
	env->ReleaseIntArrayElements(j_rgb, rgb, 0);

	return 0;
}


extern "C" jint
Java_cf_u_android_camera_JNIUtility_decodeYUV420SPAndRotate( JNIEnv* env,
                                                  jclass thiz,
                                                  jintArray j_rgb, jbyteArray j_yuv420sp, jint width, jint height,jint flag
                                                  )
{

	const int frameSize = width * height;
	//const int d_width=height;
	//const int d_height=width;
	int i,j,yp,uvp,y,y1192,r,g,b,u,v,doff,soff;

//	unsigned char *yuv420sp = (unsigned char *) env->GetPrimitiveArrayCritical(j_yuv420sp,NULL);//GetByteArrayElements(jSrc, &b);
//	jint *rgb = (jint *)env->GetPrimitiveArrayCritical(j_rgb,NULL);//GetIntArrayElements(jDst, &b);
	unsigned char *yuv420sp = (unsigned char *) env->GetByteArrayElements(j_yuv420sp,NULL);
	jint *rgb = (jint *)env->GetIntArrayElements(j_rgb,NULL);

	for (j = 0, yp = 0; j < height; j++) {
		uvp = frameSize + (j >> 1) * width; u = 0; v = 0;
		doff = flag==0 ? height-j-1 : j;
		for (i = 0, soff = (flag==0?0:frameSize-height-1) ; i<width; i++, yp++, soff+=(flag==0?+height:-height) ) {
			y = (0xff & ((int) yuv420sp[yp])) - 16;
			if (y < 0) y = 0;
			if ((i & 1) == 0) {
				v = (0xff & yuv420sp[uvp++]) - 128;
				u = (0xff & yuv420sp[uvp++]) - 128;
			}
			y1192 = 1192 * y;
			r = (y1192 + 1634 * v);
			g = (y1192 - 833 * v - 400 * u);
			b = (y1192 + 2066 * u);
			if (r < 0) r = 0; else if (r > 262143) r = 262143;
			if (g < 0) g = 0; else if (g > 262143) g = 262143;
			if (b < 0) b = 0; else if (b > 262143) b = 262143;
			rgb[soff+doff] = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
		}
	}

	//env->ReleasePrimitiveArrayCritical(j_yuv420sp, yuv420sp,0);//ReleaseByteArrayElements(jSrc, (jbyte *)aSrc, 0);
	//env->ReleasePrimitiveArrayCritical(j_rgb, rgb, 0);//ReleaseIntArrayElements(jDst, aDst, 0);
	env->ReleaseByteArrayElements(j_yuv420sp, (jbyte *)yuv420sp, JNI_ABORT);
	env->ReleaseIntArrayElements(j_rgb, rgb, 0);

	return 0;
}


extern "C" jint
Java_cf_u_android_camera_JNIUtility_invertBitmap( JNIEnv* env,
                                                  jclass thiz,
                                                  jintArray j_rgb, jint width, jint height
                                                  )
{

	int i,j,index;

	jint *rgb = (jint *) env->GetIntArrayElements(j_rgb,NULL);

	for(j=0; j<height; j++){
		for(i=0; i<width; i++){
			index = width*j + i;
			rgb[index] = 0xff000000 | ~rgb[index];
		}
	}

	env->ReleaseIntArrayElements(j_rgb, rgb, 0);

	return 0;
}

extern "C" jint
Java_cf_u_android_camera_JNIUtility_flipBitmap( JNIEnv* env,
                                                  jclass thiz,
                                                  jintArray j_dst, jintArray j_src, jint width, jint height, jint flip_h, jint flip_v
                                                  )
{
	int i,j,index=0;
	int x,y;
	const int w=width,h=height;

	jint *dst = (jint *) env->GetIntArrayElements(j_dst,NULL);
	jint *src = (jint *) env->GetIntArrayElements(j_src,NULL);

	for(j=0; j<h; j++){
		for(i=0; i<w; i++, index++){
			//index = width*j + i;
			if(flip_h!=0) x = width -i-1; else x = i;
			if(flip_v!=0) y = height-j-1; else y = j;
			dst[index] = 0xff000000 | src[w*y + x];
		}
	}

	env->ReleaseIntArrayElements(j_dst, dst, 0);
	env->ReleaseIntArrayElements(j_src, src, JNI_ABORT);

	return 0;
}




extern "C" jint
Java_cf_u_android_camera_JNIUtility_grayscaleBitmap( JNIEnv* env,
                                                  jclass thiz,
                                                  jintArray j_rgb, jint width, jint height
                                                  )
{

	const int rp = 0.298912 * 1024;
	const int gp = 0.586611 * 1024;
	const int bp = 0.114478 * 1024;
	int i,j,y,index;

	jint *rgb = (jint *) env->GetIntArrayElements(j_rgb,NULL);

	for(j=0; j<height; j++){
		for(i=0; i<width; i++){
			index = width*j + i;
			y = (((rgb[index] & 0xff0000) >> 16)*rp + ((rgb[index] & 0x00ff00) >> 8)*gp + (rgb[index] & 0x0000ff)*bp) >> 10;
			rgb[index] = 0xff000000 | ((0xff & y)<<16) | ((0xff & y)<<8) | (0xff & y);
		}
	}

	env->ReleaseIntArrayElements(j_rgb, rgb, 0);

	return 0;
}


extern "C" jint
Java_cf_u_android_camera_JNIUtility_sepiaBitmap( JNIEnv* env,
                                                  jclass thiz,
                                                  jintArray j_rgb, jint width, jint height
                                                  )
{

	const int rp = 0.298912 * 1024;
	const int gp = 0.586611 * 1024;
	const int bp = 0.114478 * 1024;
	const float rs = 240.0f/255.0f;
	const float gs = 200.0f/255.0f;
	const float bs = 145.0f/255.0f;
	int i,j,y,index;

	jint *rgb = (jint *) env->GetIntArrayElements(j_rgb,NULL);

	for(j=0; j<height; j++){
		for(i=0; i<width; i++){
			index = width*j + i;
			y = (((rgb[index] & 0xff0000) >> 16)*rp + ((rgb[index] & 0x00ff00) >> 8)*gp + (rgb[index] & 0x0000ff)*bp) >> 10;
			rgb[index] = 0xff000000 | ((0xff & (int)(y*rs))<<16) | ((0xff & (int)(y*gs))<<8) | (0xff & (int)(y*bs));
			//rgb[index] = 0xff000000 | ((0xff & (((y+1)*240)>>8))<<16) | ((0xff & (int)(y*gs))<<8) | (0xff & (int)(y*bs));
		}
	}

	env->ReleaseIntArrayElements(j_rgb, rgb, 0);

	return 0;
}


extern "C" jint
Java_cf_u_android_camera_JNIUtility_brightnessBitmap( JNIEnv* env,
                                                  jclass thiz,
                                                  jintArray j_rgb, jint width, jint height, jint bright
                                                  )
{
	int i,j,index,r,g,b;

	jint *rgb = (jint *) env->GetIntArrayElements(j_rgb,NULL);

	for(j=0; j<height; j++){
		for(i=0; i<width; i++){
			index = width*j + i;

			r = ((rgb[index] & 0xff0000) >> 16) + bright;
			g = ((rgb[index] & 0x00ff00) >>  8) + bright;
			b = ((rgb[index] & 0x0000ff)      ) + bright;

			if(r>255) r=255; else if(r<0) r = 0;
			if(g>255) g=255; else if(g<0) g = 0;
			if(b>255) b=255; else if(b<0) b = 0;

			rgb[index] = 0xff000000 | r<<16 | g<<8 | b;
		}
	}

	env->ReleaseIntArrayElements(j_rgb, rgb, 0);

	return 0;
}


extern "C" jint
Java_cf_u_android_camera_JNIUtility_contrastBitmap( JNIEnv* env,
                                                  jclass thiz,
                                                  jintArray j_rgb, jint width, jint height, jfloat contrast, jint brightness
                                                  )
{
	const float waru3 = 1.0/3.0;
	int i,j,index,r,g,b,m,c;

	jint *rgb = (jint *) env->GetIntArrayElements(j_rgb,NULL);

	for(j=0; j<height; j++){
		for(i=0; i<width; i++){
			index = width*j + i;

			r = ((rgb[index] & 0xff0000) >> 16);
			g = ((rgb[index] & 0x00ff00) >>  8);
			b = ((rgb[index] & 0x0000ff)      );
			m = (r + g + b) * waru3;
			c = (m-128)*contrast;

			r += c + brightness;
			g += c + brightness;
			b += c + brightness;

			if(r>255) r=255; else if(r<0) r = 0;
			if(g>255) g=255; else if(g<0) g = 0;
			if(b>255) b=255; else if(b<0) b = 0;

			rgb[index] = 0xff000000 | r<<16 | g<<8 | b;
		}
	}

	env->ReleaseIntArrayElements(j_rgb, rgb, 0);

	return 0;
}


extern "C" jint
Java_cf_u_android_camera_JNIUtility_createToyTable( JNIEnv* env,
                                                  jclass thiz,
                                                  jshortArray j_buf, jint width, jint height
                                                  )
{

	const int wh=width*0.5, hh=height*0.5;
	const float whw2 = 1.0/(width *0.5f);
	const float hhw2 = 1.0/(height*0.5f);
	int i,j,index;
	float dx,dy,dd;

	jshort *buf = (jshort *) env->GetShortArrayElements(j_buf,NULL);

	for(j=0, index=0; j<height; j++){
		for(i=0; i<width; i++, index++){
			dx = (i - wh) * whw2 * 0.75f;
			dy = (j - hh) * hhw2 * 0.75f;

			dd = sqrt(dx*dx + dy*dy);
			dd = dd * dd * dd;
			dd = 1.0f - dd ;//+ 0.5f;
			if(dd>1) dd = 1;

			buf[index] = (short)( dd * 8192 );
		}
	}

	env->ReleaseShortArrayElements(j_buf, buf, 0);

	return 0;
}

extern "C" jint
Java_cf_u_android_camera_JNIUtility_toyBitmap( JNIEnv* env,
                                                  jclass thiz,
                                                  jintArray j_rgb, jint width, jint height, jshortArray j_table, jfloat contrast, jint brightness
                                                  )
{
	const float waru3 = 1.0/3.0;
	//const int wh=width*0.5, hh=height*0.5;
	//const float whw2 = 1.0/(width *0.5f);
	//const float hhw2 = 1.0/(height*0.5f);
	int i,j,index,r,g,b,m,c;
	float dd;

	jshort *table = (jshort *) env->GetShortArrayElements(j_table,NULL);
	jint *rgb = (jint *) env->GetIntArrayElements(j_rgb,NULL);

	for(j=0; j<height; j++){
		for(i=0; i<width; i++){
			index = width*j + i;

			r = ((rgb[index] & 0xff0000) >> 16) + brightness;
			g = ((rgb[index] & 0x00ff00) >>  8) + brightness;
			b = ((rgb[index] & 0x0000ff)      ) + brightness;
			m = (r + g + b) * waru3;
			c = (m-128)*contrast;

			r += c;
			g += c;
			b += c;

			dd = table[index] / 8192.0f;

			r = r*dd;
			g = g*dd;
			b = b*dd;

			if(r>255) r=255; else if(r<0) r = 0;
			if(g>255) g=255; else if(g<0) g = 0;
			if(b>255) b=255; else if(b<0) b = 0;

			rgb[index] = 0xff000000 | r<<16 | g<<8 | b;
		}
	}

	env->ReleaseShortArrayElements(j_table, table, JNI_ABORT);
	env->ReleaseIntArrayElements(j_rgb, rgb, 0);

	return 0;
}




extern "C" jint
Java_cf_u_android_camera_JNIUtility_createFishEyeTable( JNIEnv* env,
                                                  jclass thiz,
                                                  jshortArray j_buf, jint width, jint height, jint rx, jint ry, jint D
                                                  )
{

	//const int D = 0;
	const int w_half = width/2;
	const int h_half = height/2;
	const float waru_w = 1.0f / rx;
	const float waru_h = 1.0f / ry;
	int i,j,index,dx,dy;
	float rp;

	//if(width>height) r = h_half; else r = w_half;

	jshort *buf = (jshort *) env->GetShortArrayElements(j_buf,NULL);

	for(j=0, index=0; j<height; j++){
		for(i=0; i<width; i++, index++){
			dx = i-w_half;
			dy = j-h_half;
			rp = sqrt( D + dx*dx + dy*dy );
			buf[index*2  ] = (short)(( dx*rp*waru_w + w_half ));
			buf[index*2+1] = (short)(( dy*rp*waru_h + h_half ));
		}
	}

	env->ReleaseShortArrayElements(j_buf, buf, 0);

	return 0;
}

extern "C" jint
Java_cf_u_android_camera_JNIUtility_createTunnelTable( JNIEnv* env,
                                                  jclass thiz,
                                                  jshortArray j_buf, jint width, jint height, jint rx, jint ry, jint D
                                                  )
{

	//const int D = 0;
	const int w_half = width/2;
	const int h_half = height/2;
	const float waru_w = 1.0f / rx;
	const float waru_h = 1.0f / ry;
	int i,j,index,dx,dy,sa,sax,say;
	float rp,rpx,rpy;
	float asp = height / (float) width;

	//if(width>height) r = h_half; else r = w_half;

	jshort *buf = (jshort *) env->GetShortArrayElements(j_buf,NULL);

	for(j=0, index=0; j<height; j++){
		for(i=0; i<width; i++, index++){
			dx = i-w_half;
			dy = j-h_half;
//			rp = sqrt( D + dx*dx + dy*dy );
			sa = (w_half*w_half)- (dx*dx+dy*dy +D);
			if(sa<0) sa = 0;
//			sax = w_half*w_half - dx*dx;
//			say = h_half*h_half - dy*dy;
//			sax = sax*0.05f + sa*0.95f;
//			say = say*0.05f + sa*0.95f;
//			sax = w_half*w_half - dx*dx;
//			say = h_half*h_half - dy*dy;
			rp = w_half - sqrt( sa );
//			rpx = rp;
//			rpy = rp * asp;
//			rpx = w_half - sqrt( sax );
//			rpy = h_half - sqrt( say );
//			rp = sqrt( D*D + dx*dx+dy*dy );
			buf[index*2  ] = (short)( dx*(rp)*waru_w + w_half );
			buf[index*2+1] = (short)( dy*(rp)*waru_h + h_half );
//			buf[index*2  ] = (short)( rx*w_half/rp + w_half );
//			buf[index*2+1] = (short)( ry*h_half/rp + h_half );
		}
	}

	env->ReleaseShortArrayElements(j_buf, buf, 0);

	return 0;
}

extern "C" jint
Java_cf_u_android_camera_JNIUtility_fishEyeBitmap( JNIEnv* env,
                                                  jclass thiz,
                                                  jintArray j_dst, jintArray j_src, jshortArray j_table, jint width, jint height
                                                  )
{
	int i,j,index;
	int x,y;
//	int x1,x2,y1,y2;
//	float fx,fy;
//	int c1,c2;
//	int r1,g1,b1;
//	int r2,g2,b2;
//	float wx,wy;

	jint *dst = (jint *) env->GetIntArrayElements(j_dst,NULL);
	jint *src = (jint *) env->GetIntArrayElements(j_src,NULL);
	jshort *table = (jshort *) env->GetShortArrayElements(j_table,NULL);

	for(j=0, index=0; j<height; j++){
		for(i=0; i<width; i++, index++){
			x = (int) table[index*2  ];
			y = (int) table[index*2+1];
//			fx =  table[index*2  ] * 0.25f;
//			fy =  table[index*2+1] * 0.25f;
//
//			if(fx>=0 && fx<width && fy>=0 && fy<height) c1 = src[(int)(fy*width + fx)];
//			else c1 = 0xff000000;
//			if(fx+1>=0 && fx+1<width && fy+1>=0 && fy+1<height) c2 = src[(int)((fy+1)*width + fx+1)];
//			else c2 = 0xff000000;
//
//			wx = fx - (int)fx;
//			wy = fy - (int)fy;
//
//			r1 = ((c1 & 0xff0000) >> 16);
//			g1 = ((c1 & 0x00ff00) >>  8);
//			b1 = ((c1 & 0x0000ff)      );
//
//			r2 = ((c2 & 0xff0000) >> 16);
//			g2 = ((c2 & 0x00ff00) >>  8);
//			b2 = ((c2 & 0x0000ff)      );
//
//			r1 = r2*wy + r1*(1-wy);
//			g1 = g2*wy + g1*(1-wy);
//			b1 = b2*wy + b1*(1-wy);
//
//			dst[index] = 0xff000000 | r1<<16 | g1<<8 | b1;

			if(x>=0 && x<width && y>=0 && y<height){
				dst[index] = src[y*width + x];
			}else{
				dst[index] = 0xff000000;
			}
		}
	}

	env->ReleaseIntArrayElements(j_dst, dst, 0);
	env->ReleaseIntArrayElements(j_src, src, JNI_ABORT);
	env->ReleaseShortArrayElements(j_table, table, JNI_ABORT);

	return 0;
}




extern "C" jint
Java_cf_u_android_camera_JNIUtility_calcHistogram( JNIEnv* env,
                                                  jclass thiz,
                                                  jintArray j_out, jint j_size, jintArray j_rgb, jint width, jint height
                                                  )
{
	const float waru3 = 1.0/3.0;
	const float m_weight = j_size / 256.0;
	int i,j,index;
	int r,g,b,m;


	jint *out = (jint *) env->GetIntArrayElements(j_out,NULL);
	jint *rgb = (jint *) env->GetIntArrayElements(j_rgb,NULL);

	for(j=0, index=0; j<height; j++){
		for(i=0; i<width; i++, index++){

			r = ((rgb[index] & 0xff0000) >> 16);
			g = ((rgb[index] & 0x00ff00) >>  8);
			b = ((rgb[index] & 0x0000ff)      );
			m = (r + g + b) * waru3;

			out[(int)(m*m_weight)]++;
		}
	}

	env->ReleaseIntArrayElements(j_out, out, 0);
	env->ReleaseIntArrayElements(j_rgb, rgb, JNI_ABORT);

	return 0;
}

