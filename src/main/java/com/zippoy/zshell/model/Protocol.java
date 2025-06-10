package com.zippoy.zshell.model;

import lombok.Getter;

/**
 * Protocols allowed by "Putty" or "KiTTY".
 *
 * @author Carlos SS
 */
@Getter
public enum Protocol {

    // Following order is also used on lists to be showed:
    SSH2("SSH2", "-ssh -2"),
    SSH("SSH", "-ssh -1"),
    TELNET("Telnet", "-telnet"),
    RLOGIN("Rlogin", "-rlogin"),
    RAW("Raw", "-raw"),
    SERIAL("Serial", "-serial");

    /**
     * Visible name
     */
    private final String name;
    /**
     * Command-line parameter to be passed to "Putty" or "KiTTY"
     */
    private final String parameter;

    Protocol(String name, String parameter) {
        this.name = name;
        this.parameter = parameter;
    }

}
