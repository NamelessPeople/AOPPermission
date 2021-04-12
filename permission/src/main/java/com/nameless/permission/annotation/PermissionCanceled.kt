package com.nameless.permission.annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class PermissionCanceled(val requestCode: Int = 0)