package com.werber.newsbj.bean;

import java.util.List;

/**
 * News新闻数据的封装
 */
public class NewsData {

    public String retcode;
    public List<NewsMenuData> data;

    //侧边栏菜单数据
    public class NewsMenuData{
        public String id;
        public String title;
        public int type;
        public String url;

        public List<NewsTabData> children;

        @Override
        public String toString() {
            return "NewsMenuData{" +
                    "id='" + id + '\'' +
                    ", title='" + title + '\'' +
                    ", children=" + children +
                    '}';
        }
    }

    //新闻界面下的11个tab标签数据
    public class NewsTabData{
        public String id;
        public String title;
        public int type;
        public String url;

        @Override
        public String toString() {
            return "NewsTabData{" +
                    "title='" + title + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "NewsData{" +
                "data=" + data +
                '}';
    }
}
