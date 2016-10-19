package edu.feicui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * 封装RecyclerView.Adapter的基类
 * Created by zhaoCe on 2016/10/17.
 */

public abstract class MyRecyclerViewBaseAdapter<T> extends RecyclerView.Adapter<MyRecyclerViewBaseAdapter.MyHolder> {
    Context mContext;
    LayoutInflater mInflater;
    ArrayList<T> mList;
    int mLayoutID;
    public MyRecyclerViewBaseAdapter(ArrayList<T> mList, Context mContext, int mLayoutID){
        this.mContext=mContext;
        this.mInflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mList=mList;
        this.mLayoutID=mLayoutID;
    }

    @Override
    public MyRecyclerViewBaseAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return MyHolder.getHolder(mContext,parent,mLayoutID);
    }
    @Override
    public void onBindViewHolder(MyRecyclerViewBaseAdapter.MyHolder holder, int position) {
        setView(holder,position);
        holder.mConvertView.setOnClickListener(new MyListener(holder,position));
    }

    @Override
    public int getItemCount() {
        return mList==null?0:mList.size();
    }
    public abstract void setView(MyHolder holder,int position);
    /**
     * 外部类实现接口监听
     */
    public class MyListener implements View.OnClickListener{
        protected MyHolder holder;
        protected int position;
        public MyListener(MyHolder holder,int position){
            this.holder=holder;
            this.position=position;
        }
        @Override
        public void onClick(View v) {
            setClick(holder,position);
        }
    }

    /**
     * 点击子条目后的具体操作
     * @param holder 子条目的holder
     * @param position 子条目的下标
     */
    public  void setClick(MyHolder holder,int position){}
    public static class MyHolder<V extends View> extends RecyclerView.ViewHolder{
        public ArrayList<View> mViews;
        public View mConvertView;
        private Context mContext;
        public MyHolder(Context mContext,View itemView,ViewGroup parent) {
            super(itemView);
            this.mContext=mContext;
            mConvertView=itemView;
            mViews=new ArrayList<>();
        }

        /**
         * 拿到holder
         * @param mContext 上下文
         * @param parent 装子条目的view
         * @param layoutID 子条目布局id
         * @return  返回holder
         */
        public static MyHolder getHolder(Context mContext,ViewGroup parent,int layoutID){
           LayoutInflater mInflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view=mInflater.inflate(layoutID,parent,false);
            return new MyHolder(mContext,view,parent);
        }

        /**
         * holder中找出控件的方法（每个holder都有一套控件）
         * @param itemViewID 控件id
         * @return
         */
        public  V findViewById(int itemViewID){
            View view=mConvertView.findViewById(itemViewID);
            mViews.add(view);
            return (V) view;
        }
//        public MyHolder setText(int itemViewID,String str){
//            TextView mTv= (TextView) getView(itemViewID);
//            mTv.setText(str);
//            return this;
//        }
//        public MyHolder setImageResource(int itemViewID,int resourceID){
//            ImageView mImg= (ImageView) mConvertView.findViewById(itemViewID);
//            mImg.setImageResource(resourceID);
//            return this;
//        }
    }
}
