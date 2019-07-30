package com.newland.paas.paasservice.sysmgr.utils;

import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Author:PanYang
 * Date:Created in 下午3:11 2018/7/24
 * Modified By:
 */
@Component
public class XmlUtil {

    private static final Log LOGGER = LogFactory.getLogger(XmlUtil.class);

    private XmlUtil() {

    }

    /**
     * @param obj
     * @return
     */
    public static String javaBeanToXml(Object obj) {
        try {
            JAXBContext jc = JAXBContext.newInstance(obj.getClass());
            Marshaller ms = jc.createMarshaller();
            StringWriter sw = new StringWriter();
            ms.marshal(obj, sw);
            return sw.toString();
        } catch (JAXBException e) {
            LOGGER.info(LogProperty.LOGTYPE_CALL, "实体转换XML错误:" + e.getMessage());
            LOGGER.error(LogProperty.LOGTYPE_CALL, "实体转换XML错误:", e);
        }
        return null;
    }

    /**
     * @param xml
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T xmlToJavaBean(String xml, Class<T> clazz) {
        try {
            JAXBContext jc = JAXBContext.newInstance(clazz);
            Unmarshaller unmar = jc.createUnmarshaller();
            return (T) unmar.unmarshal(new StringReader(xml));
        } catch (JAXBException e) {
            LOGGER.info(LogProperty.LOGTYPE_CALL, "XML转实体错误:" + e.getMessage());
            LOGGER.error(LogProperty.LOGTYPE_CALL, "XML转实体错误:", e);
        }
        return null;
    }

    /**
     * @param obj
     * @param load
     * @return
     * @throws JAXBException
     */
    public static String beanToXml(Object obj, Class<?> load)
            throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(load);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        StringWriter writer = new StringWriter();
        marshaller.marshal(obj, writer);
        return writer.toString();
    }
}
