/*
 *
 */
package com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;

/**
 * 描述
 * @author linkun
 * @created 2018年6月27日 下午4:27:37
 */
public class OperBO {
    private static final Log logger = LogFactory.getLogger(OperBO.class);
    
    /**
     * 操作ID
     */
    private Long operateId;
    /**
     * 操作名称
     */
    private String name;
    /**
     * 操作图标
     */
    private String icon;
    /**
     * 操作编码
     */
    private String code;
    /**
     * 上级ID
     */
    private Long parentId;
    /**
     * 排序号
     */
    private String orderNumber;
    /**
     * 隐藏标志   false不隐藏，true隐藏
     */
    private String hidden;
    /**
     * @author linkun
     * @created 2018年6月27日 下午4:31:45
     * @return 
     */
    public Long getOperateId() {
        return operateId;
    }
    /**
     * @author linkun
     * @created 2018年6月27日 下午4:31:45
     * @param operateId
     */
    public void setOperateId(Long operateId) {
        this.operateId = operateId;
    }
    /**
     * @author linkun
     * @created 2018年6月27日 下午4:31:45
     * @return 
     */
    public String getName() {
        return name;
    }
    /**
     * @author linkun
     * @created 2018年6月27日 下午4:31:45
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @author linkun
     * @created 2018年6月27日 下午4:31:45
     * @return 
     */
    public String getIcon() {
        return icon;
    }
    /**
     * @author linkun
     * @created 2018年6月27日 下午4:31:45
     * @param icon
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }
    /**
     * @author linkun
     * @created 2018年6月27日 下午4:31:45
     * @return 
     */
    public String getCode() {
        return code;
    }
    /**
     * @author linkun
     * @created 2018年6月27日 下午4:31:45
     * @param code
     */
    public void setCode(String code) {
        this.code = code;
    }
    /**
     * @author linkun
     * @created 2018年6月27日 下午4:31:45
     * @return 
     */
    public Long getParentId() {
        return parentId;
    }
    /**
     * @author linkun
     * @created 2018年6月27日 下午4:31:45
     * @param parentId
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    /**
     * @author linkun
     * @created 2018年7月2日 下午2:01:43
     * @return 
     */
    public String getOrderNumber() {
        return orderNumber;
    }
    /**
     * @author linkun
     * @created 2018年7月2日 下午2:01:43
     * @param orderNumber
     */
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
    
    
}

