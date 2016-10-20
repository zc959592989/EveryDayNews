package edu.feicui.Util;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Map;

import edu.feicui.net.Constants;
import edu.feicui.net.Utils;

/**
 * 对volley进行二次封装
 * Created by zhaoCe on 2016/9/27.
 */

public class MyVolley{
    /**
     * get请求
     * @param context 用来获取请求队列 RequestQueue
     * @param url     域名（网址）
     * @param params  传递的值（key，value）
     * @param result  回调接口，获取从服务器返回的数据
     */
    public static void get(Context context, String url, Map<String ,String> params, final OnVolleyResult result){
        RequestQueue queue=Volley.newRequestQueue(context);
        StringRequest request=new StringRequest(Request.Method.GET, url+ Utils.getUrl(params, Constants.GET_TYPE), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                result.success(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                result.failed(volleyError);
            }
        });
        queue.add(request);
    }
    /**
     * post请求
     * @param context 用来获取请求队列 RequestQueue
     * @param url     域名（网址）
     * @param params  传递的值（key，value）
     * @param result  回调接口，获取从服务器返回的数据
     */
    public static void post(Context context, String url, final Map<String ,String> params, final OnVolleyResult result){
        RequestQueue queue=Volley.newRequestQueue(context);
        StringRequest request=new StringRequest(Request.Method.POST, url+ Utils.getUrl(params, Constants.POST_TYPE), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                result.success(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                result.failed(volleyError);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        queue.add(request);
    }
    public interface OnVolleyResult{
        void success(String s);
        void failed(VolleyError volleyError);
    }
}
