package org.baiyu.fuckssaid

import android.app.AndroidAppHelper
import android.content.ContentResolver
import android.content.Context
import android.provider.Settings.Secure
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam
import java.util.UUID

class MainHook : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: LoadPackageParam) {
        if (lpparam.packageName == BuildConfig.APPLICATION_ID) {
            return
        }
        try {
            XposedHelpers.findAndHookMethod(
                Secure::class.java,
                "getStringForUser",
                ContentResolver::class.java,
                String::class.java,
                Int::class.javaPrimitiveType,
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val context: Context? = AndroidAppHelper.currentApplication()
                        if (context == null) {
                            XposedBridge.log("Fuck SSAID: cannot get context for " + lpparam.packageName)
                            return
                        }
                        val settings: Settings = Settings.getInstance(
                            context.getSharedPreferences(
                                Settings.PREF_SSAID,
                                Context.MODE_PRIVATE
                            )
                        )

                        var id = settings.idForPackage
                        if (id == null) {
                            id = UUID.randomUUID().toString()
                            settings.idForPackage = id
                            XposedBridge.log("Fuck SSAID: new id generated for " + lpparam.packageName + ": " + id)
                        }
                        if (Secure.ANDROID_ID == param.args[1]) {
                            param.result = id
                            XposedBridge.log(lpparam.packageName + " is getString android id (hooked): " + id)
                        }
                    }
                }
            )
        } catch (t: Throwable) {
            XposedBridge.log(t)
        }
    }
}
