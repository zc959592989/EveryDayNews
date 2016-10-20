package edu.feicui.net;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by zhaoCe on 2016/9/22.
 */
public class NetAsync extends AsyncTask<Request,Object,Response> {
    ProgressDialog mDialog;
    OnResultFinishListener mListener;
    public NetAsync(Context context,OnResultFinishListener mListener){
       mDialog=ProgressDialog.show(context,"","加载中...");
        this.mListener=mListener;
    }
    @Override
    protected Response doInBackground(Request... params) {
        Request request=params[0];
        Response response=new Response();
        HttpURLConnection httpURLConnection=null;
        try {
            /**
             * 拿到链接
             */
            URL url=new URL(request.url);
                /**
                 * 打开链接
                 */
                httpURLConnection= (HttpURLConnection) url.openConnection();
                /**
                 * 设置一些信息
                 */
                httpURLConnection.setConnectTimeout(Constants.CONNEVT_TIMEOUT);//设置连接超时的时长
                httpURLConnection.setReadTimeout(Constants.READ_TIMEOUT);//设置读取数据超时的时长

                if(request.type==Constants.GET_TYPE){//GET
                    httpURLConnection.setRequestMethod("GET");

                }else{
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    OutputStream out=httpURLConnection.getOutputStream();
                    out.write(Utils.getUrl(request.params,Constants.POST_TYPE).getBytes());
                }

                int code=httpURLConnection.getResponseCode();
                response.code=code;
                if(code==HttpURLConnection.HTTP_OK){
                    InputStream in=httpURLConnection.getInputStream();
                    byte[] bytes=new byte[1024];
                    int len;
                    StringBuffer buff=new StringBuffer();
                    while ((len=in.read(bytes))!=-1){
                        buff.append(new String(bytes,0,len));
                    }
                    //拿到了结果
                    response.result=buff.toString();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            if (httpURLConnection!=null){
                httpURLConnection.disconnect();
            }
        }

        return response;
    }

    @Override
    protected void onPostExecute(Response o) {
        super.onPostExecute(o);
        //拿到结果
        mDialog.dismiss();
        Response response= o;
        if(o.code!=HttpURLConnection.HTTP_OK){ //失败
            mListener.failed(response);
        }else{ //成功
            mListener.success(response);
        }
    }
}
