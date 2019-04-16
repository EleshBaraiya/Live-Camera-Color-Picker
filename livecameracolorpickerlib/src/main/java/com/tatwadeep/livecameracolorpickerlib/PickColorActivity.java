package com.tatwadeep.livecameracolorpickerlib;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class PickColorActivity extends Activity implements CameraInterface.CamOpenOverCallback, Camera.PreviewCallback {
    private CameraSurfaceView surfaceView = null;
    private ImageView mImageViewCapture;
    private ImageView mImageViewColor;
    private ImageView mImageViewClose;
    private TextView tvColorHEX;
    private float previewRate = -1f;
    private int[] pixels;
    private String color_code = "";
    public static final String COLOR_CODE = "";
    public static final int REQUEST_PIC_COLOR = 101;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (checkAndRequestPermissions()) {
            Thread openThread = new Thread() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    CameraInterface.getInstance().doOpenCamera(PickColorActivity.this);
                }
            };
            openThread.start();
        }
        setContentView(R.layout.activity_pick_color);
        initUI();
        initViewParams();

        mImageViewClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent resultIntent = new Intent();
                resultIntent.putExtra(COLOR_CODE, "#" + color_code);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    private void initUI() {
        surfaceView = (CameraSurfaceView) findViewById(R.id.camera_surfaceview);
        mImageViewCapture = (ImageView) findViewById(R.id.a_camera_iv_capture);
        mImageViewColor = (ImageView) findViewById(R.id.a_camera_iv_color);
        mImageViewClose = (ImageView) findViewById(R.id.a_camera_iv_close);
        tvColorHEX = (TextView) findViewById(R.id.tv_color_HEX);
        mImageViewCapture.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraInterface.getInstance().doTakePicture();
            }
        });
    }

    private void initViewParams() {
        LayoutParams params = surfaceView.getLayoutParams();
        Point p = DisplayUtil.getScreenMetrics(this);
        params.width = p.x;
        params.height = p.y;
        previewRate = DisplayUtil.getScreenRate(this);
        surfaceView.setLayoutParams(params);
        pixels = new int[params.width * params.height];
    }

    @Override
    public void cameraHasOpened() {
        // TODO Auto-generated method stub
        SurfaceHolder holder = surfaceView.getSurfaceHolder();
        CameraInterface.getInstance().setPreviewCallback(this);
        CameraInterface.getInstance().doStartPreview(holder, previewRate);
    }

    /**
     * Method call will check camera and storage permission are granted or not
     *
     * @return true or false
     */
    private boolean checkAndRequestPermissions() {
        int camerapermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int writepermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (camerapermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (writepermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @android.support.annotation.NonNull String[] permissions, @android.support.annotation.NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Thread openThread = new Thread() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        CameraInterface.getInstance().doOpenCamera(PickColorActivity.this);
                    }
                };
                openThread.start();

            }

        }
    }


    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (pixels == null) return;
        try {
            Point p = DisplayUtil.getScreenMetrics(this);
            decodeYUV420SP(pixels, data, p.x, p.y);
            color_code = Integer.toHexString(pixels[pixels.length / 2 + p.y / 2]);
            tvColorHEX.setText(color_code);
            mImageViewColor.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#" + color_code), PorterDuff.Mode.SRC_ATOP));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void decodeYUV420SP(int[] rgb, byte[] yuv420sp, int width, int height) {
        final int frameSize = width * height;
        try {
            for (int j = 0, yp = 0; j < height; j++) {
                int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
                for (int i = 0; i < width; i++, yp++) {
                    int y = (0xff & ((int) yuv420sp[yp])) - 16;
                    if (y < 0)
                        y = 0;
                    if ((i & 1) == 0) {

                        try {
                            v = (0xff & yuv420sp[uvp++]) - 128;
                            u = (0xff & yuv420sp[uvp++]) - 128;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    int y1192 = 1192 * y;
                    int r = (y1192 + 1634 * v);
                    int g = (y1192 - 833 * v - 400 * u);
                    int b = (y1192 + 2066 * u);

                    if (r < 0)
                        r = 0;
                    else if (r > 262143)
                        r = 262143;
                    if (g < 0)
                        g = 0;
                    else if (g > 262143)
                        g = 262143;
                    if (b < 0)
                        b = 0;
                    else if (b > 262143)
                        b = 262143;

                    rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000)
                            | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}