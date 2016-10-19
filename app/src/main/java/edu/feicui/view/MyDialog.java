package edu.feicui.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import edu.feicui.everydaynews.R;

/**
 * 自定义dialog
 * Created by zhaoCe on 2016/10/12.
 */

public class MyDialog extends Dialog {
    Context mContext;
    LayoutInflater mInflater;
    public MyDialog(Context context) {
        super(context);
        mContext=context;
        mInflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
View view;//该界面的view
    ImageView mImgLoadData;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        view=mInflater.inflate(R.layout.my_dialog,null);
//        setContentView(view);
//        init();
//    }
    void init(){
        mImgLoadData= (ImageView) view.findViewById(R.id.img_loading_data);
        Animation animation= AnimationUtils.loadAnimation(mContext,R.anim.loading_data_anim);
        mImgLoadData.startAnimation(animation);
    }
}
