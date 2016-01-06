package com.werber.newsbj.utils;

import android.content.Context;

/**
 * 设置缓存
 */
public class CacheUtils {

    /**
     * 设置缓存
     * @param ctx
     * @param key url
     * @param value json数据
     */
    public static void setCache(Context ctx,String key,String value){
        PrefUtils.setString(ctx,key,value);
    }

    /**
     * 获取缓存
     * @param ctx
     * @param key
     * @return
     */
    public static String getCache(Context ctx,String key){
        return PrefUtils.getString(ctx,key,null);
    }

}
