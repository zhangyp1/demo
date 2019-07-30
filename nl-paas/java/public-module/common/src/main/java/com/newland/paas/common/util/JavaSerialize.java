package com.newland.paas.common.util;

import org.apache.commons.codec.binary.Base64;

import java.io.*;

/**
 * java自带序列化
 * @author Administrator
 *
 */
@Deprecated
public class JavaSerialize {

    public static String serializeBase64(Object obj) {
        String reStr = null;
        byte[] bytes = serialize(obj);
        reStr = Base64.encodeBase64String(bytes);
        return reStr;
    }

    public static byte[] serialize(Object obj) {
        ByteArrayOutputStream ByteOutputS = new ByteArrayOutputStream();
        ObjectOutputStream ObjOutputS = null;
        try {
            ObjOutputS = new ObjectOutputStream(ByteOutputS);
            ObjOutputS.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                ObjOutputS.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                ByteOutputS.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ByteOutputS.toByteArray();
    }

    public static Object deserializeBase64(String str) {
        if (str != null) {
            byte[] bytes = Base64.decodeBase64(str);
            return deserialize(bytes);
        }
        return null;
    }

    public static Object deserialize(byte[] b) {
        Object obj = null;
        ByteArrayInputStream ByteInputS = new ByteArrayInputStream(b);
        ObjectInputStream ObjInputS = null;
        try {
            ObjInputS = new ObjectInputStream(ByteInputS);
            obj = ObjInputS.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                ObjInputS.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                ByteInputS.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return obj;
    }
}
