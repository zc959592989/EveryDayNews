package edu.feicui.activity;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import edu.feicui.SeverUrl;
import edu.feicui.Util.MyVolley;
import edu.feicui.activity.fragment.HomeNewsFragment;
import edu.feicui.activity.fragment.LoginFragment;
import edu.feicui.adapter.HomeLeftAdapter;
import edu.feicui.entity.LeftItem;
import edu.feicui.entity.RegisterResponse;
import edu.feicui.entity.UpdateResponse;
import edu.feicui.everydaynews.R;
import edu.zx.slidingmenu.SlidingMenu;

/**
 * 主界面
 * Created by zhaoCe on 2016/9/26.
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    /**
     * 登陆之后，服务器返回的信息
     */
   public RegisterResponse response;
    /**
     * 传入用户中心的名字
     */
    String loginName;
    /**
     * 跳转登陆界面按钮
     */
    ImageView mImgLogin;
    /**
     * 初始化loginFragment对象
     */
    LoginFragment loginFragment=new LoginFragment();
    /**
     * 屏幕左滑listview适配器
     */
    HomeLeftAdapter adapterLeft;
    /**
     * 初始化一个HomeFragment对象
     */
   HomeNewsFragment homeFragment=new HomeNewsFragment();

    /**
     * 左滑的listview
     */
    ListView mListViewLeft;
    /**
     * 右滑的界面
     */
    LinearLayout mTvRight;
    /**
     * 版本更新按钮
     */
    TextView mTvUpdate;
    /**
     * 选择更多新闻按钮
     */
    TextView mTvMore;
    /**
     * fragment管理器
     */
    FragmentManager fragmentManager;
    /**
     * 登陆成功，显示用户名
     */
    TextView mTvCenterName;
    /**
     * 登陆成功的响应
     */
    boolean flag=true;
    /**
     * 侧滑对象
     */
    SlidingMenu menu;
    DownloadManager manager;
    /**
     * fragment事务管理的操作对象
     */
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

     @Override
    public void initView() {
         manager= (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
         menu=new SlidingMenu(this);
         menu.attachToActivity(this,SlidingMenu.SLIDING_CONTENT);//不含标题栏
         LayoutInflater inflater=getLayoutInflater();
         View viewLeft=inflater.inflate(R.layout.activity_home_view_left,null);
         View viewRight=inflater.inflate(R.layout.activity_home_view_right,null);
         menu.setMenu(viewLeft);
         menu.setSecondaryMenu(viewRight);
         menu.setMode(SlidingMenu.LEFT_RIGHT);
         menu.setBehindOffset(400);
         menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//全屏触摸滑动

        mListViewLeft= (ListView) viewLeft.findViewById(R.id.lv_home_left);
        mTvRight= (LinearLayout) viewRight.findViewById(R.id.ll_home_right);
        mTvMore= (TextView) findViewById(R.id.tv_choose_more_news_home);
        mImgLogin= (ImageView) viewRight.findViewById(R.id.img_home_right_login);
        mTvCenterName= (TextView) viewRight.findViewById(R.id.tv_home_center_name);
        mTvUpdate= (TextView) viewRight.findViewById(R.id.tv_home_update);


        setOnClick(this,mImgLeft,mImgRight,mImgLogin,mTvUpdate);
        mListViewLeft.setOnItemClickListener(this);
        fragmentManager=getSupportFragmentManager();
        fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.ll_home_fg,homeFragment);
        fragmentTransaction.commit();

        ArrayList<LeftItem> listLeft=new ArrayList<>();
        listLeft.add(new LeftItem(R.mipmap.biz_navigation_tab_news,"新闻","NEWS"));
        listLeft.add(new LeftItem(R.mipmap.biz_navigation_tab_read,"收藏","FAVORITE"));
        listLeft.add(new LeftItem(R.mipmap.biz_navigation_tab_local_news,"本地","LOCAL"));
        listLeft.add(new LeftItem(R.mipmap.biz_navigation_tab_ties,"跟帖","COMMENT"));
        listLeft.add(new LeftItem(R.mipmap.biz_navigation_tab_pics,"图片","PHOTO"));
        adapterLeft=new HomeLeftAdapter(this,listLeft,R.layout.item_list_home_left);
        mListViewLeft.setAdapter(adapterLeft);
        mTvCenterName.setText("立即登陆");
        Intent intentLoad= getIntent();
        int loadInt= intentLoad.getIntExtra("int",1);//返回值判断，是否携带着登录返回的response

         //从个人中心回退到该界面
        if(loadInt==1){//携带着登录返回的response
            i++;
            Intent intent=getIntent();
            flag=intent.getBooleanExtra("flag",true);
            response= (RegisterResponse) intent.getSerializableExtra("response");
            String photo_path= intent.getStringExtra("photo_path");
            loginName=intent.getStringExtra("loginName");
            mTvCenterName.setText(loginName);
            mImgLogin.setImageBitmap(BitmapFactory.decodeFile(photo_path));
        }
    }
    int i=0;//判断跳转到哪个activity
    DownloadReceiver downloadReceiver;
    @Override
    protected void onResume() {
        super.onResume();
        downloadReceiver=new DownloadReceiver();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downloadReceiver,intentFilter);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(downloadReceiver);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        fragmentTransaction= fragmentManager.beginTransaction();
        switch (v.getId()){
            case R.id.img_base_activity_left://左滑界面
                menu.showMenu();
                break;
            case R.id.img_base_activity_right://右滑界面
                menu.showSecondaryMenu();
                break;
            case R.id.tv_choose_more_news_home://更多新闻按钮
                break;
            case R.id.img_home_right_login:
            {
                if(flag&&i==0){//跳到登陆界面
                    fragmentTransaction.replace(R.id.ll_home_fg,loginFragment);
                    fragmentTransaction.commit();
                    menu.showContent();
                }else{//跳到用户中心界面
                    Intent intent=new Intent();
                    intent.putExtra("loginName",loginName);
                    intent.putExtra("success",response);
                    intent.setClass(this,MyAccountActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
                break;
            case R.id.tv_home_update://版本更新
            {
                Map<String,String> params=new HashMap<>();
                params.put("imei","000000000000000");
                params.put("pkg","edu.feicui.everydaynews");
                params.put("ver","0000000");
                MyVolley.get(this, SeverUrl.UPDATE, params, new MyVolley.OnVolleyResult() {
                    @Override
                    public void success(String s) {
                        Gson gson=new Gson();
                        UpdateResponse update= gson.fromJson(s, UpdateResponse.class);
                        if(update!=null){
//                            download("");
                            Toast.makeText(HomeActivity.this,"已经是最新版本",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void failed(VolleyError volleyError) {
                    }
                });
            }
                break;
        }
    }
    long downloadID;

    /**
     * 下载最新版本的apk
     * @param downloadUri apk地址
     * @param downloadPath 下载到本地的路径
     */
    private void download(String downloadUri,String downloadPath){
        DownloadManager.Request request=new DownloadManager.Request(Uri.parse(downloadUri));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE| DownloadManager.Request.NETWORK_WIFI);
        request.setShowRunningNotification(true);
        request.setDestinationInExternalFilesDir(this,null,downloadPath);
        downloadID= manager.enqueue(request);
    }
    public class DownloadReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
           long id= intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1);
            if(downloadID==id){
                Uri uri= manager.getUriForDownloadedFile(id);
                String mimeType= manager.getMimeTypeForDownloadedFile(id);
                Intent goToAPK=new Intent();
                goToAPK.setAction(Intent.ACTION_VIEW);
                goToAPK.setDataAndType(uri,mimeType);
                startActivity(goToAPK);
            }
        }
    }

    /**
     * 左滑界面的子条目点击事件
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        fragmentTransaction=fragmentManager.beginTransaction();
        switch (position){
            case 0://跳到新闻列表
            {
                fragmentTransaction.replace(R.id.ll_home_fg,homeFragment);
                fragmentTransaction.commit();
                menu.showContent();
            }
                break;
            case 1://跳转收藏界面
                Toast.makeText(this,"收藏界面",Toast.LENGTH_SHORT).show();
                menu.showContent();
                break;
            case 2://跳转本地新闻界面
                Toast.makeText(this,"本地新闻界面",Toast.LENGTH_SHORT).show();
                menu.showContent();
                break;
            case 3://跳转跟帖界面
                Toast.makeText(this,"跟帖界面",Toast.LENGTH_SHORT).show();
                menu.showContent();
                break;
            case 4://跳转图片界面
                Toast.makeText(this,"图片界面",Toast.LENGTH_SHORT).show();
                menu.showContent();
                break;

        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode == KeyEvent.KEYCODE_BACK);
        {
            exitBy2Click();
        }
        return false;
    }
    private static Boolean isExit = false;
    private void exitBy2Click() {//双击退出
        // TODO Auto-generated method stub
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "请再次点击返回键退出", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        }
        else {
            finish();
            System.exit(0);
        }
    }
}
