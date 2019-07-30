package com.newland.paas.paasservice.sysmgr.service.impl;

import com.newland.paas.advice.request.RequestContext;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysGroupMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysGroupUserMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysRoleUserMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysTenantLimitMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysTenantMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysTenantMemberMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroup;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroupUser;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysTenant;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysTenantLimit;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysTenantMember;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupReqBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysRoleUserReqBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysTenantBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysTenantLimitBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysTenantLimitInfoVo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysTenantReqBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.TenantUserBO;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.SysTenantFlowReqBo;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.SysTenantLimitVo;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.SysTenantReqVo;
import com.newland.paas.paasservice.controllerapi.sysmgr.vo.SysTenantRespVo;
import com.newland.paas.paasservice.sysmgr.async.DepotHelperAsync;
import com.newland.paas.paasservice.sysmgr.service.ActivitiOperationsService;
import com.newland.paas.paasservice.sysmgr.service.SysGroupService;
import com.newland.paas.paasservice.sysmgr.service.SysRoleUserService;
import com.newland.paas.paasservice.sysmgr.service.SysTenantMemberService;
import com.newland.paas.paasservice.sysmgr.vo.ActivitiRunProcessInput;
import com.newland.paas.paasservice.sysmgr.vo.ActivitiTaskStartOutput;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.PageInfo;
import mockit.MockUp;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

public class SysTenantServiceImplTest {
	@InjectMocks
	private SysTenantServiceImpl sysTenantService = new SysTenantServiceImpl();

	@Mock
	private SysTenantMapper sysTenantMapper;
	@Mock
	private SysGroupMapper sysGroupMapper;
	@Mock
	private SysGroupUserMapper sysGroupUserMapper;
	@Mock
	private SysRoleUserService sysRoleUserService;
	@Mock
	private SysTenantMemberService sysTenantMemberService;
	@Mock
	private SysTenantLimitMapper sysTenantLimitMapper;
	@Mock
	private DepotHelperAsync depotHelperAsync;
	@Mock
	private ActivitiOperationsService activitiOperationsService;
	@Mock
    private SysRoleUserService sysRoleUserSerive;
    @Mock
    private SysGroupService sysGroupService;
	@Mock
	private SysRoleUserMapper sysRoleUserMapper;
	@Mock
	private SysTenantMemberMapper sysTenantMemberMapper;

	@BeforeClass
	public void beforeClass() throws ApplicationException {
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

			@mockit.Mock
			public String getToken() {
				return "111";
			}
		};

		when(sysTenantMapper.insert(any(SysTenant.class))).thenAnswer(
				invocation -> 1);
		when(sysTenantMapper.countBySelective(any(SysTenant.class)))
				.thenAnswer(invocation -> 10);
		when(sysTenantMapper.deleteByPrimaryKey(any(Long.class))).thenAnswer(
				invocation -> 1);
		when(sysTenantMapper.deleteBySelective(any(SysTenant.class)))
				.thenAnswer(invocation -> 1);
		when(
				sysTenantMapper.getSysTenantUsers(anyLong(), anyShort(),
						anyString())).thenAnswer(
				new Answer<List<TenantUserBO>>() {

					@Override
					public List<TenantUserBO> answer(InvocationOnMock invocation)
							throws Throwable {
						List<TenantUserBO> tenantUserVos = new ArrayList<>();
						for (int i = 0; i < 10; i++) {
							TenantUserBO bo = new TenantUserBO();
							bo.setAccount("account" + i);
							bo.setDept("dept" + i);
							bo.setDescription("description" + i);
							bo.setPhone("phone");
							bo.setUserId((long) i);
							bo.setUserName("name" + i);
							tenantUserVos.add(bo);
						}
						return tenantUserVos;
					}
				});
		when(
				sysTenantMapper.selectTenantBo(any(String.class),
						any(String.class), any(String.class),
						any(Boolean.class), any(Boolean.class))).thenAnswer(
				new Answer<List<SysTenantBo>>() {

					@Override
					public List<SysTenantBo> answer(InvocationOnMock invocation)
							throws Throwable {
						List<SysTenantBo> resultList = new ArrayList<>();
						resultList.add(new SysTenantBo());
						return resultList;
					}
				});
		when(sysTenantMapper.getSysTenantsByIds(any(String[].class)))
				.thenAnswer(new Answer<List<SysTenant>>() {

					@Override
					public List<SysTenant> answer(InvocationOnMock invocation)
							throws Throwable {
						List<SysTenant> list = new ArrayList<>();
						for (int i = 0; i < 5; i++) {
							SysTenant info = new SysTenant();
							info.setAstAddress("astAddress" + i);
							info.setChangeDate(new Date());
							info.setCreateDate(new Date());
							info.setCreatorId(1l);
							info.setDelFlag((short) 0);
							// info.setHarborAddress("harborAddress"+i);
							info.setId((long) i);
							list.add(info);
						}
						return list;
					}
				});

		when(sysTenantMapper.selectBySelective(any(SysTenant.class)))
				.thenAnswer(new Answer<List<SysTenant>>() {

					@Override
					public List<SysTenant> answer(InvocationOnMock invocation)
							throws Throwable {
						List<SysTenant> list = new ArrayList<>();
						for (int i = 0; i < 10; i++) {
							SysTenant info = new SysTenant();
							list.add(info);
						}

						return list;
					}
				});

		when(sysTenantMapper.countBySelective(any(SysTenant.class)))
				.thenAnswer(new Answer<Integer>() {

					@Override
					public Integer answer(InvocationOnMock invocation)
							throws Throwable {
						return 10;
					}
				});

		when(sysTenantMapper.updateByPrimaryKey(any(SysTenant.class)))
				.thenAnswer(invocation -> 1);
		when(sysTenantMapper.updateByPrimaryKeySelective(any(SysTenant.class)))
				.thenAnswer(invocation -> 1);
		when(sysTenantMapper.countByTenantName(anyString(), anyLong()))
				.thenAnswer(new Answer<Integer>() {

					@Override
					public Integer answer(InvocationOnMock invocation)
							throws Throwable {
						String tenantName = (String) invocation.getArguments()[0];
						if (tenantName.equals("租户1")) {
							return 1;
						} else {
							return 0;
						}
					}
				});

		when(sysTenantMapper.insert(any(SysTenant.class))).thenAnswer(
				new Answer<Integer>() {

					@Override
					public Integer answer(InvocationOnMock invocation)
							throws Throwable {
						return 1;
					}
				});

		when(sysTenantMapper.insertSelective(any(SysTenant.class))).thenAnswer(
				new Answer<Integer>() {

					@Override
					public Integer answer(InvocationOnMock invocation)
							throws Throwable {
						return 1;
					}
				});

		when(sysTenantMapper.selectByPrimaryKey(any(Long.class))).thenAnswer(
				new Answer<SysTenant>() {

					@Override
					public SysTenant answer(InvocationOnMock invocation)
							throws Throwable {
						SysTenant info = new SysTenant();
						info.setId(1l);
						info.setTenantName("name1");
						return info;
					}
				});

		when(
				sysTenantMemberService
						.deleteBySelective(any(SysTenantMember.class)))
				.thenReturn(5);

		when(sysTenantMemberService.insertSelective(any(SysTenantMember.class)))
				.thenReturn(1);

		when(sysGroupMapper.insertSelective(any(SysGroup.class))).thenAnswer(
				invocation -> 1);
		when(sysGroupUserMapper.insert(any(SysGroupUser.class))).thenAnswer(
				invocation -> 1);
		when(sysTenantMemberService.addMembers(any(SysRoleUserReqBo.class)))
				.thenAnswer(invocation -> 1);
		when(sysTenantLimitMapper.batchInsert(any(List.class), any(Long.class)))
				.thenAnswer(invocation -> 5);
		when(sysTenantLimitMapper.batchUpdate(any(List.class), any(Long.class)))
				.thenAnswer(invocation -> 5);

		when(
				sysTenantMemberService
						.countBySelective(any(SysTenantMember.class)))
				.thenAnswer(
						invocation -> {
							SysTenantMember params = invocation.getArgumentAt(
									0, SysTenantMember.class);
							if (1 == params.getTenantId()
									&& 1 == params.getUserId()
									&& "1".equals(params.getStatus())) {
								return 1;
							} else if (0 == params.getTenantId()
									&& 0 == params.getUserId()
									&& "0".equals(params.getStatus())) {
								return 1;
							} else {
								return 0;
							}
						});

		when(
				activitiOperationsService
						.runProcess(any(ActivitiRunProcessInput.class)))
				.thenAnswer(invocation -> new ActivitiTaskStartOutput());
		
		when(sysTenantMapper.getAllTenantsCanJoin(any(Long.class))).thenAnswer(invocation -> new ArrayList<SysTenant>() );
		
		when(sysTenantLimitMapper.getLimitsByTenantId(any(Long.class))).thenAnswer(invocation -> new SysTenantLimitInfoVo());
		
		when(sysTenantLimitMapper.listLimitsByTenantId(any(Long.class))).thenAnswer(invocation -> {
			List<SysTenantLimitBo> list = new ArrayList<>();
			for(long i = 0;i < 5;i++){
				SysTenantLimitBo info = new SysTenantLimitBo();
				info.setId(i);
				info.setQuotaItem("aa"+i);
				list.add(info);
			}
			return list;
		});
		
		when(sysRoleUserSerive.list(any(SysRoleUserReqBo.class))).thenAnswer(invocation -> new ArrayList<SysRoleUserReqBo>());
		when(sysGroupService.list(any(SysGroupReqBo.class))).thenAnswer(invocation -> new ArrayList<SysGroupReqBo>());

		when(sysRoleUserMapper.deleteBySelective(any(SysRoleUserReqBo.class))).thenAnswer(
				invocation -> 1);
		when(sysRoleUserMapper.deleteBySelective(any(SysRoleUserReqBo.class))).thenAnswer(
				invocation -> 1);
		when(sysRoleUserMapper.deleteBySelective(any(SysRoleUserReqBo.class))).thenAnswer(
				invocation -> 1);
		when(sysRoleUserMapper.deleteBySelective(any(SysRoleUserReqBo.class))).thenAnswer(
				invocation -> 1);
	}

	@BeforeTest
	public void beforeTest() {

	}

	@Test
	public void addSysTenant() {

		int count = sysTenantService.insert(new SysTenant());
		Assert.assertEquals(count, 1);
	}

	@Test
	public void addSysTenantAndAdmins() throws ApplicationException {

		new MockUp<SysTenantServiceImpl>() {
			@mockit.Mock
			public SysTenant addSysTenant(SysTenant sysTenant)
					throws ApplicationException {
				sysTenant.setId(1l);
				return sysTenant;
			}
		};

		SysTenantReqBo params = new SysTenantReqBo();
		params.setTenantName("租户1");
		try {
			sysTenantService.addSysTenantAndMembers(params);
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof ApplicationException);
		}
		params.setTenantName("租户2");
		List<SysTenantLimit> limits = new ArrayList<>();
		for (long i = 1; i < 10; i++) {
			SysTenantLimit info = new SysTenantLimit();
			info.setQuotaValue(i);
			limits.add(info);
		}
		params.setTenantLimits(limits);
		Assert.assertNotNull(sysTenantService.addSysTenantAndMembers(params));

	}

	@Test
	public void updateSysTenantAndMembers() throws ApplicationException {
		new MockUp<SysTenantServiceImpl>() {
			@mockit.Mock
			public SysTenant updateSysTenant(SysTenant sysTenant)
					throws ApplicationException {
				sysTenant.setId(1l);
				return sysTenant;
			}
		};

		SysTenantReqBo params = new SysTenantReqBo();
		params.setTenantName("租户1");
		try {
			sysTenantService.updateSysTenantAndMembers(params);
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof ApplicationException);
		}
		params.setTenantName("租户2");
		List<SysTenantLimit> limits = new ArrayList<>();
		for (long i = 1; i < 10; i++) {
			SysTenantLimit info = new SysTenantLimit();
			info.setQuotaValue(i);
			limits.add(info);
		}
		params.setTenantLimits(limits);
		Assert.assertNotNull(sysTenantService.updateSysTenantAndMembers(params));
	}

	@Test
	public void countBySelective() {

		int count = sysTenantService.countBySelective(new SysTenant());
		Assert.assertEquals(count, 10);
	}

	@Test
	public void countByTenantName() throws ApplicationException {

		int count1 = sysTenantService.countByTenantName("租户1", 1l);
		assertEquals(count1, 1);
		int count2 = sysTenantService.countByTenantName("租户2", 1l);
		assertEquals(count2, 0);
	}

	@Test
	public void deleteByPrimaryKey() {

		int count = sysTenantService.deleteByPrimaryKey(1l);
		Assert.assertEquals(count, 1);
	}

	@Test
	public void deleteBySelective() {

		/*int count = sysTenantService.deleteBySelective(new SysTenant());
		Assert.assertEquals(count, 1);*/
	}

	@Test
	public void deleteSysTenant() throws ApplicationException {
		try{
			sysTenantService.deleteSysTenant(1l);
		}catch(Exception ex){
			Assert.assertTrue(ex instanceof ApplicationException);
		}
		Assert.assertEquals(sysTenantService.deleteSysTenant(5l),1);
	}

	@Test
	public void getSysTenantAndAdmins() throws ApplicationException {

		SysTenantRespVo response = sysTenantService.getSysTenantAndAdmins(1l);
		Assert.assertNotNull(response.getTenantId());
		Assert.assertEquals(response.getAdminUsers().size(), 10);

	}

	@Test
	public void getSysTenants() {

		SysTenantReqVo reqVo = new SysTenantReqVo();
		reqVo.setTenantName("租户1");
		List<SysTenantBo> list = sysTenantService.getSysTenantsByPage(reqVo,
				new PageInfo()).getList();

		Assert.assertEquals(list.size(), 1);
	}

	@Test
	public void getSysTenantsByIds() {

		List<SysTenant> list = sysTenantService
				.getSysTenantsByIds(new String[] { "1", "2", "3" });
		assertEquals(list.size(), 5);
	}

	@Test
	public void insert() {

		int count = sysTenantService.insert(new SysTenant());
		Assert.assertEquals(count, 1);
	}

	@Test
	public void insertSelective() {

		int count = sysTenantService.insertSelective(new SysTenant());
		Assert.assertEquals(count, 1);
	}

	@Test
	public void querySysTenants() {

		List<SysTenant> list = sysTenantService.querySysTenants(
				new SysTenant(), new PageInfo()).getList();
		Assert.assertEquals(list.size(), 10);
	}

	@Test
	public void tenantCount() throws ApplicationException {

		int count = sysTenantService.tenantCount();
		Assert.assertEquals(count, 10);
	}

	@Test
	public void selectByPrimaryKey() {

		SysTenant info = sysTenantService.selectByPrimaryKey(11l);
		Assert.assertNotNull(info);
	}

	@Test
	public void selectBySelective() {

		List<SysTenant> list = sysTenantService
				.selectBySelective(new SysTenant());
		Assert.assertEquals(list.size(), 10);
	}

	@Test
	public void updateByPrimaryKey() {

		int count = sysTenantService.updateByPrimaryKey(new SysTenant());
		Assert.assertEquals(count, 1);
	}

	@Test
	public void updateByPrimaryKeySelective() {

		int count = sysTenantService
				.updateByPrimaryKeySelective(new SysTenant());
		Assert.assertEquals(count, 1);
	}

	@Test
	public void updateSysTenant() throws ApplicationException {

		SysTenant info = sysTenantService.updateSysTenant(new SysTenant());
		Assert.assertNotNull(info);
	}

	@Test
	public void joinTenantSubmitProcess() throws ApplicationException {
		SysTenantFlowReqBo params = new SysTenantFlowReqBo();
		params.setUserId(1l);
		params.setTenantId(1l);
		try {
			sysTenantService.joinTenantSubmitProcess(params);
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof ApplicationException);
		}
		params.setUserId(0l);
		params.setTenantId(0l);
		try {
			sysTenantService.joinTenantSubmitProcess(params);
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof ApplicationException);
		}
		params.setTenantId(1l);
//		Assert.assertNotNull(sysTenantService.joinTenantSubmitProcess(params));
	}
	
	@Test
	public void joinTenantNextProcess(){
		Assert.assertTrue(sysTenantService.joinTenantNextProcess(new SysTenantFlowReqBo()));
	} 
	
	@Test
	public void joninTenantApprove() throws ApplicationException{
		Assert.assertTrue(sysTenantService.joninTenantApprove(new SysTenantFlowReqBo()));
	}
	
	@Test
	public void countTenantByDepot() throws ApplicationException{
		Assert.assertTrue(sysTenantService.countTenantByDepot(new SysTenant())>0);
	}
	
	@Test
	public void getLimitsByTenantId(){
		Assert.assertNotNull(sysTenantService.getLimitsByTenantId(1l));
	}
	
	@Test
	public void listSysTenantLimits(){
		Assert.assertNotNull(sysTenantService.listSysTenantLimits(1l));
	}
	
	@Test
	public void addSysTenantLimits(){
		List<SysTenantLimitVo> list = new ArrayList<>();
		for(long i = 0;i < 5;i++){
			SysTenantLimitVo info = new SysTenantLimitVo();
			info.setId(i);
			info.setQuotaItem("aa"+i);
			list.add(info);
		}
		Assert.assertEquals(sysTenantService.addSysTenantLimits(list, 1l), 1);
	}
	
	@Test
	public void getSysTenant() throws ApplicationException{
		Assert.assertNotNull(sysTenantService.getSysTenant(new SysTenant()));
	}
	

}
