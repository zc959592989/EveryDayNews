package edu.feicui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import edu.feicui.entity.LoginNoteUserAddressInfo;
import edu.feicui.everydaynews.R;

/**
 * 登陆日志适配器
 * Created by zhaoCe
 * on 2016/10/12.
 */

public class LoginNoteAdapter extends MyBaseAdapter<LoginNoteUserAddressInfo> {
    public LoginNoteAdapter(Context mContext, ArrayList<LoginNoteUserAddressInfo> mList, int mLayoutId) {
        super(mContext, mList, mLayoutId);
    }

    @Override
    public void setView(int position, View convertView, MyHolder holder, LoginNoteUserAddressInfo loginNoteUserAddressInfo) {
        TextView loginlog= (TextView) convertView.findViewById(R.id.tv_item_login_log);
        loginlog.setText(loginNoteUserAddressInfo.time+"   "+loginNoteUserAddressInfo.address);
    }
}
