package edu.feicui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.feicui.Util.MyVolley;
import edu.feicui.adapter.GuideActivityPagerAdapter;
import edu.feicui.everydaynews.R;

/**
 * 向导界面
 * Created by zhaoCe on 2016/9/26.
 */
public class GuideActivity extends Activity implements ViewPager.OnPageChangeListener {
    /**
     * 向导界面的显示滑动控件
     */
    ViewPager mVp;

    /**
     *
     */
    int []pic={R.mipmap.welcome,R.mipmap.wy,R.mipmap.bd,R.mipmap.small};
    /**
     * 数据源
     */
    ArrayList<ImageView> list;
    ImageView mImg1;//小红点
    ImageView mImg2;//小红点
    ImageView mImg3;//小红点
    ImageView mImg4;//小红点

    /**
     * 装小红点的数组
     */
    ArrayList<ImageView> imgs=new ArrayList<>();
    /**
     * 存储小数据
     */
    SharedPreferences mPreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        init();
        if(!getData()){//如果不是第一次进入app，则直接跳过该界面，进入加载界面
            Intent intent=new Intent();
            intent.setClass(GuideActivity.this,LoadActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * 初始化
     */
     void init(){
         mVp= (ViewPager) findViewById(R.id.vp_guide);
         mImg1= (ImageView) findViewById(R.id.img1_guide);
         mImg2= (ImageView) findViewById(R.id.img2_guide);
         mImg3= (ImageView) findViewById(R.id.img3_guide);
         mImg4= (ImageView) findViewById(R.id.img4_guide);
         imgs.add(mImg1);
         imgs.add(mImg2);
         imgs.add(mImg3);
         imgs.add(mImg4);
         list=new ArrayList<>();
         for (int i = 0; i < pic.length; i++) {
             ImageView img=new ImageView(this);
             img.setBackgroundResource(pic[i]);
             list.add(img);
         }
         GuideActivityPagerAdapter adapter=new GuideActivityPagerAdapter(list);
         mVp.setAdapter(adapter);
         mVp.setOnPageChangeListener(this);

         Map<String,String> params=new HashMap<>();
         params.put("","");
         MyVolley.post(this, "https://www.baidu.com/", params, new MyVolley.OnVolleyResult() {
             @Override
             public void success(String s) {
                 Log.e("aac", "success: -------成功" );
             }

             @Override
             public void failed(VolleyError volleyError) {
                 Log.e("aac", "failed: -----------失败");
             }
         });

     }

    /**
     * volley框架的String请求
     *
     */
    public void stringRequest(){
        RequestQueue queue= Volley.newRequestQueue(this);
        StringRequest request=new StringRequest(Request.Method.GET, "https://www.baidu.com/", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("aac", "onResponse: --------"+"success" );

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("aac", "onResponse: --------"+"fail" );
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };
        queue.add(request);
    }

    /**
     * volley框架json请求
     * 该类方法有问题，不能通过数据拿到正确的返回数据
     */
    public void jsonResquest(){
        RequestQueue queue= Volley.newRequestQueue(this);
//        JsonRequest jsonR=new JsonObjectRequest(Request.Method.GET,"",);
    }


    /**
     * 判断是不是第一次进入该app
     * @return
     */
    private boolean getData(){
        mPreference=getSharedPreferences("Preference_shared",MODE_PRIVATE);
       boolean isFirst= mPreference.getBoolean("is_first",true);
        return isFirst;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }
    boolean flag=false;
    int count=0;
    @Override
    public void onPageSelected(int position) {
        for (ImageView img:
             imgs) {
            img.setImageResource(R.mipmap.a5);
        }

        imgs.get(position).setImageResource(R.mipmap.a4);
        if(position==3){
            flag=true;
            count++;
        }
        if(flag){
            mVp.setCurrentItem(3);//设置viewpager的滑动条目下标
            if(count==1){//设置只开一次timer
                CountDownTimer timer=new CountDownTimer(3000,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }
                    @Override
                    public void onFinish() {
                        SharedPreferences.Editor edit= mPreference.edit();
                        edit.putBoolean("is_first",false);
                        edit.commit();
                        Intent intent=new Intent();
                        intent.setClass(GuideActivity.this,LoadActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }.start();

            }
             if(count>=2){
                Toast.makeText(this,"不能滑动，页面将自动跳转",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}

