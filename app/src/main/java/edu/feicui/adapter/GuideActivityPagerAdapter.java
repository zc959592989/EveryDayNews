package edu.feicui.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * 向导界面viewpager的适配器
 * Created by zhaoCe on 2016/9/26.
 */

public class GuideActivityPagerAdapter extends PagerAdapter{
    /**
     * 数据源
     */
    ArrayList<ImageView> mList;
    /**
     * 布局填充器的对象
     */
    LayoutInflater mInfalter;
    public GuideActivityPagerAdapter(ArrayList<ImageView> mList){
        this.mList=mList;
    }
    @Override
    public int getCount() {
        return mList==null?0:mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {


        ImageView img=mList.get(position);
        ViewPager.LayoutParams params=new ViewPager.LayoutParams();
        img.setLayoutParams(params);
        container.addView(img,params);
        return img;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mList.get(position));
    }
}
