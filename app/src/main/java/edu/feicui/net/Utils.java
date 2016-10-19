package edu.feicui.net;

import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2016/9/22.
 * 拿到url的工具类
 */
public class Utils {
    /**
     *
     * @param params {"name":"zs","psw","123"}--->JSON
     * @return ?name=zs&psw=123
     */
    public static String getUrl(Map<String,String> params,int type){
        StringBuffer stringBuffer=new StringBuffer();
        if(params!=null&&params.size()!=0){
            if(type==Constants.GET_TYPE) {
                stringBuffer.append("?");
            }
            /**
             * keySet方法用于获取Map集合的所有key（键名），并存放在一个Set集合对象中。
             */
            Set<String> keySet=params.keySet();
            for (String key:keySet) {
                stringBuffer.append(key)
                            .append("=")
                            .append(params.get(key))
                            .append("&");
            }
            stringBuffer.deleteCharAt(stringBuffer.length()-1);
        }
        return stringBuffer.toString();
    }
}
