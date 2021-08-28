package com.qianbajin.dyhook.h;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.qianbajin.dyhook.Util;

import java.lang.reflect.Method;
import java.util.Arrays;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
/**
 * @author qianbajin
 * @date at 2021/8/28 0028  10:09
 */
public class HookPiPiXia {

    private static final String TAG = HookPiPiXia.class.getSimpleName();

    public void hook() {
        try {
            Method setFlags = XposedHelpers.findMethodExact(Window.class, "setFlags", int.class, int.class);
            Class<?> StatusBarHelper = Util.getClassLoader().loadClass("com.sup.android.utils.StatusBarHelper");
//            Class<?> WindowTintManager = Util.getClassLoader().loadClass("com.bytedance.ies.uikit.statusbar.WindowTintManager");
//            Method setStatusBarTintEnabled = XposedHelpers.findMethodExact(WindowTintManager, "setStatusBarTintEnabled", boolean.class);
            Method changeStatusBarTopMargin = XposedHelpers.findMethodExact(StatusBarHelper, "changeStatusBarTopMargin", boolean.class);
//            Method changeSlideBarTopMargin = XposedHelpers.findMethodExact(StatusBarHelper, "changeSlideBarTopMargin", boolean.class);
            Method modifySlideStatusBar = XposedHelpers.findMethodExact(StatusBarHelper, "modifySlideStatusBar", boolean.class);
            XposedBridge.hookMethod(setFlags, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    Object[] args = param.args;
                    Log.d(TAG, "setFlags param.thisObject:" + param.thisObject + " args:" + Arrays.toString(args));
                    if (((int) args[0]) == 1024) {
//                        try {
//                            throw new NullPointerException();
//                        } catch (NullPointerException e) {
//                            Log.d(TAG, "setFlags:" + Log.getStackTraceString(e));
//                        }
//                        args[0] = 0;
                    }
                }
            });

            Log.d(TAG, "changeStatusBarTopMargin:" + changeStatusBarTopMargin);
            Method modifyStatusBar = XposedHelpers.findMethodExact(StatusBarHelper, "modifyStatusBar", boolean.class);
            XposedBridge.hookMethod(modifyStatusBar, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    Object arg0 = param.args[0];
                    Log.d(TAG, "modifyStatusBar:" + param.thisObject + " " + arg0);
                    if (((boolean) arg0)) {
                        param.setResult(null);
                        changeStatusBarTopMargin.invoke(param.thisObject, false);
//                        Object mTintManager = XposedHelpers.getObjectField(param.thisObject, "mTintManager");
//                        setStatusBarTintEnabled.invoke(mTintManager, false);
//                        changeSlideBarTopMargin.invoke(param.thisObject, false);
                        Activity activity = (Activity) XposedHelpers.getObjectField(param.thisObject, "activity");
                        setColor(activity.getWindow(), Color.parseColor("#BAB3C1"));
//                        if (activity != null) {
//                            Window window = activity.getWindow();
////                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
////                            window.setStatusBarColor(Color.parseColor("#BFBBB7"));
//                            View decorView = activity.getWindow().getDecorView();
////                            Log.d(TAG, "decorView:" + decorView);
////                            Object rootView = XposedHelpers.getObjectField(param.thisObject, "rootView");
////                            Log.d(TAG, "rootView:" + rootView);
////                            if (decorView != null) {
////                                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
////                            }
//                        }
                    }
                }
            });
            XposedBridge.hookMethod(modifySlideStatusBar, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    Log.d(TAG, "param.args[0]:" + param.args[0]);
                    Activity activity = (Activity) XposedHelpers.getObjectField(param.thisObject, "activity");
                    if (activity != null) {
                        setColor(activity.getWindow(), Color.TRANSPARENT);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setColor(Window window, int color) {
        if (window != null) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }
}
