package cf.android.ar.d3;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import jp.crudefox.pumpkin.AppUtil;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Point;
import android.opengl.GLUtils;
import android.text.TextPaint;

public class Text {

	int mTextId;

	FloatBuffer mVertexBuffer;
	FloatBuffer mCordBuffer;

	public Text(){

	}

	public int set(GL10 gl,String text,int color,int text_size,int height){

		TextPaint tp = createTextPaint(color, text_size);
		Point szRange = calcTextAreaSize(text, tp);

		float scale = (float) height / (float) szRange.y ;
		szRange.x *= scale;
		szRange.y *= scale;
		
		szRange.x = szRange.y;

		AppUtil.Log("Text imagesize x="+szRange.x+"y="+szRange.y);

		Bitmap image = Bitmap.createBitmap(szRange.x,szRange.y, Config.ARGB_8888);

		Canvas canvas = new Canvas(image);
		canvas.scale(scale, scale);
		drawText(canvas, tp, text, szRange, HAlign.Left, VAlign.Top);

	    float vertices[] = {
	  	      // ?O
	  	      -0.5f, -0.5f, 0.0f,
	  	      0.5f, -0.5f, 0.0f,
	  	      -0.5f, 0.5f, 0.0f,
	  	      0.5f, 0.5f, 0.0f
	    };
	    float cord[] = {
	    		1.0f,1.0f,
	    		0.0f,1.0f,
	    		1.0f,0.0f,
	    		0.0f,0.0f,
		    };

	    mVertexBuffer = GLHelper.createFloatBuffer(vertices);
	    mCordBuffer = GLHelper.createFloatBuffer(cord);


		int[] textureNo = new int[1];
		gl.glGenTextures(1, textureNo, 0);
		//?e?N?X?`?????????o?C???h
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureNo[0]);
		//GL?p?????????????f?[?^??n???B???o?C???h?????e?N?X?`?????????????t??????B
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, image, 0);

		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);// ?k??????t?B???^
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);// ?g????t?B???^

		image.recycle();
		image = null;

		return mTextId = textureNo[0];
	}

	public void draw(GL10 gl){

//		Log.d("cf",""+mTextId);

//		gl.glActiveTexture( GL10.GL_TEXTURE0 );
//		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextId);

	    gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	    gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);

	    gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	    gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mCordBuffer);

	    // Front
	    //gl.glNormal3f(0, 0, 1.0f);
	    gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

	}




	private TextPaint createTextPaint(int color,int size){
		TextPaint p = new TextPaint();
		p.setStrokeWidth(1.0f);
		p.setColor(color);
		p.setTextSize(size);
		p.setAntiAlias(true);
		p.setTextAlign(Paint.Align.LEFT);
		p.setSubpixelText(true);
		return p;
	}

	enum HAlign{
		Left,
		Center,
		Right,
	}
	enum VAlign{
		Top,
		Center,
		Bottom,
	}

	public static void drawText(Canvas cv,TextPaint paint,String text,Point sc,HAlign ha,VAlign va){
		Point Pt = new Point(0,0);

		switch(ha){
		case Left:
			paint.setTextAlign(Align.LEFT);
			Pt.x = 0;
			break;
		case Center:
			paint.setTextAlign(Align.CENTER);
			Pt.x = 0 + sc.x;
			break;
		case Right:
			paint.setTextAlign(Align.RIGHT);
			Pt.x = 0 + sc.x;
			break;
		}

		FontMetrics fm = paint.getFontMetrics();
		switch(va){
		case Top:
			Pt.y = (int)( 0 - fm.ascent );
			break;
		case Center:
			Pt.y = (int)( 0+sc.y/2.0f - (fm.ascent+fm.descent)/2.0f );
			break;
		case Bottom:
			Pt.y = (int)( 0+sc.y - fm.descent );
			break;
		}

		cv.drawText(text, Pt.x, Pt.y, paint);
	}


	public static Point calcTextAreaSize(String text,TextPaint paint){
		Point tsize = new Point();
		FontMetrics fm = paint.getFontMetrics();
		tsize.y = (int) (fm.descent - fm.ascent);
		tsize.x = (int) (paint.measureText(text) + 0.99f);
		return tsize;
	}

}
