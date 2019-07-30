package com.newland.paas.paasdatamodule.activitiflowdatamodule.dao;

import com.newland.paas.paasdatamodule.activitiflowdatamodule.model.bo.RunTaskBo;
import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author WRP
 * @since 2019/7/12
 */
public interface ActRuTaskMapper {

    List<TaskEntityImpl> selectBySelective(RunTaskBo runTaskBo);

    String getAssigneeByTaskId(@Param("taskId") String taskId);

}
