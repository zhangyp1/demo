package com.newland.paas.paasdatamodule.sysmgrdatamodule.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据字典
 *
 * @author wrp
 * @Date 2018/6/26
 */
public class GlbDict implements Serializable {
    private static final long serialVersionUID = 938681074559906956L;

    private Long dictId;

    private String dictCode;

    private String dictName;

    private String dictPcode;

    private Date inureDate;

    private Date expireDate;

    private Short dictLevel;

    private Date createDate;

    private Date changeDate;

    private Long operatorId;

    public GlbDict() {
    }

    public GlbDict(String dictCode, String dictName) {
        this.dictCode = dictCode;
        this.dictName = dictName;
    }

    public GlbDict(String dictCode, String dictName, String dictPcode) {
        this.dictCode = dictCode;
        this.dictName = dictName;
        this.dictPcode = dictPcode;
    }

    public String getDictCode() {
        return dictCode;
    }

    public void setDictCode(String dictCode) {
        this.dictCode = dictCode == null ? null : dictCode.trim();
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName == null ? null : dictName.trim();
    }

    public String getDictPcode() {
        return dictPcode;
    }

    public void setDictPcode(String dictPcode) {
        this.dictPcode = dictPcode == null ? null : dictPcode.trim();
    }

    public Date getInureDate() {
        return inureDate;
    }

    public void setInureDate(Date inureDate) {
        this.inureDate = inureDate;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public Short getDictLevel() {
        return dictLevel;
    }

    public void setDictLevel(Short dictLevel) {
        this.dictLevel = dictLevel;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public Long getDictId() {
        return dictId;
    }

    public void setDictId(Long dictId) {
        this.dictId = dictId;
    }

}