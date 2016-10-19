package edu.feicui.activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import edu.feicui.SeverUrl;
import edu.feicui.Util.MyVolley;
import edu.feicui.activity.HomeActivity;
import edu.feicui.activity.MyAccountActivity;
import edu.feicui.entity.RegisterResponse;
import edu.feicui.entity.RegisterResponseDetail;
import edu.feicui.everydaynews.R;

/**
 * 登陆界面
 * Created by zhaoCe on 2016/9/28.
 */
public class LoginFragment extends Fragment implements View.OnClickListener{

    /**
     * 登录用户名
     */
    EditText mEdtLoginName;
    /**
     * 登录密码
     */
    EditText mEdtLoginPwd;

    /**
     * 登录按钮
     */
    Button mBtnLogin;
    /**
     * 忘记密码按钮
     */
    Button mBtnForget;
    /**
     * 注册按钮
     */
    Button mBtnRegiste;
    /**
     * 存放账号密码
     */
    SharedPreferences mSharedPreferences;
    /**
     * 加载该碎片的activity
     */
    HomeActivity activity;
    View view;
    FragmentManager fragmentManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_login,container,false);
        init();
        return view;
    }
    /**
     * 记住密码按钮
     */
    CheckBox mChMePwd;
    /**
     * 初始化工作
     */
   void init(){
       activity= (HomeActivity) getActivity();
       activity.mTvTitle.setText("登录");
       mBtnRegiste= (Button) view.findViewById(R.id.btn_login_fragment_regist);
       mBtnForget= (Button) view.findViewById(R.id.btn_login_fragment_forget);
       mBtnLogin= (Button) view.findViewById(R.id.btn_login_fragment_login);
       mEdtLoginName= (EditText) view.findViewById(R.id.edt_login_fragment_name);
       mEdtLoginPwd= (EditText) view.findViewById(R.id.edt_login_fragment_pwd);
       mChMePwd= (CheckBox) view.findViewById(R.id.ch_fragment_login_memory_pwd);
       activity.setOnClick(this,mBtnForget,mBtnLogin,mBtnRegiste);
       fragmentManager=activity.getSupportFragmentManager();

   }
    boolean selectCh;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mSharedPreferences=activity.getSharedPreferences("uid_pwd", Context.MODE_PRIVATE);
        editor= mSharedPreferences.edit();
        selectCh=mSharedPreferences.getBoolean("selectCh",false);
       if(selectCh){//记住密码了
           mEdtLoginName.setText(mSharedPreferences.getString("loginName",""));
           mEdtLoginPwd.setText(mSharedPreferences.getString("loginPwd",""));
       }
        mChMePwd.setChecked(selectCh);//不管有没有记住密码，checkbox的状态是跟着shareprefrence文件中的boolean值走的
    }

    /**
     * 编写shareprefrence文件
     */
    SharedPreferences.Editor editor;
    /**
     * 编辑框中的登陆用户名
     */
    String loginName;
    /**
     * 编辑框中的登陆用户密码
     */
    String loginPwd;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login_fragment_regist:
            {
                /**
                 * 注册界面
                 */
                RegistFragment rFragment=new RegistFragment();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.ll_home_fg,rFragment);
                fragmentTransaction.commit();
            }
                break;
            case R.id.btn_login_fragment_forget://跳到找回密码界面（忘记密码按钮）
            {
                ForgetPassFragment forgetPassFragment=new ForgetPassFragment();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.ll_home_fg,forgetPassFragment);
                fragmentTransaction.commit();
            }
                break;
            case R.id.btn_login_fragment_login:
            {
                loginName=mEdtLoginName.getText().toString();
                loginPwd=mEdtLoginPwd.getText().toString();
                if(loginName!=null&&loginPwd!=null){
                    //ver=版本号&uid=用户名&pwd=密码&device=0
                    Map<String,String> params=new HashMap<>();
                    params.put("ver","0000000");
                    params.put("uid",loginName);
                    params.put("pwd",loginPwd);
                    params.put("device","0");
                    MyVolley.get(activity, SeverUrl.USER_LOGIN, params, new MyVolley.OnVolleyResult() {
                        @Override
                        public void success(String s) {
                            Gson gson=new Gson();
                            RegisterResponse response=new RegisterResponse();
                            response.data=new RegisterResponseDetail();
                            response= gson.fromJson(s, RegisterResponse.class);
                            if(response.data.result==0){//登陆成功
                                Toast.makeText(activity,"登陆成功",Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent();
                                intent.putExtra("loginName",loginName);
                                intent.putExtra("success",response);
                                intent.setClass(activity, MyAccountActivity.class);
                                activity.startActivity(intent);
                                if(mChMePwd.isChecked()){
                                    editor.putString("loginName",loginName);
                                    editor.putString("loginPwd",loginPwd);
                                }
                                editor.putBoolean("selectCh",mChMePwd.isChecked());
                                editor.commit();
                                activity.finish();
                            }
                        }
                        @Override
                        public void failed(VolleyError volleyError) {
                        }
                    });
                }

            }
                break;
        }
    }
}
