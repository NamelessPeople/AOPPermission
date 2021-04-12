package com.nameless.aoppermission

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nameless.permission.annotation.NeedPermission
import com.nameless.permission.annotation.PermissionCanceled
import com.nameless.permission.annotation.PermissionDenied
import com.nameless.permission.bean.CancelBean
import com.nameless.permission.bean.DenyBean
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        text.setOnClickListener {
            test()
        }

        text2.setOnClickListener {
            startActivity(Intent(this@MainActivity, MainActivity2::class.java))
        }
    }

    @NeedPermission([android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE])
    fun test() {
        Toast.makeText(this, "PermissionSuccess", Toast.LENGTH_SHORT).show()
    }

    @PermissionDenied
    fun test1(d: DenyBean) {
        Toast.makeText(this, "PermissionDenied", Toast.LENGTH_SHORT).show()
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = Uri.fromParts("package", packageName, null)
        startActivity(intent)
    }

    @PermissionCanceled
    fun test2(cancel: CancelBean) {
        Toast.makeText(this, "PermissionCanceled", Toast.LENGTH_SHORT).show()
    }
}