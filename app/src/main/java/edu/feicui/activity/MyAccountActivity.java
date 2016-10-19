package edu.feicui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import edu.feicui.SeverUrl;
import edu.feicui.Util.MyVolley;
import edu.feicui.adapter.LoginNoteAdapter;
import edu.feicui.entity.LoginNoteResponse;
import edu.feicui.entity.RegisterResponse;
import edu.feicui.everydaynews.R;

/**
 * 用户中心界面
 * Created by zhaoCe on 2016/9/30.
 */

public class MyAccountActivity extends BaseActivity implements View.OnClickListener{
    SharedPreferences mSharedPreferences;
    /**
     * 系统图片路径
     */
    String path;
    /**
     * 照片文件夹路径
     */
    public static final String DIR_PATH= Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"EveryDayNews";
    /**
     * 照片文件夹路径
     */
    public static final String PHOTO_PATH=DIR_PATH+File.separator+"photo.png";

    /**
     * 退出登录按钮
     */
    Button mBtnOutLogin;
    /**
     * 运用相机拍照
     */
    LinearLayout mLLCamera;
    /**
     * 调用图库
     */
    LinearLayout mLLImage;
    /**
     * 图片设置选择框
     */
    PopupWindow mPopPhoto;
    /**
     * 用户头像
     */
    ImageView mImgPhoto;
    /**
     * 登陆日志listview适配器
     */
    LoginNoteAdapter mLoginNoteAdapter;
    /**
     * 用户积分
     */
    TextView mTvIntegration;
    /**
     * 跟帖数量
     */
    TextView mTvCommentNum;
    /**
     * 服务端的响应
     */
    RegisterResponse response;
    /**
     *用户名
     */
    TextView mTvCenterName;
    /**
     * 登陆日志
     */
    ListView mLvLoginNote;
    /**
     * 登陆用户名
     */
    String loginName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_acount);
    }
boolean flag=true;
    @Override
    public void initView() {
        mImgLeft.setBackgroundResource(R.mipmap.back);
        mTvTitle.setText("我的账号");
        mImgRight.setVisibility(View.INVISIBLE);

        mSharedPreferences=getSharedPreferences("photo_path",MODE_PRIVATE);

        mTvIntegration= (TextView) findViewById(R.id.tv_account_integration);
        mTvCommentNum= (TextView) findViewById(R.id.tv_account_comnum);
        mTvCenterName=(TextView) findViewById(R.id.tv_center_name);
        mImgPhoto= (ImageView) findViewById(R.id.img_acount_photo);
        mBtnOutLogin= (Button) findViewById(R.id.btn_my_account_out_login);
        mLvLoginNote= (ListView) findViewById(R.id.lv_account_activity);


        Intent intent=getIntent();
        response= (RegisterResponse) intent.getSerializableExtra("success");//接收服务器端的响应
        if(response==null)
            {
            flag=false;
            }
        loginName=intent.getStringExtra("loginName");//接收登陆成功后的用户名
        mTvCenterName.setText(loginName);//显示在用户中心
        View view=getLayoutInflater().inflate(R.layout.popup_window_photo,null);
        mPopPhoto=new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        mLLCamera= (LinearLayout) view.findViewById(R.id.ll_account_pop_camera);
        mLLImage= (LinearLayout) view.findViewById(R.id.ll_account_pop_image);
        mPopPhoto.setOutsideTouchable(true);
        mPopPhoto.setBackgroundDrawable(new BitmapDrawable());

        File file=new File(PHOTO_PATH);
        if(file.exists()){//此项目 头像是在本地文件下的，所以只要存在该文件，就直接设置头像
            mImgPhoto.setImageBitmap(BitmapFactory.decodeFile(PHOTO_PATH));
        }
       String photo_path= mSharedPreferences.getString("path","无此路径");
        if(!photo_path.equals("无此路径")){
            mImgPhoto.setImageBitmap(BitmapFactory.decodeFile(photo_path));
        }
        setOnClick(this,mImgLeft,mImgPhoto,mLLCamera,mLLImage,mBtnOutLogin);
        mLoginNoteAdapter=new LoginNoteAdapter(this,null,R.layout.item_account_login_note);
        mLvLoginNote.setAdapter(mLoginNoteAdapter);
        getHttpData();//调接口

    }

    /**
     * 获取服务器中的用户登陆信息的数据
     */
    void getHttpData(){
        Map<String,String> params=new HashMap<>();
        params.put("ver","0000000");
        params.put("imei","000000000000000");
        if(response!=null){
            params.put("token",response.data.token);
        }else{
            return;
        }
        MyVolley.get(this, SeverUrl.USER_HOME, params, new MyVolley.OnVolleyResult() {
            @Override
            public void success(String s) {
                Gson gson=new Gson();
                LoginNoteResponse loginNoteResponse=  gson.fromJson(s, LoginNoteResponse.class);
                mTvIntegration.setText("积分:"+loginNoteResponse.data.integration);
                mTvCommentNum.setText("跟帖数量:"+loginNoteResponse.data.comnum);
                mLoginNoteAdapter.mList=loginNoteResponse.data.loginlog;
                mLoginNoteAdapter.notifyDataSetChanged();
            }

            @Override
            public void failed(VolleyError volleyError) {

            }
        });

    }


    @Override
    public void onClick(View v) {
        Intent intent=new Intent();
        switch (v.getId()){
            case R.id.img_base_activity_left://回退   跳转到新闻界面
            {
                Log.e("aac", "onClick: "+loginName );
                intent.putExtra("loginName",loginName);
                if(flag){
                intent.putExtra("response",response);
            }
                intent.putExtra("photo_path",PHOTO_PATH);
                intent.putExtra("flag",flag);
                intent.setClass(this,HomeActivity.class);//需要传值
                startActivity(intent);
                finish();
            }
                break;
            case R.id.img_acount_photo://展示弹框，进行头像设置选择
            {
                mPopPhoto.showAtLocation(mImgPhoto, Gravity.BOTTOM,0,0);
            }
                break;
            case R.id.ll_account_pop_camera://跳转到相机
                Toast.makeText(this,"camera",Toast.LENGTH_SHORT).show();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//大于api23的手机，需要权限
                    if(checkSelfPermission("android.Manifest.permission.CAMERA")== PackageManager.PERMISSION_GRANTED
                            &&checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){//有权限
                        File file=new File(DIR_PATH);
                        if(!file.exists()){
                            file.mkdirs();
                        }
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(PHOTO_PATH)));
                        startActivityForResult(intent,202);
                    }else{//没权限，就要申请
                        requestPermissions(new String[]{"android.Manifest.permission.CAMERA",android.Manifest.permission.WRITE_EXTERNAL_STORAGE},201);
                    }
                }else{//小于api23   不用权限申请，直接进行操作
                    File file=new File(DIR_PATH);
                    if(!file.exists()){
                        file.mkdirs();
                    }
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(PHOTO_PATH)));
                    startActivityForResult(intent,202);
                }
                mPopPhoto.dismiss();
                break;
            case R.id.ll_account_pop_image://跳到图库
                Toast.makeText(this,"image",Toast.LENGTH_SHORT).show();
                Intent intent1=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent1,203);
                mPopPhoto.dismiss();
                break;
            case R.id.btn_my_account_out_login://退出登录按钮
            {
                intent.putExtra("int",2);
                response=null;
                intent.setClass(this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 201://申请权限
            {
                if(permissions[0].equals("android.Manifest.permission.CAMERA")){
                    if(grantResults[0]==PackageManager.PERMISSION_GRANTED&&grantResults[1]==PackageManager.PERMISSION_GRANTED){//获取到权限   判断系统的弹框，有没有确定权限
                        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File file=new File(DIR_PATH);
                        if(!file.exists()){
                            file.mkdirs();
                        }
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(PHOTO_PATH)));
                        startActivityForResult(intent,202);
                    }else{
                        Toast.makeText(this,"打开相机或上传头像需要使用权限，权限管理—应用—相应权限",Toast.LENGTH_SHORT).show();
                    }
                }
            }
                break;
        }
    }
    Bitmap bitmap;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 202://对应请求码
                if(resultCode==RESULT_OK){
                    bitmap= BitmapFactory.decodeFile(PHOTO_PATH);
                    mImgPhoto.setImageBitmap(bitmap);
                }
                break;
            case 203:
            {
                if(resultCode==RESULT_OK){
                   Uri uri= data.getData();
                    String[] filePathColumn={MediaStore.Images.Media.DATA};
                    Cursor curs=getContentResolver().query(uri,filePathColumn,null,null,null);
                    curs.moveToFirst();
                    int columIndex=curs.getColumnIndex(filePathColumn[0]);
                    path=curs.getString(columIndex);
                    mImgPhoto.setImageBitmap(BitmapFactory.decodeFile(path));

                    SharedPreferences.Editor editor=mSharedPreferences.edit();
                    editor.putString("path",path);
                    editor.commit();
                }
            }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode == KeyEvent.KEYCODE_BACK);
        {
            exitByTwoClick();
        }
        return false;
    }
    private static Boolean isExit = false;
    private void exitByTwoClick() {//双击退出
        // TODO Auto-generated method stub
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再次点击退出！", Toast.LENGTH_SHORT).show();
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
