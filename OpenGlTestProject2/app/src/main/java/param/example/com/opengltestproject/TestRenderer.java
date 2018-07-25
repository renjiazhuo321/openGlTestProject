package param.example.com.opengltestproject;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


/**
 * Created by Administrator on 2018/6/15 0015.
 */

public class TestRenderer implements GLSurfaceView.Renderer {
    private Context context;
    private static final String A_COLOR = "a_Color";
    private static final String A_POSITION = "a_Position";
    private static final String U_MATRIX = "u_Matrix";
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int BYTES_PER_FLOAT = 4;
    private FloatBuffer ffloatBuffer;
    private int aColorLocation = -1;
    private int aPositionLocation = -1;
    private int aMatrix = -1;
    private float projectMatrix[] = new float[16];
    private static final int STRIDE =
            (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    public TestRenderer(Context context) {
        float[] tableVerticesWithTriangles = {
                // Triangle 1   X,Y,R,G,B
                0f, 0f, 1f, 1f, 1f,
                -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
                0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
                0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
                -0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
                -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,

                // Line 1
                -0.5f, 0f, 1f, 0f, 0f,
                0.5f, 0f, 0f, 1f, 0f,

                // Mallets
                0f, -0.25f, 0f, 0f, 1f,
                0f, 0.25f, 1f, 0f, 0f
        };
        this.context = context;
        ffloatBuffer = ByteBuffer.allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        ffloatBuffer.put(tableVerticesWithTriangles);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        //获取glsl代码，得到类型
        String vertexShaderType = TextReadUtil.readTextFileFromResource(context, R.raw.simple_vertex_shader);
        String fragmentShaderType = TextReadUtil.readTextFileFromResource(context, R.raw.simple_fragment_shader);
        //创建顶点着色器和片段着色器
        int vertexShader = ShaderHelper.comileVertexShader(vertexShaderType);
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderType);
        //链接两个着色器
        int programShader = ShaderHelper.linkProgram(vertexShader, fragmentShader);
        //判断是否链接成功
        ShaderHelper.validateProgram(programShader);
        //应用
        GLES20.glUseProgram(programShader);
        //获取着色器的颜色和位置
        aColorLocation = GLES20.glGetAttribLocation(programShader, A_COLOR);
        aPositionLocation = GLES20.glGetAttribLocation(programShader, A_POSITION);
        aMatrix = GLES20.glGetUniformLocation(programShader, U_MATRIX);
        //初始化数据为开始位置
        ffloatBuffer.position(0);

        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT, false, STRIDE, ffloatBuffer);
        GLES20.glEnableVertexAttribArray(aPositionLocation);

        ffloatBuffer.position(POSITION_COMPONENT_COUNT);
        GLES20.glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GLES20.GL_FLOAT, false, STRIDE, ffloatBuffer);
        GLES20.glEnableVertexAttribArray(aColorLocation);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float aspectRatio = width > height ?
                (float) width / (float) height :
                (float) height / (float) width;

        if (width > height) {
            Matrix.orthoM(projectMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
        } else {
            Matrix.orthoM(projectMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
        }
    }


    @Override
    public void onDrawFrame(GL10 gl10) {

        // Clear the rendering surface.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glUniformMatrix4fv(aMatrix, 1, false, projectMatrix, 0);
        // Draw the table.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6);

        // Draw the center dividing line.
        GLES20.glDrawArrays(GLES20.GL_LINES, 6, 2);

        // Draw the first mallet blue.
        GLES20.glDrawArrays(GLES20.GL_POINTS, 8, 1);

        // Draw the second mallet red.
        GLES20.glDrawArrays(GLES20.GL_POINTS, 9, 1);

        // Draw the second mallet red.
//        GLES20.glDrawArrays(GLES20.GL_POINTS, 10, 1);
    }
}
