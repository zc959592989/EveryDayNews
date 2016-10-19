package edu.feicui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import edu.feicui.everydaynews.R;

/**
 * Created by Administrator on 2016/9/27.
 */

public abstract class MyBaseAdapter<T> extends BaseAdapter {
    Context mContext;
   public ArrayList<T> mList;
    LayoutInflater mInflater;
    int mLayoutId;
    public MyBaseAdapter(Context mContext,ArrayList<T> mList,int mLayoutId){
        this.mContext=mContext;
        if(mList==null){
            mList=new ArrayList<>();
        }
        this.mList=mList;
        mInflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       this.mLayoutId=mLayoutId;
    }
    @Override
    public int getCount() {
        return mList==null?0:mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyHolder holder;
        if(convertView==null){
            convertView=mInflater.inflate(mLayoutId,null);
            holder=new MyHolder();
            convertView.setTag(holder);
        }else{
            holder= (MyHolder) convertView.getTag();
        }
        setView(position,convertView,holder,mList.get(position));
        return convertView;
    }

    /**
     * 渲染view
     * @param position  对应条目的下标
     * @param convertView 对应条目的view
     * @param holder 对应条目的holder
     * @param t 对应条目数据的实体
     */
    public abstract void setView(int position,View convertView,MyHolder holder,T t);
    class MyHolder{
    }
}
