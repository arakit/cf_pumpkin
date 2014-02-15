package cf.android.ar.d3;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;

public class Line {

	
	private final int mPolyNum;
	private final Buffer mVertexBuffer;

	private final Buffer mColorBuffer;	

	
	private Line(FloatBuffer v,FloatBuffer c,int polyNum){
		mVertexBuffer = v;
		mPolyNum = polyNum;
		mColorBuffer = c;
	}	
	
	
	public void draw(GL10 gl,boolean col){
		
		//色指定
		if(col && mColorBuffer!=null){
			gl.glColorPointer(4, GL10.GL_FLOAT, 0, mColorBuffer);
			gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		}else{
			gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
		}
		
		//ポリゴン描画
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer );
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

		//3つの頂点を持つポリゴンn個で構成されている。
		final int polygonNum =  mPolyNum;
		gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, polygonNum);
		
		
	}
	
	
	public static Line createRing(int div,float radius,float x,float y,float z){

		float[] vertices = new float[(div+1)*3];
		int count = 0;
		for (int i = 0; i < div; i++) {
			float theta1 = (float) (2.0f /div*i*Math.PI);
			vertices[count++] = (float)Math.cos((double)theta1)*radius+x;
			vertices[count++] = (float)Math.sin((double)theta1)*radius+y;
			vertices[count++] = z;
		}
		vertices[count++] = vertices[0];
		vertices[count++] = vertices[1];
		vertices[count++] = vertices[2];
		
	    ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
  	    vbb.order(ByteOrder.nativeOrder());
  	    FloatBuffer vertexBuffer = vbb.asFloatBuffer();
  	    vertexBuffer.put(vertices);
  	    vertexBuffer.position(0);
		
  	    Line result = new Line(vertexBuffer,null,div+1);
  	    
  	    return result;
	}
	
	public static Line createLine(float[] vertices,float[] cols){

//		float[] vertices = new float[(2)*3];
//		int count=0;
//		vertices[count++] = 0;
//		vertices[count++] = 0;
//		vertices[count++] = 0;
//		vertices[count++] = x;
//		vertices[count++] = y;
//		vertices[count++] = z;
		
//	    ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
//  	    vbb.order(ByteOrder.nativeOrder());
//  	    FloatBuffer vertexBuffer = vbb.asFloatBuffer();
//  	    vertexBuffer.put(vertices);
//  	    vertexBuffer.position(0);
//  	    
//	    ByteBuffer cbb = ByteBuffer.allocateDirect(cols.length*4);
//	    cbb.order(ByteOrder.nativeOrder());
//		FloatBuffer colorBuffer = cbb.asFloatBuffer();
//		colorBuffer.put(cols);
//		colorBuffer.position(0);
		
		FloatBuffer vertexBuffer = GLHelper.createFloatBuffer(vertices);
		FloatBuffer colorBuffer = GLHelper.createFloatBuffer(cols);
		
  	    Line result = new Line(vertexBuffer,colorBuffer,vertices.length/3);
  	    
  	    return result;
	}
	
}
