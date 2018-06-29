package com.qiniu.pandora.util;

import com.qiniu.pandora.common.QiniuRuntimeException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

/**
 * URL安全的Base64编码和解码
 */

public final class UrlSafeBase64 {

    public static final Charset UTF_8 = Charset.forName("UTF-8");

    private UrlSafeBase64() {
    }   // don't instantiate

    /**
     * 编码字符串
     *
     * @param data 待编码字符串
     * @return 结果字符串
     */
    public static String encodeToString(String data) {
        return encodeToString(data.getBytes(UTF_8));
    }

    /**
     * 编码数据
     *
     * @param data 字节数组
     * @return 结果字符串
     */
    public static String encodeToString(byte[] data) {
        return Base64.encodeToString(data, Base64.URL_SAFE | Base64.NO_WRAP);
    }

    /**
     * 解码数据
     *
     * @param data 编码过的字符串
     * @return 原始数据
     */
    public static byte[] decode(String data) {
        return Base64.decode(data, Base64.URL_SAFE | Base64.NO_WRAP);
    }

    public static String urlEscape(String s) throws QiniuRuntimeException {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new QiniuRuntimeException(e);
        }
    }
}