package com.mooney.appinfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {

    // 当前上下文
    private Context mContext;
    // ================ 控件View ================
    // HintTv
    private TextView am_hint_tv;
    // 显示App信息
    private ListView am_listview;
    // ================ 其他对象  ================
    // 获取全部app信息
    private ArrayList<PackageInfo> listPInfos = new ArrayList<PackageInfo>();
    // 适配器
    private AppAdapter aAdapter;
    // 获取图片、应用名、包名
    private PackageManager pManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;
        // 初始化操作0
        initViews();
        initValues();
        initListeners();
    }

    /**
     * 初始化控件
     */
    private void initViews() {
        am_hint_tv = (TextView) this.findViewById(R.id.am_hint_tv);
        am_listview = (ListView) this.findViewById(R.id.am_listview);
    }

    /**
     * 初始化数据
     */
    private void initValues() {
        // 字体加粗
        TextPaint tp = am_hint_tv.getPaint();
        tp.setFakeBoldText(true);
        // 初始化
        pManager = mContext.getPackageManager();
        // 初始化APP数据
        getAllApps();
        // 初始化适配器,并绑定
        aAdapter = new AppAdapter();
        am_listview.setAdapter(aAdapter);
    }

    /**
     * 初始化事件
     */
    private void initListeners() {
        am_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> vAdapter, View view, int position, long id) {
                try {
                    // 原始的PackageInfo
                    PackageInfo oPInfo = listPInfos.get(position);
                    // 获取包名
                    String packName = oPInfo.applicationInfo.packageName;

                    Intent intent = new Intent(mContext,SignaturesActivity.class);
                    intent.putExtra("packName", packName);
                    mContext.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("获取App信息失败");
                }
            }
        });
    }



    /**
     * 点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    /**
     * 重写返回键
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            MainActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // =============================  其他操作  =============================
    /**
     * 获取全部app信息
     */
    @SuppressWarnings("static-access")
    public void getAllApps() {
        this.listPInfos.clear(); // 清空旧数据
        // 管理应用程序包
        PackageManager pManager = mContext.getPackageManager();
        // 获取手机内所有应用
        List<PackageInfo> packlist = pManager.getInstalledPackages(0);
        for (int i = 0; i < packlist.size(); i++) {
            PackageInfo pak = (PackageInfo) packlist.get(i);
            // 判断是否为非系统预装的应用程序
            // 这里还可以添加系统自带的，这里就先不添加了，如果有需要可以自己添加
            // if()里的值如果<=0则为自己装的程序，否则为系统工程自带
            if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0) {
                // 添加自己已经安装的应用程序
                this.listPInfos.add(pak);
            }
        }
    }

    // =============================  适配器    =============================
    class AppAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return listPInfos.size();
        }

        @Override
        public PackageInfo getItem(int position) {
            return listPInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                // 导入XML
                convertView = LayoutInflater.from(mContext).inflate(R.layout.app_item, null);
                holder = new ViewHolder();
                convertView.setTag(holder);
                holder.ai_igview = (ImageView) convertView.findViewById(R.id.ai_igview);
                holder.ai_name_tv = (TextView) convertView.findViewById(R.id.ai_name_tv);
                holder.ai_pack_tv = (TextView) convertView.findViewById(R.id.ai_pack_tv);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            // 获取App信息
            PackageInfo pInfo = this.getItem(position);
            // 包名
            String aPack = pInfo.applicationInfo.packageName;
            // app名
            String aName = pManager.getApplicationLabel(pInfo.applicationInfo).toString();
            // app图标
            Drawable aIcon = pManager.getApplicationIcon(pInfo.applicationInfo);
            // 设置显示信息
            holder.ai_igview.setImageDrawable(aIcon); // 设置图标
            holder.ai_name_tv.setText(aName); // 设置app名
            holder.ai_pack_tv.setText(aPack); // 设置pack名
            return convertView;
        }

        class ViewHolder {
            ImageView ai_igview;
            TextView ai_name_tv;
            TextView ai_pack_tv;
        }
    }

    // =================== 显示Dialog =====================
    private Toast mToast;
    private void showToast(String hint){
        if (mToast != null) {
            mToast.setText(hint);
        } else {
            mToast = Toast.makeText(mContext, hint, Toast.LENGTH_SHORT);
        }
        mToast.show();
    }
}
