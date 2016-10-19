package edu.feicui.net;

/**
 * Created by Administrator on 2016/9/22.
 */
public interface OnResultFinishListener {
    void success(Response response);
    void failed(Response response);
}
