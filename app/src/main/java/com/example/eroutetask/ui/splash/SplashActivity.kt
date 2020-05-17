package com.example.eroutetask.ui.splash

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.example.eroutetask.R
import com.example.eroutetask.ui.scan.ScanActivity

class SplashActivity : AppCompatActivity() {

    companion object{
        private const val REQUEST_CODE: Int = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            checkForPermission()
        },2000)
    }

    private fun checkForPermission() {
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED)
            gotoMain()
        else
            requestForPermission()
    }

    private fun requestForPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA)
            ,
            REQUEST_CODE
        )
    }

    private fun gotoMain() {
        startActivity(Intent(this, ScanActivity::class.java))
        finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == REQUEST_CODE){
            if(grantResults.isNotEmpty()&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
                gotoMain()

            else if(checkForPermanentlyDenied())
                showAppSettingsDialog()

            else
                requestForPermission()

        }

    }

    private fun showAppSettingsDialog() {
        AlertDialog.Builder(this,
            R.style.CustomAlertDialog
        )
            .setTitle("Grant permission")
            .setMessage("We need camera permission to scan QR codes. Go to App Settings and manage permissions")
            .setPositiveButton("Grant"){
                    _, _ -> gotoAppSetting()
            }
            .setNegativeButton("Cancel") {
                    _,_ -> run {
                finish()
            }
            }.show()
    }

    private fun gotoAppSetting() {
        val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", packageName, null))
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun checkForPermanentlyDenied(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA).not()
        } else {
            false
        }
    }


    override fun onRestart() {
        super.onRestart()
        checkForPermission()
    }
}
