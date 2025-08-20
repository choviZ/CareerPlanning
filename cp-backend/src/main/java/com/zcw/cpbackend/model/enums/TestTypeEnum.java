package com.zcw.cpbackend.model.enums;

import cn.hutool.core.util.ObjUtil;

/**
 * 测评类型枚举
 */
public enum TestTypeEnum {

    MBTI("MBTI测试","MBTI");

    private final String text;
    private final String value;

    TestTypeEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value 枚举值的value
     * @return 枚举值
     */
    public static TestTypeEnum getEnumByValue(String value) {
        if (ObjUtil.isEmpty(value)) {
            return null;
        }
        for (TestTypeEnum anEnum : TestTypeEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

}
