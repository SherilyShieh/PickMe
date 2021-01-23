package com.petcity.pickme.common.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.petcity.pickme.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @ClassName StatusBarUtils
 * @Description StatusBarUtils
 * @Author sherily
 * @Date 7/01/21 6:50 PM
 * @Version 1.0
 */
public class StatusBarUtils {

    private static final int FAKE_STATUS_BAR_VIEW_ID = R.id.fake_status_bar_view;

    /**
     * get the height oif StatusBar
     *
     * @param context
     * @return height
     */
    public static int getHeight(Context context) {
        int statusBarHeight = 0;
        try {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                    "android");
            if (resourceId > 0) {
                statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * set the StatusBar's background color
     *
     * @param context it's better to use the context of Activity
     * @param color   target color
     */
    public static void setColor(Context context, int color) {
        if (context instanceof Activity) {
            setColor(((Activity) context).getWindow(), color);
        }
    }

    /**
     * set the StatusBar's background color
     *
     * @param window use for Activity or Dialog
     * @param color  target color
     */
    public static void setColor(Window window, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setStatusBarColor(color);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setColor(window, color, false);
        }
    }

    /**
     * set the StatusBar's background color for Android Version <= 5.0
     *
     * @param window
     * @param color
     * @param isTransparent
     */
    public static void setColor(Window window, int color,
                                boolean isTransparent) {
        Context context = window.getContext();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        ViewGroup decorView = (ViewGroup) window.getDecorView();
        View contentView = decorView.findViewById(android.R.id.content);
        if (contentView != null) {
            contentView.setPadding(0, isTransparent ? 0 : getHeight(context), 0, 0);
        }
        View fakeStatusBarView = decorView.findViewById(FAKE_STATUS_BAR_VIEW_ID);
        if (fakeStatusBarView != null) {
            fakeStatusBarView.setBackgroundColor(color);
            if (fakeStatusBarView.getVisibility() == View.GONE) {
                fakeStatusBarView.setVisibility(View.VISIBLE);
            }
        } else {
            // 绘制一个和状态栏一样高的矩形
            View statusBarView = new View(context);
            FrameLayout.LayoutParams layoutParams =
                    new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            getHeight(context));
            statusBarView.setLayoutParams(layoutParams);
            statusBarView.setBackgroundColor(color);
            statusBarView.setId(FAKE_STATUS_BAR_VIEW_ID);
            decorView.addView(statusBarView);
        }
    }

    /**
     * set the StatusBar as transparent
     *
     * @param context it's better to use the context of Activity
     */
    public static void setTransparent(Context context) {
        if (context instanceof Activity) {
            setTransparent(((Activity) context).getWindow());
        }
    }

    /**
     * 设置状态栏透明
     *
     * @param window 窗口，可用于Activity和Dialog等
     */
    public static void setTransparent(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setColor(window, 0x80000000, true);
        }
    }

    /**
     * 设置状态栏是否为黑色文字
     *
     * @param context 上下文，尽量使用Activity
     * @param isDark  是否为黑色文字
     */
    public static void setTextDark(Context context, boolean isDark) {
        if (context instanceof Activity) {
            setTextDark(((Activity) context).getWindow(), isDark);
        }
    }

    /**
     * 设置状态栏是否为黑色文字
     *
     * @param window 窗口，可用于Activity和全屏Dialog
     * @param isDark 是否为黑色文字
     */
    private static void setTextDark(Window window, boolean isDark) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            View decorView = window.getDecorView();
            int systemUiVisibility = decorView.getSystemUiVisibility();
            if (isDark) {
                decorView.setSystemUiVisibility(systemUiVisibility | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                decorView.setSystemUiVisibility(systemUiVisibility & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            switch (OSUtils.getRomType()) {
                case MIUI:
                    setMIUIDark(window, isDark);
                    break;
                case Flyme:
                    setFlymeDark(window, isDark);
                    break;
                default:
            }
        }
    }

    /**
     * 设置MIUI系统状态栏是否为黑色文字
     *
     * @param window 窗口，仅可用于Activity
     * @param isDark 是否为黑色文字
     */
    private static void setMIUIDark(Window window, boolean isDark) {
        try {
            Class<? extends Window> clazz = window.getClass();
            int darkModeFlag;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(window, isDark ? darkModeFlag : 0, darkModeFlag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置Flyme系统状态栏是否为黑色文字
     *
     * @param window 窗口
     * @param isDark 是否为黑色文字
     */
    private static void setFlymeDark(Window window, boolean isDark) {
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (isDark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
