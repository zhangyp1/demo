package com.newland.paas.paasservice.sysmgr.service.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysGroupMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysGroupRoleMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysUserGroupRoleMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroupRole;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupRespBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupRoleReqBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupRoleRespBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysUserGroupRoleReqBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysUserGroupRoleRespBo;
import com.newland.paas.paasservice.sysmgr.service.SysUserGroupRoleService;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.PageInfo;

public class SysGroupRoleServiceImplTest {
	
	@InjectMocks
	private SysGroupRoleServiceImpl sysGroupRoleService;
	
	@Mock
	private SysGroupRoleMapper sysGroupRoleMapper;
	@Mock
	private SysGroupMapper sysGroupMapper;
	@Mock
	private SysUserGroupRoleMapper sysUserGroupRoleMapper;
	@Mock
	private SysUserGroupRoleService sysUserGroupRoleService;
	
	@BeforeClass
	public void beforeClass() throws ApplicationException {
		MockitoAnnotations.initMocks(this);
		
		when(sysGroupRoleMapper.countBySelective(any(SysGroupRoleReqBo.class))).thenAnswer(invocation -> 10);
		
		when(sysGroupRoleMapper.selectBySelective(any(SysGroupRoleReqBo.class)))
			.thenAnswer(invocation -> new ArrayList<SysGroupRoleReqBo>());
		when(sysGroupMapper.selectByPrimaryKey(any(Long.class))).thenAnswer(invocation -> {
			Long groupId = invocation.getArgumentAt(0, Long.class);
			if(groupId == -1l){
				return null;
			}else{
				return new SysGroupRespBo();
			}
		});
		
		when(sysGroupRoleMapper.countBySelective(any(SysGroupRoleReqBo.class))).thenAnswer(invocation ->{
			SysGroupRoleReqBo params = invocation.getArgumentAt(0, SysGroupRoleReqBo.class);
			if(params == null){
				return 10;
			}
			if("重复".equals(params.getGroupRoleName())){
				return 1;
			}else{
				return 0;
			}
		});
		when(sysUserGroupRoleMapper.countBySelective(any(SysUserGroupRoleReqBo.class))).thenAnswer(invocation ->{
			SysUserGroupRoleReqBo params = invocation.getArgumentAt(0, SysUserGroupRoleReqBo.class);
			if(params.getGroupRoleId() == -1l){
				return 0;
			}else{
				return 10;
			}
		});
		
		when(sysGroupRoleMapper.updateByPrimaryKeySelective(any(SysGroupRole.class))).thenAnswer(invocation -> 1);
		
		when(sysGroupRoleMapper.selectByPrimaryKey(any(Long.class))).thenAnswer(invocation -> new SysGroupRoleRespBo());
		
		when(sysGroupRoleMapper.insertSelective(any(SysGroupRole.class))).thenAnswer(invocation -> 1);
		
		when(sysUserGroupRoleService.list(any(SysUserGroupRoleReqBo.class))).thenAnswer(invocation -> new ArrayList<SysUserGroupRoleRespBo>());
	}

	@BeforeTest
	public void beforeTest() {
		
	}

	@Test
	public void add() throws ApplicationException {
		SysGroupRole params = new SysGroupRole();
		params.setGroupId(-1l);
		try{
			sysGroupRoleService.add(params);
		}catch(Exception ex){
			Assert.assertTrue(ex instanceof ApplicationException);
		}
		params.setGroupId(1l);
		params.setGroupRoleName("重复");
		try{
			sysGroupRoleService.add(params);
		}catch(Exception ex){
			Assert.assertTrue(ex instanceof ApplicationException);
		}
		params.setGroupRoleName("groupRoleName");
		sysGroupRoleService.add(params);
	}

	@Test
	public void delete() throws ApplicationException {
		try{
			sysGroupRoleService.delete(10l, true);
		}catch(Exception ex){
			Assert.assertTrue(ex instanceof ApplicationException);
		}
		sysGroupRoleService.delete(-1l, true);
	}

	@Test
	public void get() throws ApplicationException {
		Assert.assertNotNull(sysGroupRoleService.get(1l));
	}

	@Test
	public void groupCount() throws ApplicationException {
		Assert.assertTrue(sysGroupRoleService.groupCount()>0);
	}

	@Test
	public void list() throws ApplicationException {
		SysGroupRoleReqBo req = new SysGroupRoleReqBo();
		Assert.assertNotNull(sysGroupRoleService.list(req));
	}

	@Test
	public void page() throws ApplicationException {
		SysGroupRoleReqBo req = new SysGroupRoleReqBo();
		PageInfo pageInfo = new PageInfo();
		Assert.assertNotNull(sysGroupRoleService.page(req, pageInfo).getList());
	}

	@Test
	public void update() throws ApplicationException {
		SysGroupRole params = new SysGroupRole();
		params.setGroupId(-1l);
		try{
			sysGroupRoleService.update(params);
		}catch(Exception ex){
			Assert.assertTrue(ex instanceof ApplicationException);
		}
		params.setGroupId(1l);
		params.setGroupRoleName("重复");
		try{
			sysGroupRoleService.update(params);
		}catch(Exception ex){
			Assert.assertTrue(ex instanceof ApplicationException);
		}
		params.setGroupRoleName("groupRoleName");
		sysGroupRoleService.update(params);
	}
}
