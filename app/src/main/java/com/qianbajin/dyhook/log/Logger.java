package com.qianbajin.dyhook.log;

import android.util.Log;
/**
 * @author Administrator
 * @date at 2018/11/18 0018  19:46
 */
public class Logger {

    private static final int CHUNK_SIZE = 4000;

    public static void d(String tag, String msg) {
        if (msg.length() < CHUNK_SIZE) {
            log(tag, msg);
            return;
        }
        byte[] bytes = msg.getBytes();
        int length = bytes.length;
        for (int i = 0; i < length; i += CHUNK_SIZE) {
            int count = Math.min(length - i, CHUNK_SIZE);
            log(tag, new String(bytes, i, count));
        }
    }

    private static void log(String tag, String msg) {
        Log.d(tag, msg);
    }
}
