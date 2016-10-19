package edu.feicui.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import edu.feicui.SeverUrl;
import edu.feicui.Util.MyVolley;
import edu.feicui.activity.HomeActivity;
import edu.feicui.entity.ForgetResponse;
import edu.feicui.entity.ForgetResponseDetail;
import edu.feicui.everydaynews.R;

/**
 * 找回密码界面
 * Created by zhaoCe on 2016/9/30.
 */

public class ForgetPassFragment extends Fragment implements View.OnClickListener{
    /**
     * 加载该碎片的activity
     */
    HomeActivity activity;
    /**
     * 邮箱编辑框的内容
     */
    String registerMain;
    /**
     * 登陆时的邮箱编辑框
     */
    EditText mEdtMail;
    /**
     * 确认按钮
     */
    Button mBtnConfirm;
    /**
     * 该界面的view
     */
    View view;
    FragmentManager fragmentManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_forget_pass,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity= (HomeActivity) getActivity();
        mBtnConfirm= (Button) view.findViewById(R.id.btn_forget_pass_fragment_confirm);
        mEdtMail= (EditText) view.findViewById(R.id.edt_forget_pass_fragment);
        activity.setOnClick(this,mBtnConfirm);
        fragmentManager=activity.getSupportFragmentManager();
        activity.mTvTitle.setText("找回密码");
    }
    //
    @Override
    public void onClick(View v) {
        registerMain=mEdtMail.getText().toString();
        if(registerMain!=null){
            Map<String,String> params=new HashMap<>();
            params.put("ver","0000000");
            params.put("email",registerMain);
            MyVolley.get(activity, SeverUrl.USER_FORGET_PASS, params, new MyVolley.OnVolleyResult() {
                @Override
                public void success(String s) {
                    ForgetResponse forgetResponse=new ForgetResponse();
                    forgetResponse.data=new ForgetResponseDetail();
                    Gson gson=new Gson();
                    forgetResponse=  gson.fromJson(s, ForgetResponse.class);
                    Toast.makeText(activity,forgetResponse.data.result+"",Toast.LENGTH_SHORT).show();
                    if(forgetResponse.data.result==0){
                        LoginFragment loginFragment=new LoginFragment();
                        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.ll_home_fg,loginFragment);
                        fragmentTransaction.commit();
                    }
                }

                @Override
                public void failed(VolleyError volleyError) {

                }
            });
        }

    }
}
