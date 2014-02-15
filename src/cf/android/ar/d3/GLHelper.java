package cf.android.ar.d3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class GLHelper {

	public static final FloatBuffer createFloatBuffer(float[] arr){
		
	    ByteBuffer cbb = ByteBuffer.allocateDirect(arr.length*4);
	    cbb.order(ByteOrder.nativeOrder());
		FloatBuffer buffer = cbb.asFloatBuffer();
		buffer.put(arr);
		buffer.position(0);
		
		return buffer;
	}
	
	public static final IntBuffer createIntBuffer(int[] arr){
		
	    ByteBuffer cbb = ByteBuffer.allocateDirect(arr.length*4);
	    cbb.order(ByteOrder.nativeOrder());
		IntBuffer buffer = cbb.asIntBuffer();
		buffer.put(arr);
		buffer.position(0);
		
		return buffer;
	}	
	
	
	
}
