package com.newland.paas.paasdatamodule.activitiflowdatamodule.dao;

import com.newland.paas.paasdatamodule.activitiflowdatamodule.model.bo.HistoricProcessInstanceBo;
import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntityImpl;

import java.util.List;

/**
 * @author WRP
 * @since 2019/7/11
 */
public interface ActHiProcinstMapper {

    List<HistoricProcessInstanceEntityImpl> selectBySelective(HistoricProcessInstanceBo historicProcessInstanceBo);

}
