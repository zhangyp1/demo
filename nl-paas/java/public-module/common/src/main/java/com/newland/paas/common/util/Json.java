package com.newland.paas.common.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.newland.paas.common.exception.ErrorCode;
import com.newland.paas.common.exception.RtcException;

/**
 * json 工具
 * 
 * @author Administrator
 *
 */
public class Json {
    /**
     * 功能：将 Object 对象转换成 JSON String串
     * 
     * @param object 需要转换成Json String的 Object 对象
     * @return 返回 String 串 当传入的对象参数为空时，返回 null 的串 否则转换成正常的Json String串
     */
    public static String toJson(Object object) {
        return object == null ? null : JSON.toJSONString(object);
    }

    public static String toJson(Object object, SerializeFilter filter, SerializerFeature... features) {

        return object == null ? null : JSON.toJSONString(object, filter, features);
    }

    /**
     * 将 JSON 文本解析成 你直接需要的 对象（反序列化----推荐使用：反序列化的结果与原装的数据一致）。第二个参数要输入你要转的对象类名,可不必对其进行对象的强转 @param str 要解析的 JSON
     * String串。 @param s 解析 Json 为具体的对象名字 @return 返回指定的第二个参数对象 @throws
     */
    public static <T> T toObject(String str, Class<T> s) {
        try {
            return JSON.parseObject(str, s);
        } catch (JSONException e) {
            throw new RtcException(ErrorCode.BUSERROR_JSON_TRANSFORM, "Json的String串转换成具体对象失败,str:" + str, e);
        }
    }

    /**
     * 将 JSON 文本解析成 Object 对象。
     *
     * @param jsonText 要解析的 JSON 文本。
     * @return 解析后生成的对象。
     * @throws IllegalArgumentException 参数异常。
     * @throws RtcException JSON 文本解析失败。
     */
    public static Object toObject(String jsonText) {
        if (jsonText == null)
            throw new IllegalArgumentException("jsonText 参数异常");
        try {
            Object object = JSON.parse(jsonText);
            return traversalObject(object);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("jsonText 参数异常,jsonText:" + jsonText);
        } catch (Exception e) {
            StringBuilder message = new StringBuilder();
            message.append("JSON 文本解析失败（原因“").append(e.toString().replace("\r", " ").replace("\n", " ").trim())
                .append("”）");
            throw new RtcException(message.toString(), e);
        }
    }

    /**
     * 遍历给定的 Object 对象。
     *
     * @param object 要遍历的 Object 对象。
     * @return 遍历后生成的对象。
     * @throws IllegalArgumentException 参数异常。
     */
    private static Object traversalObject(Object object) {
        if (object instanceof JSONArray) {
            ArrayList<Object> arrayList = new ArrayList<Object>();
            JSONArray jsonArray = (JSONArray)object;
            for (Object obj : jsonArray) {
                if ((obj instanceof JSONArray) || (obj instanceof JSONObject)) {
                    arrayList.add(traversalObject(obj));
                } else {
                    arrayList.add(obj);
                }
            }
            return arrayList;
        } else if (object instanceof JSONObject) {
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            JSONObject jsonObject = (JSONObject)object;
            for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if ((value instanceof JSONArray) || (value instanceof JSONObject)) {
                    linkedHashMap.put(key, traversalObject(value));
                } else {
                    linkedHashMap.put(key, value);
                }
            }
            return linkedHashMap;
        } else {
            throw new IllegalArgumentException("object 参数异常");
        }
    }

    private static String getToken(String json) {
        StringBuilder buf = new StringBuilder();
        boolean isInYinHao = false;
        while (json.length() > 0) {
            String token = json.substring(0, 1);
            json = json.substring(1);

            if (!isInYinHao && (token.equals(":") || token.equals("{") || token.equals("}") || token.equals("[")
                || token.equals("]") || token.equals(","))) {
                if (buf.toString().trim().length() == 0) {
                    buf.append(token);
                }

                break;
            }

            if (token.equals("\\")) {
                buf.append(token);
                buf.append(json.substring(0, 1));
                json = json.substring(1);
                continue;
            }
            if (token.equals("\"")) {
                buf.append(token);
                if (isInYinHao) {
                    break;
                } else {
                    isInYinHao = true;
                    continue;
                }
            }
            buf.append(token);
        }
        return buf.toString();
    }

    /**
     * json字符串的格式化
     *
     * @param json 需要格式的json串
     * @param fillStringUnit 每一层之前的占位符号比如空格 制表符
     * @return
     */
    public static String formatJson(String json, String fillStringUnit) {
        if (json == null || json.trim().length() == 0) {
            return null;
        }

        int fixedLenth = 0;
        ArrayList<String> tokenList = new ArrayList<String>();
        {
            String jsonTemp = json;
            // 预读取
            while (jsonTemp.length() > 0) {
                String token = getToken(jsonTemp);
                jsonTemp = jsonTemp.substring(token.length());
                token = token.trim();
                tokenList.add(token);
            }
        }

        for (int i = 0; i < tokenList.size(); i++) {
            String token = tokenList.get(i);
            int length = token.getBytes().length;
            if (length > fixedLenth && i < tokenList.size() - 1 && tokenList.get(i + 1).equals(":")) {
                fixedLenth = length;
            }
        }

        StringBuilder buf = new StringBuilder();
        int count = 0;
        for (int i = 0; i < tokenList.size(); i++) {

            String token = tokenList.get(i);

            if (token.equals(",")) {
                buf.append(token);
                doFill(buf, count, fillStringUnit);
                continue;
            }
            if (token.equals(":")) {
                buf.append(" ").append(token).append(" ");
                continue;
            }
            if (token.equals("{")) {
                String nextToken = tokenList.get(i + 1);
                if (nextToken.equals("}")) {
                    i++;
                    buf.append("{ }");
                } else {
                    count++;
                    buf.append(token);
                    doFill(buf, count, fillStringUnit);
                }
                continue;
            }
            if (token.equals("}")) {
                count--;
                doFill(buf, count, fillStringUnit);
                buf.append(token);
                continue;
            }
            if (token.equals("[")) {
                String nextToken = tokenList.get(i + 1);
                if (nextToken.equals("]")) {
                    i++;
                    buf.append("[ ]");
                } else {
                    count++;
                    buf.append(token);
                    doFill(buf, count, fillStringUnit);
                }
                continue;
            }
            if (token.equals("]")) {
                count--;
                doFill(buf, count, fillStringUnit);
                buf.append(token);
                continue;
            }

            buf.append(token);
            // 左对齐
            if (i < tokenList.size() - 1 && tokenList.get(i + 1).equals(":")) {
                int fillLength = fixedLenth - token.getBytes().length;
                if (fillLength > 0) {
                    for (int j = 0; j < fillLength; j++) {
                        buf.append("");
                    }
                }
            }
        }
        return buf.toString();
    }

    private static void doFill(StringBuilder buf, int count, String fillStringUnit) {
        buf.append("\n");
        for (int i = 0; i < count; i++) {
            buf.append(fillStringUnit);
        }
    }

    public static void main(String[] args) {
        Json.toObject("{}");
    }
}
