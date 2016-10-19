package edu.feicui.net;

import android.content.Context;

import java.util.Map;

/**
 * Created by Administrator on 2016/9/22.
 * 用于网络请求的类
 */
public  class MyHttp {
    public static void get(Context context,String url, Map<String,String > params,
    OnResultFinishListener mListener){
        //进行网络请求
        Request request=new Request();
        request.params=params;
        request.type=Constants.GET_TYPE;
        request.url=url+Utils.getUrl(params,Constants.GET_TYPE);
        //请求
        NetAsync netAsync=new NetAsync(context,mListener);
        netAsync.execute(request);
    }

    public static void post(Context context,String url, Map<String,String > params,
                            OnResultFinishListener mListener){
        Request request=new Request();
        request.params=params;
        request.type=Constants.POST_TYPE;
        request.url=url;

        NetAsync async=new NetAsync(context,mListener);
        async.execute(request);
    }
}
