package com.petcity.pickme.common.utils;

import android.text.TextUtils;

import java.io.File;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @ClassName ImageFileParse
 * @Description ImageFileParse
 * @Author sherily
 * @Date 23/01/21 6:53 PM
 * @Version 1.0
 */
public class ImageFileParse {
    //多图
    public static Map<String, RequestBody> typeTurn(List<String> imgs, Map<String, RequestBody> map, String value) {
        if (null != imgs && !imgs.isEmpty()) {
            for (int i = 0; i < imgs.size(); i++) {
                String filePath = imgs.get(i);
                if (!TextUtils.isEmpty(filePath)) {
                    File file = new File(filePath);
                    String fileName = file.getName();
                    RequestBody photo = RequestBody.create(MediaType.parse("image/jepg"), file);
                    map.put(value + i + "\";filename=\"" + fileName, photo);
                }

            }
        }
        return map;
    }

    public static Map<String, RequestBody> singleTypeTurn(String filePath, Map<String, RequestBody> map, String value) {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            String fileName = file.getName();
            RequestBody photo = RequestBody.create(MediaType.parse("image/jepg"), file);
            map.put(value + "\";filename=\"" + fileName, photo);
        }
        return map;
    }
}
