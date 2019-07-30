package com.newland.paas.paasservice.sysmgr.service.impl;

import com.newland.paas.advice.request.RequestContext;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.GlbDictMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysGroupMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysGroupObjMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysGroupUserMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysObjFrightMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysObjSrightMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysTenantMemberMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.GlbDict;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroup;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroupObj;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysObjFright;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysObjSright;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysTenantMember;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupObjBO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupReqBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupRespBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupUserReqBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupUserRespBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysObjFrightBO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysObjSrightBO;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.SysObjOperateVo;
import com.newland.paas.paasservice.sysmgr.vo.FrightReqVo;
import com.newland.paas.paasservice.sysmgr.vo.SrightFrightReqVo;
import com.newland.paas.sbcommon.vo.PageInfo;
import mockit.MockUp;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.aop.framework.AopContext;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * @author WRP
 * @since 2018/10/8
 */
public class SysObjServiceImplTest {

    @InjectMocks
    private SysObjServiceImpl sysObjService = new SysObjServiceImpl();

    @Mock
    private SysGroupObjMapper sysGroupObjMapper;
    @Mock
    private SysGroupMapper sysGroupMapper;
    @Mock
    private SysTenantMemberMapper sysTenantMemberMapper;
    @Mock
    private SysObjSrightMapper sysObjSrightMapper;
    @Mock
    private SysGroupUserMapper sysGroupUserMapper;
    @Mock
    private GlbDictMapper glbDictMapper;
    @Mock
    private SysObjFrightMapper sysObjFrightMapper;

    @BeforeClass
    public void beforeClass() {
        MockitoAnnotations.initMocks(this);

        new MockUp<AopContext>() {
            @mockit.Mock
            public Object currentProxy() throws IllegalStateException {
                return sysObjService;
            }
        };

        new MockUp<RequestContext>() {
            @mockit.Mock
            public Long getUserId() {
                return 1L;
            }

            @mockit.Mock
            public Long getTenantId() {
                return 1L;
            }
        };

        Long groupId = 1L;

        when(sysGroupObjMapper.selectDetailBySelective(any(SysGroupObjBO.class), any(List.class))).thenAnswer(invocation -> {
            List<SysGroupObjBO> list = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                SysGroupObjBO info = new SysGroupObjBO();
                info.setGroupId(groupId);
                info.setObjId(Long.valueOf(i));
                info.setObjType("cluster");
                info.setObjName("cluster_" + i);
                info.setIsSright("0");
                info.setObjTypeName("集群");
                info.setSysCategoryName("333");
                list.add(info);
            }
            return list;
        });
        when(sysGroupObjMapper.selectInGroupIds(any(SysGroupObjBO.class), any(List.class))).thenAnswer(invocation -> {
            List<SysGroupObj> list = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                SysGroupObj info = new SysGroupObj();
                info.setGroupId(groupId);
                info.setObjId(Long.valueOf(i));
                info.setObjType("cluster");
                info.setObjName("cluster_" + i);
                info.setIsSright("0");
                list.add(info);
            }
            return list;
        });
        when(sysGroupObjMapper.selectGroupRoleOperates(any(Long.class), any(Long.class))).thenAnswer(invocation -> {
            List<SysObjFrightBO> list = new ArrayList<>();
            for (int i = 0; i < 1; i++) {
                SysObjFrightBO info = new SysObjFrightBO();
                info.setUserId(1L);
                info.setGroupRoleId(1L);
                info.setGroupId(groupId);
                info.setObjId(Long.valueOf(i));
                info.setOperates("pf_modify,pf_delete");
                list.add(info);
            }
            return list;
        });

        when(sysGroupMapper.findBaseAll()).thenAnswer(invocation -> {
            List<SysGroup> list = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                SysGroup sysGroup = new SysGroup();
                sysGroup.setGroupId(Long.valueOf(i));
                sysGroup.setGroupName("name_" + i);
                list.add(sysGroup);
            }
            return list;
        });
        when(sysGroupMapper.selectBySelective(any(SysGroupReqBo.class))).thenAnswer(invocation -> {
            List<SysGroupRespBo> list = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                SysGroupRespBo sysGroup = new SysGroupRespBo();
                sysGroup.setGroupId(Long.valueOf(i));
                sysGroup.setGroupName("name_" + i);
                list.add(sysGroup);
            }
            return list;
        });
        when(sysGroupMapper.selectGroupByGroupIds(any(List.class))).thenAnswer(invocation -> {
            List<SysGroupRespBo> list = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                SysGroupRespBo sysGroup = new SysGroupRespBo();
                sysGroup.setGroupId(Long.valueOf(i));
                sysGroup.setGroupName("name_" + i);
                list.add(sysGroup);
            }
            return list;
        });
        when(sysGroupMapper.selectByPrimaryKey(any(Long.class))).thenAnswer(invocation -> {
            SysGroupRespBo sysGroup = new SysGroupRespBo();
            sysGroup.setGroupId(1L);
            sysGroup.setGroupName("name_" + 1);
            return sysGroup;
        });

        when(sysTenantMemberMapper.selectBySelective(any(SysTenantMember.class))).thenAnswer(invocation -> {
            List<SysTenantMember> list = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                SysTenantMember sysTenantMember = new SysTenantMember();
                sysTenantMember.setId(Long.valueOf(i));
                list.add(sysTenantMember);
            }
            return list;
        });

        when(sysObjSrightMapper.selectGroupDetail(any(SysObjSrightBO.class))).thenAnswer(invocation -> {
            List<SysObjSrightBO> list = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                SysObjSrightBO sysObjSrightBO = new SysObjSrightBO();
                sysObjSrightBO.setGroupId(Long.valueOf(i));
                sysObjSrightBO.setObjId(Long.valueOf(i));
                sysObjSrightBO.setObjName("name_" + i);
                sysObjSrightBO.setObjType("cluster");
                sysObjSrightBO.setCreatorId(Long.valueOf(i));
                list.add(sysObjSrightBO);
            }
            return list;
        });
        when(sysObjSrightMapper.deleteBySelective(any(SysObjSright.class))).thenAnswer(invocation -> 1);
        when(sysObjSrightMapper.insertSelective(any(SysObjSright.class))).thenAnswer(invocation -> 1);
        when(sysObjSrightMapper.selectObjSrightByParams(any(Long.class), any(List.class))).thenAnswer(invocation -> {
            List<SysObjSrightBO> list = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                SysObjSrightBO sysObjSrightBO = new SysObjSrightBO();
                sysObjSrightBO.setGroupId(Long.valueOf(i));
                sysObjSrightBO.setObjId(Long.valueOf(i));
                sysObjSrightBO.setObjName("name_" + i);
                sysObjSrightBO.setObjType("cluster");
                sysObjSrightBO.setCreatorId(Long.valueOf(i));
                list.add(sysObjSrightBO);
            }
            return list;
        });

        when(sysGroupUserMapper.selectBySelective(any(SysGroupUserReqBo.class))).thenAnswer(invocation -> {
            List<SysGroupUserRespBo> list = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                SysGroupUserRespBo sysGroupUserRespBo = new SysGroupUserRespBo();
                sysGroupUserRespBo.setGroupId(Long.valueOf(i));
                sysGroupUserRespBo.setIsAdmin(Short.valueOf("1"));
                list.add(sysGroupUserRespBo);
            }
            return list;
        });

        when(glbDictMapper.selectByDictPcode(any(String.class))).thenAnswer(invocation -> {
            List<GlbDict> list = new ArrayList<>();
            for (int i = 0; i < 1; i++) {
                GlbDict glbDict = new GlbDict();
                glbDict.setDictCode("pf_modify");
                glbDict.setDictName("修改");
                glbDict.setDictPcode("cluster_op");
                list.add(glbDict);
            }
            return list;
        });
        when(glbDictMapper.selectDetailByDictCode(any(String.class), any(List.class), null)).thenAnswer(invocation -> {
            List<GlbDict> list = new ArrayList<>();
            for (int i = 0; i < 1; i++) {
                GlbDict glbDict = new GlbDict();
                glbDict.setDictCode("pf_modify");
                glbDict.setDictName("修改");
                glbDict.setDictPcode("cluster_op");
                list.add(glbDict);
            }
            return list;
        });
        when(glbDictMapper.selectByDictCode(any(String.class), any(List.class), null)).thenAnswer(invocation -> {
            List<GlbDict> list = new ArrayList<>();
            for (int i = 0; i < 1; i++) {
                GlbDict glbDict = new GlbDict();
                glbDict.setDictCode("pf_modify");
                glbDict.setDictName("修改");
                glbDict.setDictPcode("cluster_op");
                list.add(glbDict);
            }
            return list;
        });

        when(sysObjFrightMapper.selectGroupRoleDetail(any(SysObjFrightBO.class))).thenAnswer(invocation -> {
            List<SysObjFrightBO> list = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                SysObjFrightBO sysObjFrightBO = new SysObjFrightBO();
                sysObjFrightBO.setGroupRoleId(Long.valueOf(i));
                sysObjFrightBO.setGroupRoleName("name_" + i);
                sysObjFrightBO.setOperates("pf_modify");
                list.add(sysObjFrightBO);
            }
            return list;
        });
        when(sysObjFrightMapper.deleteBySelective(any(SysObjFright.class))).thenAnswer(invocation -> 1);
        when(sysObjFrightMapper.insertSelective(any(SysObjFright.class))).thenAnswer(invocation -> 1);
    }

    @Test
    public void pageViewByParams() {
        PageInfo pageInfo = new PageInfo(1, 10);
        Assert.assertNotNull(sysObjService.pageViewByParams(pageInfo, new SysGroupObjBO()));
    }

    @Test
    public void getGroupObjDetail() {
        Assert.assertNotNull(sysObjService.getGroupObjDetail(1L));
    }

    @Test
    public void getGroupAndGroupRole() {
        List<Long> groupIds = new ArrayList<>();
        groupIds.add(1L);
        Assert.assertNotNull(sysObjService.getGroupAndGroupRole(1L, groupIds));
    }

    @Test
    public void srightFright() {
        List<SrightFrightReqVo> srightFrights = new ArrayList<>();
        SrightFrightReqVo srightFrightReqVo = new SrightFrightReqVo();
        srightFrightReqVo.setIsAdmin(false);
        srightFrightReqVo.setGroupId(1L);
        List<String> operates = new ArrayList<>();
        operates.add("pf_modify");
        srightFrightReqVo.setOperates(operates);
        srightFrights.add(srightFrightReqVo);
        srightFrightReqVo = new SrightFrightReqVo();
        srightFrightReqVo.setIsAdmin(true);
        srightFrightReqVo.setGroupId(2L);
        operates.add("pf_modify");
        srightFrightReqVo.setOperates(operates);
        List<FrightReqVo> groupRoles = new ArrayList<>();
        FrightReqVo frightReqVo = new FrightReqVo();
        frightReqVo.setRoleId(1L);
        frightReqVo.setOperates(operates);
        groupRoles.add(frightReqVo);
        srightFrightReqVo.setGroupRoles(groupRoles);
        srightFrights.add(srightFrightReqVo);
        sysObjService.srightFright(1L, srightFrights);
    }

}
