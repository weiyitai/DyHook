package com.qianbajin.dyhook.h;

import android.util.Log;

import com.qianbajin.dyhook.Util;

import java.lang.reflect.Method;
import java.util.List;

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
 * @date 2020/1/3
 */
public class EbusHook {

    private static final String TAG = "EbusHook";

    public void hook() {
        try {
            addInter(Util.getClassLoader());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void addInter(ClassLoader loader) throws ClassNotFoundException {
        Class<?> Interceptor = loader.loadClass("okhttp3.Interceptor");
        Class<?> builder = loader.loadClass("okhttp3.OkHttpClient$Builder");
        Method addInterceptor = XposedHelpers.findMethodExact(builder, "addInterceptor", Interceptor);
//
        XposedBridge.hookMethod(addInterceptor, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
//                    addInterceptor.invoke(param.thisObject, getInterceptor());
//                    List interceptors = (List) XposedHelpers.getObjectField(param.thisObject, "interceptors");
                try {
                    List interceptors = (List) XposedHelpers.getObjectField(param.thisObject, "networkInterceptors");
                    Log.d(TAG, "interceptors:" + interceptors);
                    Object interceptor = Util.getInterceptor();
                    if (interceptor != null) {
                        interceptors.add(interceptor);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
