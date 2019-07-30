package com.newland.paas.paasservice.sysmgr.service.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.GlbDictMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysCategoryMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysGroupMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysTenantMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.SysUserMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.GlbDict;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysCategory;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysGroup;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysTenant;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysUser;
import com.newland.paas.sbcommon.common.ApplicationException;

public class GlbDictServiceImplTest {
	
	@InjectMocks
	private GlbDictServiceImpl glbDictService = new GlbDictServiceImpl();
	
	@Mock
    private GlbDictMapper glbDictMapper;
	@Mock
    private SysUserMapper sysUserMapper;
	@Mock
    private SysTenantMapper sysTenantMapper;
	@Mock
    private SysGroupMapper sysGroupMapper;
	@Mock
    private SysCategoryMapper sysCategoryMapper;
	
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
		
		when(glbDictMapper.findBaseAll()).thenAnswer(invocation -> {
			List<GlbDict> list = new ArrayList<GlbDict>();
			for(int i = 0;i < 19; i++){
				GlbDict info = new GlbDict();
				info.setDictId((long)i);
				info.setDictLevel((short)i);
				info.setDictCode(i+"");
				info.setDictPcode(""+i%10);
				list.add(info);
			}
			return list;
		});
		
		when(sysUserMapper.findBaseAll()).thenAnswer(invocation -> new ArrayList<SysUser>());
		
		when(sysTenantMapper.findBaseAll()).thenAnswer(invocation -> new ArrayList<SysTenant>());
		
		when(sysGroupMapper.findBaseAll()).thenAnswer(invocation -> new ArrayList<SysGroup>());
		
		when(sysCategoryMapper.findBaseAll()).thenAnswer(invocation -> new ArrayList<SysCategory>());
		
		when(glbDictMapper.selectBySelective(any(GlbDict.class))).thenAnswer(invocation -> {
			List<GlbDict> list = new ArrayList<GlbDict>();
			for(int i = 0;i < 19; i++){
				GlbDict info = new GlbDict();
				info.setDictId((long)i);
				info.setDictLevel((short)i);
				info.setDictCode(i+"");
				info.setDictPcode(""+i%10);
				list.add(info);
			}
			return list;
		});
		
		when(glbDictMapper.countBySelective(any(GlbDict.class))).thenAnswer(invocation -> {
			GlbDict params = invocation.getArgumentAt(0, GlbDict.class);
			if("重复".equals(params.getDictCode())){
				return 1;
			}else{
				return 0;
			}
			
		});
		
		when(glbDictMapper.insert(any(GlbDict.class))).thenAnswer(invocation -> 1);
		
		when(glbDictMapper.updateByDictId(any(GlbDict.class))).thenAnswer(invocation -> 1);
	}

	@BeforeTest
	public void beforeTest() {
	}

	@Test
	public void delByDictCodeAndPcode() throws ApplicationException {
		try{
			glbDictService.delByDictCodeAndPcode("重复", "test2");
		}catch(Exception ex){
			ex.printStackTrace();
			Assert.assertTrue(ex instanceof ApplicationException);
		}
		
		
		Assert.assertTrue(glbDictService.delByDictCodeAndPcode("test", "test2") > 0);
	}

	@Test
	public void findAll() {
		Assert.assertNotNull(glbDictService.findAll());
	}

	@Test
	public void getAll() {
		Assert.assertNotNull(glbDictService.getAll());
	}

	@Test
	public void getAllNode() {
		Assert.assertNotNull(glbDictService.getAllNode());
	}

	@Test
	public void getAllTree() {
		Assert.assertNotNull(glbDictService.getAllTree());
	}

	@Test
	public void getByDictCode() {
		Assert.assertTrue(glbDictService.getByDictCode("test").size() > 0);
	}

	@Test
	public void getByDictPcode() {
		Assert.assertTrue(glbDictService.getByDictPcode("test").size() > 0);
	}

	@Test
	public void getTree() {
		Assert.assertNotNull(glbDictService.getTree("1"));
	}

	@Test
	public void insertDict() {
		Assert.assertEquals(glbDictService.insertDict(new GlbDict()), 1);
	}

	@Test
	public void insertOrUpdate() {
//		throw new RuntimeException("Test not implemented");
	}
}
