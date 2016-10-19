package edu.feicui;

/**
 * Created by Administrator on 2016/9/27.
 */

public class SeverUrl {
    /**
     * 根链接
     */
    public static final String BASE_URL="http://118.244.212.82:9092/newsClient";

    /**
     * 子链接 新闻列表
     */
    public static final String NEWS_LIST=BASE_URL+"/news_list";

    /**
     * 注册链接
     */
    public static final String USER_REGISTER=BASE_URL+"/user_register";
    /**
     * 登陆链接
     */
    public static final String USER_LOGIN=BASE_URL+"/user_login";
    /**
     * 忘记密码链接
     */
    public static final String USER_FORGET_PASS=BASE_URL+"/user_forgetpass";
    /**
     * 评论数量的链接
     */
    public static final String CMT_NUM=BASE_URL+"/cmt_num";

    /**
     * 评论列表链接
     */
    public static final String CMT_LIST=BASE_URL+"/cmt_list";
    /**
     * 发布评论链接
     */
    public static final String CMT_COMMIT=BASE_URL+"/cmt_commit";
    /**
     * 用户中心链接
     */
    public static final String USER_HOME=BASE_URL+"/user_home";
    /**
     * 版本更新链接
     */
    public static final String UPDATE=BASE_URL+"/update";

    /**
     * 新闻列表
     */
    public static final String NEWS_SORT=BASE_URL+"/news_sort";
}
