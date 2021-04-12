package com.nameless.permission.aop


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.nameless.permission.PermissionRequestFragment
import com.nameless.permission.annotation.NeedPermission
import com.nameless.permission.annotation.PermissionCanceled
import com.nameless.permission.annotation.PermissionDenied
import com.nameless.permission.bean.CancelBean
import com.nameless.permission.bean.DenyBean
import com.nameless.permission.interf.IPermission
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import java.lang.reflect.InvocationTargetException

@Aspect
class PermissionAspect{
    @Pointcut("execution(@com.nameless.permission.annotation.NeedPermission * *(..)) && @annotation(needPermission)")
    fun requestPermissionMethod(needPermission: NeedPermission) {

    }

    @Around("requestPermissionMethod(needPermission)")
    fun joinPoint(joinPoint: ProceedingJoinPoint, needPermission: NeedPermission){

        var activity: FragmentActivity? = null
        val obj = joinPoint.getThis()
        if (obj is FragmentActivity) {
            activity = obj
        } else if (obj is Fragment) {
            activity = obj.activity
        }
        if (activity == null) return

        PermissionRequestFragment.permissionRequest(needPermission.value,needPermission.requestCode,activity,object :IPermission{
            override fun permissionGranted() {
                try {
                    joinPoint.proceed()
                } catch (throwable: Throwable) {
                    throwable.printStackTrace()
                }
            }

            override fun permissionDenied(requestCode: Int, denyList: List<String>) {
                val cls = obj.javaClass
                val methods = cls.declaredMethods
                if (methods.isEmpty()) return
                for (method in methods) {
                    //过滤不含自定义注解PermissionDenied的方法
                    val isHasAnnotation = method.isAnnotationPresent(PermissionDenied::class.java)
                    if (isHasAnnotation) {
                        method.isAccessible = true
                        //获取方法类型
                        val types = method.parameterTypes
                        if (types.size != 1) return
                        //获取方法上的注解
                        method.getAnnotation(PermissionDenied::class.java) ?: return
                        //解析注解上对应的信息
                        val bean = DenyBean(requestCode,denyList)
                        try {
                            method.invoke(obj, bean)
                        } catch (e: IllegalAccessException) {
                            e.printStackTrace()
                        } catch (e: InvocationTargetException) {
                            e.printStackTrace()
                        }
                    }
                }
            }

            override fun permissionCanceled(requestCode: Int) {
                val cls =obj.javaClass
                val methods = cls.declaredMethods
                if (methods.isEmpty()) return
                for (method in methods) {
                    //过滤不含自定义注解PermissionCanceled的方法
                    val isHasAnnotation = method.isAnnotationPresent(PermissionCanceled::class.java)
                    if (isHasAnnotation) {
                        method.isAccessible = true
                        //获取方法类型
                        val types = method.parameterTypes
                        if (types.size != 1) return
                        //获取方法上的注解
                        method.getAnnotation(PermissionCanceled::class.java) ?: return
                        //解析注解上对应的信息
                        val bean = CancelBean(requestCode)
                        try {
                            method.invoke(obj, bean)
                        } catch (e: IllegalAccessException) {
                            e.printStackTrace()
                        } catch (e: InvocationTargetException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        })
    }
}