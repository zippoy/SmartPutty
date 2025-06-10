package com.zippoy.zshell.util;

import com.bastiaanjansen.otp.TOTPGenerator;
import com.zippoy.zshell.config.ConfigServiceFactory;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * <br>
 *
 * @author zippoy
 * @date 2025-06-09
 */
public class TotpUtil {

    private static TOTPGenerator totpGenerator;

    static {
        try {
            totpGenerator = TOTPGenerator.fromURI(new URI(ConfigServiceFactory.getConfigService().getOtp()));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getCurrentOtp() {
        // Based on current time
        return totpGenerator.now();
    }

}
