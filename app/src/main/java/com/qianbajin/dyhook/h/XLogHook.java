package com.qianbajin.dyhook.h;

import android.util.Log;

import com.qianbajin.dyhook.Util;

import java.lang.reflect.Method;

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
public class XLogHook {

    public void hook() {
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

}
