package com.zippoy.zshell.model;

import lombok.Getter;

/**
 * <br>
 *
 * @author zippoy
 * @date 2025-06-06
 */
@Getter
public enum JumpHost {

    CN0("l-rtools1.ops.cn0"),
    CN1("l-rtools1.ops.cn1"),
    CN2("l-rtools1.ops.cn2"),
    CN5("l-rtools1.ops.cn5"),
    CN6("l-rtools1.ops.cn6"),
    CN8("l-rtools1.ops.cn8"),
    CNA("l-rtools1.ops.cna"),
    CNB("l-rtools1.ops.cnb"),
    ;

    private final String host;

    JumpHost(String host) {
        this.host = host;
    }
}
