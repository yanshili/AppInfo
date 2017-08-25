package me.mooney.lib.permission;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;

import com.didi.virtualapk.PluginManager;


public class PermissionHelper {

    public static void checkPermissions(Context context
            ,PermissionActivity.OnPermissionListener listener
            , String permissionDescription
            , String... permissions){

        PermissionEngine.mListener=listener;

        Intent intent=new Intent();
        intent.setComponent(new ComponentName(
                PluginManager.getInstance(context).getHostContext().getPackageName()
                ,PermissionActivity.class.getName()));

        intent.putExtra("permission_description",permissionDescription);
        intent.putExtra("permissions",permissions);
        context.startActivity(intent);
    }



}

class PermissionEngine{

    private static Activity sActivity;              // 需要传入的Activity Fragment
    private static int mRequestCode;            // 请求码
    private String[] mRequestPermission; // 需要请求的权限集合
    private static String mPermissionDes;       // 需要显示的权限说明

    public static PermissionActivity.OnPermissionListener mListener;

    public PermissionEngine(Activity activity) {
        this.sActivity = activity;
    }

    public static PermissionEngine with(Activity activity) {
        return new PermissionEngine(activity);
    }


    public PermissionEngine requestCode(int requestCode) {
        this.mRequestCode = requestCode;
        return this;
    }

    public PermissionEngine setPermissionDes(String permissionDes) {
        this.mPermissionDes = permissionDes;
        return this;
    }

    public PermissionEngine requestPermissions(String... requestPermission) {
        this.mRequestPermission = requestPermission;
        return this;
    }

    public PermissionEngine setListener(OnPermissionListener listener){
        sListener=listener;
        return this;
    }


//    /**
//     * 权限的判断和请求的发送
//     */
//    public void request() {
//        // 1.判断是否是6.0版本以上
//        if (!PermissionUtils.isOverMarshmallow()) {
//            // 2.如果不是6.0以上，直接通过反射获取执行方法，执行方法
//            PermissionUtils.executeSuccessMethod(sActivity, mRequestCode);
//            return;
//        } else {
//            // 3.如果是6.0 以上，首先需要判断权限是否已经授予
//            //执行什么方法并不确定，只能通过注解的方式给方法打一个标记
//            //通过反射去执行  注解+反射
//            //获取没有授予权限的列表
//            if (PermissionUtils.hasAllPermissionsGranted(sActivity,mRequestPermission)) {
//                // 3.1. 授予：通过反射获取方法并执行
//                PermissionUtils.executeSuccessMethod(sActivity,mRequestCode);
//            } else {
//                // 3.2. 没有全部授予： 申请权限
//                PermissionUtils.requestPermissions(sActivity,mRequestPermission, mRequestCode);
//            }
//        }
//    }
//
//    /**
//     * 处理权限申请的回调
//     * @param object
//     * @param requestCode
//     * @param grantResults
//     */
//    public static void requestPermissionResult(Object object, int requestCode, int[] grantResults) {
//        if (requestCode == mRequestCode){
//            if(PermissionUtils.hasAllPermissionsGranted(grantResults)){
//                //权限全部授予 执行方法
//                PermissionUtils.executeSuccessMethod(object, requestCode);
//            }else{
//                //权限没有全部授予，再次请求权限
//                PermissionUtils.showMissingPermissionDialog(sActivity,mPermissionDes);
//            }
//        }
//    }


    /**
     * 权限的判断和请求的发送
     */
    public void request() {
        // 1.判断是否是6.0版本以上
        if (!PermissionUtils.isOverMarshmallow()) {
            // 2.如果不是6.0以上，直接通过反射获取执行方法，执行方法
//            PermissionUtils.executeSuccessMethod(sActivity, mRequestCode);
            if (sListener!=null)
                sListener.onGranted();
            return;
        } else {
            // 3.如果是6.0 以上，首先需要判断权限是否已经授予
            //执行什么方法并不确定，只能通过注解的方式给方法打一个标记
            //通过反射去执行  注解+反射
            //获取没有授予权限的列表
            if (PermissionUtils.hasAllPermissionsGranted(sActivity,mRequestPermission)) {
                // 3.1. 授予：通过反射获取方法并执行
//                PermissionUtils.executeSuccessMethod(sActivity,mRequestCode);
                if (sListener!=null)
                    sListener.onGranted();
            } else {
                // 3.2. 没有全部授予： 申请权限
                PermissionUtils.requestPermissions(sActivity,mRequestPermission, mRequestCode);
            }
        }
    }

    /**
     * 处理权限申请的回调
     * @param requestCode
     * @param grantResults
     */
    public static void requestPermissionResult(int requestCode, int[] grantResults) {
        if (requestCode == mRequestCode){
            if(PermissionUtils.hasAllPermissionsGranted(grantResults)){
                //权限全部授予 执行方法
//                PermissionUtils.executeSuccessMethod(object, requestCode);
                if (sListener!=null)
                    sListener.onGranted();
            }else{
                //权限没有全部授予，再次请求权限
                showMissingPermissionDialog(sActivity,mPermissionDes);
            }
        }
    }

    /**
     * 显示权限对话框
     *
     * @param activity
     * @param permissionDes
     */
    public static void showMissingPermissionDialog(final Activity activity, String permissionDes) {
        String message = String.format("APP需要%1$s的权限为您提供服务,是否去设置", TextUtils.isEmpty(permissionDes) ? "必要" : permissionDes);
        new AlertDialog.Builder(activity)
                .setTitle("提示")
                .setMessage(message)
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PermissionUtils.startAppSettings(activity);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        sListener.onDenied();
                    }
                })
                .setCancelable(false)
                .show();
    }


    private static OnPermissionListener sListener;

    public interface OnPermissionListener {

        void onGranted();

        void onDenied();
    }
}