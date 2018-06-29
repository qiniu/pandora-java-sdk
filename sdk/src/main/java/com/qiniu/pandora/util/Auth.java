package com.qiniu.pandora.util;


import com.qiniu.pandora.common.Constants;
import org.codehaus.jackson.map.ObjectMapper;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.security.GeneralSecurityException;
import java.util.*;

public final class Auth {
    private final String accessKey;
    private final SecretKeySpec secretKey;
    private static final String QiniuHeaderPrefix = "X-Pandora-";
    public static final String Pandora = "Pandora ";
    public static final String PandoraAdmin = "PandoraAdmin ";
    public static final String AuthorizationPandoraAdmin = "Auth: PandoraAdmin ";
    public static final String ContentMD5 = "Content-MD5";
    public static final String ContentType = "Content-Type";
    public static final String Date = "Date";
    public static final String HTTPHeaderAuthorization = "Authorization";

    private Auth(String accessKey, SecretKeySpec secretKeySpec) {
        this.accessKey = accessKey;
        this.secretKey = secretKeySpec;
    }

    public static Auth create(String accessKey, String secretKey) {
        if (StringUtils.isNullOrEmpty(accessKey) || StringUtils.isNullOrEmpty(secretKey)) {
            throw new IllegalArgumentException("empty key");
        }
        byte[] sk = StringUtils.utf8Bytes(secretKey);
        SecretKeySpec secretKeySpec = new SecretKeySpec(sk, "HmacSHA1");
        return new Auth(accessKey, secretKeySpec);
    }

    public Mac createMac() {
        Mac mac;
        try {
            mac = javax.crypto.Mac.getInstance("HmacSHA1");
            mac.init(secretKey);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
        return mac;
    }


    public String signRequest(String urlString, String method, Map<String, Object> headers,
                              Map<String, String> qiniuSubResource) throws Exception {
        Mac mac = createMac();
        mac.update(StringUtils.utf8Bytes(signQiniuHeader(headers, method)));
        String headerValues = signQiniuHeader(headers);
        mac.update(StringUtils.utf8Bytes(headerValues));
        mac.update(StringUtils.utf8Bytes(signQiniuResource(urlString, qiniuSubResource)));
        String result = UrlSafeBase64.encodeToString(mac.doFinal());
        return (Pandora + accessKey + ":" + result);
    }


    public String signAdminRequest(String urlString, String method, Map<String, Object> headers,
                                   Map<String, String> qiniuSubResource, String suInfo) throws Exception {
        Mac mac = createMac();
        mac.update(StringUtils.utf8Bytes(signQiniuHeader(headers, method)));
        mac.update(StringUtils.utf8Bytes(AuthorizationPandoraAdmin + suInfo));
        String headerValues = signQiniuHeader(headers);
        mac.update(StringUtils.utf8Bytes(headerValues));
        mac.update(StringUtils.utf8Bytes(signQiniuResource(urlString, qiniuSubResource)));
        String result = UrlSafeBase64.encodeToString(mac.doFinal());
        return (PandoraAdmin + suInfo + ":" + accessKey + ":" + result);
    }


    public String signQiniuHeader(Map<String, Object> headers, String method) {
        StringBuffer sb = new StringBuffer();
        sb.append(method + "\n");
        if (headers != null && headers.size() != 0) {
            if (headers.containsKey(ContentMD5))
                sb.append(headers.get(ContentMD5).toString());
            sb.append("\n");

            if (headers.containsKey(ContentType))
                sb.append(headers.get(ContentType).toString());
            sb.append("\n");

            if (headers.containsKey(Date))
                sb.append(headers.get(Date).toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    public String signQiniuHeader(Map<String, Object> headers) {
        StringBuffer sb = new StringBuffer();
        if (headers == null || headers.size() == 0)
            return sb.toString();

        Set<Map.Entry<String, Object>> entries = headers.entrySet();
        Iterator<Map.Entry<String, Object>> it = entries.iterator();
        List<String> keys = new ArrayList<String>();
        while (it.hasNext()) {
            Map.Entry<String, Object> next = it.next();
            String key = next.getKey();
            if (key.startsWith(QiniuHeaderPrefix) && key.length() > QiniuHeaderPrefix.length())
                keys.add(key);
        }

        if (keys.size() != 0) {
            Collections.sort(keys);
            for (String key : keys) {
                sb.append("\n" + key.toLowerCase() + ":" + headers.get(key));
            }
        }
        return sb.toString();
    }

    public String signQiniuResource(String uriString, Map<String, String> qiniuSubResource)
            throws UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer();
        URI uri = URI.create(uriString);
        String path = uri.getRawPath();
        String query = uri.getRawQuery();
        sb.append(path);

        Map<String, String> queryParameter = splitQuery(query);
        List<String> keys = new ArrayList<String>();
        if (qiniuSubResource != null && qiniuSubResource.size() != 0 && queryParameter != null
                && queryParameter.size() != 0) {
            Set<Map.Entry<String, String>> entries = qiniuSubResource.entrySet();
            Iterator<Map.Entry<String, String>> it = entries.iterator();

            while (it.hasNext()) {
                Map.Entry<String, String> next = it.next();
                String value = next.getValue();
                if (queryParameter.containsKey(value) && !queryParameter.get(value).equals(""))
                    keys.add(queryParameter.get(value));
            }
        }

        for (int i = 0; i < keys.size(); i++) {
            if (i == 0)
                sb.append("?");

            sb.append(keys.get(i) + "=" + queryParameter.get(keys.get(i)));

            if (i != keys.size() - 1) {
                sb.append("&");
            }
        }
        return sb.toString();
    }

    public Map<String, String> splitQuery(String query) throws UnsupportedEncodingException {
        if (query == null)
            return null;

        Map<String, String> queryParameter = new HashMap<String, String>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            queryParameter.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
                    URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return queryParameter;
    }

    public String makeToken(String urlString, String method, Long expires, Map<String, Object> headers,
                            Map<String, String> qiniuSubResource) throws Exception {
        Mac mac = createMac();

        ObjectMapper mapper = new ObjectMapper();
        String headersStr = signQiniuHeader(headers);
        String resource = signQiniuResource(urlString, qiniuSubResource);
        String contentMD5 = "";
        String contentType = "";
        if (headers != null && headers.size() != 0) {
            contentMD5 = headers.containsKey(ContentMD5) ? (headers.get(ContentMD5).toString()) : "";
            contentType = headers.containsKey(ContentType) ? (headers.get(ContentType).toString()) : "";
        }

        Token token = new Token(resource, expires, contentMD5, contentType, headersStr, method.toUpperCase());
        String tokenJson = mapper.writeValueAsString(token);
        String encodedTokenDesc = UrlSafeBase64.encodeToString(tokenJson.getBytes(Constants.UTF_8));
        mac.update(StringUtils.utf8Bytes(encodedTokenDesc));
        String encodedSign = UrlSafeBase64.encodeToString(mac.doFinal());
        return (Pandora + accessKey + ":" + encodedSign + ":" + encodedTokenDesc);
    }

}