package openapi.ainemo.com.cameratest.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyGLSurfaceView extends GLSurfaceView {

    private MyRenderer myRenderer;
    private float mPreviousX;
    private float mPreviousY;
    private final float TOUCH_SCARE_FACTOR = 180.0f / 320;

    public MyGLSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        myRenderer = new MyRenderer();
        setRenderer(myRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
        myRenderer = new MyRenderer();
        setRenderer(myRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dx = x - mPreviousX;
                float dy = y - mPreviousY;
                if (y > getHeight() / 2) {
                    dx = dx * -1;
                }
                if (dx < getWidth() / 2) {
                    dy = dy * -1;
                }

                myRenderer.setmAngle(myRenderer.getmAngle() + (dx + dy) * TOUCH_SCARE_FACTOR);
                requestRender();
                break;

        }
        mPreviousX = x;
        mPreviousY = y;
        return true;
    }


}
