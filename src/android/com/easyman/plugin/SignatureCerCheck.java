package com.easyman.plugin;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.widget.Toast;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import java.security.MessageDigest;

/**
 * Created by easyman-imac on 2018/1/2.
 */
public class SignatureCerCheck extends CordovaPlugin {
    private static final String ACTION_CHECK_SIGN_CER = "checkSignature";
    private static final String ACTION_GET_SING_HASH_CODE = "getSignatureHashCode";
    private static final String ACTION_GET_SING_INFO = "getSignatureInfo";
    private static final String ACTION_GET_SING_INFO_HASH = "getSignatureSHA1";
    private int hashValue = 0;
    private String SHA1Value = "";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        // isJailBreak
        if (action.equals(ACTION_CHECK_SIGN_CER)) {
            if (args != null && args.length() > 0) {
                if(args.getJSONObject(0).has("hashValue")){
                    hashValue = args.getJSONObject(0).getInt("hashValue");
                }
                if(args.getJSONObject(0).has("SHA1Value")){
                    SHA1Value = args.getJSONObject(0).getString("SHA1Value");
                }
            }
            this.checkSignature(callbackContext);
            return true;
        }else if (action.equals(ACTION_GET_SING_HASH_CODE)) {
            String errInfo = "";
            int tempValue = getSignatureHashCode(errInfo);
            if(tempValue == 0){
                callbackContext.error("获取签名hashCode失败:"+errInfo);
            }else{
                callbackContext.success(tempValue+"");
            }
            return true;
        }else if (action.equals(ACTION_GET_SING_INFO)) {
            String errInfo = "";
            String tempStr = getSignatureInfo(errInfo);
            if(tempStr == null || tempStr.equals("")){
                callbackContext.error("获取签名信息失败:"+errInfo);
            }else{
                callbackContext.success(tempStr);
            }
            return true;
        }else if (action.equals(ACTION_GET_SING_INFO_HASH)) {
            String errInfo = "";
            String tempStr = getSignatureSHA1(errInfo);
            if(tempStr == null || tempStr.equals("")){
                callbackContext.error("获取签名SHA1信息失败:"+errInfo);
            }else{
                callbackContext.success(tempStr);
            }
            return true;
        }
        return false;
    }

    private void checkSignature(CallbackContext callbackContext) {
        if (authHashValue() || authSHA1()) {
            callbackContext.success("true");
        } else {
            callbackContext.success("false");
        }
    }

    /**
     * 签名的hash值校验
     * @return
     */
    private boolean authHashValue(){
        int tempValue = getSignatureHashCode("");
        if (tempValue != 0 && tempValue == hashValue) {
            return true;
        }else{
            Toast.makeText(cordova.getActivity(), "应用验证失败！，请确认你安装的是正版应用！", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    /**
     * 签名的hash值校验
     * @return
     */
    private boolean authSHA1(){
        String tempStr = getSignatureSHA1("");
        if (tempStr != null && tempStr.equals(SHA1Value)) {
            return true;
        }else{
            Toast.makeText(cordova.getActivity(), "应用验证失败！，请确认你安装的是正版应用！", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private String getSignatureInfo(String errInfo) {
        PackageManager manager = cordova.getActivity().getPackageManager();
        StringBuilder builder = new StringBuilder();
        String pkgname = cordova.getActivity().getPackageName();
        boolean isEmpty = pkgname.isEmpty();
        if (isEmpty) {
            Toast.makeText(cordova.getActivity(), "应用程序的包名不能为空！", Toast.LENGTH_SHORT).show();
            errInfo = "应用程序的包名不能为空！";
        } else {
            try {

                PackageInfo packageInfo = manager.getPackageInfo(pkgname, PackageManager.GET_SIGNATURES);

                Signature[] signatures = packageInfo.signatures;
                Signature sign = signatures[0];
                byte[] signByte = sign.toByteArray();
                return  bytesToHexString(signByte);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                errInfo = e.getMessage();
            }
        }
        return null;
    }

    private int getSignatureHashCode(String errInfo) {
        final String packname = cordova.getActivity().getPackageName();
        int code = 0;
        boolean isEmpty = packname.isEmpty();
        if (isEmpty) {
            Toast.makeText(cordova.getActivity(), "应用程序的包名不能为空！", Toast.LENGTH_SHORT).show();
            errInfo = "应用程序的包名不能为空！";
        }else{
            try {
                PackageInfo packageInfo = cordova.getActivity().getPackageManager().getPackageInfo(packname, PackageManager.GET_SIGNATURES);
                Signature[] signs = packageInfo.signatures;
                Signature sign = signs[0];
                code = sign.hashCode();
            } catch (PackageManager.NameNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return code;
    }

    private String getSignatureSHA1(String errInfo) {
        PackageManager manager = cordova.getActivity().getPackageManager();
        StringBuilder builder = new StringBuilder();
        String pkgname = cordova.getActivity().getPackageName();
        boolean isEmpty = pkgname.isEmpty();
        if (isEmpty) {
            Toast.makeText(cordova.getActivity(), "应用程序的包名不能为空！", Toast.LENGTH_SHORT).show();
            errInfo = "应用程序的包名不能为空！";
        } else {
            try {

                PackageInfo packageInfo = manager.getPackageInfo(pkgname, PackageManager.GET_SIGNATURES);

                Signature[] signatures = packageInfo.signatures;
                Signature sign = signatures[0];

                byte[] signByte = sign.toByteArray();
                return  bytesToHexString(generateSHA1Bytes(signByte));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                errInfo = e.getMessage();
            }
        }
        return null;
    }

    private byte[] generateSHA1Bytes(byte[] data) {
        try {
            // 使用getInstance("算法")来获得消息摘要,这里使用SHA-1的160位算法
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            // 开始使用算法
            messageDigest.update(data);
            // 输出算法运算结果
            return messageDigest.digest();// 20位字节
        } catch (Exception e) {
//            Log.e("generateSHA1", e.getMessage());
        }
        return null;
    }

    private String bytesToHexString(byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        StringBuilder buff = new StringBuilder();
        for (byte aByte : bytes) {
            if ((aByte & 0xff) < 16) {
                buff.append('0');
            }
            buff.append(Integer.toHexString(aByte & 0xff));
        }
        return buff.toString();
    }
}
