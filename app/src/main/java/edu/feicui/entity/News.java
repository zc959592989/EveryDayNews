package edu.feicui.entity;

/**
 * 接受新闻各种信息的实体
 * Created by zhaoCe on 2016/9/27.
 */

public class News {
    /**
     *
     */
    public String summary;//新闻列表中每个新闻的摘要，一般不为空
    public String icon;//对应的图片
    public String stamp;//新闻发布的时间（如20140321表示2014年3月21号）
    public String title;//新闻列表中每个新闻的标题
    public int nid;//每条新闻的唯一标识符。即使同一新闻归属于不同的分类也只有唯一一个ID
    public String link;//新闻内容的链接
    public int type;//新闻类型   1：列表新闻；2：大图新闻；
}
