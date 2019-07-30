package com.newland.paas.paasservice.sysmgr.ws;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type的 Java 类。
 * <p>
 * <p>以下模式片段指定包含在此类中的预期内容。
 * <p>
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="UpdateAppAcctSoapReturn" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "updateAppAcctSoapReturn"
})
@XmlRootElement(name = "UpdateAppAcctSoapResponse")
public class UpdateAppAcctSoapResponse {

    @XmlElement(name = "UpdateAppAcctSoapReturn", required = true)
    protected String updateAppAcctSoapReturn;

    /**
     * 获取updateAppAcctSoapReturn属性的值。
     *
     * @return possible object is
     * {@link String }
     */
    public String getUpdateAppAcctSoapReturn() {
        return updateAppAcctSoapReturn;
    }

    /**
     * 设置updateAppAcctSoapReturn属性的值。
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setUpdateAppAcctSoapReturn(String value) {
        this.updateAppAcctSoapReturn = value;
    }

}
