package com.newland.paas.common.util.ftp;

public enum DownloadStatusEnum {

    REMOTE_FILE_NOEXIST("remote_file_noexist", "远程文件不存在"), 
    LOCAL_BIGGER_REMOTE("local_bigger_remote", "本地文件大于远程文件"),
    DOWNLOAD_FROM_BREAK_SUCCESS("download_from_break_success", " 断点下载文件成功"),
    DOWNLOAD_FROM_BREAK_FAILED("download_from_break_failed", "断点下载文件失败"),
    DOWNLOAD_NEW_SUCCESS("download_new_success", "全新下载文件成功"), 
    DOWNLOAD_NEW_FAILED("download_new_failed", "全新下载文件失败"),
    ;

    public final String code;
    public final String name;

    DownloadStatusEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static DownloadStatusEnum getEnum(String code) {
        for (DownloadStatusEnum e : DownloadStatusEnum.values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return null;
    }
}
