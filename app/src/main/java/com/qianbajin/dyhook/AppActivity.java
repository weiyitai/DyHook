package com.qianbajin.dyhook;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;
/**
 * ----------------------
 * 代码千万行
 * 注释第一行
 * 代码不注释
 * 改bug两行泪
 * -----------------------
 *
 * @author weiyitai
 * @date 2020/1/2
 */
public class AppActivity extends ListActivity {

    public static final String PKG_NAME = "pkg_name";
    public static final String PKG_LIST = "pkg_list";
    private ArrayAdapter<String> mAdapter;
    private String[] mNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.sel_app);
        String[] extra = getIntent().getStringArrayExtra(PKG_LIST);
        Log.d("AppActivity", "extra:" + Arrays.toString(extra));
        if (extra != null) {
            onAppListLoaded(extra);
        } else {
            new LoadAppList(this).execute(getPackageManager());
        }
    }

    private static class LoadAppList extends AsyncTask<PackageManager, Void, String[]> {

        WeakReference<AppActivity> callbackHolder;
        ProgressDialog mDialog;

        LoadAppList(AppActivity activity) {
            callbackHolder = new WeakReference<>(activity);
            mDialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog.show();
        }

        @Override
        protected String[] doInBackground(PackageManager... params) {
            PackageManager pm = params[0];
            final List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
            String[] names = new String[packages.size()];
            int i = 0;
            for (ApplicationInfo info : packages) {
                names[i] = info.packageName + "\n" + info.loadLabel(pm);
                i++;
            }
            Arrays.sort(names);
            return names;
        }

        @Override
        protected void onPostExecute(String[] names) {
            if (mDialog != null) {
                mDialog.dismiss();
            }
            // Now that the list is loaded, show the dialog on the UI thread
            final AppActivity callbackReference;
            if ((callbackReference = callbackHolder.get()) != null) {
                callbackReference.onAppListLoaded(names);
            }
        }
    }

    @Override
    public void onBackPressed() {
        setResult("", mNames);
        super.onBackPressed();
    }

    private void onAppListLoaded(String[] names) {
        if (isDestroyed() || isFinishing()) {
            return;
        }
        mNames = names;
        mAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, names);
        setListAdapter(mAdapter);
        getListView().setFastScrollEnabled(true);
        getListView().setOnItemClickListener((parent, view, position, id) -> {
            String item = mAdapter.getItem(position);
            if (item != null) {
                setResult(item, names);
                finish();
            }
        });
    }

    private void setResult(String item, String[] names) {
//        String s = item.split("\n")[0];
        Intent intent = new Intent();
        intent.putExtra(PKG_NAME, item);
        intent.putExtra(PKG_LIST, names);
        setResult(RESULT_OK, intent);
    }
}
