package com.newland.paas.sbcommon.activitiflow.vo;

import java.util.List;
import java.util.Map;


/**   
*      
* @类名称：ActivitiTaskExecuteInput  表运行 ACT_RU_EXECUTION;
* @类描述：   
* @创建人：zyp
* @创建时间：2019/06/08  
*    
*/
public class ActivitiTaskExecuteInput {

    private String taskId;
    private List<Map<String, String>> properties;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public List<Map<String, String>> getProperties() {
        return properties;
    }

    public void setProperties(List<Map<String, String>> properties) {
        this.properties = properties;
    }
}
