package edu.feicui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import edu.feicui.entity.News;
import edu.feicui.entity.NewsArray;
import edu.feicui.everydaynews.R;

/**
 * HomeAvtivity中listview显示新闻列表的适配器
 * Created by zhaoCe on 2016/9/27.
 */

public class HomeNewsAdapter extends MyBaseAdapter<News> {
    Context mContext;

    public HomeNewsAdapter(Context mContext, ArrayList<News> mList, int layoutId) {
        super(mContext, mList,layoutId);
        this.mContext=mContext;
    }

    @Override
    public void setView(int position, View convertView, MyHolder holder, News news) {
        TextView title= (TextView) convertView.findViewById(R.id.tv_item_home_news_title);
        TextView content= (TextView) convertView.findViewById(R.id.tv_item_home_news_content);
        TextView time= (TextView) convertView.findViewById(R.id.tv_item_home_news_time);
        ImageView iconIv= (ImageView) convertView.findViewById(R.id.img_item_home_news_icon);
        title.setText(news.title);
        content.setText(news.summary);
        time.setText(news.stamp);
        String icon=news.icon;//获取到图片的url
        Glide.with(mContext).load(icon).into(iconIv);//第三方jar将图片的url加载到view上
    }

    /**
     * 将url转换成bitmap
     * @param url 图片的链接
     * @return 返回一个bitmap
     */
    public Bitmap getBitmap(String url) {
        Bitmap bm = null;
        try {
            URL iconUrl = new URL(url);
            URLConnection conn = iconUrl.openConnection();
            HttpURLConnection http = (HttpURLConnection) conn;
            int length = http.getContentLength();
            conn.connect();
            // 获得图像的字符流
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is, length);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();// 关闭流
            ((HttpURLConnection) conn).disconnect();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }
}
