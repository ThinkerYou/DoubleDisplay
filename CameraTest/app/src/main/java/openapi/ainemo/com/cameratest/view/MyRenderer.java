package openapi.ainemo.com.cameratest.view;

import android.opengl.GLES20;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyRenderer implements GLSurfaceView.Renderer {

    private static String TAG = MyRenderer.class.getSimpleName();
    private Square mSquare;
    private Triangle mTriangle;
    private final float[] mViewMatrix = new float[16];
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mRotateMatrix = new float[16];
    private float mAngle;


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(255.0f, 255.0f, 255.0f, 1.0f);
        mSquare = new Square();
        mTriangle = new Triangle();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0,0,width,height);
        float ratio = (float) width / height;
        Matrix.frustumM(mProjectionMatrix,0,-ratio,ratio,-1,1,3,7);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        float[] scratch = new float[16];
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 10f, 0f);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        mSquare.draw(mMVPMatrix);
        Matrix.setRotateM(mRotateMatrix, 0, mAngle, 0, 0, 1.0f);
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotateMatrix, 0);

        mTriangle.draw(scratch);

    }

    public static int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    public static void checkGLError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, "checkGLError: " + error);
            throw new RuntimeException(glOperation + ":glError" + error);
        }

    }

    public float getmAngle() {
        return mAngle;
    }

    public void setmAngle(float mAngle) {
        this.mAngle = mAngle;
    }
}
