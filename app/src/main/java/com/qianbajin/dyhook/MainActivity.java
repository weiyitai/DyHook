package com.qianbajin.dyhook;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.qianbajin.dyhook.log.DyInterceptor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 * @author weiyitai
 * @date
 */
public class MainActivity extends ListActivity {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_APP = 0x01;
    private ArrayAdapter<String> mAdapter;
    private SharedPreferences mSp;
    private String[] mAppList;
    private List<String> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSp = getSharedPreferences(Constant.HOOK, MODE_WORLD_READABLE);
        Set<String> set = mSp.getStringSet(SpKey.APK_LIST, new HashSet<>());
        mList = new ArrayList<>(set);
        Log.d("MainActivity", "strings:" + mList);
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mList);
        setListAdapter(mAdapter);
        getListView().setOnItemClickListener((adapterView, view, i, l) -> removeItem(i));
        String sourceDir = getApplicationInfo().sourceDir;
        Log.d("MainActivity", sourceDir);
        mSp.edit().putString(Constant.APK_PATH, sourceDir).apply();
        getInterceptor();

    }

    private void getInterceptor() {
        try {
            Class<?> HttpLoggingInterceptor = getClassLoader().loadClass("okhttp3.logging.HttpLoggingInterceptor");
            Constructor<?> constructor = HttpLoggingInterceptor.getDeclaredConstructor();
            Log.d("MainActivity", "constructor:" + constructor);
            Object instance = constructor.newInstance();
            Log.d("MainActivity", "instance:" + instance);
            Class<?> Level = getClassLoader().loadClass("okhttp3.logging.HttpLoggingInterceptor$Level");
            Log.d("MainActivity", "Level:" + Level);
            Object[] o = Level.getEnumConstants();
            if (o != null) {
                Log.d("MainActivity", "o:" + Arrays.toString(o));
            }
            Object o1 = o[3];
            Method setLevel = instance.getClass().getDeclaredMethod("setLevel", Level);
            setLevel.setAccessible(true);
            Object invoke = setLevel.invoke(instance, o1);
            Log.d("MainActivity", "invoke:" + invoke);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, Log.getStackTraceString(e));
        }
        Constructor<?>[] constructors = DyInterceptor.class.getDeclaredConstructors();
        Log.d(TAG, "constructors:" + Arrays.toString(constructors));
        try {
            DyInterceptor logInterceptor = DyInterceptor.class.getDeclaredConstructor().newInstance();
            Log.d(TAG, "logInterceptor:" + logInterceptor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeItem(int position) {
        new AlertDialog.Builder(this)
                .setMessage(R.string.remove_confirm)
                .setPositiveButton(android.R.string.ok, (dialog, which) ->
                        mAdapter.remove(mAdapter.getItem(position)))
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.customize_apps, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_new) {
            Intent intent = new Intent(this, AppActivity.class);
            intent.putExtra(AppActivity.PKG_LIST, mAppList);
            startActivityForResult(intent, REQUEST_APP);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if (!saveAll()) {
            int count = mAdapter.getCount();
            Set<String> set = new HashSet<>();
            for (int i = 0; i < count; i++) {
                String item = mAdapter.getItem(i);
                set.add(item);
            }
            mSp.edit().putStringSet(SpKey.APK_LIST, set).apply();
        }
        super.onDestroy();

    }

    private boolean saveAll() {
        try {
            Field mObjects = mAdapter.getClass().getDeclaredField("mObjects");
            mObjects.setAccessible(true);
            List<String> list = (List<String>) mObjects.get(mAdapter);
            Set<String> set = new HashSet<>(list);
            mSp.edit().putStringSet(SpKey.APK_LIST, set).apply();
            return true;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            Log.d("MainActivity", "saveAll fail");
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_APP && data != null) {
            String pkg = data.getStringExtra(AppActivity.PKG_NAME);
            mAppList = data.getStringArrayExtra(AppActivity.PKG_LIST);
            if (!TextUtils.isEmpty(pkg) && !mList.contains(pkg)) {
                mAdapter.add(pkg.split("\n")[0]);
            }
        }
    }
}
