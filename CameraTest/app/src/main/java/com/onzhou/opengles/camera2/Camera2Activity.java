package com.onzhou.opengles.camera2;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.drawable.GradientDrawable;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.Contacts;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.util.Arrays;

import openapi.ainemo.com.cameratest.R;

public class Camera2Activity extends Activity {

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Handler childHandler;
    private String mCameraID;
    private ImageReader mImageReader;
    private Handler mainHandler;
    private CameraManager mCameraManager;
    private CameraDevice mCameraDevice;
    private ImageView ivShow;
    private CameraCaptureSession cameraCaptureSession;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera2);
        surfaceView = (SurfaceView) findViewById(R.id.gl_surface_view);
        ivShow = (ImageView) findViewById(R.id.iv_show);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                initCamera2();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (null != mCameraDevice) {
                    mCameraDevice.close();
                    mCameraDevice = null;
                }
            }
        });
    }

    private void initCamera2() {
        HandlerThread handlerThread = new HandlerThread("Camera2");
        handlerThread.start();
        childHandler = new Handler(handlerThread.getLooper());
        mainHandler = new Handler(getMainLooper());
        mCameraID = "" + CameraCharacteristics.LENS_FACING_FRONT;
        mImageReader = ImageReader.newInstance(1080, 1920, ImageFormat.JPEG, 1);
        mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
                mCameraDevice.close();
                surfaceView.setVisibility(View.GONE);
                ivShow.setVisibility(View.VISIBLE);
                Image image = reader.acquireLatestImage();
                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                if (bitmap != null) {
                    ivShow.setImageBitmap(bitmap);
                }
            }
        }, mainHandler);

        mCameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);

        try {
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }

            mCameraManager.openCamera(mCameraID, stateCallback, mainHandler);
            String[] cameraIdList = mCameraManager.getCameraIdList();
            Log.i("CameraCharacteristics", "initCamera2: " + cameraIdList.length);
            for (String camereId : cameraIdList) {
                CameraCharacteristics cameraCharacteristics = mCameraManager.getCameraCharacteristics(camereId);
                Integer level = cameraCharacteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
                Log.i("CameraCharacteristics", "initCamera2: " + cameraCharacteristics.toString() + ":level:" + level + "::" + CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY);
            }
        } catch (CameraAccessException e) {

        }
    }

    CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            mCameraDevice = camera;
            tackPreview();
        }

        @Override
        public void onDisconnected(CameraDevice camera) {
            if (null != mCameraDevice) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            Toast.makeText(Camera2Activity.this, "摄像头开启失败", Toast.LENGTH_SHORT).show();
        }
    };

    private void tackPreview() {
        try {
            final CaptureRequest.Builder previewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            previewRequestBuilder.addTarget(surfaceHolder.getSurface());
            mCameraDevice.createCaptureSession(Arrays.asList(surfaceHolder.getSurface(), mImageReader.getSurface()), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    try {
                        if (null == mCameraDevice) {
                            return;
                        }
                        cameraCaptureSession = session;

                        previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                        previewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
                        CaptureRequest captureRequest = previewRequestBuilder.build();

                        cameraCaptureSession.setRepeatingRequest(captureRequest, null, childHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {

                }
            }, childHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void takePicture() {
        if (mCameraDevice == null) return;
        CaptureRequest.Builder captureRequest;
        try {
            captureRequest = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureRequest.addTarget(mImageReader.getSurface());

            captureRequest.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            captureRequest.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            captureRequest.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
            CaptureRequest build = captureRequest.build();
            cameraCaptureSession.capture(build, null, childHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
}
