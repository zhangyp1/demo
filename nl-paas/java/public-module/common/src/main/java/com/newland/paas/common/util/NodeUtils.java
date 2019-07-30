
 /**
 * NodeUtils.java
 * com.newland.sri.ccp.rtc.rtccommon.utils.node
 *
 * @author   zhangdx
 * @version  
 * @Date	 2016年1月5日		下午9:49:24
 *
 */

package com.newland.paas.common.util;

import com.newland.paas.common.constant.ZkConfigPath;

 /**
  * Copyright (c) 2016, NEWLAND , LTD All Rights Reserved.
  *
  * ClassName:NodeUtils
  * Description: TODO 节点相关公共处理
  * Funtion List:TODO 主要函数及其功能
  *
  * @since    Ver 1.1
  * @Date	 2016年1月5日		下午9:49:24
  *
  * @see
  * @History:         // 历史修改记录 
  *	       <author>  // 作者
  *         <time>    // 修改时间
  *         <version> // 版本
  *         <desc>    // 描述
  */
 public class NodeUtils {
     /**
      * @Function:     getNodeName 
      * @Description:  获取节点名称(在zk路径上路径)
      * @param nodePath
      * @return String
      */
     public static String getNodeName(String nodePath){
         String  nodeName = nodePath;
         int p = nodeName.lastIndexOf(ZkConfigPath.PATH_SEPERATE);
         if(p == nodeName.length() - 1){
             nodeName = nodePath.substring(0, p);
             p = nodeName.lastIndexOf(ZkConfigPath.PATH_SEPERATE);
         }
         if(p == -1)
             return nodeName;
         else
             return nodeName.substring(p+1, nodeName.length());
     }
 }

