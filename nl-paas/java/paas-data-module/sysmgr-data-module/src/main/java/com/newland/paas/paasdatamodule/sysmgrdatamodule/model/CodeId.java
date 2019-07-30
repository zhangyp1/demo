package com.newland.paas.paasdatamodule.sysmgrdatamodule.model;

import java.io.Serializable;

/**
 * 视图V_CODE_ID
 *
 * @author WRP
 * @since 2019/1/10
 */
public class CodeId implements Serializable {

    private static final long serialVersionUID = -7741292254359054701L;
    private String tableName;
    private String id;
    private String code;
    private String name;
    private String type;
    private Long tenantId;

    private boolean codeNotNull = false;

    public CodeId() {
    }

    public CodeId(String tableName, String id, String code) {
        this.tableName = tableName;
        this.id = id;
        this.code = code;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public boolean isCodeNotNull() {
        return codeNotNull;
    }

    public void setCodeNotNull(boolean codeNotNull) {
        this.codeNotNull = codeNotNull;
    }
}
