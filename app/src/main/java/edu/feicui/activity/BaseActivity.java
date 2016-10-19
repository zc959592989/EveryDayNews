package edu.feicui.activity;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import edu.feicui.everydaynews.R;

/**
 * 基类activity
 * Created by zhaoCe on 2016/9/27.
 */

public abstract class BaseActivity extends FragmentActivity {
    /**
     * 收藏按钮
     */
    ImageView mImgFavorite;
    /**
     * 标题
     */
    public TextView mTvTitle;
    /**
     * 左上角图标
     */
    public ImageView mImgRight;
    /**
     * 左上角图标
     */
    public ImageView mImgLeft;
    /**
     * 评论数量
     */
    TextView mTvComment;
    /**
     * 布局填充器
     */
    public LayoutInflater mInflater;
    /**
     * 子类布局写入的一个viewgroup
     */
    public RelativeLayout mRel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);
        mInflater=getLayoutInflater();
        mRel= (RelativeLayout) findViewById(R.id.rl_activity_base);
        mImgLeft= (ImageView) findViewById(R.id.img_base_activity_left);
        mImgRight= (ImageView) findViewById(R.id.img_base_activity_right);
        mTvTitle= (TextView) findViewById(R.id.tv_base_activity_title);
        mImgFavorite= (ImageView) findViewById(R.id.img_base_activity_favorite);
        mTvComment= (TextView) findViewById(R.id.tv_base_activity_comment);
    }
    /**
     * 通过传入控件id绑定监听事件
     * @param listener  绑定的监听器
     * @param ids       需要绑定点击事件的控件id
     */
    public void setOnClick(View.OnClickListener listener, int...ids){
        if(listener==null){return;}
        for (int id : ids) {
            findViewById(id).setOnClickListener(listener);
        }
    }
    /**
     * 通过传入view绑定监听事件
     * @param listener  绑定的监听器
     * @param views     需要绑定点击事件的view
     */
    public void setOnClick(View.OnClickListener listener, View...views){
        if(listener==null){return;}
        for (View view : views) {
            view.setOnClickListener(listener);
        }
    }
    /**
     * 骨架  子类实现该方法，按照该方法的顺序执行
     */
    public final void setContentView(int id){
        mInflater.inflate(id, mRel);
        initView();
    }
    /**
     * 初始化数据
     */
    public abstract void initView();
}
