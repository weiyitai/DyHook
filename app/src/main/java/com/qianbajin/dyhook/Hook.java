package com.qianbajin.dyhook;

import android.util.Log;

import com.qianbajin.dyhook.h.Anh;
import com.qianbajin.dyhook.h.EbusHook;
import com.qianbajin.dyhook.h.HookContact;
import com.qianbajin.dyhook.h.HookStep;
import com.qianbajin.dyhook.h.HookTextView;
import com.qianbajin.dyhook.h.HookWeLink;
import com.qianbajin.dyhook.h.KuaiMaoHook;
import com.qianbajin.dyhook.h.MiPayHook;
import com.qianbajin.dyhook.h.WeChatHook;

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
                new WeChatHook().hookWeb();
                break;
            case Constant.PKG_ANT_HELPER:
                new Anh().hook();
                break;
            case Constant.PKG_JOYOSE:
                new HookStep().hook();
                break;
            case Constant.PKG_WORKS:
                new HookTextView().hook();
                break;
            case Constant.PKG_WELINK:
                new HookWeLink().hook();
                break;
            case Constant.PKG_MIPAY_WALLET:
                new MiPayHook().hook();
                break;
            case Constant.PKG_ANDROID_CONTACTS:
                new HookContact().hook();
                break;
            default:
                break;
        }
    }
}
