package com.qianbajin.dyhook.h;

import android.app.AndroidAppHelper;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.CancellationSignal;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.Arrays;

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
 * @author qianbajin
 * @date at 2020/8/29 0029  14:29
 */
public class HookStep {

    public void hook() {
        Method insertWithOnConflict = XposedHelpers.findMethodBestMatch(SQLiteDatabase.class, "insertWithOnConflict", String.class,
                String.class, ContentValues.class, int.class);
        Method getPath = XposedHelpers.findMethodExact(SQLiteDatabase.class, "getPath");
        XposedBridge.hookMethod(insertWithOnConflict, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Object[] args = param.args;
                String table = (String) args[0];
                ContentValues values = (ContentValues) args[2];
                Log.d("HookStep", "insertWithOnConflict table:" + table + " values:" + values);
                Object path = getPath.invoke(param.thisObject);
                Log.d("HookStep", "insertWithOnConflict data path:" + path);
                Toast.makeText(AndroidAppHelper.currentApplication(), "劫持到步数插入", Toast.LENGTH_SHORT).show();
            }
        });
        /*
            boolean distinct, String table, String[] columns,
            String selection, String[] selectionArgs, String groupBy,
            String having, String orderBy, String limit, CancellationSignal cancellationSignal*/
        Method queryWithFactory = XposedHelpers.findMethodExact(SQLiteDatabase.class, "queryWithFactory", SQLiteDatabase.CursorFactory.class,
                boolean.class, String.class, String[].class, String.class, String[].class,
                String.class, String.class, String.class, String.class, CancellationSignal.class);
        XposedBridge.hookMethod(queryWithFactory, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Object path = getPath.invoke(param.thisObject);
                Log.d("HookStep", "queryWithFactory data path:" + path);
                Log.d("HookStep", "queryWithFactory param.args:" + Arrays.toString(param.args));
                Toast.makeText(AndroidAppHelper.currentApplication(), "劫持到步数查询", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
