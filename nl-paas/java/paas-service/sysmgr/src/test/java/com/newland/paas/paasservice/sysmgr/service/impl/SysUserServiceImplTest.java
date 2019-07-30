package com.newland.paas.paasservice.sysmgr.service.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.*;
import mockit.MockUp;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.newland.paas.advice.request.RequestContext;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysRole;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysTenant;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysUser;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupRoleEx;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupUserReqBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysTenantRoleBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.TenantUserBO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.TenantUserRO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.UserDeptInfoBO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.UserGroupInfoBO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.UserTenantInfoBO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.UserWorkInfoBO;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.BasicRequestContentVo;
import com.newland.paas.sbcommon.vo.PageInfo;

public class SysUserServiceImplTest {
	
	@InjectMocks
	private SysUserServiceImpl sysUserService = new SysUserServiceImpl();
	@Mock
    private SysUserMapper sysUserMapper;
	@Mock
    private SysTenantMapper sysTenantMapper;
	@Mock
    private SysTenantMemberMapper sysTenantMemberMapper;
	@Mock
    private SysGroupUserMapper sysGroupUserMapper;
	@Mock
    private SysRoleUserMapper sysRoleUserMapper;
	@Mock
	private SysRoleMapper sysRoleMapper;

	@BeforeClass
	public void beforeClass() {
		MockitoAnnotations.initMocks(this);

		new MockUp<RequestContext>() {
			@mockit.Mock
			public Long getUserId() {
				return 1l;
			}

			@mockit.Mock
			public Long getTenantId() {
				return 1l;
			}
		};
		
		when(sysUserMapper.selectBySelective(any(SysUser.class))).thenAnswer(invocation -> {
			List<SysUser> users = new ArrayList<>();
			for(long i = 0;i < 10;i++){
				SysUser user = new SysUser();
				user.setUserId(i);
				user.setUsername("name"+i);
				users.add(user);
			}
			return users;
		});
		
		when(sysUserMapper.getTenantInfosByUserId(any(Long.class))).thenAnswer(invocation -> new ArrayList<UserTenantInfoBO>());
		
		when(sysTenantMapper.selectByPrimaryKey(any(Long.class))).thenAnswer(invocation -> new SysTenant());
		
		when(sysUserMapper.countBySelective(any(SysUser.class))).thenAnswer(invocation -> 10);
		
		when(sysUserMapper.userCount(any(String.class), any(String.class))).thenAnswer(invocation -> 10);
		
		when(sysUserMapper.getGroupsByUserId(any(Long.class))).thenAnswer(invocation -> new ArrayList<UserGroupInfoBO>() );
		
		when(sysGroupUserMapper.countBySelective(any(SysGroupUserReqBo.class))).thenAnswer(invocation -> {
			SysGroupUserReqBo params = invocation.getArgumentAt(0, SysGroupUserReqBo.class);
			if(params.getUserId() == 1l){
				if(params.getGroupId() == 1l){
					return 1;
				}else{
					return 0;
				}
			}else{
				return 10;
			}
		});
		
		when(sysUserMapper.getWorkInfosByUserId(any(Long.class))).thenAnswer(invocation -> new ArrayList<UserWorkInfoBO>() );
		
		when(sysUserMapper.selectByPrimaryKey(any(Long.class))).thenAnswer(invocation -> new SysUser());
		
		when(sysUserMapper.deleteByPrimaryKey(any(Long.class))).thenAnswer(invocation -> 1);
		
		when(sysUserMapper.deleteBySelective(any(SysUser.class))).thenAnswer(invocation -> 1);
		
		when(sysUserMapper.insert(any(SysUser.class))).thenAnswer(invocation -> 1);
		
		when(sysUserMapper.insertSelective(any(SysUser.class))).thenAnswer(invocation -> 1);
		
		when(sysUserMapper.countBySelective(any(SysUser.class))).thenAnswer(invocation -> 10);
		
		when(sysUserMapper.updateByPrimaryKeySelective(any(SysUser.class))).thenAnswer(invocation -> 1);
		
		when(sysUserMapper.updateByPrimaryKey(any(SysUser.class))).thenAnswer(invocation -> 1);
		
		when(sysUserMapper.getSysUsersByIds(any(String[].class))).thenAnswer(invocation -> new ArrayList<SysUser>());
		
		when(sysUserMapper.getUsers(any(TenantUserRO.class))).thenAnswer(invocation -> new ArrayList<TenantUserBO>());
		
		when(sysUserMapper.countDupSysUser(any(SysUser.class))).thenAnswer(invocation -> {
			SysUser params = invocation.getArgumentAt(0, SysUser.class);
			if("重复".equals(params.getAccount())){
				return 1;
			}else if("重复".equals(params.getUsername())){
				return 1;
			}else{
				return 0;
			}
		});
		
		when(sysUserMapper.listSysUser(any(SysUser.class))).thenAnswer(invocation -> new ArrayList<SysUser>());
		
		when(sysTenantMemberMapper.listSysTenantByUserId(any(Long.class))).thenAnswer(invocation -> new ArrayList<SysTenantRoleBo>());
		when(sysGroupUserMapper.listSysGroupByUserId(any(Long.class))).thenAnswer(invocation -> new ArrayList<SysGroupRoleEx>());
		when(sysRoleMapper.listSysRoleByUserId(any(Long.class))).thenAnswer(invocation -> new ArrayList<SysRole>());
		
		when(sysUserMapper.findDeptAll()).thenAnswer(invocation -> new ArrayList<UserDeptInfoBO>());
		
		when(sysUserMapper.getUnRoleSysUser(any(SysUser.class))).thenAnswer(invocation -> new ArrayList<SysUser>());
		
		when(sysUserMapper.getSysRoleUser(any(Long.class))).thenAnswer(invocation -> new ArrayList<SysUser>());
		
	}

	@BeforeTest
	public void beforeTest() {
	}

	@Test
	public void countBySelective() {
		Assert.assertTrue(sysUserService.countBySelective(new SysUser()) > 0);
	}

	@Test
	public void delSysUser() throws ApplicationException {
		sysUserService.delSysUser(new SysUser());
	}

	@Test
	public void deleteByPrimaryKey() {
		Assert.assertEquals(sysUserService.deleteByPrimaryKey(1l), 1);
	}

	@Test
	public void deleteBySelective() {
		Assert.assertEquals(sysUserService.deleteBySelective(new SysUser()), 1);
	}

	@Test
	public void getGroupsByUserId() throws ApplicationException {
		Assert.assertNotNull(sysUserService.getGroupsByUserId(1l));
	}

	@Test
	public void getSysRoleUser() {
		Assert.assertNotNull(sysUserService.getSysRoleUser(1l));
	}

	@Test
	public void getSysUserAllInfo() {
		Assert.assertNotNull(sysUserService.getSysUserAllInfo(new SysUser()));
	}

	@Test
	public void getSysUsers() {
		Assert.assertNotNull(sysUserService.getSysUsers(new TenantUserRO()));
	}

	@Test
	public void getSysUsersByIds() {
		Assert.assertNotNull(sysUserService.getSysUsersByIds(new String[]{"1","2","3"}));
	}

	@Test
	public void getTenantById() {
		Assert.assertNotNull(sysUserService.getTenantById(1l));
	}

	@Test
	public void getTenantInfosByUserId() throws ApplicationException {
		Assert.assertNotNull(sysUserService.getTenantInfosByUserId(1l));
	}

	@Test
	public void getUnRoleSysUser() {
		BasicRequestContentVo<SysUser> vo = new BasicRequestContentVo<SysUser>();
		SysUser params = new SysUser();
		vo.setParams(params);
		Assert.assertNotNull(sysUserService.getUnRoleSysUser(vo));
	}

	@Test
	public void getUserByName() throws ApplicationException {
//		Assert.assertNotNull(sysUserService.getUserByAccount("aaa"));
	}

	@Test
	public void getUserDeptList() {
		Assert.assertNotNull(sysUserService.getUserDeptList());
	}

	@Test
	public void getWorkInfosByUserId() throws ApplicationException {
		Assert.assertNotNull(sysUserService.getWorkInfosByUserId(1l));
	}

	@Test
	public void insert() {
		Assert.assertEquals(sysUserService.insert(new SysUser()), 1);
	}

	@Test
	public void insertSelective() {
		Assert.assertEquals(sysUserService.insertSelective(new SysUser()), 1);
	}

	@Test
	public void isUserInGroup() {
		Assert.assertTrue(sysUserService.isUserInGroup(1l,1l));
		Assert.assertFalse(sysUserService.isUserInGroup(1l, 2l));
	}

	@Test
	public void pageQuerySysUser() throws ApplicationException {
		Assert.assertNotNull(sysUserService.pageQuerySysUser(new SysUser(), new PageInfo()).getList());
	}

	@Test
	public void saveSysUser() throws ApplicationException {
		SysUser params = new SysUser();
		sysUserService.saveSysUser(params, null);
		params.setUsername("重复");
		try{
			sysUserService.saveSysUser(params, null);
		}catch(Exception ex){
			Assert.assertTrue(ex instanceof ApplicationException);
		}
		params.setAccount("重复");
		params.setUsername("aaa");
		params.setAccount(null);
		sysUserService.saveSysUser(params, null);
		params.setAccount("aaa");
		sysUserService.saveSysUser(params, null);
		try{
			sysUserService.saveSysUser(params, null);
		}catch(Exception ex){
			Assert.assertTrue(ex instanceof ApplicationException);
		}
		sysUserService.saveSysUser(params, 1);
		params.setUserId(1l);
		sysUserService.saveSysUser(params, null);
		sysUserService.saveSysUser(params, 1);

	}

	@Test
	public void selectByPrimaryKey() {
		Assert.assertNotNull(sysUserService.selectByPrimaryKey(1l));
	}

	@Test
	public void selectBySelective() {
		Assert.assertNotNull(sysUserService.selectBySelective(new SysUser()));
	}

	@Test
	public void updateByPrimaryKey() {
		Assert.assertEquals(sysUserService.updateByPrimaryKey(new SysUser()), 1);
	}

	@Test
	public void updateByPrimaryKeySelective() {
		Assert.assertEquals(sysUserService.updateByPrimaryKeySelective(new SysUser()), 1);
	}

	@Test
	public void userCount() {
		Assert.assertTrue(sysUserService.userCount() > 0);
	}

	@Test
	public void userCountStringString() {
		Assert.assertTrue(sysUserService.userCount("2017-11-11", "2018-11-11")> 0);
	}

	@Test
	public void userLoginInfo() {
		Assert.assertNotNull(sysUserService.userLoginInfo());
	}
}
