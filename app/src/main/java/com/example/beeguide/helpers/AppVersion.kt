package com.example.beeguide.helpers

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager

fun getVersionName(context: Context): String {
    try {
        val packageInfo: PackageInfo =
            context.packageManager.getPackageInfo(context.packageName, 0)
        return packageInfo.versionName
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return "N/A"
}