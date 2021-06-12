package com.qianbajin.dyhook.h;

import android.app.AndroidAppHelper;
import android.content.Context;
import android.text.SpannableString;
import android.util.Log;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.qianbajin.dyhook.Util;
import com.qianbajin.dyhook.log.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
/**
 * @author qianbajin
 * @date at 2021/4/18 0018  23:24
 */
public class HookContact {

    private static final String TAG = HookContact.class.getSimpleName();

    public void hook() {
        try {
            Class<?> PhoneNumber = Util.getClassLoader().loadClass("miui.telephony.PhoneNumberUtils$PhoneNumber");
            Log.d(TAG, "PhoneNumber.getPackage():" + PhoneNumber.getPackage() + "\n" + PhoneNumber.getPackage().getName());
            Class<?> aClass = Util.getClassLoader().loadClass("miui.telephony.PhoneNumberUtilsCompat$PhoneNumberCompat");
            Method getLocation = XposedHelpers.findMethodExact(aClass, "getLocation", Context.class);
            Method getLocation2 = XposedHelpers.findMethodExact(aClass, "getLocation", Context.class, CharSequence.class);
            Method parse = XposedHelpers.findMethodExact(aClass, "parse", CharSequence.class, boolean.class, String.class);
            XposedBridge.hookMethod(getLocation, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Log.d("HookContact", "getLocation param.args:" + Arrays.toString(param.args));
                    Log.d("HookContact", "getLocation param.getResult():" + param.getResult());
                }
            });
            XposedBridge.hookMethod(parse, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Log.d("HookContact", "parse param.args:" + Arrays.toString(param.args));
                    Log.d("HookContact", "parse param.getResult():" + JSON.toJSON(param.getResult()));
                    Field[] declaredFields = param.getResult().getClass().getDeclaredFields();
                    Method[] declaredMethods = param.getResult().getClass().getDeclaredMethods();
                    Log.d(TAG, "declaredMethods:" + Arrays.toString(declaredMethods));
                    Log.d(TAG, "declaredFields:" + Arrays.toString(declaredFields));
                    if ("136 6226".equals(param.args[0])) {
                        Object invoke = getLocation2.invoke(null, AndroidAppHelper.currentApplication(), "136 64589999");
                        Log.d(TAG, "getLocation2 invoke:" + invoke);
                    }
                }
            });
            XposedHelpers.findAndHookMethod(TextView.class, "setText", CharSequence.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Object arg0 = param.args[0];
                    if (arg0 instanceof String || arg0 instanceof SpannableString) {
                        String arg = ((CharSequence) arg0).toString();
                        Log.d("HookContact", "setText:" + arg);
                        if (arg.contains("广东深圳")) {
                            TextView textView = (TextView) param.thisObject;
                            int id = textView.getId();
                            String resourceEntryName = textView.getResources().getResourceEntryName(id);
                            Log.d(TAG, "resourceEntryName:" + resourceEntryName + " " + id);
                            if ("digits_label".equals(resourceEntryName)) {
                                try {
                                    throw new NullPointerException();
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                    Logger.d(TAG, Log.getStackTraceString(e));
                                }
                            }
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
