package com.newland.paas.paasservice.sysmgr.service.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import mockit.MockUp;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.newland.paas.advice.request.RequestContext;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysGroupMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysGroupUserMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysRoleUserMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysTenantMemberMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroupUser;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysRoleUser;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysTenantMember;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.RoleUserReqBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupReqBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupRespBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupUserReqBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysRoleUserReqBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysRoleUserRespBo;
import com.newland.paas.paasservice.sysmgr.service.SysGroupUserService;
import com.newland.paas.paasservice.sysmgr.service.SysUserGroupRoleService;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.utils.SpringContextUtil;
import com.newland.paas.sbcommon.vo.PageInfo;

public class SysRoleUserServiceImplTest {

    @InjectMocks
    private SysRoleUserServiceImpl sysRoleUserService;

    @Mock
    private SysRoleUserMapper sysRoleUserMapper;
    @Mock
    private SysTenantMemberMapper sysTenantMemberMapper;
    @Mock
    private SysGroupMapper sysGroupMapper;
    @Mock
    private SysGroupUserMapper sysGroupUserMapper;

    @Mock
    private SysGroupUserService sysGroupUserService;
    @Mock
    private SysUserGroupRoleService sysUserGroupRoleService;

    @BeforeClass
    public void beforeClass() throws ApplicationException {
        MockitoAnnotations.initMocks(this);

        new MockUp<RequestContext>() {
            @mockit.Mock
            public Long getUserId() {
				return 12l;
            }

			@mockit.Mock
			public Long getTenantId() {
				return 1l;
			}
		};
		
		when(sysRoleUserMapper.selectBySelective(any(SysRoleUserReqBo.class)))
			.thenAnswer(invocation -> new ArrayList<SysRoleUserRespBo>());
		
		when(sysRoleUserMapper.insertSelective(any(SysRoleUser.class))).thenAnswer(invocation -> 1);
		
		when(sysRoleUserMapper.updateByPrimaryKeySelective(any(SysRoleUser.class))).thenAnswer(invocation -> 1);
		
		when(sysRoleUserMapper.deleteByPrimaryKey(any(Long.class))).thenAnswer(invocation -> 1);
		
		when(sysRoleUserMapper.selectByPrimaryKey(any(Long.class))).thenAnswer(invocation -> new SysRoleUserRespBo());
		
		when(sysTenantMemberMapper.selectBySelective(any(SysTenantMember.class))).thenAnswer(invocation ->{
			List<SysTenantMember> list = new ArrayList<>();
			for(long i = 1;i< 10;i++){
				SysTenantMember info = new SysTenantMember();
				info.setUserId(i);
				info.setTenantId(1l);
				list.add(info);
			}
			return list;
		});
		
		when(sysRoleUserMapper.deleteBySelective(any(SysRoleUserReqBo.class))).thenAnswer(invocation -> 1);
		
		when(sysTenantMemberMapper.deleteBySelective(any(SysTenantMember.class))).thenAnswer(invocation -> 1);
		
		when(sysGroupMapper.selectBySelective(any(SysGroupReqBo.class))).thenAnswer(invocation ->{
			List<SysGroupRespBo> groups = new ArrayList<>();
			for(long i = 0;i < 10;i++){
				SysGroupRespBo info = new SysGroupRespBo();
				info.setGroupId(i);
				groups.add(info);
			}
			return groups;
		});
		
		when(sysGroupUserMapper.deleteBySelective(any(SysGroupUserReqBo.class))).thenAnswer(invocation -> 1);
		
		when(sysGroupUserMapper.insertSelective(any(SysGroupUser.class))).thenAnswer(invocation -> 1);
		
		when(sysTenantMemberMapper.insertSelective(any(SysTenantMember.class))).thenAnswer(invocation -> 1);
		
		when(sysRoleUserMapper.getRoleUsersByTenant(any(Long.class))).thenAnswer(invocation -> new ArrayList<SysRoleUserRespBo>());
		
		when(sysTenantMemberMapper.updateByPrimaryKeySelective(any(SysTenantMember.class))).thenAnswer(invocation -> 1);
		

        when(sysGroupUserService.delete(any(Long.class))).thenAnswer(invocation -> 1);
        when(sysUserGroupRoleService.delete(any(Long.class))).thenAnswer(invocation -> 1);
    }

	@BeforeTest
	public void beforeTest() {
	}

    @Test
    public void add() throws ApplicationException {
        Assert.assertEquals(sysRoleUserService.add(new SysRoleUser()), 1);
    }

    @Test
    public void addUsers() throws ApplicationException {
        /*SysRoleUserReqBo params = new SysRoleUserReqBo();
        try {
            sysRoleUserService.addUsers(params);
        } catch (Exception ex) {
            Assert.assertTrue(ex instanceof ApplicationException);
        }

        params.setTenantId(1l);
        RoleUserReqBo[] roleUsers = new RoleUserReqBo[10];
        for (int i = 0; i < 10; i++) {
            RoleUserReqBo roleUser = new RoleUserReqBo();
            roleUser.setRoleId((long)i);
            roleUser.setUserId((long)i);
            roleUsers[i] = roleUser;
        }
        params.setRoleUsers(roleUsers);
        Assert.assertTrue(sysRoleUserService.addUsers(params) > 0);*/

    }

    @Test
    public void delete() throws ApplicationException {
        Assert.assertEquals(sysRoleUserService.delete(1l), 1);
    }

    @Test
    public void get() throws ApplicationException {
        Assert.assertNotNull(sysRoleUserService.get(1l));
    }

    @Test
    public void getRoleUsersByTenant() {
        Assert.assertNotNull(sysRoleUserService.getRoleUsersByTenant(1l));
    }

    @Test
    public void list() throws ApplicationException {
        Assert.assertNotNull(sysRoleUserService.list(new SysRoleUserReqBo()));
    }

    @Test
    public void page() throws ApplicationException {
        Assert.assertNotNull(sysRoleUserService.page(new SysRoleUserReqBo(), new PageInfo()).getList());
    }

    @Test
    public void setAdmin() throws ApplicationException {
        /*SysRoleUserReqBo params = new SysRoleUserReqBo();

        Assert.assertTrue(sysRoleUserService.setAdmin(params) > 0);*/
    }

    @Test
    public void update() throws ApplicationException {
        Assert.assertEquals(sysRoleUserService.update(new SysRoleUser()), 1);
    }
}
