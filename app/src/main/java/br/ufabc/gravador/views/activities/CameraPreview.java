package br.ufabc.gravador.views.activities;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

//TODO Delete?

/**
 * A basic Camera preview class
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;

    public CameraPreview ( Context context, Camera camera ) {
        super(context);
        mCamera = camera;

        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public static void setCameraDisplayOrientation ( Activity activity, int cameraId, Camera camera ) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch ( rotation ) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if ( info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT ) {
            result = ( info.orientation + degrees ) % 360;
            result = ( 360 - result ) % 360;  // compensate the mirror
        } else {  // back-facing
            result = ( info.orientation - degrees + 360 ) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    public void surfaceCreated ( SurfaceHolder holder ) {
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch ( IOException e ) {
            Log.d("CameraPreview", "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed ( SurfaceHolder holder ) {
        // empty. Take care of releasing the Camera preview in your activity.
    }

    public void surfaceChanged ( SurfaceHolder holder, int format, int w, int h ) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if ( mHolder.getSurface() == null ) {
            return;
        }

        try {
            mCamera.stopPreview();
        } catch ( Exception e ) {
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        //mCamera.setDisplayOrientation();


        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch ( Exception e ) {
            Log.d("CameraPreview", "Error starting camera preview: " + e.getMessage());
        }
    }
}