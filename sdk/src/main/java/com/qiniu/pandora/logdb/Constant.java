package com.qiniu.pandora.logdb;

/**
 * Created by tuo on 2017/6/3.
 */
public class Constant {
    public final static String HOST = "https://logdb.qiniu.com";
    public final static String VERSION = "v5";
    public final static String GET_SEARCH = "/"+VERSION +"/repos/%s/search";
    public final static String PARTIAL_SEARCH = "/"+VERSION +"/repos/%s/s";
    public final static String POST_MSEARCH = "/" + VERSION+"/logdbkibana/msearch";
    public final static String POST_SCROLL ="/"+VERSION+"/scroll";
}
