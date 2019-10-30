package openapi.ainemo.com.cameratest.view;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Triangle {

    static float triangleCoords[] = {
            0.0f, 0.622008459f, 0.0f,
            -0.5f, -0.311004342f, 0.0f,
            0.5f, -0.311004342f, 0.0f
    };

    private final String vertexShaderCode = "uniform mat4 uMVPMatrix;" + "attribute vec4 vPosition;" + "void main(){"
            + " gl_Position = uMVPMatrix * vPosition;" + "}";

    private final String fragmentShaderCode = "precision mediump float;" + "uniform vec4 vColor;" + "void main(){" +
            " gl_FragColor = vColor;" + "}";

    private final short drawOrder[] = {0, 1, 2, 0, 2, 3};
    private final FloatBuffer asFloatBuffer;
    private final int glCreateProgram;
    private int mPostionHandle;

    static final int COORDS_PER_VERTEX = 3;
    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4;
    private int mColorHandle;

    float color[] = {0.02f, 0.709803922f, 0.898039216f, 1.0f};
    private int uMVPMatrixHandle;

    public Triangle() {

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(triangleCoords.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        asFloatBuffer = byteBuffer.asFloatBuffer();
        asFloatBuffer.put(triangleCoords);
        asFloatBuffer.position(0);

        int vertexShader = MyRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragShader = MyRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
        glCreateProgram = GLES20.glCreateProgram();

        GLES20.glAttachShader(glCreateProgram, vertexShader);
        GLES20.glAttachShader(glCreateProgram, fragShader);

        GLES20.glLinkProgram(glCreateProgram);

    }


    public void draw(float[] mvpMatrix) {
        GLES20.glUseProgram(glCreateProgram);

        mPostionHandle = GLES20.glGetAttribLocation(glCreateProgram, "vPosition");

        GLES20.glEnableVertexAttribArray(mPostionHandle);

        GLES20.glVertexAttribPointer(mPostionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, asFloatBuffer);

        mColorHandle = GLES20.glGetUniformLocation(glCreateProgram, "vColor");
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        uMVPMatrixHandle = GLES20.glGetUniformLocation(glCreateProgram, "uMVPMatrix");

        MyRenderer.checkGLError("glGetUniformLocation");
        GLES20.glUniformMatrix4fv(uMVPMatrixHandle, 1, false, mvpMatrix, 0);
        MyRenderer.checkGLError("glUniformMatrix4fv");

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        GLES20.glDisableVertexAttribArray(mPostionHandle);
    }

}
