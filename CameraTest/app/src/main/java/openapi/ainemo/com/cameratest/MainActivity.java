package openapi.ainemo.com.cameratest;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import generateAPPID.APPIDTest;

public class MainActivity extends Activity implements TextureView.SurfaceTextureListener {

    private MyPresentation myPresentation;
    private TextureView mTextureView;
    private boolean isMain = false;
    private LinearLayout linearLayoutContainer;
    private Button switchCamera;
    private String TAG = MainActivity.class.getSimpleName();

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        linearLayoutContainer = (LinearLayout) findViewById(R.id.linear_layout_container);
        DisplayManager displayManager = (DisplayManager) getSystemService(DISPLAY_SERVICE);
        Display[] displays = displayManager.getDisplays();
        if (displays != null && displays.length > 0) {
            Log.i(TAG, "onCreate: " + displays.length + ":0:"+displays[0] );
            myPresentation = new MyPresentation(this, displays[0]);
            myPresentation.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            myPresentation.show();
        }
        switchCamera = ((Button) findViewById(R.id.open_camera));

        switchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isMain) {
                    initTextureView();
                    switchCamera.setText("关闭摄像头");
                } else {
                    myPresentation.requestCamera(0);
                    switchCamera.setText("打开像头");
                    if (mTextureView != null) {
                        linearLayoutContainer.removeView(mTextureView);
                    }
                }
                isMain = !isMain;
            }
        });

        findViewById(R.id.open_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPresentation.stopCamera();
            }
        });

//        APPIDTest appidTest = new APPIDTest();
//        appidTest.generateAppId("com.shgbit.android.nemoplc");
//
//        TelephonyManager telecomManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        String line1Number = telecomManager.getLine1Number();
//        System.out.println(":line1Number:" + line1Number);

    }

    private void initTextureView() {
        Log.i("MainActivity", "initTextureView: " + mTextureView);
        mTextureView = new TextureView(this);
        mTextureView.setSurfaceTextureListener(this);
        linearLayoutContainer.addView(mTextureView);
        myPresentation.requestCamera(0, mTextureView);
    }


    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Log.i("MainActivity", "onSurfaceTextureAvailable: ");
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        Log.i("MainActivity", "onSurfaceTextureDestroyed: ");
        myPresentation.upDateSurfaceView(surface);
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}
