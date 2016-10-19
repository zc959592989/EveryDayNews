package edu.feicui.entity;

import android.graphics.drawable.Drawable;

/**
 * 主界面左滑数据的实体
 * Created by zhaoCe on 2016/9/28.
 */

public class LeftItem {
   public int icon;
   public String titleChinese;
   public String titleEnglish;

    public LeftItem(int icon, String titleChinese, String titleEnglish) {
        this.icon = icon;
        this.titleChinese = titleChinese;
        this.titleEnglish = titleEnglish;
    }
}
