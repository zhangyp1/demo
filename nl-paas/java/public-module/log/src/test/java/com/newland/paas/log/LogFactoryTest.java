package com.newland.paas.log;

import org.junit.Assert;
import org.testng.annotations.Test;

/**
 * 日志单元测试
 * @author sunxm
 */
public class LogFactoryTest {

    @Test
    public void testGetLogger() {
        Log log = LogFactory.getLogger(LogFactoryTest.class);
        Assert.assertNotNull(log);
    }
}