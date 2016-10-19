package edu.feicui.entity;

import java.io.Serializable;

/**
 * 注册，登陆共用实体  接收响应
 * Created by zhaoCe on 2016/9/29.
 */

public class RegisterResponse implements Serializable {
    public int status;
    public String message;
    public RegisterResponseDetail data;
}
