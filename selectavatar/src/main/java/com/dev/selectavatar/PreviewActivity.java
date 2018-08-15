package com.dev.selectavatar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.dev.selectavatar.utils.BitmapUtil;
import com.dev.selectavatar.utils.SDFileUtil;
import com.dev.selectavatar.utils.compress.CompressHelper;
import com.dev.selectavatar.view.ClipImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class PreviewActivity extends Activity {

    private ClipImageView civDisplay;
    private Button btnCancel, btnSave, btnLeftRotate, btnRightRotate;
    private Bitmap bitmap = null;
    private int degrees = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_preview);

        civDisplay = (ClipImageView) findViewById(R.id.civ_display);
        btnLeftRotate = (Button) findViewById(R.id.btn_left_rotate);
        btnRightRotate = (Button) findViewById(R.id.btn_right_rotate);

        initBitmap();

        btnLeftRotate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                degrees -= 90;
                civDisplay.setImageBitmap(setBitmapRotate(bitmap, degrees));
            }
        });
        btnRightRotate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                degrees += 90;
                civDisplay.setImageBitmap(setBitmapRotate(bitmap, degrees));
            }
        });

        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SDFileUtil.delFile(SDFileUtil.getPhotoCachePath() + "temp.jpg");
                finish();
            }
        });

        btnSave = (Button) findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 此处获取剪裁后的bitmap
                degrees = 0;
                Bitmap bitmap = civDisplay.clip();
                String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".jpg";//直接以当前时间戳作为文件名
                if (bitmap != null) {
                    BitmapUtil.saveToFile(bitmap, new File(SDFileUtil.getPhotoCachePath()));
                    Log.e("本地图片保存", SDFileUtil.getPhotoCachePath() + fileName);
                }
                Intent intent = new Intent();
                intent.putExtra("path", SDFileUtil.getPhotoCachePath() + fileName);
                setResult(10001, intent);

                try {
                    // 释放图片资源
                    if (bitmap != null && !bitmap.isRecycled()) {
                        bitmap.recycle();
                        bitmap = null;
                    }
                    SDFileUtil.delFile(SDFileUtil.getPhotoCachePath() + "temp.jpg");
                } catch (Exception e) {
                    // TODO: handle exception
                }
                // 结束当前这个Activity对象的生命
                finish();
            }
        });
    }

    // ////////////////////////////////////////////////////////////////////////////////

    /**
     * 初始化图片资源
     */
    @SuppressWarnings("deprecation")
    private void initBitmap() {
        try {
            String path = getIntent().getStringExtra("path");
            if (TextUtils.isEmpty(path)) {
                path = SDFileUtil.getPhotoCachePath() + "temp.jpg";
            }
            File file = new File(path);
            if (file.exists()) {
                bitmap = CompressHelper.getDefault(this).compressToBitmap(file);
                civDisplay.setImageBitmap(bitmap);
            } else {
                finish();
            }
        } catch (Exception e) {
            // TODO: handle exception
            // showToast("图片太大剪切失败，请重试");
            finish();
        }
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    private int getBitmapDegree(String path) {
        int degree = 0;
        try {
            File file = new File(path);
            if (file.exists()) {
                // 从指定路径下读取图片，并获取其EXIF信息
                ExifInterface exifInterface = new ExifInterface(path);
                // 获取图片的旋转信息
                int orientation = exifInterface.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL);
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("旋转角度", "" + degree);
        return degree;
    }

    /**
     * 图片旋转
     */
    private Bitmap setBitmapRotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.setRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, false);
    }
    // ////////////////////////////////////////////////////////////////////////////////
}
