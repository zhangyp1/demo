package com.newland.paas.sbcommon.activitiflow.vo;

/**   
*      
* @类名称：ActivitiVariableHistory   
* @类描述：   
* @创建人：zyp
* @创建时间：2019/06/11  
*    
*/
public class ActivitiVariableHistory {

    private String id;
    private String processInstanceId;
    private String processInstanceUrl;
    private ActivitiVariable variable;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getProcessInstanceUrl() {
        return processInstanceUrl;
    }

    public void setProcessInstanceUrl(String processInstanceUrl) {
        this.processInstanceUrl = processInstanceUrl;
    }

    public ActivitiVariable getVariable() {
        return variable;
    }

    public void setVariable(ActivitiVariable variable) {
        this.variable = variable;
    }


}
