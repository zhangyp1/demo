package com.newland.paas.paasservice.sysmgr.service.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import mockit.MockUp;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.newland.paas.advice.request.RequestContext;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysGroupRoleMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysUserGroupRoleMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysUserMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroupRole;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysUser;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysUserGroupRole;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupRoleRespBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysUserGroupRoleReqBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysUserGroupRoleRespBo;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.PageInfo;

public class SysUserGroupRoleServiceImplTest {

	@InjectMocks
	private SysUserGroupRoleServiceImpl sysUserGroupRoleService;
	
	@Mock
	private SysUserGroupRoleMapper sysUserGroupRoleMapper;
	@Mock
	private SysUserMapper sysUserMapper;
	@Mock
	private SysGroupRoleMapper sysGroupRoleMapper;

	@BeforeClass
	public void beforeClass() {
		MockitoAnnotations.initMocks(this);
		
		new MockUp<RequestContext>() {
            @mockit.Mock
            public Long getUserId() {
                return 1l;
            }
            
            @mockit.Mock
            public Long getTenantId(){
            	return 1l;
            }
        };
		
		when(sysUserGroupRoleMapper.countBySelective(any(SysUserGroupRoleReqBo.class))).thenAnswer(invocation -> {
			SysUserGroupRoleReqBo params = invocation.getArgumentAt(0, SysUserGroupRoleReqBo.class);
			if(params == null){
				return 100;
			}
			return 1;
		});
		
		when(sysUserGroupRoleMapper.selectBySelective(any(SysUserGroupRoleReqBo.class)))
			.thenAnswer(invocation->new ArrayList<SysUserGroupRoleRespBo>());
		
		when(sysUserGroupRoleMapper.insertSelective(any(SysUserGroupRoleReqBo.class))).thenAnswer(invocation -> 1);
		
		when(sysUserGroupRoleMapper.updateByPrimaryKeySelective(any(SysUserGroupRole.class))).thenAnswer(invocation -> 1);
		
		when(sysUserGroupRoleMapper.selectByPrimaryKey(any(Long.class))).thenAnswer(invocation -> new SysUserGroupRoleRespBo());
		
		when(sysUserGroupRoleMapper.deleteBySelective(any(SysUserGroupRoleReqBo.class))).thenAnswer(invocation -> 1);
		
		when(sysUserMapper.selectByPrimaryKey(any(Long.class))).thenAnswer(invocation ->{
			Long userId = invocation.getArgumentAt(0, Long.class);
			if(userId == -1){
				return null;
			}else{
				SysUser user = new SysUser();
				user.setUserId(userId);
				return user;
			}
		});
		when(sysUserGroupRoleMapper.insertSelective(any(SysUserGroupRole.class))).thenAnswer(invocation -> 1);
		
		when(sysGroupRoleMapper.selectByPrimaryKey(any(Long.class))).thenAnswer(invocation ->{
			Long groupRoleId = invocation.getArgumentAt(0, Long.class);
			if(groupRoleId == -1){
				return null;
			}else{
				SysGroupRoleRespBo bo = new SysGroupRoleRespBo();
				bo.setGroupRoleId(groupRoleId);
				return bo;
			}
		});
		
		when(sysUserGroupRoleMapper.getUsersByGroupRole(any(Long.class)))
			.thenAnswer(invocation -> new ArrayList<SysUserGroupRoleReqBo>());
		
		when(sysUserGroupRoleMapper.getGroupRolesByUser(any(Long.class),any(Long.class)))
			.thenAnswer(invocation -> new ArrayList<SysUserGroupRoleReqBo>());
		
		when(sysUserGroupRoleMapper.getAllUsersByGroup(any(Long.class), any(Long.class),any(String.class)))
			.thenAnswer(invocation-> new ArrayList<SysUserGroupRoleRespBo>());
	}

	@BeforeTest
	public void beforeTest() {
		
	}

	@Test
	public void addUsers() throws ApplicationException {
		SysUserGroupRoleReqBo params = new SysUserGroupRoleReqBo();
		try{
			sysUserGroupRoleService.removeAllAndAddUsers(params);
		}catch(Exception ex){
			Assert.assertTrue(ex instanceof ApplicationException);
		}
		params.setGroupRoleId(1l);
		params.setUserIds(new Long[]{-1l,1l,2l});
		try{
			sysUserGroupRoleService.removeAllAndAddUsers(params);
		}catch(Exception ex){
			Assert.assertTrue(ex instanceof ApplicationException);
		}
		params.setUserIds(new Long[]{1l,2l,3l});
		Assert.assertTrue(sysUserGroupRoleService.removeAllAndAddUsers(params)>0);

	}

	@Test
	public void delete() throws ApplicationException {
		Assert.assertEquals(sysUserGroupRoleService.delete(1l), 1);
	}

	@Test
	public void getAllUsersByGroup() throws ApplicationException {
		SysUserGroupRoleReqBo params = new SysUserGroupRoleReqBo();
		params.setGroupRoleId(1l);
		Assert.assertNotNull(sysUserGroupRoleService.getAllUsersByGroup(params));
	}

	@Test
	public void getGroupRolesByUser() {
		Assert.assertNotNull(sysUserGroupRoleService.getGroupRolesByUser(1l, 1l));
	}

	@Test
	public void getUsersByGroupRole() {
		Assert.assertNotNull(sysUserGroupRoleService.getUsersByGroupRole(1l));
	}

	@Test
	public void groupCount() throws ApplicationException {
		Assert.assertTrue(sysUserGroupRoleService.groupCount()>0);
	}

	@Test
	public void list() throws ApplicationException {
		SysUserGroupRoleReqBo params = new SysUserGroupRoleReqBo();
		Assert.assertNotNull(sysUserGroupRoleService.list(params));
	}

	@Test
	public void page() throws ApplicationException {
		SysUserGroupRoleReqBo params = new SysUserGroupRoleReqBo();
		PageInfo pageInfo = new PageInfo();
		Assert.assertNotNull(sysUserGroupRoleService.page(params, pageInfo).getList());
	}
}
