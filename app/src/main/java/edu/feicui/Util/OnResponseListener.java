package edu.feicui.Util;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by zhaoCe on 2016/10/17.
 */

public interface OnResponseListener {
    void success(Call call, Response response) throws IOException;
    void fail(Call call);
}
