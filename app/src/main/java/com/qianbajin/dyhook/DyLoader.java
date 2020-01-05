package com.qianbajin.dyhook;

import android.util.Log;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;

import dalvik.system.PathClassLoader;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
/**
 * ----------------------
 * 代码千万行
 * 注释第一行
 * 代码不注释
 * 改bug两行泪
 * -----------------------
 *
 * @author weiyitai
 * @date 2019/12/31
 */
public class DyLoader implements IXposedHookLoadPackage {

    private static final String TAG = "DyLoader";
    /**
     * 实际处理hook逻辑的类
     */
    private static final String HANDLE_HOOK_CLASS = Hook.class.getName();

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        try {
            XSharedPreferences preferences = new XSharedPreferences(BuildConfig.APPLICATION_ID, Constant.HOOK);
            Set<String> stringSet = preferences.getStringSet(SpKey.APK_LIST, Collections.emptySet());
            String packageName = lpparam.packageName;
            boolean b;
            if (b = (stringSet.contains(packageName))) {
                String apkPath = preferences.getString(Constant.APK_PATH, "");
                Log.d(TAG, "apkPath:" + apkPath);
                PathClassLoader pathClassLoader = new PathClassLoader(apkPath, XposedBridge.BOOTCLASSLOADER);
                Class<?> handleHookClass = pathClassLoader.loadClass(HANDLE_HOOK_CLASS);
                Log.d(TAG, "handleHookClass:" + handleHookClass + " " + pathClassLoader);
                Object instance = handleHookClass.newInstance();
                Method handleLoadPackage = handleHookClass.getDeclaredMethod("handleLoadPackage", XC_LoadPackage.LoadPackageParam.class);
                handleLoadPackage.invoke(instance, lpparam);
            }
            Log.d(TAG, "contains:" + b + " packageName:" + packageName + " stringSet:" + stringSet);
        } catch (Throwable e) {
            Log.d(TAG, Log.getStackTraceString(e));

        }
    }

}
