package edu.feicui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
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
import edu.feicui.adapter.CommentAdapter;
import edu.feicui.entity.CommentDetailResponse;
import edu.feicui.entity.CommentResponse;
import edu.feicui.entity.News;
import edu.feicui.entity.RegisterResponse;
import edu.feicui.everydaynews.R;
import edu.feicui.view.XListView;

/**
 * 查看，发布评论界面
 * Created by zhaoCe on 2016/10/9.
 */

public class CommentActivity extends BaseActivity implements View.OnClickListener, XListView.IXListViewListener {
    int cid=0;
    int dir=1;
    /**
     * 该界面的适配器
     */
    CommentAdapter adapter;
    /**
     * 评论编辑框
     */
    TextInputEditText mEdtContent;
    /**
     * 发布评论按钮
     */
    ImageView mImgSend;
    /**
     * 新闻id
     */
    int nid;
    /**
     * 拿到的用户登录信息
     */
    public RegisterResponse response;
    /**
     *显示评论的listView
     */
    XListView mListView;
    TextView mTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
    }

    @Override
    public void initView() {
        mImgLeft.setBackgroundResource(R.mipmap.back);
        mImgRight.setVisibility(View.INVISIBLE);
        mTvTitle.setText("评论");
        Intent intent=getIntent();
        nid=intent.getIntExtra("nid",0);
        response= (RegisterResponse) intent.getSerializableExtra("response");

        mListView= (XListView) findViewById(R.id.lv_comment_activity);
        mImgSend= (ImageView) findViewById(R.id.img_comment_activity_send_content);
        mEdtContent= (TextInputEditText) findViewById(R.id.edt_comment_activity_content);
//        mTv= (TextView) mListView.findViewById(R.id.tv_xlist_view_header_updata_date);



        adapter=new CommentAdapter(this,null,R.layout.item_comment_activity_list_view);
        mListView.setAdapter(adapter);
        mListView.setPullLoadEnable(true);
        mListView.setPullRefreshEnable(true);
        mListView.setXListViewListener(this);
        setOnClick(this,mImgLeft,mImgSend);
        getHttpData();

    }

    /**
     * 从服务器获取评论数据
     */
    void getHttpData(){
        Calendar calendar=new GregorianCalendar();
        Date date= calendar.getTime();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss"); //设置日期格式
        String time= sdf.format(date);
        mListView.setRefreshTime(time);

        final ProgressDialog dialog= ProgressDialog.show(this,"","加载中...");
        Map<String,String> params=new HashMap<>();
        params.put("ver","0000000");
        params.put("dir",""+dir);
        params.put("cid",""+cid);
        params.put("nid",""+nid);
        params.put("stamp","20140321");
        params.put("cnt","20");
        params.put("type","1");

        MyVolley.get(this, SeverUrl.CMT_LIST, params, new MyVolley.OnVolleyResult() {//get请求
            @Override
            public void success(String s) {
                Gson gson=new Gson();
                CommentResponse array=gson.fromJson(s, CommentResponse.class);
                if(array.data!=null&&array.data.size()>0){
                    adapter.mList.addAll(array.data);
                    adapter.notifyDataSetChanged();
                }
                mListView.stopLoadMore();
                mListView.stopRefresh();
                dialog.dismiss();//在数据刷新之后，关闭加载框
            }
            @Override
            public void failed(VolleyError volleyError) {
                dialog.dismiss();//关闭加载框
                Toast.makeText(CommentActivity.this,"失败！",Toast.LENGTH_SHORT).show();
                mListView.stopLoadMore();
                mListView.stopRefresh();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_base_activity_left://回退
                finish();
                break;
            case R.id.img_comment_activity_send_content://发布评论按钮
            {
                //cmt_commit?ver=版本号&nid=新闻编号&token=用户令牌&imei=手机标识符&ctx=评论内容
                String ctx=mEdtContent.getText().toString();
                if(ctx!=null&&ctx.length()>0){
                    Map<String,String> params=new HashMap<>();
                    params.put("ver","0000000");
                    params.put("nid",""+nid);
                    if(response!=null){
                        params.put("token",response.data.token);
                    }else{
                        Toast.makeText(this,"请先登录",Toast.LENGTH_SHORT).show();
                        params.put("token","无效");
                    }
                    params.put("imei","000000000000000");
                    params.put("ctx",ctx);
                    MyVolley.get(this, SeverUrl.CMT_COMMIT, params, new MyVolley.OnVolleyResult() {
                        @Override
                        public void success(String s) {
                            mEdtContent.setText("");
                            Toast.makeText(CommentActivity.this,"评论成功",Toast.LENGTH_SHORT).show();
                            cid=0;
                            adapter.mList.clear();
                            getHttpData();
                        }
                        @Override
                        public void failed(VolleyError volleyError) {
                        }
                    });
                }else{
                    Toast.makeText(this,"评论内容不能为空",Toast.LENGTH_SHORT).show();
                }
            }
                break;
        }
    }

    @Override
    public void onRefresh() {
        dir=1;
        adapter.mList.clear();
        getHttpData();
    }

    @Override
    public void onLoadMore() {
        dir=2;
        if(adapter.mList.size()>0){
            CommentDetailResponse comment=adapter.mList.get(adapter.mList.size()-1);
            cid=comment.cid;
        }
        getHttpData();
    }
}
