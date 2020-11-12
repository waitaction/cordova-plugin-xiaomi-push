var XiaomiPush = function () { };


/**
 * 注册推送服务
 * @param successCallback 成功回调
 * @param errorCallback 失败回调
 * @param options 参数
 */
XiaomiPush.prototype.register = function (successCallback, errorCallback, options) {
    cordova.exec(successCallback, errorCallback, "XiaomiPushPlugin", "register", options);
};

/**
 * 当token变化后，会触发方法的successCallback回调
 * @param successCallback token被自动变更时通知变更后的token
 * @param errorCallback 通知失败的回调
 */
XiaomiPush.prototype.onNewToken = function (successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, "XiaomiPushPlugin", "onNewToken", []);
};

module.exports = new XiaomiPush();









