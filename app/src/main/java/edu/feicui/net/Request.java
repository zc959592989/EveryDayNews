package edu.feicui.net;

import java.util.Map;

/**
 * Created by Administrator on 2016/9/22.
 * 用于获取网络连接请求的所有数据
 */
public class Request {

    public String url;//请求路径
    public Map<String,String> params;//参数
    public int type;//请求类型（通信方式GET、POST）
}
