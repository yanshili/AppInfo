package me.mooney.plugin;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.didi.virtualapk.PluginManager;
import com.didi.virtualapk.internal.LoadedPlugin;

import java.io.File;

import me.mooney.lib.base.BaseActivity;

/**
 * 作者： mooney
 * 日期： 2017/8/17
 * 邮箱： shili_yan@sina.com
 * 描述：
 */

public class PluginMainActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plugin_main);
    }

    public void onMainClick(View view){
        toast("点击插件主按钮00");
    }


    public void onMessageClick(View view){
        SharedPreferences sp=getSharedPreferences("test",MODE_PRIVATE);
        String message=sp.getString("host","nothing");
        toast(message);

        sp.edit()
                .putString("host","this message is changed in plugin!!!")
                .apply();

        try {
            LoadedPlugin plugin=LoadedPlugin.create(PluginManager.getInstance(this)
                    ,getApplication(),new File(getExternalCacheDir()+"/plugin.apk"));
            plugin.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
