## 一、配置

Supports android gradle plugin 3.6.1

Upgrade inner aspectjrt version to 1.9.5

[![](https://jitpack.io/v/NamelessPeople/AOPPermission.svg)](https://jitpack.io/#NamelessPeople/AOPPermission)

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
	dependencies {
		classpath 'com.hujiang.aspectjx:gradle-android-plugin-aspectjx:2.0.8'
	}
  
	dependencies {
			implementation 'com.github.NamelessPeople:AOPPermission:1.0.0'
	}
	
	apply plugin: 'android-aspectjx'
	
## 二、使用举例
    @NeedPermission([android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE])
    fun test() {
        Toast.makeText(this,"PermissionSuccess",Toast.LENGTH_SHORT).show()
    }

    @PermissionDenied
    fun test1(d: DenyBean) {
        Toast.makeText(this,"PermissionDenied",Toast.LENGTH_SHORT).show()
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = Uri.fromParts("package", getPackageName(), null)
        startActivity(intent)
    }
    @PermissionCanceled
    fun test2(cancel: CancelBean) {
        Toast.makeText(this,"PermissionCanceled",Toast.LENGTH_SHORT).show()
    }
    
    
