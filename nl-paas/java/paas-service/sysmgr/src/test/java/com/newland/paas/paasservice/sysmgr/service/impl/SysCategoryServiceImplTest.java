package com.newland.paas.paasservice.sysmgr.service.impl;

import com.newland.paas.advice.request.RequestContext;
import com.newland.paas.advice.request.SessionInfo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysCategoryMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysCategory;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.BaseTreeDo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysCategoryBo;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.SysCategoryVO;
import com.newland.paas.paasservice.sysmgr.service.SysCategoryService;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;
import mockit.MockUp;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

/**
 * @program: paas-all
 * @description: 系统分组测试
 * @author: Frown
 * @create: 2018-09-05 16:49
 **/
public class SysCategoryServiceImplTest {

    @InjectMocks
    private SysCategoryService sysCategoryService = new SysCategoryServiceImpl();

    @Mock
    private SysCategoryMapper sysCategoryMapper;


    /**
     * data
     */
    List<SysCategory> sysCategoryList;
    List<BaseTreeDo> baseTreeDoList;

    @BeforeClass
    public void beforeClass() {
        MockitoAnnotations.initMocks(this);

        new MockUp<RequestContext>() {
            @mockit.Mock
            public Long getUserId() {
                return 1001L;
            }
        };

        new MockUp<RequestContext>() {
            @mockit.Mock
            public Long getTenantId() {
                return 2001L;
            }
        };

        new MockUp<RequestContext>() {
            @mockit.Mock
            public SessionInfo getSession() {
                return new SessionInfo(1L, "admin", 1L);
            }
        };

        /**
         * init data
         */
        sysCategoryList = new ArrayList<SysCategory>();
        baseTreeDoList = new ArrayList<BaseTreeDo>();
        initSysCategory();
    }

    @Test
    public void testInsertSysCategory() {
        SysCategoryBo sysCategory = new SysCategoryBo();
        sysCategory.setSysCategoryPid(4L);
        sysCategory.setSysCategoryName("系统分组211");
        sysCategory.setGroupId(1L);
        when(sysCategoryMapper.selectBySelective(any(SysCategory.class))).thenAnswer(new Answer<List<SysCategory>>() {

            @Override
            public List<SysCategory> answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        });

        when(sysCategoryMapper.insertSelective(any(SysCategory.class))).thenAnswer(new Answer<Integer>() {

            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                sysCategory.setSysCategoryId(5L);
                sysCategoryList.add(sysCategory);
                return 1;
            }
        });
        try {
            sysCategoryService.saveSysCategory(sysCategory);
            Assert.assertEquals(sysCategoryList.size(), 3);
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdateSysCategory() {

        when(sysCategoryMapper.selectByPrimaryKey(any(Long.class))).thenAnswer(new Answer<SysCategory>() {

            @Override
            public SysCategory answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        });

        when(sysCategoryMapper.selectBySelective(any(SysCategory.class))).thenAnswer(new Answer<List<SysCategory>>() {

            @Override
            public List<SysCategory> answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        });

        when(sysCategoryMapper.updateByPrimaryKeySelective(any(SysCategory.class))).thenAnswer(new Answer<Integer>() {

            @Override
            public Integer answer(InvocationOnMock invocationOnMock) throws Throwable {
                for (SysCategory r : sysCategoryList) {
                    if (Objects.equals(r.getSysCategoryId(),1L)) {
                        r.setSysCategoryName("系统分组1x");
                        break;
                    }
                }
                return 1;
            }
        });

        SysCategoryBo  sysCategory = new SysCategoryBo();
        sysCategory.setSysCategoryId(1L);
        sysCategory.setSysCategoryName("系统分组1x");
        try {
            sysCategoryService.saveSysCategory(sysCategory);
            Assert.assertEquals(sysCategoryList.get(0).getSysCategoryName(), "系统分组1x");
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeleteSysCategory() {
        doAnswer(new Answer<Object>() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                sysCategoryList.removeIf(item -> item.getSysCategoryName().toString().startsWith("系统分组2"));
                return null;
            }
        }).when(sysCategoryMapper).delSubSysCategory(any(SysCategory.class));

        SysCategory  sysCategory = new SysCategory();
        sysCategory.setSysCategoryId(2L);
        try {
            sysCategoryService.deleteSysCategory(sysCategory);
            Assert.assertEquals(sysCategoryList.size(), 2);
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListSysCategory() {
        when(sysCategoryMapper.selectBySelective(any(SysCategory.class))).thenAnswer(new Answer<List<SysCategory>>() {

            @Override
            public List<SysCategory> answer(InvocationOnMock invocation) throws Throwable {
                return sysCategoryList;
            }
        });
        SysCategory  sysCategory = new SysCategory();
        try {
            List<SysCategory> result = sysCategoryService.listSysCategory(sysCategory);
            Assert.assertEquals(result.size(), 3);
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPageQuerySysCategory() {
        SysCategory  sysCategory = new SysCategory();
        sysCategory.setSysCategoryName("系统分组");
        PageInfo pageInfo = new PageInfo();
        pageInfo.setCurrentPage(1);
        pageInfo.setPageSize(5);

        when(sysCategoryMapper.listSubSysCategory(any(SysCategory.class))).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                return sysCategoryList;
            }
        });
        ResultPageData resultPageData = null;
        try {
            resultPageData = sysCategoryService.pageQuerySysCategory(sysCategory, pageInfo);
            Assert.assertEquals(resultPageData.getList(), sysCategoryList);
            Assert.assertEquals(resultPageData.getPageInfo(), pageInfo);
        } catch (ApplicationException e) {
            Assert.fail();
        }

    }

    @Test
    public void testGetSysCategory() {
        when(sysCategoryMapper.selectByPrimaryKey(any(Long.class))).thenAnswer(new Answer<SysCategory>() {

            @Override
            public SysCategory answer(InvocationOnMock invocation) throws Throwable {
                return sysCategoryList.get(0);
            }
        });
        SysCategory  sysCategory = new SysCategory();
        sysCategory.setSysCategoryId(1L);
        try {
            SysCategory result = sysCategoryService.getSysCategory(sysCategory);
            Assert.assertEquals(result.getSysCategoryName(), "系统分组1");
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTreeListSysCategory() {
        when(sysCategoryMapper.listCurSysCategory(any(SysCategoryBo.class))).thenAnswer(new Answer<List<SysCategory>>() {

            @Override
            public List<SysCategory> answer(InvocationOnMock invocation) throws Throwable {
                return sysCategoryList;
            }
        });

        when(sysCategoryMapper.listSubsSysCategory(any(List.class))).thenAnswer(new Answer<List<SysCategory>>() {

            @Override
            public List<SysCategory> answer(InvocationOnMock invocation) throws Throwable {
                return sysCategoryList;
            }
        });
        SysCategory  sysCategory = new SysCategory();
        sysCategory.setSysCategoryId(4L);
        sysCategory.setGroupId(1L);
        try {
            List<SysCategoryVO> sysCategoryVOS = sysCategoryService.listSysCategory();
            Assert.assertEquals(sysCategoryVOS.size(), 3);
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListSubSysCategory() {
        when(sysCategoryMapper.listSubSysCategory(any(SysCategory.class))).thenAnswer(new Answer<List<SysCategory>>() {

            @Override
            public List<SysCategory> answer(InvocationOnMock invocation) throws Throwable {
                return sysCategoryList;
            }
        });
        SysCategory  sysCategory = new SysCategory();
        List<SysCategory> result = sysCategoryService.listSubSysCategory(sysCategory);
        Assert.assertEquals(result.size(), 3);
    }

    private void initSysCategory() {
        SysCategory  sysCategory = null;

        sysCategory = new SysCategory();
        sysCategory.setSysCategoryId(1L);
        sysCategory.setSysCategoryPid(-1L);
        sysCategory.setSysCategoryName("系统分组1");
        sysCategory.setGroupId(1L);
        sysCategory.setTenantId(1L);
        sysCategoryList.add(sysCategory);

        sysCategory = new SysCategory();
        sysCategory.setSysCategoryId(2L);
        sysCategory.setSysCategoryPid(-1L);
        sysCategory.setSysCategoryName("系统分组2");
        sysCategory.setGroupId(1L);
        sysCategory.setTenantId(1L);
        sysCategoryList.add(sysCategory);

        sysCategory = new SysCategory();
        sysCategory.setSysCategoryId(3L);
        sysCategory.setSysCategoryPid(1L);
        sysCategory.setSysCategoryName("系统分组11");
        sysCategory.setGroupId(1L);
        sysCategory.setTenantId(1L);
        sysCategoryList.add(sysCategory);

        sysCategory = new SysCategory();
        sysCategory.setSysCategoryId(4L);
        sysCategory.setSysCategoryPid(2L);
        sysCategory.setSysCategoryName("系统分组21");
        sysCategory.setGroupId(1L);
        sysCategory.setTenantId(1L);
        sysCategoryList.add(sysCategory);

        baseTreeDoList = sysCategoryList.stream()
                .map(item -> new BaseTreeDo(item.getSysCategoryId()+"", item.getSysCategoryPid()+"", item.getSysCategoryName()))
                .collect(Collectors.toList());
    }
}