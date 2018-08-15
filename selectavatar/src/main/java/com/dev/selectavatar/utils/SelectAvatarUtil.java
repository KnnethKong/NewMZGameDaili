package com.dev.selectavatar.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.dev.selectavatar.PreviewActivity;
import com.dev.selectavatar.R;
import com.dev.selectavatar.view.sheet.SheetFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;


/**
 * Created by Administrator on 2017/5/4.
 */

public class SelectAvatarUtil {
    public static final int OpenCamera = 1;//打开相机
    public static final int OpenAlbum = 2;//打开相册
    public static final int OpenPreview = 3;//跳转剪切页面

    /**
     * 弹出选择框
     */
    public static void showSheet(final Context context, final FragmentManager fragmentManager) {

        List<PermissionItem> permissionItems = new ArrayList<>();
        permissionItems.add(new PermissionItem(Manifest.permission.CAMERA, "照相机", R.drawable.permission_ic_camera));
        permissionItems.add(new PermissionItem(Manifest.permission.READ_EXTERNAL_STORAGE, "读取内存卡", R.drawable.permission_ic_storage));
        permissionItems.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "写入内存卡", R.drawable.permission_ic_storage));
        HiPermission.create(context).permissions(permissionItems).checkMutiPermission(new PermissionCallback() {
            @Override
            public void onClose() {
                Toast.makeText(context, "取消了权限授权请求", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                final String[] arr = new String[]{"拍照", "从手机相册选择"};
                SheetFragment.build(fragmentManager)
                        .setItems(arr)
                        .setOnItemClickListener(new SheetFragment.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                if (position == 0) {
                                    Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(SDFileUtil.getPhotoCachePath(), "temp.jpg")));
                                        ((Activity) context).startActivityForResult(openCameraIntent, OpenCamera);
                                    } else {
                                        Uri imageUri = FileProvider.getUriForFile(context, "com.game_camera_photos.fileprovider", new File(SDFileUtil.getPhotoCachePath(), "temp.jpg"));
                                        openCameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                        ((Activity) context).startActivityForResult(openCameraIntent, OpenCamera);
                                    }
                                } else if (position == 1) {
                                    Intent intent = new Intent(Intent.ACTION_PICK, null);
                                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                                    ((Activity) context).startActivityForResult(intent, OpenAlbum);
                                }
                            }
                        }).show();
            }

            @Override
            public void onDeny(String permission, int position) {

            }

            @Override
            public void onGuarantee(String permission, int position) {

            }
        });
    }

    /**
     * 裁剪图片方法实现
     *
     * @param path
     */
    public static void startPhotoZoom(Context context, String path) {
        Intent intent = new Intent(context, PreviewActivity.class);
        intent.putExtra("path", path);
        ((Activity) context).startActivityForResult(intent, OpenPreview);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param picdata
     */
    public static String setPicToView(Intent picdata) {
        String path = picdata.getStringExtra("path");
        return path;
    }

    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}
