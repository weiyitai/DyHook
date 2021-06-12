package com.qianbajin.dyhook.util;

import android.content.Intent;
import android.os.Bundle;

import com.qianbajin.dyhook.log.Logger;

import java.util.Set;
/**
 * ----------------------
 * 代码千万行
 * 注释第一行
 * 代码不注释
 * 改bug两行泪
 * -----------------------
 *
 * @author weiyitai
 * @date 2019/6/18 0018  9:02
 */
public class IntentUtil {

    private static final String TAG = IntentUtil.class.getSimpleName();

    public static boolean printIntent(Intent intent) {
        if (intent == null) {
            Logger.d(TAG, "intent == null");
            return false;
        }
        Logger.d(TAG, "printIntent---------");
        return printBundle(intent.getExtras());
    }

    public static boolean printBundle(Bundle bundle) {
        if (bundle == null) {
            Logger.d(TAG, "bundle == null");
            return false;
        }
        Logger.d(TAG, "printBundle------------------>");
        Set<String> keySet = bundle.keySet();
        for (String key : keySet) {
            Object value = bundle.get(key);
            Logger.d(TAG, "key:" + key + "  -->value:" + value);
            if (value instanceof Bundle) {
                Logger.d(TAG, "key:" + key + " isBundle");
                printBundle((Bundle) value);
            }
        }
        return true;
    }

    public static boolean printBundle(String tag, Bundle bundle) {
        if (bundle == null) {
            Logger.d(TAG, "bundle == null");
            return false;
        }
        Logger.d(TAG, "printBundle------------->tag:" + tag);
        printBundle(bundle);
        return true;
    }

}
