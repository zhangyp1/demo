package com.newland.paas.sbcommon.vo;

/**
 * 请求body结构，业务参数 + 分页参数
 *
 * @param <T>
 * @author wrp
 */
public class BasicPageRequestContentVo<T> extends BasicRequestContentVo<T> {

    private static PageInfo PAGE_INFO = new PageInfo();

    private PageInfo pageInfo;

    public PageInfo getPageInfo() {
        if (pageInfo == null) {
            pageInfo = PAGE_INFO;
        }
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }
}
