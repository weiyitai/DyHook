package com.qianbajin.dyhook;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
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
public class AppActivity extends Activity {

    public static final String PKG_NAME = "pkg_name";
    public static final String PKG_LIST = "pkg_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        setTitle(R.string.sel_app);
        String[] extra = getIntent().getStringArrayExtra(PKG_LIST);
        AppFragment listFragment = new AppFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArray(PKG_LIST, extra);
        listFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.content, listFragment).commit();
        ((EditText) findViewById(R.id.et_search)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                listFragment.onTextChange(s.toString());
            }
        });
    }
}
