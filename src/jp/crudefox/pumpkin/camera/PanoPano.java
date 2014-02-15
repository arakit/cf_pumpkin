package jp.crudefox.pumpkin.camera;

import jp.crudefox.pumpkin.AppUtil;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region.Op;


public class PanoPano {


	private Bitmap mBmp;
	private Canvas mCanvas;
	private Paint mPaint;

	private Paint mToCanvasPaint;

	private Path mPathCircle;

	private int mBmpWidth;
	private int mBmpHeight;



	public PanoPano(){
		mBmpWidth = 2048;
		mBmpHeight = mBmpWidth / 4;
		mBmp = Bitmap.createBitmap(mBmpWidth, mBmpHeight, Config.RGB_565);

		mCanvas = new Canvas(mBmp);
		mCanvas.drawColor(Color.WHITE);

		mPaint = new Paint();
		mPaint.setAlpha(255);
		mPaint.setColor(Color.WHITE);

		mToCanvasPaint = new Paint();
		mToCanvasPaint.setAlpha(255);
		mToCanvasPaint.setColor(Color.WHITE);


		mPathCircle = new Path();

	}


	/**
	 * @param
	 *
	 * */
	public void draw(Bitmap bmp, float x, float y, float w, float h, float roll){
		//mCanvas.dra

		AppUtil.Log("draw pano " + String.format("x=%f, y=%f, w=%f, h=%f bmp_w=%d", x, y, w, h, bmp.getWidth()) );

		final Rect s_rc = new Rect();
		final RectF d_rc = new RectF();

		//float wei = 0.25f;

		s_rc.left = 0;
		s_rc.top = 0;
		s_rc.right = bmp.getWidth();
		s_rc.bottom = bmp.getHeight();


//		d_rc.left = x - bmp.getWidth()/2 * wei;
//		d_rc.right = x + bmp.getWidth()/2 * wei;
//
//		d_rc.top = y - bmp.getHeight()/2 * wei;
//		d_rc.bottom = y + bmp.getHeight()/2 * wei;

		d_rc.left = x - w/2 ;
		d_rc.right = x + w/2 ;

		d_rc.top = y - h/2 ;
		d_rc.bottom = y + h/2;

		mPathCircle.reset();
		//mPathCircle.addCircle(x, y, 10, Direction.CW);
		mPathCircle.addRoundRect(new RectF(x-15, -45, x+15, +45), 5, 5, Direction.CW);


		mCanvas.save();
		mCanvas.translate(0, +mBmpHeight/2.0f);
		mCanvas.scale(mBmpWidth/360.0f, mBmpHeight/90.0f);
		mCanvas.rotate(roll, x, y);

		mCanvas.clipPath(mPathCircle, Op.INTERSECT);


		mPaint.setStrokeWidth(1.0f);
		mPaint.setAlpha( (int) (0.80f*255) );
		mCanvas.drawBitmap(bmp, s_rc, d_rc, mPaint);


		mPaint.setStrokeWidth(5.0f);
		mPaint.setAlpha( (int) (1.0f*255) );

		mPaint.setColor(Color.RED);
		//mCanvas.drawRect(d_rc, mPaint);
		mCanvas.drawCircle(x, y, 1.0f, mPaint);

		//mCanvas.drawPath(mPathCircle, mPaint);

//		mPaint.setColor(Color.MAGENTA);
//		mCanvas.drawLine(0, -45, 360, -45, mPaint);
//
//		mPaint.setColor(Color.GREEN);
//		mCanvas.drawLine(0, 0, 360, 0, mPaint);
//
//		mPaint.setColor(Color.YELLOW);
//		mCanvas.drawLine(0, +45, 360, +45, mPaint);



		mCanvas.restore();

	}


	public void destroy(){
		if(!mBmp.isRecycled() && !mBmp.isMutable()) mBmp.recycle();
		mBmp = null;
	}



	public Bitmap getBitmap(){
		return mBmp;
	}



	public void drawToCanvas(Canvas cv,float sx, float sy){

		final Rect s_rc = new Rect();
		final RectF d_rc = new RectF();

		float wei =  360.0f / (float)mBmpWidth;

		s_rc.left = 0;
		s_rc.top = 0;
		s_rc.right = mBmp.getWidth();
		s_rc.bottom = mBmp.getHeight();


		d_rc.left = (-sx)  ;
		d_rc.right = (-sx) + ( mBmpWidth * wei) ;

		d_rc.top = (-sy) - (mBmpHeight*wei/2.0f);
		d_rc.bottom = (-sy) + ( mBmpHeight * wei) - (mBmpHeight*wei/2.0f);

		cv.save();
//		cv.translate(0, +mBmpHeight/2.0f);
//		cv.scale(mBmpWidth/360.0f, mBmpHeight/90.0f);

		//cv.drawARGB(128, 255, 0, 0);

		//mToCanvasPaint.setAlpha((int)(255*0.5f));
		cv.drawBitmap(mBmp, s_rc, d_rc, mToCanvasPaint);

		cv.restore();


	}


}
