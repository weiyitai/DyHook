package com.qianbajin.dyhook.h;

import android.util.Log;

import com.qianbajin.dyhook.Util;
import com.qianbajin.dyhook.log.Logger;

import de.robv.android.xposed.XC_MethodHook;
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
 * @date at 2020/2/13 0013  12:14
 */
public class KuaiMaoHook {

    private static final String TAG = "mao";

    public void hook() {
        // H:\nixiang\kuaimao\smallvideo_1.1.5\classes7-dex2jar.jar!\com\xxx\svideo\okhttputils\callback\StringCallback.class
        // H:\nixiang\kuaimao\kuaimao_v1.0.2\classes-dex2jar.jar!\com\mylibrary\okhttputils\callback\StringCallback.class  1.0.2
        ClassLoader loader = Util.getClassLoader();
        Class<?> aClass = null;
        try {
            aClass = loader.loadClass("com.xxx.svideo.okhttputils.callback.StringCallback");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            hookDec(loader);
            return;
        }
        try {
            Log.d(TAG, "aClass:" + aClass);
            if (aClass == null) {
                return;
            }
            XposedBridge.hookAllMethods(aClass, "parseNetworkResponse", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Object result = param.getResult();
                    Logger.d(TAG, "result:" + result);
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
            Log.d(TAG, Log.getStackTraceString(e));
        }
    }

    private void hookDec(ClassLoader loader) {
        try {
            Class<?> aClass = loader.loadClass("com.mylibrary.utils.AesEncryptionUtil");
            XposedHelpers.findAndHookMethod(aClass, "decrypt", String.class, String.class, String.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Object result = param.getResult();
                    Logger.d(TAG, "result:" + result);
                }
            });
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.d(TAG, Log.getStackTraceString(e));
        }
    }

    public void hook2() {
        // H:\nixiang\kuaimao\smallvideo_1.1.5\classes8-dex2jar.jar!\com\luck\picture\lib\PictureSelectionModel.class
        try {
            ClassLoader loader = Util.getClassLoader();
            Class<?> aClass = loader.loadClass("com.luck.picture.lib.PictureSelectionModel");
            XposedHelpers.findAndHookMethod(aClass, "videoMaxSecond", int.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                }

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    Object[] args = param.args;
                    Object arg = args[0];
                    Log.d(TAG, "arg:" + arg);
                    param.args[0] = 600;
                }
            });

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
