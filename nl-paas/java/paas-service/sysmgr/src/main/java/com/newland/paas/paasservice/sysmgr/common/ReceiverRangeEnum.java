package com.newland.paas.paasservice.sysmgr.common;

import com.newland.paas.paasdatamodule.sysmgrdatamodule.model.GlbDict;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 接收范围（all:全系统，tenant:租户，group:工组，user:用户）
 * 用户PF_USER、租户PF_TENANT、工组PF_GROUP
 *
 * @author WRP
 * @since 2019/2/28
 */
public enum ReceiverRangeEnum {

    ALL("all", "全系统", ""),
    TENANT("tenant", "租户", "PF_TENANT"),
    GROUP("group", "工组", "PF_GROUP"),
    USER("user", "用户", "PF_USER");

    /**
     * 值
     */
    private final String value;
    /**
     * 描述
     */
    private final String desc;
    /**
     * 字典表PCODE
     */
    private final String dictPcode;

    ReceiverRangeEnum(String value, String desc, String dictPcode) {
        this.value = value;
        this.desc = desc;
        this.dictPcode = dictPcode;
    }

    /**
     * 根据value获取描述
     *
     * @param value
     * @return
     */
    public static String getDescByValue(String value) {
        for (ReceiverRangeEnum receiverRangeEnum : ReceiverRangeEnum.values()) {
            if (Objects.equals(receiverRangeEnum.getValue(), value)) {
                return receiverRangeEnum.getDesc();
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public String getDictPcode() {
        return dictPcode;
    }

    /**
     * 获取运营发布范围
     *
     * @return
     */
    public static List<GlbDict> getYunYingReceiverRanges() {
        List<GlbDict> receiverRanges = new ArrayList<>();
        for (ReceiverRangeEnum receiverRangeEnum : ReceiverRangeEnum.values()) {
            if (Objects.equals(receiverRangeEnum.getValue(), ReceiverRangeEnum.USER.getValue())) {
                continue;
            }
            receiverRanges.add(new GlbDict(receiverRangeEnum.getValue(),
                    receiverRangeEnum.getDesc(), receiverRangeEnum.getDictPcode()));
        }
        return receiverRanges;
    }

    /**
     * 获取租户发布范围
     *
     * @return
     */
    public static List<GlbDict> getTenantReceiverRanges() {
        List<GlbDict> receiverRanges = new ArrayList<>();
        for (ReceiverRangeEnum receiverRangeEnum : ReceiverRangeEnum.values()) {
            if (Objects.equals(receiverRangeEnum.getValue(), ReceiverRangeEnum.ALL.getValue())
                    || Objects.equals(receiverRangeEnum.getValue(), ReceiverRangeEnum.USER.getValue())) {
                continue;
            }
            receiverRanges.add(new GlbDict(receiverRangeEnum.getValue(),
                    receiverRangeEnum.getDesc(), receiverRangeEnum.getDictPcode()));
        }
        return receiverRanges;
    }

}
