package com.qianbajin.dyhook.h;

import android.util.Log;

import com.qianbajin.dyhook.Util;

import java.lang.reflect.Method;
import java.util.Map;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
/**
 * @author qianbajin
 * @date at 2021/5/22 0022  11:26
 */
public class HookWeLink {

    private static final String TAG = HookWeLink.class.getSimpleName();

    public void hook() {
        try {
            Class<?> aClass = Util.getClassLoader().loadClass("com.huawei.it.w3m.core.h5.ui.browser.SafeWebViewDelegate");
            Method loadUrl = XposedHelpers.findMethodExact(aClass, "loadUrl", String.class, Map.class);
            XposedBridge.hookMethod(loadUrl, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Object arg0 = param.args[0];
                    Log.d(TAG, "arg0:" + arg0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
