package com.newland.paas.paasservice.sysmgr.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 3.2.4
 * 2018-09-13T20:20:03.904+08:00
 * Generated source version: 3.2.4
 */
@WebService(targetNamespace = "UpdateAppAcctSoap", name = "UpdateAppAcctSoap")
@XmlSeeAlso({ObjectFactory.class})
public interface UpdateAppAcctSoap {

    /**
     *
     * @param requestInfo
     * @return
     */
    @WebMethod(operationName = "UpdateAppAcctSoap",
            action = "http://service.base.app.core.a4.asiainfo.com/UpdateAppAcctSoap")
    @RequestWrapper(localName = "UpdateAppAcctSoap", targetNamespace = "UpdateAppAcctSoap",
            className = "com.newland.paas.paasservice.sysmgr.ws.UpdateAppAcctSoap_Type")
    @ResponseWrapper(localName = "UpdateAppAcctSoapResponse", targetNamespace = "UpdateAppAcctSoap",
            className = "com.newland.paas.paasservice.sysmgr.ws.UpdateAppAcctSoapResponse")
    @WebResult(name = "UpdateAppAcctSoapReturn", targetNamespace = "")
    java.lang.String updateAppAcctSoap(
            @WebParam(name = "RequestInfo", targetNamespace = "")
                    java.lang.String requestInfo
    );
}
