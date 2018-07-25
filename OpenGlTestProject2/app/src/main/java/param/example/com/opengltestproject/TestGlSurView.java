package param.example.com.opengltestproject;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2018/6/15 0015.
 */

public class TestGlSurView extends GLSurfaceView {

    Context context;
    private static final int BYTES_PER_FOLAT = 4;
    private FloatBuffer floatBuffer;

    public TestGlSurView(Context context) {
        this(context, null);
    }

    public TestGlSurView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }


}
