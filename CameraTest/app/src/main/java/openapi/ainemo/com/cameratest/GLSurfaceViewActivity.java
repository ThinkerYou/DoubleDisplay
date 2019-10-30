package openapi.ainemo.com.cameratest;

import android.app.Activity;
import android.os.Bundle;

import openapi.ainemo.com.cameratest.view.MyGLSurfaceView;

public class GLSurfaceViewActivity extends Activity {

    private MyGLSurfaceView surfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glsurface_view);
        surfaceView = (MyGLSurfaceView) findViewById(R.id.gl_surface_view);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
