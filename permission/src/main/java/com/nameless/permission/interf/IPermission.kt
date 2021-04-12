package com.nameless.permission.interf

interface IPermission {
    //同意权限
    fun permissionGranted()

    //拒绝权限并且选中不再提示
    fun permissionDenied(requestCode: Int, denyList: List<String>)

    //取消权限
    fun permissionCanceled(requestCode: Int)
}