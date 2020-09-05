package com.qianbajin.dyhook.h;

import android.util.Log;
import android.widget.TextView;

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
 * @date at 2020/9/6 0006  0:46
 */
public class HookTextView {

    private static final String TAG = HookTextView.class.getSimpleName();
    public void hook() {
        XposedHelpers.findAndHookMethod(TextView.class, "setText", CharSequence.class, TextView.BufferType.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d(TAG, "param.args[0]:" + param.args[0]);
            }
        });
    }

}
