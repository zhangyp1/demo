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

import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.GlbLabelMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.GlbLabel;

public class GlbLabelServiceImplTest {
	@InjectMocks
	private GlbLabelServiceImpl glbLabelService = new GlbLabelServiceImpl();
	
	@Mock
	private GlbLabelMapper glbLabelMapper;

	@BeforeClass
	public void beforeClass() {
		MockitoAnnotations.initMocks(this);
	}

	@BeforeTest
	public void beforeTest() {
	}

	@Test
	public void findAll() {
	}

	@Test
	public void getByLabelObj() {
	}
}
