package com.lifang123.push;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;
import org.json.JSONObject;

import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageHelper;

import java.util.Map;

public class XiaomiPush extends CordovaPlugin {
    private static final String TAG = "XiaomiPushTag";
    private String appKey;
    private String appId;
    public CallbackContext callbackContext;
    public static XiaomiPush instance;

    public XiaomiPush() {
        instance = this;
    }

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        ApplicationInfo appInfo = null;
        try {
            appInfo = this.cordova.getActivity().getPackageManager().getApplicationInfo(
                    this.cordova.getContext().getPackageName(),
                    this.cordova.getContext().getPackageManager().GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Bundle bundle = appInfo.metaData;
        this.appKey = bundle.getString("XiaomiPUSH_APPKEY");
        this.appId = bundle.getString("XiaomiPUSH_APPID");
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        if (action.equals("register")) {
            this.callbackContext = callbackContext;
            getXiaomiIntentData(this.cordova.getActivity().getIntent());
            MiPushClient.registerPush(this.cordova.getContext(), this.appId, this.appKey);
            return true;
        }
        return false;
    }


    @Override
    public void onNewIntent(Intent intent) {
        try {
            Log.i(TAG, "XiaomiPush onNewIntent");
            this.cordova.getActivity().setIntent(intent);
            this.getXiaomiIntentData(intent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void bridgeWebView(JSONObject object, String bridgeJs) {
        final String js = String.format(bridgeJs, object.toString());
        this.cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                instance.webView.loadUrl("javascript:" + js);
            }
        });
    }

    public void getXiaomiIntentData(Intent intent) {
        if (null != intent) {
            MiPushMessage message = (MiPushMessage) intent.getSerializableExtra(PushMessageHelper.KEY_MESSAGE);
            if (message != null) {
                JSONObject jsonObject = new JSONObject();
                Map<String, String> map = message.getExtra();
                if (map != null) {
                    for (String key : map.keySet()) {
                        try {
                            String content = map.get(key);
                            jsonObject.put(key, content);
                            intent.putExtra(key, content);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                XiaomiPush.instance.bridgeWebView(jsonObject, String.format("cordova.fireDocumentEvent('messageReceived', %s);", jsonObject.toString()));
            }

        } else {
            Log.i(TAG, "intent is null");
        }
    }
}
