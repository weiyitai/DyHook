package com.qianbajin.dyhook;

import android.util.Log;

import com.qianbajin.dyhook.h.Anh;
import com.qianbajin.dyhook.h.EbusHook;
import com.qianbajin.dyhook.h.KuaiMaoHook;
import com.qianbajin.dyhook.h.XLogHook;

import de.robv.android.xposed.IXposedHookLoadPackage;
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
public class Hook implements IXposedHookLoadPackage {

    private static final String TAG = "Hook";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        Util.setClassLoader(lpparam.classLoader);
        Log.d(TAG, "handleLoadPackage" + lpparam.packageName);
        switch (lpparam.packageName) {
            case Constant.PKG_EBUS:
                new EbusHook().hook();
                break;
            case Constant.PKG_CAT:
                new KuaiMaoHook().hook2();
                break;
            case Constant.PKG_WECHAT:
                new XLogHook().hook();
                break;
            case Constant.PKG_ANT_HELPER:
                new Anh().hook();
                break;
            default:
                break;
        }
    }
}
