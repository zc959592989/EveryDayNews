package edu.feicui.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import edu.feicui.SeverUrl;
import edu.feicui.Util.MyVolley;
import edu.feicui.entity.CommentNumResponse;
import edu.feicui.entity.RegisterResponse;
import edu.feicui.everydaynews.R;

/**
 * 详情界面
 * Created by zhaoCe on 2016/9/29.
 */

public class NewsDetailActivity extends BaseActivity implements View.OnClickListener{
    /**
     * 加载进度条
     */
    ProgressBar mPbBar;
    /**
     * 用户登录的信息（从服务端返回的）
     */
    public RegisterResponse response;
    /**
     * 新闻ID
     */
    int nid;
    /**
     * 新闻详细信息
     */
    WebView mWbDetail;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Log.e("aac", "handleMessage: "+(Integer) msg.obj );
            mPbBar.setProgress((Integer) msg.obj);
            if((Integer) msg.obj==100){
                mPbBar.setVisibility(View.GONE);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_news_activity);
    }
    @Override
    public void initView() {
        mWbDetail= (WebView) findViewById(R.id.web_news_detail);
        mPbBar= (ProgressBar) findViewById(R.id.pb_details_news_load_url);
        mWbDetail.setWebChromeClient(new ChormeClient());
        mImgFavorite.setVisibility(View.VISIBLE);//将收藏按钮可见
        mImgLeft.setBackgroundResource(R.mipmap.back);
        mImgRight.setVisibility(View.INVISIBLE);
        mTvComment.setVisibility(View.VISIBLE);//将评论数可见
        setOnClick(this,mImgFavorite,mImgLeft,mTvComment);//绑定收藏按钮

        Intent intent= getIntent();
        nid= intent.getIntExtra("nid",0);//拿到新闻ID
        response= (RegisterResponse) intent.getSerializableExtra("response");

        String url=intent.getStringExtra("link");
        mWbDetail.loadUrl(url);
        WebSettings settings= mWbDetail.getSettings();
        settings.setSupportZoom(true);//支持缩放功能
        settings.setJavaScriptEnabled(true);//支持jsp脚本语言
        settings.setLoadWithOverviewMode(true);//按照任意比例缩放
        settings.setBuiltInZoomControls(true);//显示缩放视图
        settings.setUseWideViewPort(true);//适应屏幕大小
        mWbDetail.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mWbDetail.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        Map<String,String> params=new HashMap<>();
        params.put("ver","0000000");
        params.put("nid",""+nid);
        //cmt_num?ver=版本号& nid=新闻编号
        MyVolley.get(this, SeverUrl.CMT_NUM, params, new MyVolley.OnVolleyResult() {//获取评论数
            @Override
            public void success(String s) {
                Gson gson=new Gson();
                CommentNumResponse response= gson.fromJson(s, CommentNumResponse.class);
                mTvComment.setText(response.data+"评论");
            }

            @Override
            public void failed(VolleyError volleyError) {
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK&&mWbDetail.canGoBack()){
            mWbDetail.goBack();
            return  true;
        }else{
            finish();
        }
        return false;
    }

    /**
     * 展示收藏按钮
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_base_activity_left://回退按钮
                finish();
                break;
            case R.id.img_base_activity_favorite://加入收藏按钮
                showFavoritePopupWindow();
                break;
            case R.id.tv_base_activity_comment://进入评论页面
                Intent intent=new Intent();
                intent.putExtra("nid",nid);
                if(response!=null){
                    intent.putExtra("response",response);
                }
                intent.setClass(this,CommentActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 收藏弹窗
     */
    PopupWindow mPopFavorite;

    /**
     * 展示收藏弹窗
     */
    private void showFavoritePopupWindow(){
        View contentView= LayoutInflater.from(this).inflate(R.layout.popup_window_favorite,null);
        mPopFavorite=new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
        mPopFavorite.setContentView(contentView);
        mPopFavorite.setOutsideTouchable(true);
        mPopFavorite.setBackgroundDrawable(new BitmapDrawable());
        Button button= (Button) contentView.findViewById(R.id.btn_pop_favorite);//显示出加入收藏按钮后点击加入收藏
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NewsDetailActivity.this,"已加入收藏",Toast.LENGTH_SHORT).show();
                mPopFavorite.dismiss();
            }
        });
        mPopFavorite.showAsDropDown(mImgFavorite);
    }
    class ChormeClient extends WebChromeClient{
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            Message msg=handler.obtainMessage();
            msg.obj=newProgress;
            handler.sendMessage(msg);
        }
    }
}
