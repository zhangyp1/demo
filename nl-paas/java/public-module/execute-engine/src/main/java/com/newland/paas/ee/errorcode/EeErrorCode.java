package com.newland.paas.ee.errorcode;

import com.newland.paas.common.exception.ErrorCode;
import com.newland.paas.sbcommon.vo.PaasError;

/**
 * Copyright (c) 2018, NEWLAND , LTD All Rights Reserved.
 *
 * @ClassName:EeErrorCode
 * @Description: EeErrorCode
 * @Funtion List:
 * @Date 2018年7月2日 下午6:33:57
 */
public class EeErrorCode extends ErrorCode {
	//执行引擎异常
	public static final String BUSERROR_EXEENGINE = BUSERROR+"19";
	
	public static final PaasError EXEENGINE_GDATA_FAIL = new PaasError(BUSERROR_EXEENGINE+"00001", "获取数据失败");

	//执行自动化安装错误
	public static final PaasError EXECUTE_AUTOMATE_JOB_ERROR = new PaasError(BUSERROR_EXEENGINE+"00002", "执行自动化安装错误");

	//不支持的集群类型错误
	public static final PaasError EXEENGINE_INVALID_CLUSTER = new PaasError(BUSERROR_EXEENGINE+"00003", "不支持的集群类型");

	//public static final PaasError ASSET_NOT_METHOD_NAME = new PaasError(BUSERROR_EXEENGINE+"00002", "找不到对应的外部控制接口");
	
	//public static final PaasError EXTERN_CTL_INTF_NOT_METHOD_FILE_NAME = new PaasError(BUSERROR_EXEENGINE+"00003", "外部控制接口没有操作文件名");

}
