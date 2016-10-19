package edu.feicui.activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import edu.feicui.SeverUrl;
import edu.feicui.Util.MyVolley;
import edu.feicui.activity.HomeActivity;
import edu.feicui.activity.NewsDetailActivity;
import edu.feicui.adapter.HomeNewsAdapter;
import edu.feicui.entity.News;
import edu.feicui.entity.NewsArray;
import edu.feicui.everydaynews.R;
import edu.feicui.view.MyDialog;
import edu.feicui.view.XListView;

/**
 * 新闻列表
 * Created by zhaoCe on 2016/9/28.
 */
public class HomeNewsFragment extends Fragment implements XListView.IXListViewListener, AdapterView.OnItemClickListener ,View.OnClickListener{
    HomeActivity mHomeActivity;
    Context mContext;
    /**
     * 主界面的数据源显示的listview
     */
    XListView mListView;
    /**
     * 该界面listview的适配器
     */
    HomeNewsAdapter adapterNews;
    View view;
    int dir=1;//1是下拉刷新   2是上拉加载
    int nid=0;//新闻id
    int subid=1;//默认进入新闻界面的新闻是军事类
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_home_news,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mListView= (XListView) view.findViewById(R.id.lv_home);
        init();
        getHttpData();
        mListView.setPullLoadEnable(true);
        mListView.setPullRefreshEnable(true);
        mListView.setXListViewListener(this);
        mListView.setOnItemClickListener(this);
    }
    TextView mTvMilitary;
    TextView mTvSociety;
    TextView mTvStock;
    TextView mTvFund;
    TextView mTvPhone;
    TextView mTvNBA;
    TextView mTvFA;
    /**
     * 初始化工作
     */
  void init(){
      mContext= getActivity();
      mHomeActivity = (HomeActivity) mContext;
      adapterNews =new HomeNewsAdapter(mContext,null,R.layout.item_list_home_news_fg);
      mTvMilitary= (TextView) view.findViewById(R.id.tv_fragment_home_news_military);
      mTvSociety= (TextView) view.findViewById(R.id.tv_fragment_home_news_usually_society);
      mTvStock= (TextView) view.findViewById(R.id.tv_fragment_home_news_usually_stock);
      mTvFund= (TextView) view.findViewById(R.id.tv_fragment_home_news_usually_fund);
      mTvPhone= (TextView) view.findViewById(R.id.tv_fragment_home_news_usually_phone);
      mTvNBA= (TextView) view.findViewById(R.id.tv_fragment_home_news_usually_nba);
      mTvFA= (TextView) view.findViewById(R.id.tv_fragment_home_news_usually_fa_premier_league);
      mHomeActivity.setOnClick(this,mTvMilitary,mTvSociety,mTvStock,mTvFund,mTvPhone,mTvNBA,mTvFA);
      mListView.setAdapter(adapterNews);
      mHomeActivity.mTvTitle.setText("资讯");
      mTvMilitary.setSelected(true);
  }
    /**
     * 获取服务器的数据
     */
    void getHttpData(){
        Calendar calendar=new GregorianCalendar();
        Date date= calendar.getTime();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss"); //设置日期格式
        String time= sdf.format(date);
        mListView.setRefreshTime(time);


        final MyDialog myDialog=new MyDialog(mHomeActivity);
        View view= mHomeActivity.mInflater.inflate(R.layout.my_dialog,null);
        ImageView imageView= (ImageView) view.findViewById(R.id.img_loading_data);
        Animation animation= AnimationUtils.loadAnimation(mHomeActivity,R.anim.loading_data_anim);
        imageView.startAnimation(animation);
        myDialog.setContentView(view);
        myDialog.show();

        Map<String,String> params=new HashMap<>();
        params.put("ver","0000000");
        params.put("subid",""+subid);
        params.put("dir",""+dir);
        params.put("nid",""+nid);
        params.put("stamp","20140321000000");
        params.put("cnt","20");
        MyVolley.get(mContext, SeverUrl.NEWS_LIST, params, new MyVolley.OnVolleyResult() {//get请求
            @Override
            public void success(String s) {
                Gson gson=new Gson();
                NewsArray array=gson.fromJson(s, NewsArray.class);
                if(array.data!=null&&array.data.size()>0){
                    adapterNews.mList.addAll(array.data);
                    adapterNews.notifyDataSetChanged();
                }
                mListView.stopLoadMore();
                mListView.stopRefresh();
                myDialog.dismiss();
            }
            @Override
            public void failed(VolleyError volleyError) {
                myDialog.dismiss();//关闭加载框
                Toast.makeText(mContext,"失败！",Toast.LENGTH_SHORT).show();
                mListView.stopLoadMore();
                mListView.stopRefresh();
            }
        });
    }

    @Override
    public void onRefresh() {
        dir=1;
        adapterNews.mList.clear();
        getHttpData();
    }
    @Override
    public void onLoadMore() {
        dir=2;
        if(adapterNews.mList.size()>0){
            News news= adapterNews.mList.get(adapterNews.mList.size()-1);
            nid=news.nid;
        }
        getHttpData();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        News news= adapterNews.mList.get(position-1);//position-1   是因为xListView的头和尾（上拉加载，下拉刷新）占用了0的位置，所以在显示的时候，应该给position-1
        Intent intent=new Intent();
        intent.putExtra("link",news.link);
        intent.putExtra("nid",news.nid);
        if(mHomeActivity.response!=null){
            intent.putExtra("response",mHomeActivity.response);
        }
        intent.setClass(mContext, NewsDetailActivity.class);
        mHomeActivity.startActivity(intent);
    }

    /**
     * 设置选择状态
     * @param view true 状态的view
     * @param views false 状态的view
     */
    public void setSelected(View view,View ...views){
        view.setSelected(true);
        for(View view1:views){
            view1.setSelected(false);
        }
    }

    /**
     * 切换新闻
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_fragment_home_news_military://军事新闻 TextView mTvMilitary;
                setSelected(mTvMilitary,mTvSociety,mTvStock,mTvFund,mTvPhone,mTvNBA,mTvFA);
                subid=1;
                break;
            case R.id.tv_fragment_home_news_usually_society://社会新闻TextView mTvSociety;
                setSelected(mTvSociety,mTvMilitary,mTvStock,mTvFund,mTvPhone,mTvNBA,mTvFA);
                subid=2;
                break;
            case R.id.tv_fragment_home_news_usually_stock://股票新闻TextView mTvStock;
                setSelected(mTvStock,mTvMilitary,mTvSociety,mTvFund,mTvPhone,mTvNBA,mTvFA);
                subid=3;
                break;
            case R.id.tv_fragment_home_news_usually_fund://基金新闻TextView mTvFund;
                setSelected(mTvFund,mTvMilitary,mTvSociety,mTvStock,mTvPhone,mTvNBA,mTvFA);
                subid=4;
                break;
            case R.id.tv_fragment_home_news_usually_phone://手机新闻 TextView mTvPhone;
                setSelected(mTvPhone,mTvMilitary,mTvSociety,mTvStock,mTvFund,mTvNBA,mTvFA);
                subid=5;
                break;
            case R.id.tv_fragment_home_news_usually_nba://NBA新闻 TextView mTvNBA;
                setSelected(mTvNBA,mTvMilitary,mTvSociety,mTvStock,mTvFund,mTvPhone,mTvFA);
                subid=8;
                break;
            case R.id.tv_fragment_home_news_usually_fa_premier_league://英超新闻TextView mTvFA;
                setSelected(mTvFA,mTvMilitary,mTvSociety,mTvStock,mTvFund,mTvPhone,mTvNBA);
                subid=7;
                break;
        }
        adapterNews.mList.clear();
        getHttpData();
    }
}
