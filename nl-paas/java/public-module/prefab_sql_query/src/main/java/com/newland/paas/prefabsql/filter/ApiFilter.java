package com.newland.paas.prefabsql.filter;

import com.alibaba.fastjson.JSON;
import com.newland.paas.dataaccessmodule.model.PrefabSql;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.prefabsql.db.dbutils.handlers.MapListHandler;
import com.newland.paas.prefabsql.service.PrefabSqlService;
import com.newland.paas.prefabsql.util.SqlNameUtils;
import com.newland.paas.sbcommon.vo.BasicResponseContentVo;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * api过滤
 *
 * @author wrp
 * @since 2018/6/11
 */
@Component
@WebFilter(urlPatterns = "/*", filterName = "ApiFilter")
public class ApiFilter implements Filter {

    private static final String ENC = "utf-8";
    private static final Integer DEFAULT_PAGE_SIZE = 10;

    private static final Log LOG = LogFactory.getLogger(ApiFilter.class);

    @Autowired
    private PrefabSqlService prefabSqlService;
    @Autowired
    private SqlNameUtils sqlNameUtils;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    /**
     * 过滤器,用于api过滤，预置查询
     *
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestUri = request.getRequestURI();
        LOG.info(LogProperty.LOGTYPE_DETAIL, "ApiFilter.doFilter.requestUri:" + requestUri);
        // 配置文件存在预置查询配置
        requestUri = SqlNameUtils.removeFirstBackslash(requestUri);
        if (sqlNameUtils.getInterfaceRel().containsKey(requestUri)) {
            String interfaceUrl = SqlNameUtils.changeInterfaceRel(requestUri);
            LOG.info(LogProperty.LOGTYPE_DETAIL, "ApiFilter.doFilter.interfaceUrl:" + interfaceUrl);
            // interfaceUrl在数据库存在配置
            if (SqlNameUtils.prefabSqls.containsKey(interfaceUrl)) {
                PrefabSql prefabSql = SqlNameUtils.prefabSqls.get(interfaceUrl);
                if (prefabSql == null) {
                    filterChain.doFilter(servletRequest, servletResponse);
                }
                LOG.info(LogProperty.LOGTYPE_DETAIL, "ApiFilter.doFilter.sqlName:" + prefabSql.getSqlName());
                // 获取参数，构造参数
                String params = request.getParameter("params");
                LOG.info(LogProperty.LOGTYPE_DETAIL, "ApiFilter.doFilter.params:" + params);
                Map<String, Object> requestMap = new HashMap<>();
                if (StringUtils.isNotEmpty(params)) {
                    requestMap = JSON.parseObject(params);
                    requestMap = humpToLine(requestMap);
                }
                try {
                    if (requestMap.get("current_page") != null) {
                        PageInfo pageParams = new PageInfo();
                        pageParams.setCurrentPage(Integer.parseInt(requestMap.get("current_page").toString()));
                        int pageSize = requestMap.get("page_size") == null ? DEFAULT_PAGE_SIZE : Integer.parseInt(requestMap.get("page_size").toString());
                        pageParams.setPageSize(pageSize);
                        ResultPageData resultPageData = prefabSqlService.executeQueryPage(String.valueOf(prefabSql.getSqlId()), requestMap, pageParams);
                        BasicResponseContentVo<ResultPageData> responseData = new BasicResponseContentVo(resultPageData);
                        returnJson(response, responseData);
                    } else {
                        List<Map<String, Object>> queryResult = prefabSqlService.executeQuery(String.valueOf(prefabSql.getSqlId()), requestMap, new MapListHandler());
                        BasicResponseContentVo<Map<String, Object>> responseData = new BasicResponseContentVo(queryResult);
                        returnJson(response, responseData);
                    }
                } catch (SQLException e) {
                    LOG.error(LogProperty.LOGTYPE_DETAIL, "数据库查询操作失败", e);
                }

            } else {
                filterChain.doFilter(servletRequest, servletResponse);
            }
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }

    }

    @Override
    public void destroy() {

    }

    public static Map<String, Object> humpToLine(Map<String, Object> requestMap) {
        Map<String, Object> newRequestMap = new HashMap<String, Object>();
        for (Map.Entry<String, Object> entry : requestMap.entrySet()) {
            String first = entry.getKey();
            newRequestMap.put(com.newland.paas.common.util.StringUtils.humpToLine(first), entry.getValue());
        }
        return newRequestMap;
    }

    private void returnJson(HttpServletResponse response, Object obj) {
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(JSON.toJSONString(obj));
        } catch (IOException e) {
            LOG.error(LogProperty.LOGTYPE_DETAIL, "返回json出错:", e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

}
