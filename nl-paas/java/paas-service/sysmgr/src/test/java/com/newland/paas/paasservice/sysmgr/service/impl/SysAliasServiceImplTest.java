package com.newland.paas.paasservice.sysmgr.service.impl;

import com.newland.paas.advice.request.RequestContext;
import com.newland.paas.advice.request.SessionInfo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysAliasObjMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysAliasObj;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.BaseTreeDo;
import com.newland.paas.paasservice.sysmgr.service.SysAliasService;
import com.newland.paas.sbcommon.common.ApplicationException;
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
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

/**
 * @program: paas-all
 * @description: 系统别名测试
 * @author: czx
 * @create: 2019-01-19 8:41
 **/
public class SysAliasServiceImplTest {

    @InjectMocks
    private SysAliasService sysAliasService = new SysAliasServiceImpl();

    @Mock
    private SysAliasObjMapper sysAliasObjMapper;


    /**
     * data
     */
    List<SysAliasObj> sysAliasObjList;
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
        sysAliasObjList = new ArrayList<SysAliasObj>();
        baseTreeDoList = new ArrayList<BaseTreeDo>();
        initAliasObj();
    }

    @Test
    public void updateSysAlias() {
        when(sysAliasObjMapper.updateByPrimaryKeySelective(any(SysAliasObj.class))).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocationOnMock) throws Throwable {
                sysAliasObjList.stream().forEach(sysAliasObj -> {
                    if (Objects.equals(sysAliasObj.getAliasObjCode(), "czxTest1")) {
                        sysAliasObj.setAliasObjName("czxTest1-update");
                        return;
                    }
                });
                return 1;
            }
        });
        SysAliasObj sysAliasObj = new SysAliasObj();
        sysAliasObj.setAliasObjCode("czxTest1");

        try {
            sysAliasService.updateSysAlias(sysAliasObj);
            Assert.assertEquals(sysAliasObjList.get(0).getAliasObjName(), "czxTest1-update");
        } catch (ApplicationException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void getSysAliasObj() {
        SysAliasObj sysAliasObj = new SysAliasObj();
        sysAliasObj.setObjCodeP("czxTest1");
        sysAliasObj.setAliasObjName("czxTest1");
        sysAliasObj.setObjName("czxTest1");
        sysAliasObj.setTenantId(1L);
        sysAliasObj.setCreateDate(new Date());
        sysAliasObj.setChangeDate(new Date());
        sysAliasObj.setCreator(1L);
        sysAliasObj.setDelFlag(0L);
        sysAliasObj.setAliasObjCode("czxTest1");
        sysAliasObj.setAliasObjType("czxTest1");
        sysAliasObj.setObjCode("czxTest1");

        when(sysAliasObjMapper.selectBySelective(any(SysAliasObj.class))).thenAnswer(new Answer<List<SysAliasObj>>() {
            @Override
            public List<SysAliasObj> answer(InvocationOnMock invocation) throws Throwable {
                List<SysAliasObj> mockResult = new  ArrayList<SysAliasObj>();
                mockResult.add(sysAliasObj);
                return mockResult;
            }
        });
        try {
            SysAliasObj result = sysAliasService.getSysAliasObj("czxTest1");
            Assert.assertEquals(result, sysAliasObj);
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getSysAliasTree(){
        when(sysAliasObjMapper.getSysAliasTree(any(Long.class))).thenAnswer(new Answer<List<BaseTreeDo>>() {
            @Override
            public List<BaseTreeDo> answer(InvocationOnMock invocation) throws Throwable {
                return baseTreeDoList;
            }
        });
        List<BaseTreeDo> result = sysAliasService.getSysAliasTree();
        Assert.assertEquals(result.size(), 3);
    }

    @Test
    public void listSysAliasObjsBySysAliasObjCode(){
        List<SysAliasObj> list = new ArrayList<>();
        SysAliasObj sysAliasObj = new SysAliasObj();
        sysAliasObj.setObjCodeP("czxTest1");
        sysAliasObj.setAliasObjName("czxTest1");
        sysAliasObj.setObjName("czxTest1");
        sysAliasObj.setTenantId(1L);
        sysAliasObj.setCreateDate(new Date());
        sysAliasObj.setChangeDate(new Date());
        sysAliasObj.setCreator(1L);
        sysAliasObj.setDelFlag(0L);
        sysAliasObj.setAliasObjCode("czxTest1");
        sysAliasObj.setAliasObjType("czxTest1");
        sysAliasObj.setObjCode("czxTest1");
        list.add(sysAliasObj);

        when(sysAliasObjMapper.selectBySelective(any(SysAliasObj.class))).thenAnswer(new Answer<List<SysAliasObj>>() {
            @Override
            public List<SysAliasObj> answer(InvocationOnMock invocation) throws Throwable {
                List<SysAliasObj> mockResult = new  ArrayList<SysAliasObj>();
                mockResult.add(sysAliasObj);
                return mockResult;
            }
        });
        List<SysAliasObj> result = sysAliasService.listSysAliasObjsBySysAliasObjCode("czxTest1");
        Assert.assertEquals(result, list);
    }

    @Test
    public void addSysAlias() {
        SysAliasObj sysAliasObj = new SysAliasObj();
        when(sysAliasObjMapper.selectBySelective(any(SysAliasObj.class))).thenAnswer(new Answer<List<SysAliasObj>>() {
            @Override
            public List<SysAliasObj> answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        });

        when(sysAliasObjMapper.insertSelective(any(SysAliasObj.class))).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                sysAliasObj.setObjCodeP("czxTest4");
                sysAliasObj.setAliasObjName("czxTest4");
                sysAliasObj.setObjName("czxTest4");
                sysAliasObj.setTenantId(4L);
                sysAliasObj.setCreateDate(new Date());
                sysAliasObj.setChangeDate(new Date());
                sysAliasObj.setCreator(4L);
                sysAliasObj.setDelFlag(0L);
                sysAliasObj.setAliasObjCode("czxTest4");
                sysAliasObj.setAliasObjType("czxTest4");
                sysAliasObj.setObjCode("czxTest4");
                sysAliasObjList.add(sysAliasObj);
                return 1;
            }
        });
        try {
            sysAliasService.addSysAlias(sysAliasObj);
            Assert.assertEquals(sysAliasObjList.size(), 4);
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deleteSysAliasObj() {
        when(sysAliasObjMapper.listSubAliasObj(any(SysAliasObj.class))).thenAnswer(new Answer<List<SysAliasObj>>() {
            @Override
            public List<SysAliasObj> answer(InvocationOnMock invocation) throws Throwable {
                return new ArrayList<SysAliasObj>();
            }
        });

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                sysAliasObjList.removeIf(item -> item.getAliasObjCode().equals("czxTest4"));
                return null;
            }
        }).when(sysAliasObjMapper).deleteBySelective(any(SysAliasObj.class));

        String sysAliasObjCode = "czxTest4";
        try {
            sysAliasService.deleteSysAliasObj(sysAliasObjCode);
            Assert.assertEquals(sysAliasObjList.size(), 3);
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
    }

    private void initAliasObj() {
        SysAliasObj sysAliasObj = null;
        sysAliasObj = new SysAliasObj();
        sysAliasObj.setObjCodeP("czxTest1");
        sysAliasObj.setAliasObjName("czxTest1");
        sysAliasObj.setObjName("czxTest1");
        sysAliasObj.setTenantId(1L);
        sysAliasObj.setCreateDate(new Date());
        sysAliasObj.setChangeDate(new Date());
        sysAliasObj.setCreator(1L);
        sysAliasObj.setDelFlag(0L);
        sysAliasObj.setAliasObjCode("czxTest1");
        sysAliasObj.setAliasObjType("czxTest1");
        sysAliasObj.setObjCode("czxTest1");
        sysAliasObjList.add(sysAliasObj);

        sysAliasObj = new SysAliasObj();
        sysAliasObj.setObjCodeP("czxTest2");
        sysAliasObj.setAliasObjName("czxTest2");
        sysAliasObj.setObjName("czxTest2");
        sysAliasObj.setTenantId(2L);
        sysAliasObj.setCreateDate(new Date());
        sysAliasObj.setChangeDate(new Date());
        sysAliasObj.setCreator(2L);
        sysAliasObj.setDelFlag(0L);
        sysAliasObj.setAliasObjCode("czxTest2");
        sysAliasObj.setAliasObjType("czxTest2");
        sysAliasObj.setObjCode("czxTest2");
        sysAliasObjList.add(sysAliasObj);

        sysAliasObj = new SysAliasObj();
        sysAliasObj.setObjCodeP("czxTest3");
        sysAliasObj.setAliasObjName("czxTest3");
        sysAliasObj.setObjName("czxTest3");
        sysAliasObj.setTenantId(3L);
        sysAliasObj.setCreateDate(new Date());
        sysAliasObj.setChangeDate(new Date());
        sysAliasObj.setCreator(3L);
        sysAliasObj.setDelFlag(0L);
        sysAliasObj.setAliasObjCode("czxTest3");
        sysAliasObj.setAliasObjType("czxTest3");
        sysAliasObj.setObjCode("czxTest3");
        sysAliasObjList.add(sysAliasObj);

        baseTreeDoList = sysAliasObjList.stream()
                .map(item -> new BaseTreeDo(item.getAliasObjCode() + "", item.getObjCodeP() + "", item.getAliasObjName()))
                .collect(Collectors.toList());
    }
}