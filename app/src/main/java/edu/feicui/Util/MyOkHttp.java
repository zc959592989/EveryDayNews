package edu.feicui.Util;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import edu.feicui.net.Constants;
import edu.feicui.net.Utils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 封装的OkHttp工具类
 * Created by zhaoCe
 * on 2016/10/17.
 */

public class MyOkHttp {

    public static void get(Map<String,String> params,String url, final OnResponseListener onResponseListener){
       String finalUrl=url+ Utils.getUrl(params, Constants.GET_TYPE);
        /**
         * 1.实例化OkHttpClient对象
         * 2.新建一个请求
         * 3.加入请求参数
         * 4.执行请求
         */
        OkHttpClient client=new OkHttpClient.Builder().connectTimeout(10000, TimeUnit.MILLISECONDS).build();
        Request request=new Request.Builder().url(finalUrl).get().build();
        Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onResponseListener.fail(call);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                onResponseListener.success(call,response);
            }
        });
    }
    public static void post(Map<String,String> params,String url,final OnResponseListener onResponseListener){
        OkHttpClient client=new OkHttpClient.Builder().connectTimeout(10000, TimeUnit.MILLISECONDS).build();
        FormBody.Builder builder=new FormBody.Builder();
        Set<String> set=params.keySet();//拿到所有映射中的键
        for (String key:set  //遍历set拿到所有key
             ) {
           String value= params.get(key);//通过key拿到所有value
            builder.add(key,value);//每拿到一个键值对，就将该键值对add进builder中
        }
        FormBody body=builder.build();
        Request request=new Request.Builder().url(url).post(body).build();
        Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onResponseListener.fail(call);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                onResponseListener.success(call,response);
            }
        });
    }
}
