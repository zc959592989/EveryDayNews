package edu.feicui.activity.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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
import edu.feicui.entity.RegisterResponse;
import edu.feicui.entity.RegisterResponseDetail;
import edu.feicui.everydaynews.R;

/**
 * 注册界面
 * Created by zhaoCe on 2016/9/29.
 */

public class RegistFragment extends Fragment implements View.OnClickListener{
    /**
     * 碎片管理
     */
    FragmentManager fragMentManager;
    /**
     * 服务条款
     */
    CheckBox mChAgree;
    /**
     * 注册
     */
    Button mBtnRegist;
    /**
     * 邮箱
     */
    EditText edtMail;
    /**
     * 昵称
     */
    EditText edtName;
    /**
     * 密码
     */
    EditText edtPwd;

    /**
     * 加载该界面的activity
     */
    HomeActivity activity;
    /**
     * 该碎片
     */
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_regist,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        activity= (HomeActivity) getActivity();
        activity.mTvTitle.setText("用户注册");
        fragMentManager=activity.getSupportFragmentManager();
        edtMail= (EditText) view.findViewById(R.id.edt_regist_fragment_mail);
        edtName= (EditText) view.findViewById(R.id.edt_regist_fragment_name);
        edtPwd= (EditText) view.findViewById(R.id.edt_regist_fragment_pwd);
        mBtnRegist= (Button) view.findViewById(R.id.btn_regist_fragment_regist);
        mChAgree= (CheckBox) view.findViewById(R.id.ch_regist_fragment_agree);
        activity.setOnClick(this,mBtnRegist);
    }

    /**
     * 输入框的邮箱
     */
    String mail;
    /**
     * 输入框的名字
     */
    String name;
    /**
     * 输入框的密码
     */
    String pwd;
    /**
     * 判断编辑框中的文本是否可以注册
     * @return
     */
    boolean isRegist(){
        mail= edtMail.getText().toString();
        name=edtName.getText().toString();
        pwd=edtPwd.getText().toString();
        boolean  flagMail=false;
        if(mail!=null){
            flagMail= mail.matches("[[a-z][0-9][A-Z]]{1,10}@[a-z[0-9]]{1,3}.[Cc][Oo][Mm]");
            if(!flagMail){
                Toast.makeText(activity,"邮箱格式错误",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(activity,"邮箱不能为空",Toast.LENGTH_SHORT).show();
        }
        boolean falgPwd=false;
        if(pwd!=null){
            falgPwd=pwd.matches("[[a-z][0-9][A-Z]]{6,16}");
            if(!falgPwd){
                Toast.makeText(activity,"密码格式错误",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(activity,"密码不能为空",Toast.LENGTH_SHORT).show();
        }

        boolean flagName=false;
        if(name!=null){
           String[] arr= name.split("\\s");
            if(arr.length>1){
                Toast.makeText(activity,"昵称不能有空格",Toast.LENGTH_SHORT).show();
            }else if(arr.length==0){
                Toast.makeText(activity,"昵称不能为空",Toast.LENGTH_SHORT).show();
            }
            if(arr.length==1){
                flagName=true;
            }
        }
        boolean []bm=new boolean[3];
        bm[0]=falgPwd;
        bm[1]=flagMail;
        bm[2]=flagName;
        boolean flag=true;
        for (boolean all:bm
             ) {
            if(!all){
                flag=all;
            }
        }
        return flag;
    }

    @Override
    public void onClick(View v) {
        if(mChAgree.isChecked()){
            if(isRegist()){//调接口
                //ver=版本号&uid=用户名&email=邮箱&pwd=登陆密码
                Map<String,String> params=new HashMap<>();
                params.put("ver","0000000");
                params.put("uid",name);
                params.put("email",mail);
                params.put("pwd",pwd);
                MyVolley.get(activity, SeverUrl.USER_REGISTER, params, new MyVolley.OnVolleyResult() {
                    @Override
                    public void success(String s) {
                        Gson gson=new Gson();
                        RegisterResponse response=new RegisterResponse();
                        response.data=new RegisterResponseDetail();
                       response= gson.fromJson(s, RegisterResponse.class);
                        Toast.makeText(activity,response.data.explain,Toast.LENGTH_SHORT).show();
                        if(response.data.result==0){//注册成功
                            /**
                             * 登陆界面的对象
                             */
                            LoginFragment loginFragment=new LoginFragment();
                            FragmentTransaction fragmentTransaction= fragMentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.ll_home_fg,loginFragment);
                            fragmentTransaction.commit();
                        }
                    }
                    @Override
                    public void failed(VolleyError volleyError) {
                        Log.e("aac", "failed: " );
                    }
                });
            }else{//不调接口

            }
        }else{
            Toast.makeText(activity,"请同意服务条款",Toast.LENGTH_SHORT).show();
        }

    }
}
