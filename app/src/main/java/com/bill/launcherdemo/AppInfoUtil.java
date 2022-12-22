package com.bill.launcherdemo;

import android.app.AppGlobals;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

public class AppInfoUtil {

    public static void clearLastChosenLauncher(Context context) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        try {
            final String type = intent
                    .resolveTypeIfNeeded(context.getContentResolver());
            ResolveInfo chosenActivity = AppGlobals.getPackageManager()
                    .getLastChosenActivity(intent, type,
                            PackageManager.MATCH_DEFAULT_ONLY);
            if (chosenActivity != null
                    && !chosenActivity.activityInfo.packageName.equals(context
                    .getPackageName())) {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("android.intent.action.MAIN");
                intentFilter.addCategory("android.intent.category.HOME");
                intentFilter.addCategory("android.intent.category.DEFAULT");
                AppGlobals.getPackageManager().setLastChosenActivity(intent, type,
                        PackageManager.MATCH_DEFAULT_ONLY, intentFilter, 1081344,
                        new ComponentName(context, MainActivity.class));
            }
        } catch (Throwable e) {

        }
    }

    public static String getCurrentLauncherPackageName(Context context) {
        if (null == context) {
            return null;
        }

        PackageManager pm = context.getPackageManager();
        if (null == pm) {
            return null;
        }

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        ResolveInfo resolveInfo = null;
        try {
            resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        } catch (Exception e) {

            //
        }

        if (null == resolveInfo || null == resolveInfo.activityInfo || null == resolveInfo
                .activityInfo.packageName) {
            return null;
        }

        // /< 多个launcher的情况下，没有设置默认的launcher，那么获取到的数据为android，故过滤掉
        if (resolveInfo.activityInfo.packageName.equals("android")) {
            return "";
        }

        return resolveInfo.activityInfo.packageName;
    }

}
