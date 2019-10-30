package openapi.ainemo.com.cameratest;

import android.app.Presentation;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES20;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.TextureView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;

class MyPresentation extends Presentation implements TextureView.SurfaceTextureListener {

    private Camera mCamera;
    private TextureView mTextureView;
    private TextView mSwitchCamera;
    private boolean isFont = true;
    private SurfaceTexture mSurfaceTexture = null;
    private int[] mTextures;
    private Context mContext;
    private LinearLayout layoutContainer;

    public MyPresentation(Context outerContext, Display display) {
        super(outerContext, display);
        mContext = outerContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        layoutContainer = (LinearLayout) findViewById(R.id.linear_layout_container);

        mTextureView = findViewById(R.id.textureView);
        mSwitchCamera = (TextView) findViewById(R.id.switchCamera);
        mTextureView.setSurfaceTextureListener(this);
        requestCamera(0, mTextureView);

        mSwitchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFont) {
                    stopCamera();
                    requestCamera(1, mTextureView);
                } else {
                    stopCamera();
                    requestCamera(0, mTextureView);
                }
                isFont = !isFont;
            }
        });

    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Log.i("presenstaion", "onSurfaceTextureAvailable: ");
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        Log.i("presenstaion", "onSurfaceTextureDestroyed " + outTextureView + ", " + mSurfaceTexture);
        if (outTextureView != null && mSurfaceTexture != null) {
            SurfaceTexture surfaceTexture = outTextureView.getSurfaceTexture();
            if (surfaceTexture == null) {
                outTextureView.setSurfaceTexture(mSurfaceTexture);
            }
        }
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    public void upDateSurfaceView(SurfaceTexture surfaceTexture) {
        Log.i("presenstaion", "upDateSurfaceView: " + mTextureView);
        if (mTextureView != null && surfaceTexture != null) {
            SurfaceTexture surfaceTexture1 = mTextureView.getSurfaceTexture();
            if (surfaceTexture1 == null) {
                mTextureView.setSurfaceTexture(surfaceTexture);
            }
        }

    }

    public void stopCamera() {
        mCamera.stopPreview();
        mCamera.release();
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        mSurfaceTexture.release();
        GLES20.glDeleteTextures(1, mTextures, 0);
    }

    public void requestCamera(int direction) {
        Log.i("presenstaion", "requestCamera 1: " + mSurfaceTexture);
        try {
            if (mSurfaceTexture == null) {
                mTextures = new int[1];
                GLES20.glGenTextures(1, mTextures, 0);
                GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);

                mSurfaceTexture = new SurfaceTexture(mTextures[0]);
                mSurfaceTexture.detachFromGLContext();
                mTextureView.setSurfaceTexture(mSurfaceTexture);

                mCamera = Camera.open(direction);

                mCamera.setPreviewTexture(mSurfaceTexture);

                mCamera.startPreview();
            } else {
                mTextureView = new TextureView(mContext);
                mTextureView.setSurfaceTextureListener(this);
                Log.i("presenstaion else", "textureView 1: " + mTextureView);
                layoutContainer.addView(mTextureView);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private TextureView outTextureView;

    public void requestCamera(int direction, TextureView textureView) {
        Log.i("presenstaion", "requestCamera  2: " + mSurfaceTexture);
        try {
            if (mSurfaceTexture == null) {
                mTextures = new int[1];
                GLES20.glGenTextures(1, mTextures, 0);
                GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);

                mSurfaceTexture = new SurfaceTexture(mTextures[0]);
                mSurfaceTexture.detachFromGLContext();
                textureView.setSurfaceTexture(mSurfaceTexture);

                mCamera = Camera.open(direction);

                mCamera.setPreviewTexture(mSurfaceTexture);

                mCamera.startPreview();
            } else {
                outTextureView = textureView;
                Log.i("presenstaion", "requestCamera 2 else: " + outTextureView);
                if (mTextureView != null) {
                    layoutContainer.removeView(mTextureView);
                    mTextureView = null;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}