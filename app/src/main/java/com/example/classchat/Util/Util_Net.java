package com.example.classchat.Util;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/*
网络工具类，发送Http请求和处理JSON文本
 */
public class Util_Net {
    public static void sendOKHTTPRequest(String address, okhttp3.RequestBody requestBody, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static boolean parseJSONWithJSONObject(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            return jsonObject.getBoolean("state");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // 默认登录或注册失败
    }


}
