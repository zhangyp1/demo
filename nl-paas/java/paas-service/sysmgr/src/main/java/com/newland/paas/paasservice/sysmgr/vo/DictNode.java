package com.newland.paas.paasservice.sysmgr.vo;

import java.io.Serializable;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.GlbDict;

/**
 *
 */
public class DictNode implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 90717641001019044L;


    private String id;
    private String code;

    private String label;

    public DictNode(GlbDict glbDict) {
        id = glbDict.getDictCode();
        code = glbDict.getDictCode();
        label = glbDict.getDictName();
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


}
