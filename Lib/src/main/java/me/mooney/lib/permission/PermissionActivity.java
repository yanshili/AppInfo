package me.mooney.lib.permission;

import android.app.Activity;
import android.support.annotation.NonNull;

/**
 * 作者： mooney
 * 日期： 2017/8/24
 * 邮箱： shili_yan@sina.com
 * 描述：
 */

public class PermissionActivity extends Activity {

    private boolean granted=false;

    private PermissionEngine.OnPermissionListener mListener=new PermissionEngine.OnPermissionListener() {
        @Override
        public void onGranted() {
            granted=true;
            finish();
        }

        @Override
        public void onDenied() {
            granted=false;
            finish();
        }
    };

    private PermissionActivity.OnPermissionListener mTempListener;

    @Override
    protected void onStart() {
        super.onStart();
        mTempListener=PermissionEngine.mListener;
        String description=getIntent().getStringExtra("permission_description");
        String[] permissions=getIntent().getStringArrayExtra("permissions");
        PermissionEngine.with(this)
                .requestCode(12)
                .requestPermissions(permissions)
                .setPermissionDes(description)
                .setListener(mListener)
                .request();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionEngine.requestPermissionResult(requestCode,grantResults);
    }

    public interface OnPermissionListener{
        void onGranted();

        void onDenied();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTempListener!=null){
            if (granted){
                mTempListener.onGranted();
            }else {
                mTempListener.onDenied();
            }

        }
    }
}
