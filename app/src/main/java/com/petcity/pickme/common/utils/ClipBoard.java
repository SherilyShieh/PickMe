package com.petcity.pickme.common.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * @ClassName ClipBoard
 * @Description ClipBoard
 * @Author sherily
 * @Date 22/01/21 7:08 PM
 * @Version 1.0
 */
public class ClipBoard {

    public static void copyClipboard(Activity activity, String content) {
        ClipboardManager myClipboard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
        ClipData primaryClip = ClipData.newPlainText("text", content);
        assert myClipboard != null;
        myClipboard.setPrimaryClip(primaryClip);

    }

}
