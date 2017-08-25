package me.mooney.host;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.didi.virtualapk.PluginManager;
import com.didi.virtualapk.internal.LoadedPlugin;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import me.mooney.lib.base.BaseActivity;
import me.mooney.lib.listview.BASEAdapter;
import me.mooney.lib.listview.ViewHolder;
import me.mooney.lib.permission.PermissionActivity;
import me.mooney.lib.permission.PermissionHelper;

public class HostMainActivity extends BaseActivity {


    private ListView mListView;
    private BASEAdapter<File> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_main);

        PermissionHelper.checkPermissions(this
                , new PermissionActivity.OnPermissionListener() {
                    @Override
                    public void onGranted() {
                        loadFile();
                        Log.e("HostMainActivity","HostMainActivity---- 授权成功");
                    }
                }
                , "---定位权限----"
                , Manifest.permission.WRITE_EXTERNAL_STORAGE
                );

        saveMessage("this message is saved in host!!!");

        // 加载plugin.apk插件包
        pluginManager = PluginManager.getInstance(this);

        mListView= (ListView) findViewById(R.id.plugin_list);

        mAdapter=new BASEAdapter<File>(this) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView==null){
                    convertView= LayoutInflater.from(context).inflate(R.layout.item_plugin_file,null);
                }
                ViewHolder holder=ViewHolder.getHolder(convertView);
                TextView textView=holder.getView(R.id.tv_plugin_file);
                File item=getItem(position);

                textView.setText(item.getName());
                textView.setTag(item.getPath());
                return convertView;
            }
        };

        mListView.setAdapter(mAdapter);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File file=mAdapter.getItem(position);
                Log.i("HostMainActivity","插件路径:"+file.getPath());
                if (file.exists()) {
                    try {
                        pluginManager.loadPlugin(file);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.i("HostMainActivity","插件加载失败");
                    }
                }
            }
        });
    }


    private void loadFile() {
        File file=new File(Environment.getExternalStorageDirectory()+"/plugin");
        Log.i("Host","==="+file.getPath());
        File[] list=file.listFiles();
        Log.i("Host","list==null----"+(list==null));
        if (list==null){
            return;
        }
        Log.i("Host","list.length==="+list.length);
        mAdapter.setItems(Arrays.asList(list));
    }


    private void saveMessage(String message){
        SharedPreferences sp=getSharedPreferences("test",MODE_PRIVATE);
        sp.edit()
                .putString("host",message)
                .apply();
    }

    private PluginManager pluginManager;

    public void onMainClick(View view){
        toast("点击主程序主按钮");

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent,1);


    }

    public void onPluginClick(View view){
        toast("跳转到插件页面");
        Intent intent=new Intent();
        intent.setClassName("me.mooney.plugin","me.mooney.plugin.PluginMainActivity");
        startActivity(intent);
    }

    public void onMessageClick(View view){
        SharedPreferences sp=getSharedPreferences("test",MODE_PRIVATE);
        String message=sp.getString("host","nothing");
        toast(message);
    }

    private SimpleDateFormat mFormat=new SimpleDateFormat("HH:MM:ss");

    public void onPluginInfoClick(View view){
        TextView textView= (TextView) view;

        List<LoadedPlugin> loadedPluginList=pluginManager.getAllLoadedPlugins();

        StringBuilder sb=new StringBuilder("插件信息:");
        for (LoadedPlugin plugin:loadedPluginList){
            sb
                    .append("\n======分割线=======")
                    .append("\n插件名称:")
                    .append(plugin.getPackageName())
                    .append("\n插件路径:")
                    .append(plugin.getLocation())
                    .append("\n插件资源路径:")
                    .append(plugin.getPackageResourcePath())
                    .append("\n插件代码路径:")
                    .append(plugin.getCodePath())
                    .append("\n插件版本名称:")
                    .append(plugin.getPackageInfo().versionName)
                    .append("\n首次安装时间:")
                    .append(mFormat.format(new Date(plugin.getPackageInfo().firstInstallTime)))
                    .append("\n最后一次更新时间:")
                    .append(mFormat.format(new Date(plugin.getPackageInfo().lastUpdateTime)))
                    ;
        }

        textView.setText(sb);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1&&resultCode==RESULT_OK){
            Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。


            String path=getFilePathFromContentUri(uri,getContentResolver());
            Log.i("Host","uri path=="+path);
            File file = new File(path);
            if (file.exists()) {
                try {
                    pluginManager.loadPlugin(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }else {
            toast("未选中文件");
        }
    }

    public static String getFilePathFromContentUri(Uri selectedVideoUri,
                                                   ContentResolver contentResolver) {
        String filePath;
        String[] filePathColumn = {MediaStore.MediaColumns.DATA};

        Cursor cursor = contentResolver.query(selectedVideoUri, filePathColumn, null, null, null);
//      也可用下面的方法拿到cursor
//      Cursor cursor = this.context.managedQuery(selectedVideoUri, filePathColumn, null, null, null);

        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }

}
