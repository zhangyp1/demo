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
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysUrlRoleMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysUrlRole;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysUrlRoleReqBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysUrlRoleRespBo;
import com.newland.paas.paasservice.sysmgr.runner.InitializeRunner;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.PageInfo;

public class SysUrlRoleServiceImplTest {

	@InjectMocks
	private SysUrlRoleServiceImpl sysUrlRoleService;
	@Mock
	private SysUrlRoleMapper sysUrlRoleMapper;

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
		
		when(sysUrlRoleMapper.selectBySelective(any(SysUrlRoleReqBo.class)))
			.thenAnswer(invocation -> new ArrayList<SysUrlRoleRespBo>());
		
		when(sysUrlRoleMapper.insertSelective(any(SysUrlRole.class))).thenAnswer(invocation -> 1);
		
		when(sysUrlRoleMapper.updateByPrimaryKeySelective(any(SysUrlRole.class))).thenAnswer(invocation -> 1);
		
		when(sysUrlRoleMapper.deleteByPrimaryKey(any(Long.class))).thenAnswer(invocation -> 1);
		
		when(sysUrlRoleMapper.selectByPrimaryKey(any(Long.class))).thenAnswer(invocation -> new SysUrlRoleRespBo());
		
		when(sysUrlRoleMapper.deleteBySelective(any(SysUrlRoleReqBo.class))).thenAnswer(invocation -> 1);
		
		when(sysUrlRoleMapper.insertSelective(any(SysUrlRole.class))).thenAnswer(invocation -> 1);
		
		when(sysUrlRoleMapper.getUrlsByRole(any(Long.class))).thenAnswer(invocation -> new ArrayList<SysUrlRoleRespBo>());
	}

	@BeforeTest
	public void beforeTest() {
	}

	@Test
	public void add() throws ApplicationException {
		Assert.assertEquals(sysUrlRoleService.add(new SysUrlRole()), 1);
	}

	@Test
	public void addUrls() throws ApplicationException {
		SysUrlRoleReqBo params = new SysUrlRoleReqBo();
		try{
			sysUrlRoleService.addUrls(params);
		}catch(Exception ex){
			Assert.assertTrue(ex instanceof ApplicationException);
		}
		params.setRoleId(1l);
		Assert.assertEquals(sysUrlRoleService.addUrls(params), 0);
		
		params.setUrls(new String[]{"url1","url2"});
		Assert.assertTrue(sysUrlRoleService.addUrls(params)>0);
		
	}

	@Test
	public void delete() throws ApplicationException {
		Assert.assertEquals(sysUrlRoleService.delete(1l), 1);
	}

	@Test
	public void get() throws ApplicationException {
		Assert.assertNotNull(sysUrlRoleService.get(1l));
	}

	@Test
	public void getUrlsByRole() {
		Assert.assertNotNull(sysUrlRoleService.getUrlsByRole(1l));
	}

	@Test
	public void list() throws ApplicationException {
		Assert.assertNotNull(sysUrlRoleService.list(new SysUrlRoleReqBo()));
	}

	@Test
	public void page() throws ApplicationException {
		Assert.assertNotNull(sysUrlRoleService.page(new SysUrlRoleReqBo(), new PageInfo()).getList());
	}

	@Test
	public void update() throws ApplicationException {
		Assert.assertEquals(sysUrlRoleService.update(new SysUrlRole()), 1);
	}
}
