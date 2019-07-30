package com.newland.paas.paasdatamodule.activitiflowdatamodule.dao;

import com.newland.paas.paasdatamodule.activitiflowdatamodule.model.bo.HistoricTaskInstanceBo;
import com.newland.paas.paasdatamodule.activitiflowdatamodule.model.bo.RunTaskBo;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntityImpl;
import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;

import java.util.List;

/**
 * @author WRP
 * @since 2019/7/12
 */
public interface ActHiTaskinstMapper {

    List<HistoricTaskInstanceEntityImpl> selectBySelective(HistoricTaskInstanceBo historicTaskInstanceBo);

}
