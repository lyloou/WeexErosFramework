package com.benmu.framework.update;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;

import com.benmu.framework.R;

public class VersionCheckerHelper {

    // 须在Manifest中添加权限： <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
    public static void showUpgradeDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Theme_AppCompat_Light_Dialog);
        builder.setTitle(R.string.str_update_title);
        builder.setMessage(R.string.str_update_tips)
            .setCancelable(false)
            .setPositiveButton(R.string.str_update_now, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }
            })
            .setNegativeButton(R.string.str_update_later, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
        AlertDialog alert = builder.create();
        alert.setCancelable(false);
        alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alert.show();
    }

}
