package com.nameless.aoppermission

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.nameless.permission.annotation.NeedPermission
import com.nameless.permission.annotation.PermissionCanceled
import com.nameless.permission.annotation.PermissionDenied
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
            startActivity(Intent(this@MainActivity,MainActivity2::class.java))
        }
    }

    @NeedPermission([android.Manifest.permission.READ_EXTERNAL_STORAGE])
    fun test() {
        Log.e("ssss","----------------------------------------------");
    }

    @PermissionDenied
    fun test1(d: DenyBean) {
        Log.e("ssss","-----PermissionDenied-----------------------------------------");

    }
    @PermissionCanceled
    fun test2() {
        Log.e("ssss","-----PermissionCanceled-----------------------------------------");

    }
}