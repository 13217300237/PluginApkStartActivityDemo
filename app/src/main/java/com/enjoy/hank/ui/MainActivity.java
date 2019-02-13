package com.enjoy.hank.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.enjoy.hank.R;
import com.enjoy.hank.util.AssetUtil;

import study.hank.com.plugin_lib.PluginApkConst;
import study.hank.com.plugin_lib.PluginManager;
import study.hank.com.plugin_lib.ProxyActivity;


/**
 * 主Activity，宿主的主要启动入口
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String path = AssetUtil.copyAssetToCache(MainActivity.this, "plugin_module-debug.apk");
                    PluginManager.getInstance().loadPluginApk(path);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 先跳到代理Activity，由代理Activity展示真正的Activity内容
                PluginManager.getInstance().gotoActivity(MainActivity.this,
                        PluginManager.getInstance().getPackageInfo().activities[0].name);

            }
        });
    }
}
