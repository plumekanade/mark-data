package com.plumekanade.mark.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 17名单库来源枚举
 * @author kanade
 * @date 2023-12-19
 */
@Getter
@AllArgsConstructor
public enum CompanyRosterSourceEnum {
    MARK("马克数据网"),
    BF("八方资源网")
    ;

    private final String name;
}
