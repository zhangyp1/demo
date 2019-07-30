package com.newland.paas.paasservice.sysmgr.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.google.code.kaptcha.Producer;
import com.newland.paas.common.util.StringUtils;
import com.newland.paas.paasservice.sysmgr.common.SysMgrConstants;

/**
 * @author
 * @since 2017/10/30
 */
@Controller
@RequestMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}, value = "/sysmgr/v1/captcha")
public class CodeController {
    @Autowired
    private Producer captchaProducer = null;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final Integer TIMEOUT = 5;

    /**
     * 获取验证码图片
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping()
    public void getKaptchaImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uuid = StringUtils.getUUID32();
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Captcha-ID", uuid);
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        // 生成验证码
        String capText = captchaProducer.createText();

        redisTemplate.opsForValue().set(SysMgrConstants.REDIS_CAPTCHA + uuid, capText, TIMEOUT, TimeUnit.MINUTES);

        // 向客户端写出
        BufferedImage bi = captchaProducer.createImage(capText);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
    }

}
