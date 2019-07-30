package com.newland.paas.sbcommon.activitiflow.vo;

import java.util.List;


/**   
*      
* @类名称：ActivitiRunProcessInput   
* @类描述：   
* @创建人：zyp
* @创建时间：2019/06/11  
*    
*/
public class ActivitiRunProcessInput {

    private String processDefinitionKey;
    private String businessKey;
    private List<ActivitiVariable> variables;

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public List<ActivitiVariable> getVariables() {
        return variables;
    }

    public void setVariables(List<ActivitiVariable> variables) {
        this.variables = variables;
    }
}
