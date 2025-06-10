package com.zippoy.zshell.entity;

import com.zippoy.zshell.model.ConstantValue;
import com.zippoy.zshell.model.Protocol;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Session variables to establish a communication with a SSH client.
 */
@Entity
@Data
@NoArgsConstructor
public class ConfigSession implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * 登陆类型：1-PURE_PUTTY_SESSION，2-SMART_PUTTY_SESSION, 3-OTP_PUTTY_SESSION
     */
    private int sessionType;
    private String host;
    /**
     * Can be a number or a device name (COM1, COM2, ...).
     */
    private String port;
    private String username;
    private String password;
    private Protocol protocol;
    private String file;
    private String puttySession;

    public ConfigSession(String username, String password, String port, String puttySession) {
        this.username = username;
        this.password = password;
        this.port = port;
        this.puttySession = puttySession;
        this.sessionType = ConstantValue.PURE_PUTTY_SESSION;
    }

    /**
     * SmartPutty Session constructor
     *
     * @param host
     * @param port
     * @param username
     * @param password
     * @param protocol
     * @param file
     */
    public ConfigSession(String host, String port, String username, String password, Protocol protocol, String file) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.protocol = protocol;
        this.file = file;
        this.sessionType = ConstantValue.SMART_PUTTY_SESSION;
    }

    /**
     * otpPutty Session constructor
     *
     * @param host
     * @param port
     * @param username
     * @param password
     * @param protocol
     */
    public ConfigSession(String host, String port, String username, String password, Protocol protocol) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.protocol = protocol;
        this.sessionType = ConstantValue.OTP_PUTTY_SESSION;
    }

}
