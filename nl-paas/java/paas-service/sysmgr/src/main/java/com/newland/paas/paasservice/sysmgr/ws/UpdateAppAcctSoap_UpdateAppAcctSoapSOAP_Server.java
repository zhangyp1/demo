
package com.newland.paas.paasservice.sysmgr.ws;

import javax.xml.ws.Endpoint;

/**
 * This class was generated by Apache CXF 3.2.4
 * 2018-09-13T20:20:03.945+08:00
 * Generated source version: 3.2.4
 */

public class UpdateAppAcctSoap_UpdateAppAcctSoapSOAP_Server {

    protected UpdateAppAcctSoap_UpdateAppAcctSoapSOAP_Server() throws java.lang.Exception {
        Object implementor = new UpdateAppAcctSoapSOAPImpl();
        String address = "http://www.example.org/";
        Endpoint.publish(address, implementor);
    }
}
