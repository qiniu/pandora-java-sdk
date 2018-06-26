package com.qiniu.pandora.util;

import com.qiniu.pandora.common.Constants;

import java.security.MessageDigest;

/**
 * Created by wenzhengcui on 2017/1/9.
 */
public class Md5 {

    private static MessageDigest md5 = null;

    static {
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static String getMd5(byte[] bytes) {
        byte[] bs = md5.digest(bytes);
        StringBuilder sb = new StringBuilder(40);
        for (byte x : bs) {
            if ((x & 0xff) >> 4 == 0) {
                sb.append("0").append(Integer.toHexString(x & 0xff));
            } else {
                sb.append(Integer.toHexString(x & 0xff));
            }
        }
        return sb.toString();
    }

    public static String getMd5(String str) {
        return getMd5(str.getBytes(Constants.UTF_8));
    }

}
