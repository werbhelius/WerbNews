package com.werber.newsbj.bean;

import java.util.List;

/**
 * 组图的数据封装
 */
public class PhotosData {

    public DataEntity data;
    public String retcode;

    public  class DataEntity {

        public String title;
        public List<NewsEntity> news;

        public  class NewsEntity {
            public String id;
            public String listimage;
            public String pubdate;
            public String title;
            public String type;
            public String url;

        }
    }
}
