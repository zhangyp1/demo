package com.newland.paas.paasservice.sysmgr.service.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysMenuOperMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysMenuOperRoleMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysMenuOperRole;
import com.newland.paas.sbcommon.common.ApplicationException;

public class SysMenuOperRoleServiceImplTest {
	
	@InjectMocks
	private SysMenuOperRoleServiceImpl sysMenuOperRoleService;
	
	@Mock
	private SysMenuOperRoleMapper sysMenuOperRoleMapper;
	@Mock
	private SysMenuOperMapper sysMenuOperMapper;
	
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
		
		when(sysMenuOperRoleMapper.deleteBySelective(any(SysMenuOperRole.class))).thenAnswer(invocation -> 1);
		
		when(sysMenuOperMapper.selectAllMenuIdList4Tree(any(List.class))).thenAnswer(invocation -> Arrays.asList(1l,2l,3l));
		
		when(sysMenuOperRoleMapper.batchInsert(any(List.class), any(Long.class))).thenAnswer(invocation -> 10);
		
		when(sysMenuOperRoleMapper.selectBySelective(any(SysMenuOperRole.class))).thenAnswer(invocation ->{
			List<SysMenuOperRole> list = new ArrayList<SysMenuOperRole>();
			for(int i = 0;i < 10;i++){
				SysMenuOperRole info = new SysMenuOperRole();
				info.setId((long)i);
				info.setMenuOperId((long)i);
				list.add(info);
			}
			return list;
		});
	}

	@BeforeTest
	public void beforeTest() {
	}

	@Test
	public void assignMenuPrivilegesToSysRole() throws ApplicationException {
		sysMenuOperRoleService.assignAndCorrectMenuPrivileges(1l, Arrays.asList(1l,2l,3l));
	}

	@Test
	public void getSelectMenuIdList() throws ApplicationException {
		Assert.assertTrue(sysMenuOperRoleService.getSelectMenuIdList(1l).size() > 0);
	}
}
