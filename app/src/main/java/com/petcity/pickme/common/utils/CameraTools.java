package com.petcity.pickme.common.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.petcity.pickme.BuildConfig;
import com.petcity.pickme.base.BaseActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @ClassName CameraTools
 * @Description TODO
 * @Author sherily
 * @Date 23/01/21 3:45 PM
 * @Version 1.0
 */
public class CameraTools {
    private static final String TAG = "CameraTools";
    public static final int CODE_TAKE_PHOTO = 0x00;
    public static final int CODE_CUT = 0x01;
    private Context mContext;
    private String filePath = BuildConfig.PICTURE_PATH;
    private File tempFile;

    public CameraTools(Context mContext) {
        this.mContext = mContext;
    }

    public void takePhoto() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(mContext.getPackageManager()) != null) {
            tempFile = createTempFile(filePath);
            Uri uri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                String authority = mContext.getPackageName() + ".fileprovider"; //【清单文件中provider的authorities属性的值】
                uri = FileProvider.getUriForFile(mContext, authority, tempFile);
            } else {
                uri = Uri.fromFile(tempFile);
            }
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            if (mContext instanceof BaseActivity)
                ((BaseActivity) mContext).startActivityForResult(cameraIntent, CODE_TAKE_PHOTO);
        } else {
            Toast.makeText(mContext.getApplicationContext(), "No Camera!", Toast.LENGTH_SHORT).show();
        }
    }

    public File createTempFile(String filePath) {
        String timeStamp = new SimpleDateFormat("MMddHHmmss", Locale.CHINA).format(new Date());
        File externalStorageState = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? mContext.getFilesDir() : Environment.getExternalStorageDirectory();
        File dir = new File(externalStorageState + filePath);
        if (externalStorageState.equals(Environment.MEDIA_MOUNTED)) {
            if (!dir.exists()) {
                dir.mkdirs();
            }
            return new File(dir, timeStamp + ".jpg");
        } else {
            File cacheDir = mContext.getCacheDir();
            return new File(cacheDir, timeStamp + ".jpg");
        }

    }

    public void crop(String imagePath, int aspectX, int aspectY, int outputX, int outputY) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(Uri.fromFile(new File(imagePath)), "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("return-data", false);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String authority = mContext.getPackageName() + ".fileprovider";
            uri = FileProvider.getUriForFile(mContext, authority, tempFile);
        } else {
            uri = Uri.fromFile(tempFile);
        }
        if (BuildConfig.DEBUG)
            Log.i(TAG, "crop: " + uri.toString());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        if (mContext instanceof BaseActivity)
            ((BaseActivity) mContext).startActivityForResult(intent, CODE_CUT, null);
    }

    public File getTempFile() {
        return tempFile;
    }

    public void setTempFile(File tempFile) {
        this.tempFile = tempFile;
    }
}
