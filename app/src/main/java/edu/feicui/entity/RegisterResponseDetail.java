package edu.feicui.entity;

import java.io.Serializable;

/**
 * 注册，登陆共用实体  接收响应（详细信息）
 * Created by zhaoCe on 2016/9/29.
 */

public class RegisterResponseDetail implements Serializable {
    public int result;
    public String explain;
    public String token;
}
