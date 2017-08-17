package me.mooney.lib.base;

import android.app.Application;
import android.content.Context;

/**
 * 作者： mooney
 * 日期： 2017/8/17
 * 邮箱： shili_yan@sina.com
 * 描述：
 */

public class BaseApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
