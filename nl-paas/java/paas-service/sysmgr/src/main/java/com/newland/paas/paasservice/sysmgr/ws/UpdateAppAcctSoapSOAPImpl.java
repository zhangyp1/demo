package com.newland.paas.paasservice.sysmgr.ws;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.SysUser;
import com.newland.paas.paasservice.sysmgr.common.SysUserStatusConsts;
import com.newland.paas.paasservice.sysmgr.utils.SpringBeanUtil;
import com.newland.paas.paasservice.sysmgr.utils.XmlUtil;
import com.newland.paas.paasservice.sysmgr.vo.UserModifyReqVo;
import com.newland.paas.paasservice.sysmgr.vo.UserModifyRspVo;
import com.newland.paas.sbcommon.common.ApplicationException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author:PanYang Date:Created in 下午8:21 2018/9/13 Modified By:
 */
public class UpdateAppAcctSoapSOAPImpl implements UpdateAppAcctSoap {
    private static Logger logger = LoggerFactory.getLogger(UpdateAppAcctSoapSOAPImpl.class);
    private A4InteractServiceImpl a4InteractService;

    /**
     * @return
     */
    private A4InteractServiceImpl getA4InteractService() {
        if (a4InteractService == null) {
            a4InteractService = SpringBeanUtil.getBean(A4InteractServiceImpl.class);
        }
        return this.a4InteractService;
    }

    /**
     * @param requestInfo
     * @return
     */
    @Override
    public String updateAppAcctSoap(String requestInfo) {
        logger.info("====== request from 4a =======");
        logger.info(requestInfo);
        String result = "";
        A4InteractServiceImpl interactService = this.getA4InteractService();
        try {
            Document dom = DocumentHelper.parseText(requestInfo);
            Element root = dom.getRootElement();
            String name = root.getName();
            // 封装返回报文
            UserModifyRspVo.BODY respBody = new UserModifyRspVo.BODY();
            // 4A同步数据封装
            SysUser myUser = new SysUser();
            // 删除用
            if (name.equals("USERMODIFYRSP")) {
                UserModifyRspVo usermodifyrsp = XmlUtil.xmlToJavaBean(requestInfo, UserModifyRspVo.class);
                String userId = usermodifyrsp.getBODY().getUSERID();
                myUser.setUserId(Long.parseLong(userId));
                interactService.delUser(myUser);
            } else {
                userModify(requestInfo, interactService, respBody, myUser);
            }
            UserModifyRspVo usermodifyrsp = new UserModifyRspVo();
            usermodifyrsp.setBODY(respBody);
            result = XmlUtil.javaBeanToXml(usermodifyrsp);
        } catch (DocumentException e) {
            logger.error(e.getMessage());
        }
        logger.info("===== response to 4a ======");
        logger.info(result);
        return result;
    }

    /**
     * userModify
     * 
     * @param requestInfo
     * @param interactService
     * @param respBody
     * @param myUser
     */
    private void userModify(String requestInfo, A4InteractServiceImpl interactService, UserModifyRspVo.BODY respBody,
        SysUser myUser) {
        UserModifyReqVo usermodifyreq = XmlUtil.xmlToJavaBean(requestInfo, UserModifyReqVo.class);
        String operateId = usermodifyreq.getBODY().getOPERATORID();
        String modifymode = usermodifyreq.getBODY().getMODIFYMODE();
        UserModifyReqVo.BODY.USERINFO userInfo = usermodifyreq.getBODY().getUSERINFO();
        myUser.setPassword(userInfo.getPASSWORD());
        myUser.setUsername(userInfo.getUSERNAME());
        String userId = userInfo.getUSERID();
        if (!userId.equals("")) {
            myUser.setUserId(Long.parseLong(userId));
        }
        myUser.setAccount(userInfo.getLOGINNO());
        if (userInfo.getSTATUS() == null || userInfo.getSTATUS().equals("0")) {
            myUser.setStatus(SysUserStatusConsts.NOTLOGIN.getValue());
        } else if (userInfo.getSTATUS().equals("1")) {
            myUser.setStatus(SysUserStatusConsts.ENABLED.getValue());
        }
        myUser.setPhone(userInfo.getMOBILE());
        try {
            // 更新用户信息入库
            SysUser resultUser = interactService.updateUser(operateId, modifymode, myUser);
            // 封装返回报文
            respBody.setMODIFYMODE(modifymode);
            respBody.setUSERID(String.valueOf(resultUser.getUserId()));
            respBody.setLOGINNO(resultUser.getAccount());
            respBody.setRSP("0");
        } catch (ApplicationException e) {
            logger.error("UpdateAppAcctSoapSOAPImpl updateAppAcctSoap error", e);
            respBody.setKEY(userInfo.getLOGINNO());
            respBody.setERRCODE("");
            respBody.setERRDESC(e.getMessage());
        }
    }
}
