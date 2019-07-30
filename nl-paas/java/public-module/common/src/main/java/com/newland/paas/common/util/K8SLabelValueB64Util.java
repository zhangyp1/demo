package com.newland.paas.common.util;

import java.nio.charset.Charset;
import java.util.Base64;

/**
 * k8sLabelValueB64工具类
 *
 * @author slevenc
 */
public class K8SLabelValueB64Util {

    public static final char[] SOURCE = new char[] {'+', '/', '='};
    public static final char[] REPLACE = new char[] {'-', '_', '.'};

    /**
     * 编码
     *
     * @param text
     * @return
     */
    public static String encode(String text) {
        if (StringUtils.isEmpty(text)) {
            return "";
        }
        String base64Str = Base64.getEncoder().encodeToString(text.getBytes(Charset.forName("utf-8")));
        StringBuilder sb = new StringBuilder(base64Str);
        for (int sbIdx = 0; sbIdx < sb.length(); sbIdx++) {
            char ch = sb.charAt(sbIdx);
            for (int repIdx = 0; repIdx < SOURCE.length; repIdx++) {
                if (ch == SOURCE[repIdx]) {
                    sb.setCharAt(sbIdx, REPLACE[repIdx]);
                }
            }
        }
        sb.append('0');
        sb.insert(0, "b64");
        return sb.toString();

    }

    /**
     * 解码
     *
     * @param data
     * @return
     */
    public static String decode(String data) {
        if (StringUtils.isEmpty(data)) {
            return "";
        }
        if (!data.startsWith("b64") || !data.endsWith("0")) {
            return data;
        }
        if (!data.matches("[a-zA-Z0-9-_\\.]{4,}")) {
            return data;
        }
        StringBuilder sb = new StringBuilder(data.substring(3, data.length() - 1));
        for (int sbIdx = 0; sbIdx < sb.length(); sbIdx++) {
            char ch = sb.charAt(sbIdx);
            for (int repIdx = 0; repIdx < REPLACE.length; repIdx++) {
                if (ch == REPLACE[repIdx]) {
                    sb.setCharAt(sbIdx, SOURCE[repIdx]);
                }
            }
        }
        return new String(Base64.getDecoder().decode(sb.toString()), Charset.forName("utf-8"));
    }
}
