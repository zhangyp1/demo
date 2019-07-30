package com.newland.paas.paasdatamodule.sysmgrdatamodule.model.bo;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.GlbLabel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * GlbLabelBo
 */
public class GlbLabelBo extends GlbLabel {
    private Long glbLabelValueId;

    private String labelValue;

    private List<String> objTypes;

    public Long getGlbLabelValueId() {
        return glbLabelValueId;
    }

    public void setGlbLabelValueId(Long glbLabelValueId) {
        this.glbLabelValueId = glbLabelValueId;
    }

    public String getLabelValue() {
        return labelValue;
    }

    public void setLabelValue(String labelValue) {
        this.labelValue = labelValue;
    }

    public List<String> getObjTypes() {
        return objTypes;
    }

    public void setObjTypes(List<String> objTypes) {
        this.objTypes = objTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GlbLabelBo)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        GlbLabelBo that = (GlbLabelBo) o;
        return Objects.equals(getLabelValue(), that.getLabelValue());
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), getLabelValue());
    }
}
