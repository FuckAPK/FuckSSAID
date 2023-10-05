package org.baiyu.fuckssaid;

import android.app.AndroidAppHelper;
import android.content.Context;
import android.provider.Settings;

import java.util.UUID;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (lpparam.packageName.equals(BuildConfig.APPLICATION_ID)) {
            return;
        }
        try {
            XposedBridge.hookAllMethods(
                    Settings.Secure.class,
                    "getString",
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            super.beforeHookedMethod(param);
                            Context context = AndroidAppHelper.currentApplication();
                            if (context == null) {
                                XposedBridge.log("Fuck SSAID: cannot get context for " + lpparam.packageName);
                                return;
                            }
                            org.baiyu.fuckssaid.Settings settings = org.baiyu.fuckssaid.Settings.getInstance(context);

                            String id = settings.getIdForPackage();
                            if (id == null) {
                                id = UUID.randomUUID().toString();
                                settings.setIdForPackage(id);
                                XposedBridge.log("Fuck SSAID: new id generated for " + lpparam.packageName + ": " + id);
                            }

                            if (param.args[1] instanceof String s && s.equals(Settings.Secure.ANDROID_ID)) {
                                param.setResult(id);
                                XposedBridge.log(lpparam.packageName + " is getString android id (hooked): " + id);
                            }
                        }
                    }
            );
        } catch (Throwable t) {
            XposedBridge.log(t);
        }
    }
}
