package com.zippoy.zshell.config;

/**
 * <br>
 *
 * @author zippoy
 * @date 2025-06-09
 */
public class ConfigServiceFactory {

    private static ConfigService configService;

    public static ConfigService getConfigService() {
        if (configService == null) {
            synchronized (ConfigServiceFactory.class) {
                configService = new PropertiesConfigService();
            }
        }
        return configService;
    }

}
