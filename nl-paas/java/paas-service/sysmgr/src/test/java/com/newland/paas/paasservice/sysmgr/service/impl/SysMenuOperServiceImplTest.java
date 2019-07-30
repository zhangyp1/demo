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
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysMenuOperMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysMenuOper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.MenuBO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.OperBO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysMenuOperReqBo;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.PageInfo;

public class SysMenuOperServiceImplTest {
	@InjectMocks
	private SysMenuOperServiceImpl sysMenuOperService = new SysMenuOperServiceImpl();
	
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
		
		when(sysMenuOperMapper.getMenusByUserId(any(Long.class), any(Long.class))).thenAnswer(invocation -> new ArrayList<MenuBO>());
		
		when(sysMenuOperMapper.getOpersByUserId(any(Long.class), any(Long.class))).thenAnswer(invocation -> new ArrayList<OperBO>());
		
		when(sysMenuOperMapper.selectByPrimaryKey(any(Long.class))).thenAnswer(invocation -> {
			Long menuId = invocation.getArgumentAt(0, Long.class);
			if(menuId == -1){
				return null;
			}else{
				SysMenuOper menu = new SysMenuOper();
				menu.setId(menuId);
				menu.setName("name");
				menu.setOrderNumber("01");
				return menu;
			}
		});
		
		when(sysMenuOperMapper.insert(any(SysMenuOper.class))).thenAnswer(invocation -> 1);
		
		when(sysMenuOperMapper.countBySelective(any(SysMenuOperReqBo.class))).thenAnswer(invocation ->{
			SysMenuOperReqBo params = invocation.getArgumentAt(0, SysMenuOperReqBo.class);
			if(params.getParentId() != null && 88==params.getParentId()){
				return 1;
			}
			if("01/99".equals(params.getOrderNumber())){
				return 1;
			}else{
				return 0;
			}
		});
		
		when(sysMenuOperMapper.updateByPrimaryKeySelective(any(SysMenuOper.class))).thenAnswer(invocation -> 1);
		
		when(sysMenuOperMapper.getMenusList()).thenAnswer(invocation -> new ArrayList<MenuBO>());
		
		
	}

	@BeforeTest
	public void beforeTest() {
	}

	@Test
	public void add() throws ApplicationException {
		
		new MockUp<SysMenuOperServiceImpl>() {
			@mockit.Mock
			protected boolean isOrderNumberExist(Long id,String orderNumInt, Long parentId){
				if("99".equals(orderNumInt)){
					return true;
				}else{
					return false;
				}
			}
		};
		
		SysMenuOperReqBo params = new SysMenuOperReqBo();
		params.setOrderNumber("99");
		try{
			sysMenuOperService.add(params);
		}catch(Exception ex){
			Assert.assertTrue(ex instanceof ApplicationException);
		}
		params.setOrderNumber("01");
		Assert.assertEquals(sysMenuOperService.add(params), 1);
		
		params.setParentId(10l);
		Assert.assertEquals(sysMenuOperService.add(params), 1);
		
	}

	@Test
	public void delete() throws ApplicationException {
		try{
			sysMenuOperService.delete(88l);
		}catch(Exception ex){
			Assert.assertTrue(ex instanceof ApplicationException);
		}
		Assert.assertEquals(sysMenuOperService.delete(1l), 1);
		
	}

	@Test
	public void get() {
		Assert.assertNotNull(sysMenuOperService.get(1l));
	}

	@Test
	public void getMenus() {
		Assert.assertNotNull(sysMenuOperService.getMenus(new SysMenuOperReqBo()));
	}

	@Test
	public void getMenusByTenantUserId() throws ApplicationException {
		Assert.assertNotNull(sysMenuOperService.getMenusByTenantUserId(1l, 1l));
	}

	@Test
	public void getMenusList() throws ApplicationException {
		Assert.assertNotNull(sysMenuOperService.getMenusList());
	}

	@Test
	public void getOpersByTenantUserId() throws ApplicationException {
		Assert.assertNotNull(sysMenuOperService.getOpersByTenantUserId(1l, 1l));
	}

	@Test
	public void isOrderNumberExist() throws ApplicationException {
		try{
			sysMenuOperService.isOrderNumberExist(1l, "99", -1l);
		}catch(Exception ex){
			Assert.assertTrue(ex instanceof ApplicationException);
		}
		Assert.assertEquals(sysMenuOperService.isOrderNumberExist(1l, "99", 2l), true);
		
		Assert.assertEquals(sysMenuOperService.isOrderNumberExist(1l, "88", 2l), false);
		
	}

	@Test
	public void pageQuery() {
		Assert.assertNotNull(sysMenuOperService.pageQuery(new SysMenuOperReqBo(), new PageInfo()).getList());
	}

	@Test
	public void update() throws ApplicationException {
		new MockUp<SysMenuOperServiceImpl>() {
			@mockit.Mock
			protected boolean isOrderNumberExist(Long id,String orderNumInt, Long parentId){
				if("99".equals(orderNumInt)){
					return true;
				}else{
					return false;
				}
			}
		};
		
		SysMenuOperReqBo params = new SysMenuOperReqBo();
		params.setOrderNumber("99");
		try{
			sysMenuOperService.update(params);
		}catch(Exception ex){
			Assert.assertTrue(ex instanceof ApplicationException);
		}
		params.setOrderNumber("9");
		Assert.assertEquals(sysMenuOperService.update(params), 1);
		params.setParentId(1l);
		Assert.assertEquals(sysMenuOperService.update(params), 1);
		
	}

	@Test
	public void updateChildrenOrderNum() {
		sysMenuOperService.updateChildrenOrderNum(1l, "01");
	}
}
