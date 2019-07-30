package com.newland.paas.paasservice.sysmgr.service.impl;


import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import mockit.MockUp;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.newland.paas.advice.request.RequestContext;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysTenantMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysTenantMemberMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysTenantMember;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysTenantMemberReqBO;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysTenantMemberReqEx;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.TenantUserBO;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.SysTenantMemberReqVo;
import com.newland.paas.paasservice.sysmgr.common.SysMgrConstants;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.PageInfo;

public class SysTenantMemberServiceImplTest {
	
	@InjectMocks
	private SysTenantMemberServiceImpl sysTenantMemberService;
	
	@Mock
    private SysTenantMemberMapper sysTenantMemberMapper;
    @Mock
    private SysTenantMapper sysTenantMapper;
	
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
		
		when(sysTenantMemberMapper.countBySelective(any(SysTenantMember.class))).thenAnswer(invocation -> 10);
		
		when(sysTenantMapper.getSysTenantUsers(any(Long.class), any(Short.class), any(String.class)))
			.thenAnswer(invocation -> new ArrayList<TenantUserBO>());
		
		when(sysTenantMapper.getNoMemberUsers(any(Long.class),any(String.class))).thenAnswer(invocation ->{
			List<TenantUserBO> list = new ArrayList<>();
			for(int i = 0;i < 10;i++){
				TenantUserBO info = new TenantUserBO();
				info.setUserId((long)i);
				list.add(info);
			}
			return list;
			
		});
		
		when(sysTenantMemberMapper.selectBySelective(any(SysTenantMember.class)))
			.thenAnswer(invocation -> {
				List<SysTenantMember> list = new ArrayList<>();
				for(long i = 1;i < 5;i++){
					SysTenantMember info = new SysTenantMember();
					info.setTenantId(1l);
					info.setUserId(i);
					if(i%2 == 0){
						info.setIsAdmin(SysMgrConstants.IS_ADMIN_NO);
					}else{
						info.setIsAdmin(SysMgrConstants.IS_ADMIN_YES);
					}
					list.add(info);
				}
				
				SysTenantMember params = invocation.getArgumentAt(0, SysTenantMember.class);
				Long userId = params.getUserId();
				
				if(params.getUserId() != null){
					list = list.stream().filter(sysTenantMember -> sysTenantMember.getUserId() == userId)
						.collect(Collectors.toList());
				}
				return list;
			});
		
		when(sysTenantMemberMapper.selectByPrimaryKey(any(Long.class))).thenAnswer(invocation -> new SysTenantMember());
		
		when(sysTenantMemberMapper.deleteByPrimaryKey(any(Long.class))).thenAnswer(invocation -> 1);
		
		when(sysTenantMemberMapper.deleteBySelective(any(SysTenantMember.class))).thenAnswer(invocation ->1);
		
		when(sysTenantMemberMapper.insert(any(SysTenantMember.class))).thenAnswer(invocation -> 1);
		
		when(sysTenantMemberMapper.insertSelective(any(SysTenantMember.class))).thenAnswer(invocation -> 1);
		
		when(sysTenantMemberMapper.countBySelective(any(SysTenantMember.class))).thenAnswer(invocation -> 10);
		
		when(sysTenantMemberMapper.updateByPrimaryKeySelective(any(SysTenantMember.class))).thenAnswer(invocation -> 1);
		
		when(sysTenantMemberMapper.updateByPrimaryKey(any(SysTenantMember.class))).thenAnswer(invocation -> 1);
		
		when(sysTenantMemberMapper.selectMemberById(any(Long.class))).thenAnswer(invocation -> new TenantUserBO());
		
		when(sysTenantMemberMapper.deleteByTenantId(any(Long.class))).thenAnswer(invocation -> 1);
	}

	@BeforeTest
	public void beforeTest() {
	}

	@Test
	public void countBySelective() {
		Assert.assertTrue(sysTenantMemberService.countBySelective(new SysTenantMember()) > 0);
	}

	@Test
	public void deleteByPrimaryKey() {
		Assert.assertEquals(sysTenantMemberService.deleteByPrimaryKey(1l), 1);
	}

	@Test
	public void deleteBySelective() {
		Assert.assertEquals(sysTenantMemberService.deleteBySelective(new SysTenantMember()), 1);
	}

	@Test
	public void getMemberById() throws ApplicationException {
		Assert.assertNotNull(sysTenantMemberService.getMemberById(1l));
	}

	@Test
	public void getMembersByTenant() throws ApplicationException {
		Assert.assertNotNull(sysTenantMemberService.getMembersByTenant(1l, "aaa"));
	}

	@Test
	public void getMembersByTenant1() throws ApplicationException {
		Assert.assertNotNull(sysTenantMemberService.getMembersByTenant1(new SysTenantMemberReqBO(), new PageInfo()).getList());
	}

	@Test
	public void getNoMemberUsers() throws ApplicationException {
		Assert.assertNotNull(sysTenantMemberService.getNoMemberUsers(new SysTenantMemberReqEx()));
	}

	@Test
	public void insert() {
		Assert.assertEquals(sysTenantMemberService.insert(new SysTenantMember()), 1);
	}

	@Test
	public void insertSelective() {
		Assert.assertEquals(sysTenantMemberService.insertSelective(new SysTenantMember()), 1);
	}

	@Test
	public void isTenantAdmin() {
		Assert.assertTrue(sysTenantMemberService.isTenantAdmin(1l, 1l));
		Assert.assertFalse(sysTenantMemberService.isTenantAdmin(1l, 2l));
	}

	@Test
	public void memberCount() {
		Assert.assertTrue(sysTenantMemberService.memberCount(1l)>0);
	}

	@Test
	public void selectByPrimaryKey() {
		Assert.assertNotNull(sysTenantMemberService.selectByPrimaryKey(1l));
	}

	@Test
	public void selectBySelective() {
		Assert.assertNotNull(sysTenantMemberService.selectBySelective(new SysTenantMember()));
	}

	@Test
	public void updateByPrimaryKey() {
		Assert.assertEquals(sysTenantMemberService.updateByPrimaryKey(new SysTenantMember()), 1);
	}

	@Test
	public void updateByPrimaryKeySelective() {
		Assert.assertEquals(sysTenantMemberService.updateByPrimaryKeySelective(new SysTenantMember()), 1);
	}
}
