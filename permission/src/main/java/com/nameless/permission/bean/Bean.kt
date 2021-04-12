package com.nameless.permission.bean

data class CancelBean(var requestCode: Int = 0)

data class DenyBean(var requestCode: Int = 0, internal var denyList: List<String>)