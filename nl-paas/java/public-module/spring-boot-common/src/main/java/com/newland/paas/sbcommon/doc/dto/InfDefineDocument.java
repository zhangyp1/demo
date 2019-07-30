package com.newland.paas.sbcommon.doc.dto;

import java.util.ArrayList;
import java.util.List;

import com.newland.paas.paasservice.controllerapi.annotation.Doc;

/**
 * @author laiCheng
 * @date 2018/8/3 17:01
 */
public class InfDefineDocument {

	@Doc("接口名")
	private String name;
	@Doc("接口描述")
	private String desc;
	@Doc("输入参数列表")
	private List<ParamDefine> inputParams = new ArrayList<>();
	@Doc("输入样例")
	private String inputSample;
	@Doc("输出参数列表")
	private List<ParamDefine> outputParams = new ArrayList<>();
	@Doc("输出样例")
	private String outputSample;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<ParamDefine> getInputParams() {
		return inputParams;
	}

	public void setInputParams(List<ParamDefine> inputParams) {
		this.inputParams = inputParams;
	}

	public String getInputSample() {
		return inputSample;
	}

	public void setInputSample(String inputSample) {
		this.inputSample = inputSample;
	}

	public List<ParamDefine> getOutputParams() {
		return outputParams;
	}

	public void setOutputParams(List<ParamDefine> outputParams) {
		this.outputParams = outputParams;
	}

	public String getOutputSample() {
		return outputSample;
	}

	public void setOutputSample(String outputSample) {
		this.outputSample = outputSample;
	}
}

