package com.qianbajin.dyhook;

import android.app.ListFragment;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.qianbajin.dyhook.AppActivity.PKG_LIST;
import static com.qianbajin.dyhook.AppActivity.PKG_NAME;
/**
 * ----------------------
 * 代码千万行
 * 注释第一行
 * 代码不注释
 * 改bug两行泪
 * -----------------------
 *
 * @author qianbajin
 * @date at 2020/8/22 0022  0:24
 */
public class AppFragment extends ListFragment {

    private static String[] sNames;
    private ArrayAdapter<String> mAdapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (sNames != null && sNames.length != 0) {
            onAppListLoaded(sNames);
        } else {
            new LoadAppList(this).execute(getActivity().getPackageManager());
        }

    }

    public void onTextChange(String text) {
        if (mAdapter == null || sNames == null) {
            return;
        }
        List<String> list = new ArrayList<>();
        for (String name : sNames) {
            if (name.contains(text)) {
                list.add(name);
            }
        }
        mAdapter.clear();
        mAdapter.addAll(list);
    }

    private static class LoadAppList extends AsyncTask<PackageManager, Void, String[]> {

        WeakReference<AppFragment> callbackHolder;

        LoadAppList(AppFragment activity) {
            callbackHolder = new WeakReference<>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
            // Now that the list is loaded, show the dialog on the UI thread
            final AppFragment callbackReference;
            if ((callbackReference = callbackHolder.get()) != null) {
                callbackReference.onAppListLoaded(names);
            }
        }
    }

    private void onAppListLoaded(String[] names) {
        if (!isAdded()) {
            return;
        }
        sNames = names;
        List<String> list = new ArrayList<>(Arrays.asList(names));
        mAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, list);
        setListAdapter(mAdapter);
        getListView().setFastScrollEnabled(true);
        getListView().setOnItemClickListener((parent, view, position, id) -> {
            String item = mAdapter.getItem(position);
            if (item != null) {
                setResults(item, names);
            }
        });
    }

    private void setResults(String item, String[] names) {
        Intent intent = new Intent();
        intent.putExtra(PKG_NAME, item);
        intent.putExtra(PKG_LIST, names);
        getActivity().setResult(RESULT_OK, intent);
        getActivity().finish();
    }
}
