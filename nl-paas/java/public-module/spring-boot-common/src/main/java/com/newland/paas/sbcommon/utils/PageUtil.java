package com.newland.paas.sbcommon.utils;

import java.util.List;

import org.springframework.util.CollectionUtils;

import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;

public class PageUtil {
    public static ResultPageData<?> paged(List<?> list, PageInfo pageInfo) {
        if (CollectionUtils.isEmpty(list)) {
            return new ResultPageData<>();
        }
        // 总数
        int num = list.size();

        // 当前页
        int currentPage = pageInfo.getCurrentPage();

        int startIndex = (currentPage - 1) * pageInfo.getPageSize();
        int endtIndex = currentPage * pageInfo.getPageSize();
        endtIndex = endtIndex > num ? num : endtIndex;
        pageInfo.setTotalRecord((long)num);
        return new ResultPageData<>(list.subList(startIndex, endtIndex), pageInfo);
    }
}
