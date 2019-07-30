package com.newland.paas.prefabsql.util;

import com.newland.paas.dataaccessmodule.model.PrefabSql;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author wrp
 * @since 2018/6/11
 */
@Component
@ConfigurationProperties(prefix = "prefabSql")
@RefreshScope
public class SqlNameUtils {

    public static final String BACKSLASH = "/";

    private Map<String, String> interfaceRel = new HashMap<String, String>();

    public static ConcurrentMap<String, PrefabSql> prefabSqls = new ConcurrentHashMap<String, PrefabSql>();

    public Map<String, String> getInterfaceRel() {
        return interfaceRel;
    }

    public void setInterfaceRel(Map<String, String> interfaceRel) {
        this.interfaceRel = interfaceRel;
    }

    /**
     * 将接口路径的反斜杠（/）修改为横杆（-）
     *
     * @param interfaceUrl
     * @return
     */
    public static String changeInterfaceRel(String interfaceUrl) {
        interfaceUrl = removeFirstBackslash(interfaceUrl);
        String[] urls = interfaceUrl.split("\\/");
        StringBuffer sb = new StringBuffer();
        for (String url : urls) {
            sb.append(url).append("-");
        }
        return sb.toString().substring(0, sb.length() - 1);
    }

    /**
     * 去除首字母的反斜杠
     *
     * @param str
     * @return
     */
    public static String removeFirstBackslash(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        if (str.indexOf(BACKSLASH) == 0) {
            str = str.substring(1);
        }
        return str;
    }

    /**
     * 加载库表prefab_sql数据
     *
     * @param prefabSqlList
     */
    public static void loadPrefabSql(List<PrefabSql> prefabSqlList) {
        for (PrefabSql prefabSql : prefabSqlList) {
            prefabSqls.put(prefabSql.getSqlName(), prefabSql);
        }
    }

}
