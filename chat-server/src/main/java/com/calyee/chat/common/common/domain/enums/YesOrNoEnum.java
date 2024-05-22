package com.calyee.chat.common.common.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @projectName: calyeechat
 * @package: com.calyee.chat.common.common.domain.enums
 * @className: YesOrNoEnum
 * @author: Caluee
 * @description: 是或否 的公共枚举
 * @date: 2024/03/12 012 19:43
 * @version: 1.0
 */

@AllArgsConstructor
@Getter
public enum YesOrNoEnum {
    NO(0, "否"),
    YES(1, "是"),
    ;
    private final Integer status;
    private final String desc;

    private static Map<Integer, YesOrNoEnum> cache;

    static {
        cache = Arrays.stream(YesOrNoEnum.values()).collect(Collectors.toMap(YesOrNoEnum::getStatus, Function.identity()));
    }

    public static YesOrNoEnum of(Integer type) {
        return cache.get(type);
    }

    public static Integer toStatus(Boolean bool) {
        return bool ? YES.getStatus() : NO.getStatus();
    }

}
