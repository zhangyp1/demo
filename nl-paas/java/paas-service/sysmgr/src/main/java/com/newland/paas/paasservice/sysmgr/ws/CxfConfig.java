package com.newland.paas.paasservice.sysmgr.ws;


import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

/**
 * Author:PanYang
 * Date:Created in 上午11:08 2018/9/13
 * Modified By:
 */
@Configuration
public class CxfConfig {

    /**
     *
     * @return
     */
    @Bean
    public ServletRegistrationBean myServlet() {
        return new ServletRegistrationBean(new CXFServlet(),
                "/sysmgr/v1/ws/*");

    }

    /**
     *
     * @return
     */
    @Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus springBus() {
        return new SpringBus();
    }

    /**
     *
     * @return
     */
    @Bean
    public UpdateAppAcctSoap userService() {
        return new UpdateAppAcctSoapSOAPImpl();
    }

    /**
     *
     * @return
     */
    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(springBus(), userService());
        endpoint.publish("/updateAppAcctSoapSOAP");
        return endpoint;
    }
}
