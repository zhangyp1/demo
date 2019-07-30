package com.newland.paas.common.util;

import org.junit.Assert;
import org.testng.annotations.Test;

public class K8SLabelValueB64UtilTest {

    @Test
    public void testEncode() {
        String output = K8SLabelValueB64Util.encode("陈东说的都对2333366661");
        System.out.println(output);
        Assert.assertEquals(output,"b646ZmI5Lic6K-055qE6YO95a-5MjMzMzM2NjY2MQ..0");
    }

    @Test
    public void testDecode() {
        String output = K8SLabelValueB64Util.decode("b646ZmI5Lic6K-055qE6YO95a-5MjMzMzM2NjY2MQ..0");
        System.out.println(output);
        Assert.assertEquals(output,"陈东说的都对2333366661");
    }
}