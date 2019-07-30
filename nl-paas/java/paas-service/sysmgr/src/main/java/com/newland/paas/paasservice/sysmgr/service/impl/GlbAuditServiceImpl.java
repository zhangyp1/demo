package com.newland.paas.paasservice.sysmgr.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.newland.paas.log.Log;
import com.newland.paas.log.LogFactory;
import com.newland.paas.log.LogProperty;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.dao.GlbAuditMapper;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.AuditLog;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.GlbAudit;
import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.GlbAuditLogAttr;
import com.newland.paas.paasservice.sysmgr.common.AuditConstants;
import com.newland.paas.paasservice.sysmgr.service.GlbAuditService;
import com.newland.paas.paasservice.sysmgr.utils.FtpFileUtil;
import com.newland.paas.paasservice.sysmgr.utils.XmlUtil;
import com.newland.paas.paasservice.sysmgr.vo.AuditInfoVo;
import com.newland.paas.paasservice.sysmgr.vo.AuditXmlVo;
import com.newland.paas.sbcommon.vo.PageInfo;
import com.newland.paas.sbcommon.vo.ResultPageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBException;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Author:PanYang
 * Date:Created in 下午3:09 2018/7/30
 * Modified By:
 */
@Service
public class GlbAuditServiceImpl implements GlbAuditService {
    @Value("${4AFTP.ftpName}")
    private String ftpName;
    @Value("${4AFTP.ftpPassword}")
    private String ftpPassword;
    @Value("${4AFTP.ftpPath}")
    private String ftpPath;
    @Value("${4AFTP.ftpAddress}")
    private String ftpAddress;

    private static final Log LOGGER = LogFactory.getLogger(GlbAuditServiceImpl.class);

    @Autowired
    GlbAuditMapper glbAuditMapper;

    //记录审计日志使用 队列
    private static final int MAXQUEUE = 1000000;

    private static final int SUBMIT_COUNT = 1000;

    private static BlockingQueue<AuditLog> queue = new LinkedBlockingQueue<>(MAXQUEUE);

    public static final String USR_LOCAL_NLDATA_FILES = "/usr/local/nldata/files/";

    /**
     * 分页查询审计日志
     *
     * @param glbAudit
     * @param pageInfo
     * @return
     */
    @Override
    public ResultPageData getAllGlbAudit(GlbAudit glbAudit, PageInfo pageInfo) {
        LOGGER.info(LogProperty.LOGTYPE_REQUEST,
                MessageFormat.format("######getAllGlbAudit glbAudit info is {0}, page info is {1}######",
                        JSONObject.toJSON(glbAudit),
                        JSONObject.toJSON(pageInfo)));
        //设置分页参数
        Page<GlbAudit> page = PageHelper.startPage(pageInfo.getCurrentPage(), pageInfo.getPageSize());

        //查询审计日志
        List<GlbAudit> list = glbAuditMapper.listGlbAudit(glbAudit);
        pageInfo.setTotalRecord(page.getTotal());
        ResultPageData resultPageData = new ResultPageData(list, pageInfo);
        LOGGER.info(LogProperty.LOGTYPE_REQUEST,
                MessageFormat.format("######getAllGlbAudit result is {0}######", JSONObject.toJSON(resultPageData)));
        return resultPageData;
    }

    /**
     * 分页查询审计日志关联对象
     *
     * @param id
     * @return
     */
    @Override
    public List<GlbAuditLogAttr> getAllGlbAuditOperate(Long id) {
        LOGGER.info(LogProperty.LOGTYPE_REQUEST,
                MessageFormat.format("######getAllGlbAuditOperate glbAuditObjOperate info is {0}######", id));
        GlbAuditLogAttr glbAuditLogAttr = new GlbAuditLogAttr();
        glbAuditLogAttr.setAuditLogId(id);
        //查询审计日志关联对象
        List<GlbAuditLogAttr> list = glbAuditMapper.listGlbAuditObjOperate(glbAuditLogAttr);
        LOGGER.info(LogProperty.LOGTYPE_REQUEST,
                MessageFormat.format("######getAllGlbAuditOperate result is {0}######", list));
        return list;
    }

    /**
     * 审计日志入库
     *
     * @param auditLogVo
     */
    @Override
    public void putAudit(AuditLog auditLogVo) {
        try {
            queue.put(auditLogVo);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    @PostConstruct
    private void setup() {
        //启动监听线程
        new Thread() {
            @Override
            public void run() {
                takeToDb();
            }
        }.start();

    }

    /**
     *
     */
    public void takeToDb() {
        List<AuditLog> list = new ArrayList<>();
        int count = 0;
        try {
            while (true) {
                list.add(queue.take());
                count++;
                if (count > SUBMIT_COUNT) {
                    //1000提交一次
                    saveDateForAudit(list);
                    list.clear();
                    count = 0;
                }
                if (queue.isEmpty() && count > 0) {
                    //队列为空时候，立即提交
                    saveDateForAudit(list);
                    list.clear();
                    count = 0;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 审计日志入库
     *
     * @param list
     */
    public void saveDateForAudit(List<AuditLog> list) {
        if (!list.isEmpty()) {
            for (AuditLog auditLog : list) {
                GlbAudit glbAudit = auditLog.getGlbAudit();
                if (glbAudit != null) {
                    //入库
                    glbAuditMapper.saveAudit(glbAudit);
                }
                List<GlbAuditLogAttr> glbAuditLogAttrList = auditLog.getGlbAuditLogAttr();
                Long auditAuditLogId = glbAudit.getAuditLogId();
                if (!glbAuditLogAttrList.isEmpty()) {
                    //一条数据不做循环
                    if (glbAuditLogAttrList.size() == 1) {
                        GlbAuditLogAttr glbAuditLogAttr = glbAuditLogAttrList.get(0);
                        glbAuditLogAttr.setAuditLogId(auditAuditLogId);
                        //入库
                        glbAuditMapper.saveAuditAttr(glbAuditLogAttr);
                    } else {
                        for (GlbAuditLogAttr glbAuditLogAttr : glbAuditLogAttrList) {
                            glbAuditLogAttr.setAuditLogId(auditAuditLogId);
                            //入库
                            glbAuditMapper.saveAuditAttr(glbAuditLogAttr);
                        }
                    }
                }
            }
        }
    }

    /**
     * 审计日志数据录入缓存
     */
    @Override
    public void writeAuditDate(AuditLog al, String account, String objectType,
                               String objectId, String operateCode, String operateName) {
        try {
            InetAddress address = InetAddress.getLocalHost();
            String hostAddress = address.getHostAddress();
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            LOGGER.info(LogProperty.LOGTYPE_DETAIL, "审计日志写入缓存开始");
            AuditInfoVo auditInfoVo = new AuditInfoVo();
            auditInfoVo.setIDENTITY_NAME("4APAASLog");
            auditInfoVo.setRESOURCE_KIND("1");
            auditInfoVo.setRESOURCE_CODE("JSPASS");
            auditInfoVo.setIDR_CREATION_TIME(sdf.format(date));
            //主帐号ID
            auditInfoVo.setMAIN_ACCOUNT_NAME("");
            //从帐号
            auditInfoVo.setSUB_ACCOUNT_NAME(account);
            auditInfoVo.setOPERATE_TIME(sdf.format(date));
            auditInfoVo.setOP_TYPE_ID("1-JSPASS" + operateCode);
            auditInfoVo.setOP_TYPE_NAME(objectType);
            auditInfoVo.setOP_LEVEL_ID("2");
            auditInfoVo.setOPERATE_CONTENT(operateName);
            //0-成功 1-失败
            auditInfoVo.setOPERATE_RESULT("0");
            auditInfoVo.setMODULE_ID("");
            auditInfoVo.setMODULE_NAME(al.getGlbAudit().getAuditModule());
            auditInfoVo.setTASK_CODE("");
            auditInfoVo.setBANKAPPROVE("");
            auditInfoVo.setBANKFLAG("");
            auditInfoVo.setCLIENT_NETWORK_ADDRESS(al.getGlbAudit().getIp());
            auditInfoVo.setCLIENT_NAME("");
            auditInfoVo.setCLIENT_ADDRESS(al.getGlbAudit().getIp());
            auditInfoVo.setCLIENT_PORT("");
            auditInfoVo.setCLIENT_MAC("");
            auditInfoVo.setCLIENT_CPU_SERIAL("");
            auditInfoVo.setSERVER_ADDRESS(hostAddress);
            auditInfoVo.setSERVER_PORT("");
            auditInfoVo.setSERVER_MAC("");
            auditInfoVo.setTO_PROVINCES_ID("");
            auditInfoVo.setTO_PROVINCES_NAME("");
            auditInfoVo.setFROM_PROVINCES_ID("");
            auditInfoVo.setFROM_PROVINCES_NAME("");
            synchronized (this) {
                //队列一直在写数据
                AuditConstants.queue.put(auditInfoVo);
            }
            LOGGER.info(LogProperty.LOGTYPE_DETAIL, "审计日志写入缓存结束");
        } catch (UnknownHostException e) {
            e.printStackTrace();
            LOGGER.error("writeAuditDate error!", "", e);
        } catch (InterruptedException e) {
            e.printStackTrace();
            LOGGER.error("writeAuditDate error!", "", e);
        }

    }

    /**
     * 队列中数据存入list中
     */
    @PostConstruct
    public void getInfoToList() {
        //启动监听线程
        new Thread() {
            @Override
            public void run() {
                takeToList();
            }
        }.start();
    }

    /**
     * 队列中数据存入list中
     */
    public void takeToList() {

        while (true) {
            try {
                boolean auditFlag = AuditConstants.auditFlag;
                AuditInfoVo audit = AuditConstants.queue.take();
                if (audit != null && auditFlag) {
                    //当flag为true时候写入数据进list
                    AuditConstants.list.add(audit);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 每十分钟执行一次,审计日志推送4A
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    public void createXmlTo4A() {

        try {
            //将写入list开关关闭 不让数据写进list中
            AuditConstants.auditFlag = false;
//            Thread.currentThread().sleep(2000);
            LOGGER.info(LogProperty.LOGTYPE_DETAIL, "定时器生成xml开始");
            //step1:从内存中获取xml数据
            List<AuditInfoVo> list = AuditConstants.list;
            LOGGER.info(LogProperty.LOGTYPE_REQUEST,
                    MessageFormat.format("######createXmlTo4A list.size is {0}######", list.size()));
            File file = findXmlfileName();
            //step2:生成xml文件
            if (!list.isEmpty()) {
                writeXml(file, list);
                //生成新文件
                createNewFile();
                AuditConstants.list.clear();
            }
            AuditConstants.auditFlag = true;
            LOGGER.info(LogProperty.LOGTYPE_DETAIL, "定时器生成xml结束");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * ftp上传
     *
     * @param file
     */
    public void upload(File file) {
        try {
            LOGGER.info(LogProperty.LOGTYPE_DETAIL, "ftp开始上传！");
            String fileName = file.getName();
            InputStream inputStream = new FileInputStream(file.getPath());
            if (FtpFileUtil.uploadFile(fileName, inputStream, ftpName, ftpPassword, ftpPath, ftpAddress)) {
                LOGGER.info(LogProperty.LOGTYPE_DETAIL, "ftp上传成功！");
                file.delete();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * @return
     */
    public String getPath() {
        String templateFilePath = "";
        try {
            templateFilePath = USR_LOCAL_NLDATA_FILES;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return templateFilePath;
    }

    /**
     * 创建文件名
     *
     * @throws Exception
     */
    public void createNewFile()
            throws Exception {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        String fileName = sdf.format(date);
        //以时间命名文件 文件路径+名称+文件类型
        String filenameTemp = getPath() + fileName + ".xml";
        File file = new File(filenameTemp);
        file.createNewFile();

    }

    /**
     * 查询xml文件名称
     *
     * @param auditLogVo
     */
    public File findXmlfileName() {
        Long num;
        //File file = new File(path);
        File file = new File(getPath());

        if (file.list() == null || file.list().length == 0) {
            return null;
        }
        String[] arrayFile = file.list();

        List<Long> list = new ArrayList<>();
        Long max = 0L;
        if (arrayFile.length == 1) {
            if (arrayFile[0].length() >= 16) {
                String abc = arrayFile[0].substring(0, 12).toString();
                num = Long.parseLong(abc);
                max = num;
            }
        } else if (arrayFile.length > 1) {
            for (int i = 0; i < arrayFile.length; i++) {
                //xml文件名称长度
                if (arrayFile[i].length() >= 16) {
                    String abc = arrayFile[i].substring(0, 12).toString();
                    num = Long.parseLong(abc);
                    if (num != null) {
                        list.add(num);
                    }
                }
            }
            max = list.get(0);
            //获取最新文件名
            for (int i = 0; i < list.size(); i++) {
                if (max < list.get(i)) {
                    max = list.get(i);
                }
            }
        }
        if (max != 0) {
            File pathFile = new File(getPath() + max + ".xml");

            return pathFile;
        } else {
            LOGGER.info(LogProperty.LOGTYPE_DETAIL,
                    MessageFormat.format("GlbAuditServiceImpl writeAuditToXml error file name is {0}", arrayFile[0]));
            return null;
        }
    }

    /**
     * 写入xml
     *
     * @param dest
     * @param vo
     */
    public void writeXml(File dest, List<AuditInfoVo> vo) throws Exception{
        AuditXmlVo auditXmlVo = new AuditXmlVo();
        auditXmlVo.setLog4A(vo);
        LOGGER.info(LogProperty.LOGTYPE_DETAIL, "GlbAuditServiceImpl writeXml start");
        //如果没有文件默认生成一个
        if (dest == null) {
            Date date = new Date();
            SimpleDateFormat sf = new SimpleDateFormat("MMddHHmm");
            dest = new File(getPath() + "1000" + sf.format(date) + "" + ".xml");
            dest.createNewFile();
        }
        try {
            //对象转xml
            String xml = XmlUtil.beanToXml(auditXmlVo, AuditXmlVo.class);
            PrintWriter pfp = new PrintWriter(dest, "UTF-8");
            pfp.print(xml);
            pfp.flush();
            pfp.close();
            LOGGER.info(LogProperty.LOGTYPE_DETAIL, "GlbAuditServiceImpl writeXml end");
            //step3:通过ftp上传文件志4A
            upload(dest);
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

}
