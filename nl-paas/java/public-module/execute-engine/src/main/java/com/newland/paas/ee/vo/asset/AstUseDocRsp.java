package com.newland.paas.ee.vo.asset;

import java.io.Serializable;

/**
 * 资产使用手册
 * 
 * @author cjw
 * @date 2018/12/18
 */
public class AstUseDocRsp implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6895953712729066444L;
    // 文档标识
    private Long docId;
    // 文档名称
    private String docName;
    // 文档描述
    private String docDesc;
    // 文档下载地址
    private String downloadUrl;

    public Long getDocId() {
        return docId;
    }

    public void setDocId(Long docId) {
        this.docId = docId;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getDocDesc() {
        return docDesc;
    }

    public void setDocDesc(String docDesc) {
        this.docDesc = docDesc;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
