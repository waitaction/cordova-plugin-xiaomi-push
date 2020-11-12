package com.lifang123.push.xiaomi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.lifang123.push.XiaomiPush;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class XiaomiMessageReceiver extends PushMessageReceiver {
    private static final String TAG = "XiaomiPushTag";
    private String mRegId;

    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage message) {
        if (!TextUtils.isEmpty(message.getTopic())) {

        } else if (!TextUtils.isEmpty(message.getAlias())) {

        }
    }

    /*
     * 需通知类型为自定义，需点击通知栏才会触发此方法
     * */
    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage message) {

    }

    /**
     * 用户不需点击通知栏也会触发此方法
     */
    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage message) {

    }

    @Override
    public void onCommandResult(Context context, MiPushCommandMessage message) {
        Log.v(TAG, "onCommandResult is called. " + message.toString());
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        String log;
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
                if (XiaomiPush.instance.callbackContext != null) {
                    XiaomiPush.instance.callbackContext.success(mRegId);
                }
            }
        }
    }

    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private static String getSimpleDate() {
        return new SimpleDateFormat("MM-dd hh:mm:ss").format(new Date());
    }

}
