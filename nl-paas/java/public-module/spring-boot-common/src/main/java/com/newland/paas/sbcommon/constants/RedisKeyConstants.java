package com.newland.paas.sbcommon.constants;

import com.newland.paas.sbcommon.utils.SpringContextUtil;

public interface RedisKeyConstants {
    String PROFILE = SpringContextUtil.getActiveProfile() + ":";
    String APPLICATION_NAME = SpringContextUtil.getApplicationName();
    String REDIS_PAAS_SESSION = PROFILE + "PAAS:SESSION:";

    /**
     * 各微服务API文档
     */
    String REDIS_PAAS_API_DOCS_PREFIX = PROFILE + "PAAS:API:DOCS:";
    String REDIS_PAAS_API_DOCS_ALL = REDIS_PAAS_API_DOCS_PREFIX + "*";
    String REDIS_PAAS_API_DOCS = REDIS_PAAS_API_DOCS_PREFIX + APPLICATION_NAME;
    /**
     * 受权限管控的API地址
     */
    String REDIS_PAAS_API_AUTH_PREFIX = PROFILE + "PAAS:API:AUTH:";
    String REDIS_PAAS_API_AUTH_ALL = REDIS_PAAS_API_AUTH_PREFIX + "ALL";

}
