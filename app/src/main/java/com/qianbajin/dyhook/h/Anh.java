package com.qianbajin.dyhook.h;

import android.app.Activity;
import android.app.AndroidAppHelper;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import de.robv.android.xposed.XC_MethodHook;
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
 * @date at 2020/8/22 0022  22:55
 */
public class Anh {

   public void hook() {
       XposedHelpers.findAndHookMethod(Activity.class, "onCreate", Bundle.class, new XC_MethodHook() {
           @Override
           protected void afterHookedMethod(MethodHookParam param) throws Throwable {
               super.afterHookedMethod(param);
               Log.d("Anh", "==============");
               Toast.makeText(AndroidAppHelper.currentApplication(), "---------------", Toast.LENGTH_SHORT).show();
           }
       });


    }

}
