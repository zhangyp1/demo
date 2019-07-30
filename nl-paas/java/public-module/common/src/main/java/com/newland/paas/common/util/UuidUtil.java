package com.newland.paas.common.util;

import java.util.Random;
import java.util.UUID;

/**
 * 自定义UUID生成工具类
 * @author Administrator
 * @date 2018/11/16
 */
public class UuidUtil {
    
    public static Random random = new Random();
    
    public static StringBuffer sbRadom = new StringBuffer(50);
    
    public static final String letters = "abcdefghijklmnopqrstuvwxyz";

    /**
     * 获取32位长度UUID (16进制)
     * @return
     */
    public static String buildUUID32() {
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        return uuid;
    }
    
    /**
     * 获取12位长度UUID (36进制)
     * @return
     */
    public static String buildUuid12() {
        int radomi = random.nextInt(9999);
        sbRadom.setLength(0);
        sbRadom.append(System.currentTimeMillis()).append(radomi);
        long l = Long.valueOf(sbRadom.toString()); 
        String strUuid = Long.toString(l, 36);
        int radomLetter = random.nextInt(letters.length());
        strUuid = letters.charAt(radomLetter) + strUuid;
        return strUuid;
    }
    
    public static void main(String[] args) {
      System.out.println(Long.toString(999999999999L, 36));
      System.out.println(Long.valueOf("cre66i9r", 36));
      
//        for(int i=0;i<9999;i++) {
//            System.out.println(UuidUtil.buildUuid12());
//            System.out.println(random.nextInt(letters.length()));
//        }
    }

}
