# cordova-signature-cer-check V1.0.0
自定义插件用于检测获取系统签名信息

## 1、添加插件方式：
- 直接通过 url 安装：
```shell
cordova plugin add https://github.com/ww-gh/cordova-signature-cer-check.git
```
- 或通过 Cordova Plugins 安装，要求 Cordova CLI 5.0+：
```shell
cordova plugin add cordova-signature-cer-check
```

## 2、使用说明：
- typescript语法
```typescript ionic3
this.platform.ready().then(() => {
        //检查签名
        if (typeof cordova !== 'undefined' && cordova.plugins.SignatureCerCheck) {
            cordova.plugins.SignatureCerCheck.checkSignature({
                hashValue: 1693708445,
                SHA1Value: "12312351341341341"
            }, (value) => {
                if (value === 'true') {

                }
            });
            //获取当前签hashCode信息
            cordova.plugins.SignatureCerCheck.getSignatureHashCode(suc=>{
                this.appSignHashCode = suc;
            }, err=>{
                this.appSignHashCode = err;
            });

            //获取当前签信息
            cordova.plugins.SignatureCerCheck.getSignatureInfo(suc=>{
                this.appSignInfo = suc;
            }, err=>{
                this.appSignInfo = err;
            });
            //获取当前签SHA1信息
            cordova.plugins.SignatureCerCheck.getSignatureSHA1(suc=>{
                this.appSHA1  = suc;
            }, err=>{
                this.appSHA1  = err;
            });
        }
    }
);
```
- javascript语法
```javascript
//检查签名是否正规
 cordova.plugins.SignatureCerCheck.checkSignature(function(value){
    if (value === 'false') {
        console.log("当前App不是正规签名，请谨慎使用!");
    }
});
```

## Support
- 邮箱：(wangwenxy@163.com)
- [故障反馈地址：](https://github.com/ww-gh/cordova-signature-cer-check/issues)
