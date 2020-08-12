package com.arcore.arcore_test.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.arcore.arcore_test.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

public class ConfigBrushActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private CameraBridgeViewBase cameraBridgeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_brush);

        initCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (cameraBridgeView != null)
            cameraBridgeView.disableView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Toast.makeText(this, "NOT OK", Toast.LENGTH_SHORT).show();
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, loaderCallback);
        } else {
            Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();
            loaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        cameraBridgeView.enableView();
    }

    public void onDestroy() {
        super.onDestroy();
        if (cameraBridgeView != null)
            cameraBridgeView.disableView();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
    }

    @Override
    public void onCameraViewStopped() {
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        //TODO do some magic staff here

        return inputFrame.rgba();
    }

    private void initCamera() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        cameraBridgeView = new JavaCameraView(this, -1);
        cameraBridgeView =  (JavaCameraView) findViewById(R.id.jcvCamera);

        cameraBridgeView.setCvCameraViewListener(this);
    }

    private BaseLoaderCallback loaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            if (status == LoaderCallbackInterface.SUCCESS) {
                Toast.makeText(getBaseContext(), "OpenCV loaded successfully", Toast.LENGTH_SHORT).show();
                System.loadLibrary("opencv_java4");

                cameraBridgeView.enableView();
            } else {
                super.onManagerConnected(status);
            }
        }
    };

}