package com.qianbajin.dyhook.h;

import android.util.Log;

import com.qianbajin.dyhook.Util;

import java.lang.reflect.Method;

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
 * @author weiyitai
 * @date 2020/1/2
 */
public class NetHook {

    private static final String TAG = "NetHook";

    public void hook() {
        try {
            ClassLoader loader = Util.getClassLoader();
            Class<?> aClass = null;
            aClass = loader.loadClass("com.qianba.nettest.MainActivity");
            Method toa = XposedHelpers.findMethodExact(aClass, "toa", String.class);
            XposedBridge.hookMethod(toa, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    param.args[0] = "修改了";
                }
            });

            Method add = XposedHelpers.findMethodExact(aClass, "add", int.class, int.class);
            XposedBridge.hookMethod(add, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Object result = param.getResult();
                    Log.d(TAG, "result:" + result);
                    param.setResult(15);
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
            Log.d(TAG, Log.getStackTraceString(e));
        }
    }

}
