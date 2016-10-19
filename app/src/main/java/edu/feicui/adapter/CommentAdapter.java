package edu.feicui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import edu.feicui.entity.CommentDetailResponse;
import edu.feicui.everydaynews.R;

/**
 * 评论界面的listview的适配器
 * Created by zhaoCe on 2016/10/9.
 */

public class CommentAdapter extends MyBaseAdapter<CommentDetailResponse> {
    Context mContext;
    public CommentAdapter(Context mContext, ArrayList<CommentDetailResponse> mList, int mLayoutId) {
        super(mContext, mList, mLayoutId);
        this.mContext=mContext;
    }

    @Override
    public void setView(int position, View convertView, MyHolder holder, CommentDetailResponse response) {
        ImageView portrait= (ImageView) convertView.findViewById(R.id.img_comment_portrait);
        TextView uid= (TextView) convertView.findViewById(R.id.tv_comment_uid);
        TextView stamp= (TextView) convertView.findViewById(R.id.tv_comment_stamp);
        TextView content= (TextView) convertView.findViewById(R.id.tv_comment_content);

        Glide.with(mContext).load(response.portrait).into(portrait);
        uid.setText(response.uid);
        stamp.setText(response.stamp);
        content.setText(response.content);
    }
}
