package com.qianbajin.dyhook;

import android.app.AndroidAppHelper;
import android.content.Context;
import android.util.Log;

import java.io.File;

import dalvik.system.DexClassLoader;
import de.robv.android.xposed.XSharedPreferences;

import static android.content.ContentValues.TAG;
/**
 * ----------------------
 * 代码千万行
 * 注释第一行
 * 代码不注释
 * 改bug两行泪
 * -----------------------
 *
 * @author  weiyitai
 * @date 2020/1/3
 */
public class Util {

    private static Object sInterceptor;
    private static ClassLoader sClassLoader;

    public static ClassLoader getClassLoader() {
        return sClassLoader;
    }

    public static void setClassLoader(ClassLoader classLoader) {
        sClassLoader = classLoader;
    }

    public static Object getInterceptor() {
        if (sInterceptor != null) {
            return sInterceptor;
        }
        try {
            XSharedPreferences preferences = new XSharedPreferences(BuildConfig.APPLICATION_ID, Constant.HOOK);
            Context context = AndroidAppHelper.currentApplication().getApplicationContext();
            File dex = context.getDir("dex", 0);
            String apk = preferences.getString(Constant.APK_PATH, "");
            Log.d(TAG, "dex:" + dex + " apk:" + apk);
            DexClassLoader dexClassLoader = new DexClassLoader(apk, dex.getAbsolutePath(), null, getClassLoader());
            Class<?> loginterceptor = dexClassLoader.loadClass("com.qianbajin.dyhook.log.DyInterceptor");
            sInterceptor = loginterceptor.getDeclaredConstructor().newInstance();
            Log.d(TAG, "instance:" + sInterceptor);
            return sInterceptor;
        } catch (Throwable e) {
            e.printStackTrace();
            Log.d(TAG, Log.getStackTraceString(e));
        }
        return null;
    }

}
