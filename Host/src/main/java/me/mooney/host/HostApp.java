package me.mooney.host;

import android.content.Context;

import com.didi.virtualapk.PluginManager;

import me.mooney.lib.base.BaseApplication;

/**
 * 作者： mooney
 * 日期： 2017/8/17
 * 邮箱： shili_yan@sina.com
 * 描述：
 */

public class HostApp extends BaseApplication {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        PluginManager.getInstance(base).init();
    }
}
