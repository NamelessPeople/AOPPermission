package com.nameless.permission

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.nameless.permission.interf.IPermission
import java.util.*

/**
 * @author Zhangli
 * @data: 2021/4/2 17:38
 * 类说明：
 */
class PermissionRequestFragment : Fragment() {
    private var permissions: Array<String>? = null
    private var requestCode: Int = 0

    companion object {
        @JvmStatic
        private var permissionListener: IPermission? = null

        @JvmStatic
        val PERMISSION_KEY = "permission_list"

        @JvmStatic
        val REQUEST_CODE = "request_code"

        /**
         * 跳转到Activity申请权限
         *
         * @param context     Context
         * @param permissions Permission List
         * @param iPermission Interface
         */
        @JvmStatic
        fun permissionRequest(
            permissions: Array<String>,
            requestCode: Int,
            context: FragmentActivity,
            iPermission: IPermission
        ) {
            permissionListener = iPermission
            val bundle = Bundle()
            bundle.putStringArray(PERMISSION_KEY, permissions)
            bundle.putInt(REQUEST_CODE, requestCode)
            var permissionRequestFragment: PermissionRequestFragment? = null
            if (context.supportFragmentManager.fragments.size > 0) {
                for (fragment in context.supportFragmentManager.fragments) {
                    if (fragment is PermissionRequestFragment) {
                        permissionRequestFragment = fragment
                        break
                    }
                }
            }
            if (permissionRequestFragment == null) {
                permissionRequestFragment = PermissionRequestFragment()
            }
            permissionRequestFragment.arguments = bundle
            context.supportFragmentManager.beginTransaction()
                .remove(permissionRequestFragment)
                .add(permissionRequestFragment, "PermissionRequestFragment")
                .commitNowAllowingStateLoss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        if (bundle != null) {
            permissions = bundle.getStringArray(PERMISSION_KEY)
            requestCode = bundle.getInt(REQUEST_CODE, 0)
        }
        if (permissions?.size ?: 0 <= 0) {
            return
        }
        requestPermission(permissions!!)
    }

    /**
     * 申请权限
     *
     * @param permissions permission list
     */
    private fun requestPermission(permissions: Array<String>) {

        if (hasSelfPermissions(context!!, permissions)) {
            permissionListener?.permissionGranted()
        } else {
            //request permissions
            requestPermissions(permissions, requestCode)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (verifyPermissions(grantResults)) {
            //所有权限都同意
            permissionListener?.permissionGranted()
        } else {
            if (!shouldShowRequestPermission(permissions)) {
                //权限被拒绝并且选中不再提示
                if (permissions.size != grantResults.size) return
                val denyList = ArrayList<String>()
                for (i in grantResults.indices) {
                    if (grantResults[i] == -1) {
                        denyList.add(permissions[i])
                    }
                }
                permissionListener?.permissionDenied(requestCode, denyList)
            } else {
                //权限被取消
                permissionListener?.permissionCanceled(requestCode)
            }
        }
    }

    private fun shouldShowRequestPermission(permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (shouldShowRequestPermissionRationale(permission)) {
                return true
            }
        }
        return false
    }

    /**
     * 判断是否所有权限都同意了
     *
     * @param context     context
     * @param permissions permission list
     * @return return true if all permissions granted else false
     */
    private fun hasSelfPermissions(context: Context, permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (!hasSelfPermission(
                    context,
                    permission
                )
            ) {
                return false
            }
        }
        return true
    }


    /**
     * 判断单个权限是否同意
     *
     * @param context    context
     * @param permission permission
     * @return return true if permission granted
     */
    private fun hasSelfPermission(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * 检查是否都赋予权限
     *
     * @param grantResults grantResults
     * @return 所有都同意返回true 否则返回false
     */
    private fun verifyPermissions(grantResults: IntArray): Boolean {
        if (grantResults.isEmpty()) return false
        for (result in grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }
}