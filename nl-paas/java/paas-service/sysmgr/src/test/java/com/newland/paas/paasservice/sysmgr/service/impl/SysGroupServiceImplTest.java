package com.newland.paas.paasservice.sysmgr.service.impl;

import com.newland.paas.advice.request.RequestContext;
import com.newland.paas.common.util.StringUtils;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysCategoryMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysGroupMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysGroupUserMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysUserGroupRoleMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysCategory;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroup;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroupUser;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysUserGroupRole;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.*;
import com.newland.paas.paasservice.sysmgr.service.SysGroupRoleService;
import com.newland.paas.paasservice.sysmgr.service.SysGroupUserService;
import com.newland.paas.paasservice.sysmgr.vo.SysGroupReqVO;
import com.newland.paas.paasservice.sysmgr.vo.SysGroupRoleReqVO;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;
import mockit.MockUp;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.beans.BeanUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;


public class SysGroupServiceImplTest {
	@InjectMocks
	private SysGroupServiceImpl sysGroupService = new SysGroupServiceImpl();
	
	@Mock
	private SysGroupMapper sysGroupMapper;
	@Mock
	private SysCategoryMapper sysCategoryMapper;
	@Mock
	private SysGroupUserMapper sysGroupUserMapper;
	@Mock
	private SysUserGroupRoleMapper sysUserGroupRoleMapper;
	@Mock
    private SysGroupUserService sysGroupUserService;
    @Mock
    private SysGroupRoleService sysGroupRoleService;
	
	private List<SysGroup> groups = new ArrayList<>();
	private List<SysCategory> categorys = new ArrayList<>();
	private List<SysGroupForMemberRespBO> sgBOs = new ArrayList<>();
	
	@BeforeClass
	public void before() throws ApplicationException{
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
		
		when(sysGroupMapper.selectBySelective(any(SysGroupReqBo.class))).thenAnswer((InvocationOnMock invocation)->{
			SysGroupReqBo params = invocation.getArgumentAt(0, SysGroupReqBo.class);
			List<SysGroup> results = groups;
			if(StringUtils.isNotBlank(params.getGroupNameLike())){
				results = groups.stream().filter(group -> group.getGroupName().indexOf(params.getGroupNameLike())!=-1).collect(Collectors.toList());
			}
			if(StringUtils.isNotBlank(params.getGroupName())){
				results = groups.stream().filter(group -> group.getGroupName().equals(params.getGroupName())).collect(Collectors.toList());
			}
			Stream<SysGroupRespBo> boStream = results.stream().map(group-> {
				SysGroupRespBo bo = new SysGroupRespBo();
				BeanUtils.copyProperties(group, bo);
				return bo;
			});
			return boStream.collect(Collectors.toList());
		});
		
		when(sysGroupMapper.countBySelective(any(SysGroupReqBo.class))).thenAnswer((InvocationOnMock invocation)->{
			SysGroupReqBo params = invocation.getArgumentAt(0, SysGroupReqBo.class);
			return groups.stream().filter(group -> {
				if(params == null){
					return true;
				}
				if(params.getParentGroupId() != null){
					return group.getParentGroupId() == params.getParentGroupId();
				}
				if(params.getNeqId() != null){
					return group.getGroupName().equals(params.getGroupName()) && group.getGroupId() != params.getNeqId();
				}else{
					return group.getGroupName().equals(params.getGroupName());
				}
			}).count();
		});
		
		when(sysGroupMapper.selectByPrimaryKey(any(Long.class))).thenAnswer((InvocationOnMock invocation) ->{
			Long id = invocation.getArgumentAt(0, Long.class);
			Optional<SysGroup> resultGroup = groups.stream().filter(group -> group.getGroupId() == id).findFirst();
			if(resultGroup.isPresent()){
				SysGroupRespBo bo = new SysGroupRespBo();
				BeanUtils.copyProperties(resultGroup.get(), bo);
				return bo;
			}else{
				return null;
			}
		});
		
		when(sysGroupMapper.insertSelective(any(SysGroup.class))).thenAnswer((InvocationOnMock invocation)-> 1);
		
		when(sysGroupMapper.updateByPrimaryKeySelective(any(SysGroup.class))).thenAnswer(invocation -> 1);
		
		when(sysCategoryMapper.countBySelective(any(SysCategory.class))).thenAnswer(invocation ->{
			SysCategory params = invocation.getArgumentAt(0, SysCategory.class);
			return categorys.stream().filter(category -> category.getGroupId() == params.getGroupId()).count();
		});
		
		when(sysGroupMapper.getCategorysByGroup(any(Long.class))).thenAnswer(invocation ->{
			Long groupId = invocation.getArgumentAt(0, Long.class);
			return categorys.stream().filter(category->category.getGroupId() == groupId).map(category->{
				SysGroupRespBo bo = new SysGroupRespBo();
				BeanUtils.copyProperties(category, bo);
				return bo;
			}).collect(Collectors.toList());
		});
		
		when(sysGroupMapper.getSubGroupById(any(Long.class))).thenAnswer(invocation ->{
			Long groupId = invocation.getArgumentAt(0, Long.class);
			return groups.stream().filter(group->group.getParentGroupId()== groupId).map(group->{
				SysGroupRespBo bo = new SysGroupRespBo();
				BeanUtils.copyProperties(group, bo);
				return bo;
			}).collect(Collectors.toList());
		});
		
		when(sysGroupMapper.deleteByUserId(any(Long.class))).thenAnswer(invocation -> 1);
		
		when(sysGroupUserMapper.insertSelective(any(SysGroupUser.class))).thenAnswer(invocation -> 1);
		
		when(sysGroupMapper.deleteByUserId(any(Long.class))).thenAnswer(invocation ->{
			return null;
		});
		
		when(sysUserGroupRoleMapper.deleteByParams(any(Long.class), any(Long.class))).thenAnswer(invocation->1);
		
		when(sysUserGroupRoleMapper.insertSelective(any(SysUserGroupRole.class))).thenAnswer(invocation-> 1);
		
		when(sysGroupMapper.listSubSysGroup(any(SysGroup.class))).thenAnswer(invocation -> {
			List<BaseTreeDo> list = new ArrayList<BaseTreeDo>();
			BaseTreeDo bdo = new BaseTreeDo();
			bdo.setId("1");
			bdo.setName("name");
			bdo.setParentId("2");
			list.add(bdo);
			return list;
		});
		
		when(sysCategoryMapper.selectByPrimaryKey(any(Long.class))).thenAnswer(invocation ->{
			SysCategory info = new SysCategory();
			info.setGroupId(1l);
			return info;
		});
		
		when(sysGroupUserMapper.selectBySelective(any(SysGroupUserReqBo.class))).thenAnswer(invocation -> {
			List<SysGroupUserRespBo> list = new ArrayList<>();
			for(long i = 1;i< 10;i++){
				SysGroupUserRespBo info = new SysGroupUserRespBo();
				info.setId(i);
				info.setGroupId(i);
				list.add(info);
			}
			return list;
		});
		
		when(sysGroupMapper.getSysGroupTree(any(SysGroup.class))).thenAnswer(invocation -> new ArrayList<BaseTreeDo>());
		
		when(sysGroupMapper.selectAll(any(Long.class))).thenAnswer(invocation -> sgBOs);
		when(sysGroupMapper.selectGroupId(any(Long.class),any(Long.class))).thenAnswer(invocation -> Arrays.asList(1l,2l,3l));
		
		when(sysGroupMapper.selectByUserId(any(Long.class))).thenAnswer(invocation -> sgBOs);
		
		when(sysGroupMapper.selectOtherByUserId(any(Long.class),any(String.class))).thenAnswer(invocation ->sgBOs);
		when(sysGroupMapper.selectByUserId(any(Long.class))).thenAnswer(invocation -> sgBOs);
		when(sysGroupMapper.selectRoleByParams(any(Long.class),any(Long.class))).thenAnswer(invocation ->new ArrayList<SysGroupRoleBO>());
		
//		@Mock
//	    private SysGroupUserService sysGroupUserService;
//	    @Mock
//	    private SysGroupRoleService sysGroupRoleService;
		when(sysGroupUserService.list(any(SysGroupUserReqBo.class))).thenAnswer(invocation -> new ArrayList<SysGroupUserRespBo>());
		when(sysGroupRoleService.list(any(SysGroupRoleReqBo.class))).thenAnswer(invocation -> new ArrayList<SysGroupRoleRespBo>());
	}
	
	@BeforeTest
	public void beforeTest(){
		SysGroup initGroup = new SysGroup();
		initGroup.setGroupId(1l);
		initGroup.setGroupName("ROOT");
		initGroup.setDescription(initGroup.getGroupName());
		initGroup.setTenantId(1l);
		groups = Stream.iterate(initGroup, tmp -> {
			long groupId = tmp.getGroupId()+1;
			SysGroup group = new SysGroup();
			group.setGroupId(groupId);
			group.setGroupName("groupName"+groupId);
			group.setDescription(group.getGroupName());
			group.setParentGroupId(1l);
			group.setTenantId(1l);
			group.setWholePath("/01/");
			return group;
		}).limit(15).collect(Collectors.toList());
		
		SysCategory initCategory = new SysCategory();
		initCategory.setSysCategoryId(1l);
		initCategory.setGroupId(10l);
		categorys = Stream.iterate(initCategory, tmp -> {
			long categoryId = tmp.getSysCategoryId()+1;
			SysCategory category = new SysCategory();
			category.setSysCategoryId(categoryId);
			category.setGroupId(10l);
			return category;
		}).limit(15).collect(Collectors.toList());
		
		
		for(long i =0;i < 10;i++){
			SysGroupForMemberRespBO bo = new SysGroupForMemberRespBO();
			sgBOs.add(bo);
		}
	}
	
	@Test
	public void page() throws ApplicationException{
		PageInfo pageInfo = new PageInfo();
		SysGroupReqBo params = new SysGroupReqBo();
		params.setGroupNameLike("groupName");
		ResultPageData<SysGroup> results = sysGroupService.page(params, pageInfo);
		Assert.assertTrue(results.getList().size()>0);
	}
	
	@Test
	public void add() throws ApplicationException{
		SysGroup g1 = new SysGroup();
		g1.setGroupName("groupName1");
		try{
			sysGroupService.add(g1);
		}catch(Exception ex){
			Assert.assertTrue(ex instanceof ApplicationException);
		}
		
		SysGroup g2 = new SysGroup();
		g2.setGroupName("name1");
		g2.setParentGroupId(100l);
		try{
			sysGroupService.add(g2);
		}catch(Exception ex){
			Assert.assertTrue(ex instanceof ApplicationException);
		}
		
		SysGroup g3 = new SysGroup();
		g3.setGroupName("name2");
		g3.setParentGroupId(11l);
		sysGroupService.add(g3);
		
		SysGroup g4 = new SysGroup();
		g4.setGroupName("name2");
		g4.setParentGroupId(-1l);
		sysGroupService.add(g4);
	}
	
	@Test
	public void update() throws ApplicationException{
		
		SysGroupReqBo params = new SysGroupReqBo();
		params.setNeqId(1l);
		params.setGroupName("groupName2");
		try{
			sysGroupService.update(params);
		}catch(Exception ex){
			Assert.assertTrue(ex instanceof ApplicationException);
		}
		params.setGroupName("grouname");
		params.setParentGroupId(-1l);
		sysGroupService.update(params);
		params.setParentGroupId(10l);
		sysGroupService.update(params);
		
	}
	
	@Test
	public void delete() throws ApplicationException{
		try{
			sysGroupService.delete(1l);
		}catch(Exception ex){
			Assert.assertTrue(ex instanceof ApplicationException);
		}
		
		try{
			sysGroupService.delete(10l);
		}catch(Exception ex){
			Assert.assertTrue(ex instanceof ApplicationException);
		}

		sysGroupService.delete(11l);
	}
	
	@Test
	public void get()throws ApplicationException{
		SysGroup bo = sysGroupService.get(11l);
		Assert.assertEquals(bo.getGroupId().longValue(), 11l);
	}
	
	@Test
	public void getCategorysByGroup()throws ApplicationException{
		List<SysGroupForCategoryRespBo> bos = sysGroupService.getCategorysByGroup(10l);
		Assert.assertTrue(bos.size() > 0);
	}
	
	@Test
	public void getSubGroupById()throws ApplicationException{
		List<SysGroup> results = sysGroupService.getSubGroupById(Arrays.asList(1l,11l));
		Assert.assertTrue(results.size()>0);
	}

	@Test
	public void pagedQueryListByUserId()throws ApplicationException{
		Assert.assertNotNull(sysGroupService.pagedQueryListByUserId(new SysGroupPagingReqBO(), new PageInfo()).getList());
	}
	
	@Test
	public void queryListByUserId()throws ApplicationException{
		Assert.assertNotNull(sysGroupService.queryListByUserId(1l));
	}
	
	@Test
	public void deleteGroupsByUserId()throws ApplicationException{
		sysGroupService.deleteGroupsByUserId(1l);
	}
	
	@Test
	public void addGroups()throws ApplicationException{
		SysGroupReqVO sysGroupReqVO = new SysGroupReqVO();
		sysGroupReqVO.setGroupIds(new Long[]{1l,2l,3l});
		sysGroupReqVO.setUserId(1l);
		sysGroupService.addGroups(sysGroupReqVO);
	}
	
	@Test
	public void queryAllList()throws ApplicationException{
		Assert.assertNotNull(sysGroupService.queryAllList(1l,1l));
	}
	
	@Test
	public void listSubSysGroup()throws ApplicationException{
		SysGroup group = new SysGroup();
		Assert.assertEquals(sysGroupService.listSubSysGroup(group).size(), 1);
	}

	@Test
	public void yyTenantRootGroup(){
		Assert.assertNotNull(sysGroupService.yyTenantRootGroup());
	}
	
	@Test
	public void tenantParentGroup(){
		Assert.assertNotNull(sysGroupService.tenantParentGroup());
	}
	
	@Test
	public void tenantRootGroup(){
		Assert.assertNotNull(sysGroupService.tenantRootGroup());
	}
	
	@Test
	public void getSysGroupTree() throws ApplicationException{
		SysGroup params = new SysGroup();
		params.setGroupId(-1l);
		Assert.assertNotNull(sysGroupService.getSysGroupTree(params));
	}
	
	@Test
	public void groupCount() throws ApplicationException{
		Assert.assertTrue(sysGroupService.groupCount()>0);
	}
	@Test
	public void list() throws ApplicationException{
		Assert.assertNotNull(sysGroupService.list(new SysGroupReqBo()));
	}
	
	


}
