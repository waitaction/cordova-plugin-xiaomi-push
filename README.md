 # 小米手机推送

小米手机推送

安装 `cordova-plugin-xiaomi-push`
 

``` shell
 cordova plugin add cordova-plugin-xiaomi-push --variable  APP_KEY=YOUR_APP_KEY --variable APP_ID=YOUR_APP_ID
```

## 使用

使用前需注册，以获取 `token` ，你可以将 `token` 与你的app用户信息关联后上传到服务器

``` js
// 注册推送
xiaomiPush.register(function(token) {
    console.log(token);
}, function(err) {
    console.log(err);
}, []);

// 接收token
xiaomiPush.onNewToken(function(token) {
    console.log(token); // 会多次接收到token
});
```

注册完成后，需要监听 `messageReceived` 事件

``` js
document.addEventListener("messageReceived", function(result) {
    console.log(result);
}, false);
```
