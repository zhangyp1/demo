package com.newland.paas.common.util.ftp;

public enum UploadStatusEnum {

    CREATE_DIRECTORY_FAIL("create_directory_fail", "远程服务器相应目录创建失败"),
    CREATE_DIRECTORY_SUCCESS("create_directory_success", "远程服务器闯将目录成功"),
    UPLOAD_NEW_FILE_SUCCESS("upload_new_file_success", "上传新文件成功"),
    UPLOAD_NEW_FILE_FAILED("upload_new_file_failed", "上传新文件失败"), 
    FILE_EXITS("file_exits", "文件已经存在"),
    REMOTE_BIGGER_LOCAL("remote_bigger_local", "远程文件大于本地文件"),
    UPLOAD_FROM_BREAK_SUCCESS("upload_from_break_success", "断点续传成功"),
    UPLOAD_FROM_BREAK_FAILED("upload_from_break_failed", "断点续传失败"),
    DELETE_REMOTE_FAILD("delete_remote_faild", "删除远程文件失败"),
    ;
    
    
    public final String code;
    public final String name;

    UploadStatusEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static UploadStatusEnum getEnum(String code) {
        for (UploadStatusEnum e : UploadStatusEnum.values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return null;
    }
}
