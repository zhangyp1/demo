package com.newland.paas.paasservice.activitiflow.controller;

import com.newland.paas.paasservice.activitiflow.service.IDService;
import com.newland.paas.paasservice.controllerapi.activiti.vo.IDMembershipVo;
import com.newland.paas.sbcommon.vo.BasicRequestContentVo;
import com.newland.paas.sbcommon.vo.BasicResponseContentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 权限验证
 *
 * @author WRP
 * @since 2019/7/4
 */
@RestController
@RequestMapping("v1/id")
public class IDController {

    @Autowired
    private IDService idService;

    /**
     * 是否有权限（存在与组内）
     *
     * @param inputVo
     * @return
     */
    @PostMapping(value = "isOwn", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public BasicResponseContentVo<Boolean> getTaskListByInstanceId(
            @RequestBody BasicRequestContentVo<IDMembershipVo> inputVo) {
        return new BasicResponseContentVo<>(idService.isOwn(inputVo.getParams().getUserId(), inputVo.getParams().getGroupId()));
    }

}
