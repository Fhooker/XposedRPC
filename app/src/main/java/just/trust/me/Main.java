package just.trust.me;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;

import org.json.JSONObject;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import android.provider.ContactsContract;
import android.util.Log;

public class Main implements IXposedHookLoadPackage {

    private static final String APPNAME = "JustTrustMe:";
    private static String TAG = Main.APPNAME;
    String currentPackageName = "";

    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {

        currentPackageName = lpparam.packageName;
        if (currentPackageName.equals("com.ss.android.ugc.aweme")) {
            HookMainActivity(lpparam);
            return;
        }
    }

    private static void HookMainActivity(final XC_LoadPackage.LoadPackageParam loadPackageParam) {
        try {
            //com.ss.android.ugc.aweme.main.MainActivity
            XposedHelpers.findAndHookMethod("com.ss.android.ugc.aweme.main.MainActivity", loadPackageParam.classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(final MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Log.d(TAG, "HookMainActivity afterHookedMethod##################");

                    Activity content = (Activity) param.thisObject;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            AsyncHttpServer server = new AsyncHttpServer();

                            server.get("/testRPC", new HttpServerRequestCallback() {
                                @Override
                                public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {

                                    //TODO 可以这里面调用APP的东西了
                                    JSONObject jsonObject = new JSONObject();
                                    try {
                                        jsonObject.put("status", true);
                                        jsonObject.put("message", "Xposed RPC Demo hello world");

                                        response.send(jsonObject);
                                    } catch (Exception eee) {

                                    }
                                }
                            });


                            server.listen(12345);
                            Log.d(TAG, "AsyncHttpServer start");
                        }
                    }).start();
                }
            });

        } catch (Exception eee) {
            Log.d(TAG, "HookMainActivity error:" + eee.toString());
        }
    }

}
