package com.newland.paas.sbcommon.activitiflow.vo;


/**   
*      
* @类名称：ActivitiVariable  运行时ACT_RU_VARIABLE 流程变量数据表
* @类描述：   
* @创建人：zyp
* @创建时间：2019/06/08  
*    
*/
public class ActivitiVariable {
    private String name;
    private Object value;
    private String type;   //long string 等类型
    private String scope;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
