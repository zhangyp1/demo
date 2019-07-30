package com.newland.paas.paasservice.sysmgr.vo;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Author:PanYang
 * Date:Created in 下午3:22 2018/7/25
 * Modified By:
 */
@XmlRootElement(name = "USERMODIFYREQ")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserModifyReqVo {
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
     *         &lt;element name="OPERATORID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="OPERATORPWD" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="OPERATORIP" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="MODIFYMODE" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *         &lt;element name="USERINFO"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="USERID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="LOGINNO" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="USERNAME" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="ORGID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="EMAIL" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="MOBILE" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="PASSWORD" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="STATUS" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="EFFECTDATE" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="EXPIREDATE" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="REMARK" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "operatorid",
            "operatorpwd",
            "operatorip",
            "modifymode",
            "userinfo"
    })
    public static class BODY {

        @XmlElement(name = "OPERATORID", required = true)
        protected String operatorid;
        @XmlElement(name = "OPERATORPWD", required = true)
        protected String operatorpwd;
        @XmlElement(name = "OPERATORIP", required = true)
        protected String operatorip;
        @XmlElement(name = "MODIFYMODE", required = true)
        protected String modifymode;
        @XmlElement(name = "USERINFO", required = true)
        protected USERINFO userinfo;

        /**
         * 获取operatorid属性的值。
         *
         * @return possible object is
         * {@link String }
         */
        public String getOPERATORID() {
            return operatorid;
        }

        /**
         * 设置operatorid属性的值。
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setOPERATORID(String value) {
            this.operatorid = value;
        }

        /**
         * 获取operatorpwd属性的值。
         *
         * @return possible object is
         * {@link String }
         */
        public String getOPERATORPWD() {
            return operatorpwd;
        }

        /**
         * 设置operatorpwd属性的值。
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setOPERATORPWD(String value) {
            this.operatorpwd = value;
        }

        /**
         * 获取operatorip属性的值。
         *
         * @return possible object is
         * {@link String }
         */
        public String getOPERATORIP() {
            return operatorip;
        }

        /**
         * 设置operatorip属性的值。
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setOPERATORIP(String value) {
            this.operatorip = value;
        }

        /**
         * 获取modifymode属性的值。
         *
         * @return possible object is
         * {@link String }
         */
        public String getMODIFYMODE() {
            return modifymode;
        }

        /**
         * 设置modifymode属性的值。
         *
         * @param value allowed object is
         *              {@link String }
         */
        public void setMODIFYMODE(String value) {
            this.modifymode = value;
        }

        /**
         * 获取userinfo属性的值。
         *
         * @return possible object is
         * {@link USERINFO }
         */
        public USERINFO getUSERINFO() {
            return userinfo;
        }

        /**
         * 设置userinfo属性的值。
         *
         * @param value allowed object is
         *              {@link USERINFO }
         */
        public void setUSERINFO(USERINFO value) {
            this.userinfo = value;
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
         *         &lt;element name="USERID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="LOGINNO" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="USERNAME" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="ORGID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="EMAIL" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="MOBILE" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="PASSWORD" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="STATUS" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="EFFECTDATE" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="EXPIREDATE" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="REMARK" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *       &lt;/sequence&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
                "userid",
                "loginno",
                "username",
                "orgid",
                "email",
                "mobile",
                "password",
                "status",
                "effectdate",
                "expiredate",
                "remark"
        })
        public static class USERINFO {

            @XmlElement(name = "USERID", required = true)
            protected String userid;
            @XmlElement(name = "LOGINNO", required = true)
            protected String loginno;
            @XmlElement(name = "USERNAME", required = true)
            protected String username;
            @XmlElement(name = "ORGID", required = true)
            protected String orgid;
            @XmlElement(name = "EMAIL", required = true)
            protected String email;
            @XmlElement(name = "MOBILE", required = true)
            protected String mobile;
            @XmlElement(name = "PASSWORD", required = true)
            protected String password;
            @XmlElement(name = "STATUS", required = true)
            protected String status;
            @XmlElement(name = "EFFECTDATE", required = true)
            protected String effectdate;
            @XmlElement(name = "EXPIREDATE", required = true)
            protected String expiredate;
            @XmlElement(name = "REMARK", required = true)
            protected String remark;

            /**
             * 获取userid属性的值。
             *
             * @return possible object is
             * {@link String }
             */
            public String getUSERID() {
                return userid;
            }

            /**
             * 设置userid属性的值。
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setUSERID(String value) {
                this.userid = value;
            }

            /**
             * 获取loginno属性的值。
             *
             * @return possible object is
             * {@link String }
             */
            public String getLOGINNO() {
                return loginno;
            }

            /**
             * 设置loginno属性的值。
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setLOGINNO(String value) {
                this.loginno = value;
            }

            /**
             * 获取username属性的值。
             *
             * @return possible object is
             * {@link String }
             */
            public String getUSERNAME() {
                return username;
            }

            /**
             * 设置username属性的值。
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setUSERNAME(String value) {
                this.username = value;
            }

            /**
             * 获取orgid属性的值。
             *
             * @return possible object is
             * {@link String }
             */
            public String getORGID() {
                return orgid;
            }

            /**
             * 设置orgid属性的值。
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setORGID(String value) {
                this.orgid = value;
            }

            /**
             * 获取email属性的值。
             *
             * @return possible object is
             * {@link String }
             */
            public String getEMAIL() {
                return email;
            }

            /**
             * 设置email属性的值。
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setEMAIL(String value) {
                this.email = value;
            }

            /**
             * 获取mobile属性的值。
             *
             * @return possible object is
             * {@link String }
             */
            public String getMOBILE() {
                return mobile;
            }

            /**
             * 设置mobile属性的值。
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setMOBILE(String value) {
                this.mobile = value;
            }

            /**
             * 获取password属性的值。
             *
             * @return possible object is
             * {@link String }
             */
            public String getPASSWORD() {
                return password;
            }

            /**
             * 设置password属性的值。
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setPASSWORD(String value) {
                this.password = value;
            }

            /**
             * 获取status属性的值。
             *
             * @return possible object is
             * {@link String }
             */
            public String getSTATUS() {
                return status;
            }

            /**
             * 设置status属性的值。
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setSTATUS(String value) {
                this.status = value;
            }

            /**
             * 获取effectdate属性的值。
             *
             * @return possible object is
             * {@link String }
             */
            public String getEFFECTDATE() {
                return effectdate;
            }

            /**
             * 设置effectdate属性的值。
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setEFFECTDATE(String value) {
                this.effectdate = value;
            }

            /**
             * 获取expiredate属性的值。
             *
             * @return possible object is
             * {@link String }
             */
            public String getEXPIREDATE() {
                return expiredate;
            }

            /**
             * 设置expiredate属性的值。
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setEXPIREDATE(String value) {
                this.expiredate = value;
            }

            /**
             * 获取remark属性的值。
             *
             * @return possible object is
             * {@link String }
             */
            public String getREMARK() {
                return remark;
            }

            /**
             * 设置remark属性的值。
             *
             * @param value allowed object is
             *              {@link String }
             */
            public void setREMARK(String value) {
                this.remark = value;
            }

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
