package com.newland.paas.paasservice.sysmgr.service.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import mockit.MockUp;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.newland.paas.advice.request.RequestContext;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysGroupMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysGroupUserMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysUserMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroup;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroupUser;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysUser;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupRespBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupUserReqBo;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo.SysGroupUserRespBo;
import com.newland.paas.sbcommon.common.ApplicationException;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;

public class SysGroupUserServiceImplTest {
	
	@InjectMocks
	private SysGroupUserServiceImpl sysGroupUserService;
	@Mock
	private SysGroupUserMapper sysGroupUserMapper;
	@Mock
	private SysUserMapper sysUserMapper;
	@Mock
	private SysGroupMapper sysGroupMapper;
	
	
	private List<SysGroupUser> groupUsers = new ArrayList<>();
	
	@BeforeClass
	public void beforeClass(){
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
        
        when(sysGroupUserMapper.insertSelective(any(SysGroupUser.class))).thenAnswer(invocation->1);
        
        when(sysGroupUserMapper.updateByPrimaryKeySelective(any(SysGroupUser.class))).thenAnswer(invocation->1);
        
        when(sysGroupUserMapper.selectByPrimaryKey(any(Long.class))).thenAnswer(invocation -> {
			SysGroupUserRespBo sysGroupUserRespBo = new SysGroupUserRespBo();
			sysGroupUserRespBo.setGroupId(123L);
			return sysGroupUserRespBo;

		});
       
        when(sysGroupUserMapper.deleteBySelective(any(SysGroupUserReqBo.class))).thenAnswer(invocation -> 1);
        
        when(sysGroupUserMapper.selectBySelective(any(SysGroupUserReqBo.class))).thenAnswer(invocation->{
        	return groupUsers.stream().filter(groupUser->groupUser.getGroupId() == 1l).map(groupUser->{
        		SysGroupUserRespBo bo = new SysGroupUserRespBo();
        		BeanUtils.copyProperties(groupUser, bo);
        		return bo;
        	}).collect(Collectors.toList());
        });
        
        when(sysUserMapper.selectByPrimaryKey(any(Long.class))).thenAnswer(invocation -> {
        	Long userId = invocation.getArgumentAt(0, Long.class);
        	if(userId.longValue() == -1l){
        		return null;
        	}else{
        		SysUser user = new SysUser();
        		user.setUserId(userId);
        		return user;
        	}
        });
        
        when(sysGroupMapper.selectByPrimaryKey(any(Long.class))).thenAnswer(invocation -> {
        	Long groupId = invocation.getArgumentAt(0, Long.class);
        	if(groupId.longValue() == -1){
        		return null;
        	}else{
        		SysGroupRespBo group = new SysGroupRespBo();
        		group.setGroupId(groupId);
        		return group;
        	}
        });
        
        when(sysGroupUserMapper.getUsersByGroup(any(Long.class))).thenAnswer(invocation->{
        	List<SysGroupUserRespBo> list = new ArrayList<>();
        	return list;
        });
        
        when(sysGroupUserMapper.getGroupsByUser(any(Long.class))).thenAnswer(invocation->{
        	List<SysGroupUserRespBo> list = new ArrayList<>();
        	return list;
        });
        
        when(sysGroupUserMapper.getAllUsersByTenant(any(Long.class), any(Long.class),any(String.class))).thenAnswer(invocation->{
        	List<SysGroupUserRespBo> list = new ArrayList<>();
        	return list;
        });
        
        when(sysGroupUserMapper.getAllGroupsByTenant(any(Long.class), any(Long.class))).thenAnswer(invocation->{
        	List<SysGroupUserRespBo> list = new ArrayList<>();
        	return list;
        });
        
        when(sysGroupUserMapper.deleteByPrimaryKey(any(Long.class))).thenAnswer(invocation -> 1);
        
        
	}
	
	@BeforeTest
	public void beforeTest(){
		
		for(long i = 0;i < 20;i++){
			SysGroupUser groupUser = new SysGroupUser();
			groupUser.setGroupId(i%10);
			groupUser.setUserId(i);
			if(i%5==0){
				groupUser.setIsAdmin((short)1);
			}else{
				groupUser.setIsAdmin((short)0);
			}
			groupUser.setTenantId(1l);
			groupUser.setCreatorId(1l);
			groupUsers.add(groupUser);
		}
	}

	@Test
	public void add() throws ApplicationException {
		SysGroupUser info = new SysGroupUser();
		Assert.assertEquals(sysGroupUserService.add(info), 1);
	}

	@Test
	public void addGroups() throws ApplicationException {
		SysGroupUserReqBo req = new SysGroupUserReqBo();
		try{
			sysGroupUserService.addGroups(req);
		}catch(Exception ex){
			Assert.assertTrue(ex instanceof ApplicationException);
		}
		req.setUserId(1l);
		req.setGroupIds(new Long[]{-1l,1l,2l});
		try{
			sysGroupUserService.addGroups(req);
		}catch(Exception ex){
			Assert.assertTrue(ex instanceof ApplicationException);
		}
		req.setGroupIds(new Long[]{1l,2l,3l});
		Assert.assertTrue(sysGroupUserService.addGroups(req) > 0);

	}

	@Test
	public void addUsers() throws ApplicationException {
		/*SysGroupUserReqBo req = new SysGroupUserReqBo();
		try{
			sysGroupUserService.addUsers(req);
		}catch(Exception ex){
			Assert.assertTrue(ex instanceof ApplicationException);
		}
		req.setGroupId(1l);
		req.setUserIds(new Long[]{-1l,2l,3l});
		try{
			sysGroupUserService.addUsers(req);
		}catch(Exception ex){
			Assert.assertTrue(ex instanceof ApplicationException);
		}
		req.setUserIds(new Long[]{1l,2l,3l});
		Assert.assertTrue(sysGroupUserService.addUsers(req) > 0);*/
		
	}

	@Test
	public void delete() throws ApplicationException {
		Assert.assertEquals(sysGroupUserService.delete(1l), 1);
	}

	@Test
	public void get() throws ApplicationException {
		sysGroupUserService.get(1l);
	}

	@Test
	public void getAllGroupsByTenant() {
		SysGroupUserReqBo req = new SysGroupUserReqBo();
		Assert.assertNotNull(sysGroupUserService.getAllGroupsByTenant(req));
	}

	@Test
	public void getAllUsersByTenant() {
		SysGroupUserReqBo req = new SysGroupUserReqBo();
		req.setTenantId(1l);
		req.setGroupId(1l);
		req.setUserNameLike("%");
		Assert.assertNotNull(sysGroupUserService.getAllUsersByTenant(req));
	}

	@Test
	public void getGroupsByTenantAndUser() {
		Assert.assertTrue(sysGroupUserService.getGroupsByTenantAndUser(1l, 1l).size()>0);
	}

	@Test
	public void getGroupsByUser() {
		Assert.assertNotNull(sysGroupUserService.getGroupsByUser(1l));
	}

	@Test
	public void getUsersByGroup() {
		PageInfo pageInfo = new PageInfo();
		ResultPageData<List<SysGroupUserRespBo>> res = sysGroupUserService.pageUsersByGroupId(1l, pageInfo);
		Assert.assertNotNull(res.getList());
	}

	@Test
	public void list() throws ApplicationException {
		SysGroupUserReqBo req = new SysGroupUserReqBo();
		Assert.assertNotNull(sysGroupUserService.list(req));
	}

	@Test
	public void setAdmin() throws ApplicationException {
		SysGroupUserReqBo req = new SysGroupUserReqBo();
		Assert.assertEquals(sysGroupUserService.setAdmin(req), 1);
	}

	@Test
	public void update() throws ApplicationException {
		SysGroupUser info = new SysGroupUser();
		Assert.assertEquals(sysGroupUserService.update(info), 1);
	}
}
