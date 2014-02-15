package cf.android.ar.d3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Circle {

	private final int mDivides;
	private final FloatBuffer mVertexBuffer;
	
	public Circle(){
		
		mDivides = 32;
		mVertexBuffer = createCircleBuffer( 32, 1, 0,0,0 );
		
	}	
	
	
	public void draw(GL10 gl){
		
		//色指定
		//gl.glColor4x(red, green, blue, alpha);
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);

		//ポリゴン描画
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer );
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

		//3つの頂点を持つポリゴンn個で構成されている。
		final int polygonNum =  mDivides * 3;
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, polygonNum);
		
	}
	
	
	public static FloatBuffer createCircleBuffer(int div,float radius,float x,float y,float z){

		float[] vertices = new float[div*3*3];
		int count = 0;
		for (int i = 0; i < div; i++) {
			float theta1 = (float) (2.0f /div*i*Math.PI);
			float theta2 = (float) (2.0f /div*(i+1)*Math.PI);
			vertices[count++] = x;
			vertices[count++] = y;
			vertices[count++] = z;
			vertices[count++] = (float)Math.cos((double)theta1)*radius+x;
			vertices[count++] = (float)Math.sin((double)theta1)*radius+y;
			vertices[count++] = z;
			vertices[count++] = (float)Math.cos((double)theta2)*radius+x;
			vertices[count++] = (float)Math.sin((double)theta2)*radius+y;
			vertices[count++] = z;
		}
		
	    ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
  	    vbb.order(ByteOrder.nativeOrder());
  	    FloatBuffer vertexBuffer = vbb.asFloatBuffer();
  	    vertexBuffer.put(vertices);
  	    vertexBuffer.position(0);		
		
  	    return vertexBuffer;
	}
	
	

	

//	public static void drawCircle(GL10 gl,final float x, final float y,
//			final int divides, final  float radius,
//			final int red, final int green, final int blue, final int alpha){
//		float [] vertices = new float[divides * 3 * 2];//頂点の数はn角形の場合はn*3*2になる。
//
//		int vertexId = 0;
//
//		for(int i=0; i < divides; i++){
//			float theta1  = getRadian(divides, i);
//			float theta2  = getRadian(divides, i+1);
//
//			vertices[vertexId++] = x;
//			vertices[vertexId++] = y;
//
//			vertices[vertexId++] = (float) (cos(theta1) * radius + x);
//			vertices[vertexId++] = (float) (sin(theta1) * radius + y);
//
//			vertices[vertexId++] = (float) (cos(theta2) * radius + x);
//			vertices[vertexId++] = (float) (sin(theta2) * radius + y);
//		}
//
//
//
//	}
	
	
}
