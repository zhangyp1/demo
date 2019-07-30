package com.newland.paas.paasservice.sysmgr.vo;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Author:PanYang
 * Date:Created in 下午4:04 2018/7/25
 * Modified By:
 */
@XmlRootElement(name = "USERMODIFYRSP")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserModifyRspVo {

    @XmlElement(name = "HEAD", required = true)
    protected HEAD head;
    @XmlElement(name = "BODY", required = true)
    protected BODY body;

    /**
     * 获取head属性的值。
     *
     * @return possible object is
     * {@link HEAD }
     */
    public HEAD getHEAD() {
        return head;
    }

    /**
     * 设置head属性的值。
     *
     * @param value allowed object is
     *              {@link HEAD }
     */
    public void setHEAD(HEAD value) {
        this.head = value;
    }

    /**
     * 获取body属性的值。
     *
     * @return possible object is
     * {@link BODY }
     */
    public BODY getBODY() {
        return body;
    }

    /**
     * 设置body属性的值。
     *
     * @param value allowed object is
     *              {@link BODY }
     */
    public void setBODY(BODY value) {
        this.body = value;
    }


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
     *         &lt;element name="KEY" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ERRCODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ERRDESC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="MODIFYMODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="USERID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="LOGINNO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="RSP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *         &lt;element name="ERRDESC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    //    @XmlType(name = "", propOrder = {
    //            "content"
    //    })
    public static class BODY {

        @XmlElement(name = "KEY")
        private String KEY;

        @XmlElement(name = "USERID")
        private String USERID;

        @XmlElement(name = "ERRCODE")
        private String ERRCODE;

        @XmlElement(name = "LOGINNO")
        private String LOGINNO;

        @XmlElement(name = "MODIFYMODE")
        private String MODIFYMODE;

        @XmlElement(name = "ERRDESC")
        private String ERRDESC;

        @XmlElement(name = "RSP")
        private String RSP;

        public String getKEY() {
            return KEY;
        }

        public void setKEY(String KEY) {
            this.KEY = KEY;
        }

        public String getUSERID() {
            return USERID;
        }

        public void setUSERID(String USERID) {
            this.USERID = USERID;
        }

        public String getERRCODE() {
            return ERRCODE;
        }

        public void setERRCODE(String ERRCODE) {
            this.ERRCODE = ERRCODE;
        }

        public String getLOGINNO() {
            return LOGINNO;
        }

        public void setLOGINNO(String LOGINNO) {
            this.LOGINNO = LOGINNO;
        }

        public String getMODIFYMODE() {
            return MODIFYMODE;
        }

        public void setMODIFYMODE(String MODIFYMODE) {
            this.MODIFYMODE = MODIFYMODE;
        }

        public String getERRDESC() {
            return ERRDESC;
        }

        public void setERRDESC(String ERRDESC) {
            this.ERRDESC = ERRDESC;
        }

        public String getRSP() {
            return RSP;
        }

        public void setRSP(String RSP) {
            this.RSP = RSP;
        }

    }


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
     *         &lt;element name="CODE" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="SID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="TIMESTAMP" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="SERVICEID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "code",
            "sid",
            "timestamp",
            "serviceid"
    })
    public static class HEAD {

        @XmlElement(name = "CODE", required = true)
        protected String code;
        @XmlElement(name = "SID", required = true)
        protected String sid;
        @XmlElement(name = "TIMESTAMP", required = true)
        protected String timestamp;
        @XmlElement(name = "SERVICEID", required = true)
        protected String serviceid;

        /**
         * 获取code属性的值。
         *
         * @return possible object is
         * {@link String }
         */
        public String getCODE() {
            return code;
        }

        /**
         * 设置code属性的值。
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setCODE(String value) {
            this.code = value;
        }

        /**
         * 获取sid属性的值。
         *
         * @return possible object is
         * {@link String }
         */
        public String getSID() {
            return sid;
        }

        /**
         * 设置sid属性的值。
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setSID(String value) {
            this.sid = value;
        }

        /**
         * 获取timestamp属性的值。
         *
         * @return possible object is
         * {@link String }
         */
        public String getTIMESTAMP() {
            return timestamp;
        }

        /**
         * 设置timestamp属性的值。
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setTIMESTAMP(String value) {
            this.timestamp = value;
        }

        /**
         * 获取serviceid属性的值。
         *
         * @return possible object is
         * {@link String }
         */
        public String getSERVICEID() {
            return serviceid;
        }

        /**
         * 设置serviceid属性的值。
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setSERVICEID(String value) {
            this.serviceid = value;
        }

    }
}
