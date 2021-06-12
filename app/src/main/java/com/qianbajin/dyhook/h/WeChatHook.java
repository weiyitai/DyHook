package com.qianbajin.dyhook.h;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import com.qianbajin.dyhook.Util;
import com.qianbajin.dyhook.util.IntentUtil;

import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
/**
 * ----------------------
 * 代码千万行
 * 注释第一行
 * 代码不注释
 * 改bug两行泪
 * -----------------------
 *
 * @author qianbajin
 * @date at 2020/1/11 0011  19:41
 */
public class WeChatHook {

    private static final String TAG = WeChatHook.class.getSimpleName();

    public void hookLog() {
        try {
            // public static native void appenderOpen(int var0, int var1, String var2, String var3, String var4, int var5, String var6);
            Class<?> xLog = Util.getClassLoader().loadClass("com.tencent.mars.xlog.Xlog");
            Log.d("XLogHook", "xLog:" + xLog);
            Method appenderOpen = XposedHelpers.findMethodExact(xLog, "appenderOpen", int.class, int.class, String.class, String.class,
                    String.class, int.class, String.class);
            XposedBridge.hookMethod(appenderOpen, XC_MethodReplacement.DO_NOTHING);
            Method[] declaredMethods = xLog.getDeclaredMethods();
            for (Method method : declaredMethods) {
                String name= method.getName();
                if (name.startsWith("logWrite")) {
                    XposedBridge.hookMethod(method, XC_MethodReplacement.DO_NOTHING);
                }
            }
//            XposedBridge.hookAllMethods(xLog, "logWrite2", XC_MethodReplacement.DO_NOTHING);
//            XposedBridge.hookAllMethods(xLog, "logWrite", XC_MethodReplacement.DO_NOTHING);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void hookWeb() {
        ClassLoader loader = Util.getClassLoader();
//        WebView.class
        try {
            Class<?> WebView = loader.loadClass("com.tencent.smtt.sdk.WebView");
            Log.d(TAG, "WebView:" + WebView);
            Method loadUrl = XposedHelpers.findMethodExact(WebView, "loadUrl", String.class);
            XposedBridge.hookMethod(loadUrl, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Object arg = param.args[0];
                    Log.d(TAG, "loadUrl:" + arg);
                }
            });

            Method setText = XposedHelpers.findMethodExact(TextView.class, "setText", CharSequence.class);
            XposedBridge.hookMethod(setText, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Log.d(TAG, "setText:" + param.args[0]);
                }
            });

            XposedHelpers.findAndHookMethod(Activity.class, "getIntent", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Intent intent = (Intent) param.getResult();
                    IntentUtil.printIntent(intent);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, Log.getStackTraceString(e));
        }

    }

}
