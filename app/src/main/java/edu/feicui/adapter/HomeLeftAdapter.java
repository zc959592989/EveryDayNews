package edu.feicui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.feicui.entity.LeftItem;
import edu.feicui.everydaynews.R;

/**
 * 界面左滑的listview适配器
 * Created by Administrator on 2016/9/28.
 */

public class HomeLeftAdapter extends MyBaseAdapter<LeftItem> {
    public HomeLeftAdapter(Context mContext, ArrayList<LeftItem> mList, int mLayoutId) {
        super(mContext, mList, mLayoutId);
    }

    @Override
    public void setView(int position, View convertView, MyHolder holder, LeftItem leftItem) {
        ImageView icon= (ImageView) convertView.findViewById(R.id.img_item_home_left);
        TextView chinese= (TextView) convertView.findViewById(R.id.tv_item_home_left_title_chinese);
        TextView english= (TextView) convertView.findViewById(R.id.tv_item_home_left_title_english);

        icon.setImageResource(leftItem.icon);
        chinese.setText(leftItem.titleChinese);
        english.setText(leftItem.titleEnglish);
    }


}
