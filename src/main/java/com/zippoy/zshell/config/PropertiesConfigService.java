package com.zippoy.zshell.config;

import com.zippoy.zshell.entity.SystemConfig;
import com.zippoy.zshell.model.ConstantValue;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.swt.graphics.Rectangle;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * <br>
 *
 * @author zippoy
 * @date 2025-06-09
 */
@Slf4j
public class PropertiesConfigService implements ConfigService {

    private Properties props;

    public PropertiesConfigService() {
        init();
    }

    public void init() {
        props = new Properties();
        try (InputStream in = PropertiesConfigService.class.getResourceAsStream("/config/config.properties")) {
            // 加载属性文件
            props.load(in);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public List<SystemConfig> loadAllConfig() {
        return Collections.emptyList();
    }

    public Boolean getBooleanVal(String key) {
        return Boolean.valueOf(props.getProperty(key));
    }

    @Override
    public Boolean isShowWelcomePage() {
        return getBooleanVal(SHOW_WELCOME_PAGE);
    }

    @Override
    public Boolean isViewAppBar() {
        return getBooleanVal(VIEW_APP_BAR);
    }

    @Override
    public Boolean isViewConnectionBar() {
        return getBooleanVal(VIEW_CONNECTION_BAR);
    }

    @Override
    public Boolean isViewBottomQuickBar() {
        return getBooleanVal(VIEW_BOTTOM_QUICK_BAR);
    }

    @Override
    public String getPutty() {
        return props.getProperty(PUTTY_EXECUTABLE);
    }

    @Override
    public String getPlink() {
        return props.getProperty(PLINK_EXECUTABLE);
    }

    @Override
    public String getKeyGenerator() {
        return props.getProperty(KEY_GENERATOR_EXECUTABLE);
    }

    @Override
    public String getDictionaryBaseUrl() {
        return props.getProperty(DICTIONARY_URL);
    }

    @Override
    public Integer getWaitForInitTime() {
        return Integer.parseInt(props.getProperty(WAIT_FOR_INIT_TIME));
    }

    @Override
    public String getUsername() {
        return props.getProperty(USERNAME);
    }

    @Override
    public String getWinPathBaseDrive() {
        return props.getProperty(WINDOWS_BASE_DRIVE);
    }

    @Override
    public String getPin() {
        return props.getProperty(PIN, "");
    }

    @Override
    public String getQotp() {
        return props.getProperty(QOTP);
    }

    @Override
    public String getPassphrase() {
        return props.getProperty(PASSPHRASE);
    }

    @Override
    public Rectangle getWindowPositionSize() {
        // Split comma-separated values by x, y, width, height:
        String[] array = props.getProperty(WINDOW_POSITION_SIZE).split(",");

        // If there aren't enough pieces of information...
        if (array.length < 4) {
            array = new String[4];

            // Set default safety values:
            array[0] = String.valueOf(ConstantValue.SCREEN_WIDTH / 6);
            array[1] = String.valueOf(ConstantValue.SCREEN_HEIGHT / 6);
            array[2] = String.valueOf(2 * ConstantValue.SCREEN_WIDTH / 3);
            array[3] = String.valueOf(2 * ConstantValue.SCREEN_HEIGHT / 3);
        }
        return new Rectangle(Integer.parseInt(array[0]), Integer.parseInt(array[1]), Integer.parseInt(array[2]), Integer.parseInt(array[3]));
    }

}
