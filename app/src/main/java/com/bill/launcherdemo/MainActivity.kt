package com.bill.launcherdemo

import android.app.Activity
import android.app.role.RoleManager
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.btn_default).setOnClickListener {
            Log.e("Bill", "pkg = ${AppInfoUtil.getCurrentLauncherPackageName(this)}")
            AppInfoUtil.clearLastChosenLauncher(this)
            default()
        }
        findViewById<View>(R.id.btn_new_launch).setOnClickListener {
            Log.e("Bill", "pkg = ${AppInfoUtil.getCurrentLauncherPackageName(this)}")
            newLaunch()
        }
        findViewById<View>(R.id.btn_setting).setOnClickListener {
            Log.e("Bill", "pkg = ${AppInfoUtil.getCurrentLauncherPackageName(this)}")
            gotoSetting()
        }
    }

    private fun default() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("set_from", 1)
        startActivity(intent)
    }

    private fun newLaunch() {
        if (Build.VERSION.SDK_INT >= 29) {
            showLauncherSelection()
        }
    }

    private fun gotoSetting() {
        val pm = packageManager
        val preferredApps = Intent(Settings.ACTION_HOME_SETTINGS)
        preferredApps.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (pm.resolveActivity(preferredApps, 0) != null) {
            startActivity(preferredApps);
        } else {

        }
    }

    private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->
        Log.d("Bill", "activityResult:${activityResult.resultCode}, data:${activityResult.data}")
        if (activityResult.resultCode == Activity.RESULT_OK) {
            // Perhaps log the result here.
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.e("Bill", "onNewIntent")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("Bill", "onDestroy")
    }

    @RequiresApi(29)
    private fun showLauncherSelection() {
        val roleManager = getSystemService(RoleManager::class.java)
        if (roleManager.isRoleAvailable(RoleManager.ROLE_HOME) &&
            !roleManager.isRoleHeld(RoleManager.ROLE_HOME)
        ) {
            val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_HOME)
            startForResult.launch(intent)
        }
    }
}