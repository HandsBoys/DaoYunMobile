package com.fzu.daoyunmobile.Utils.HttpUtils;

import com.alibaba.fastjson.JSONObject;
import com.fzu.daoyunmobile.Configs.UrlConfig;

import okhttp3.Callback;


/**
 * 提供常见http相关函数
 */
public class HttpUtil {

    /**
     * 发送短信接口
     *
     * @param phone    手机号
     * @param callback 回调函数
     */
    public static void sendMessage(String phone, Callback callback) {
        //OkHttpUtil.getInstance().Get(UrlConfig.getUrl(UrlConfig.UrlType.MESSAGE) + phone, callback);
        OkHttpUtil.getInstance().PostWithJson(UrlConfig.getUrl(UrlConfig.UrlType.MESSAGE) + phone, new JSONObject(),callback);

    }
}
