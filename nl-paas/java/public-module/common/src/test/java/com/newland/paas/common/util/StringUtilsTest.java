package com.newland.paas.common.util;

import org.testng.annotations.Test;

public class StringUtilsTest {

    @Test
    public void getFirstNumber() {
        System.out.println(StringUtils.getFirstNumber("123aa123"));
        System.out.println(StringUtils.getFirstNumber("12年"));
        System.out.println(StringUtils.getFirstNumber("aaa"));
        System.out.println(StringUtils.getFirstNumber("1234"));
    }

    @Test
    public void getNumberInString() {
        System.out.println(StringUtils.getNumberInString("123aa123"));
        System.out.println(StringUtils.getNumberInString("12年"));
        System.out.println(StringUtils.getNumberInString("aaa"));
        System.out.println(StringUtils.getNumberInString("1234"));
    }
}
