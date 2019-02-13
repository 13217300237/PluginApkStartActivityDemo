package com.enjoy.hank.application;

import android.app.Application;

import study.hank.com.plugin_lib.PluginManager;

/**
 * 宿主app
 * <p>
 * 它作为整个项目的外壳，负责设计项目的外部架构，并且整顿所有插件，组件.
 * 不涉及到任何具体的业务
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PluginManager.getInstance().init(this);
    }
}
