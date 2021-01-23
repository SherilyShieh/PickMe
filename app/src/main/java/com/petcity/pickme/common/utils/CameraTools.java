package com.petcity.pickme.common.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.petcity.pickme.BuildConfig;
import com.petcity.pickme.base.ActivityScope;
import com.petcity.pickme.base.BaseActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

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

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public CameraTools(Context mContext){
        this.mContext = mContext;
    }

    public void takePhoto(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(mContext.getPackageManager()) != null) {
            // 设置系统相机拍照后的输出路径
            // 创建临时文件
            tempFile = createTempFile(filePath);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
            if (mContext instanceof BaseActivity)
                ((BaseActivity) mContext).startActivityForResult(cameraIntent, CODE_TAKE_PHOTO,null);
        } else {
            Toast.makeText(mContext.getApplicationContext(), "没有可用的相机", Toast.LENGTH_SHORT).show();
        }
    }

    public File createTempFile(String filePath) {
        String timeStamp = new SimpleDateFormat("MMddHHmmss", Locale.CHINA).format(new Date());
        String externalStorageState = Environment.getExternalStorageState();
        File dir = new File(Environment.getExternalStorageDirectory() + filePath);
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
        Uri uri = Uri.fromFile(tempFile);
        if (BuildConfig.DEBUG)
            Log.i(TAG, "crop: "+uri.toString());
        intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        if (mContext instanceof BaseActivity)
            ((BaseActivity) mContext).startActivityForResult(intent, CODE_CUT,null);
    }

    public File getTempFile() {
        return tempFile;
    }

    public void setTempFile(File tempFile) {
        this.tempFile = tempFile;
    }
}
