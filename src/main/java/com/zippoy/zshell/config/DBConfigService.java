package com.zippoy.zshell.config;

import com.sp.dao.SmartSessionManager;
import com.zippoy.zshell.entity.SystemConfig;
import com.zippoy.zshell.model.ConstantValue;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.swt.graphics.Rectangle;

import java.util.List;

@Slf4j
public class DBConfigService implements ConfigService {

    private SmartSessionManager smartSessionManager;

    public DBConfigService() {
        smartSessionManager = new SmartSessionManager();
        init();
    }

    public void init() {
        List<SystemConfig> allSystemConfigs = loadAllConfig();
        if (!hasKey(allSystemConfigs, KEY_GENERATOR_EXECUTABLE)) {
            log.debug("创建系统配置：" + KEY_GENERATOR_EXECUTABLE);
            smartSessionManager.save(new SystemConfig(KEY_GENERATOR_EXECUTABLE, "app\\putty\\puttygen.exe"));
        }
        if (!hasKey(allSystemConfigs, PLINK_EXECUTABLE)) {
            log.debug("创建系统配置：" + PLINK_EXECUTABLE);
            smartSessionManager.save(new SystemConfig(PLINK_EXECUTABLE, "app\\putty\\plink.exe"));
        }
        if (!hasKey(allSystemConfigs, PUTTY_EXECUTABLE)) {
            log.debug("创建系统配置：" + PUTTY_EXECUTABLE);
            smartSessionManager.save(new SystemConfig(PUTTY_EXECUTABLE, "app\\putty\\putty.exe"));
        }
        if (!hasKey(allSystemConfigs, WAIT_FOR_INIT_TIME)) {
            log.debug("创建系统配置：" + WAIT_FOR_INIT_TIME);
            smartSessionManager.save(new SystemConfig(WAIT_FOR_INIT_TIME, "200"));
        }
        if (!hasKey(allSystemConfigs, WINDOW_POSITION_SIZE)) {
            log.debug("创建系统配置：" + WINDOW_POSITION_SIZE);
            smartSessionManager.save(new SystemConfig(WINDOW_POSITION_SIZE, "100,100,1200,900"));
        }
        if (!hasKey(allSystemConfigs, VIEW_CONNECTION_BAR)) {
            log.debug("创建系统配置：" + VIEW_CONNECTION_BAR);
            smartSessionManager.save(new SystemConfig(VIEW_CONNECTION_BAR, "true"));
        }
        if (!hasKey(allSystemConfigs, VIEW_BOTTOM_QUICK_BAR)) {
            log.debug("创建系统配置：" + VIEW_BOTTOM_QUICK_BAR);
            smartSessionManager.save(new SystemConfig(VIEW_BOTTOM_QUICK_BAR, "true"));
        }
        if (!hasKey(allSystemConfigs, SHOW_WELCOME_PAGE)) {
            log.debug("创建系统配置：" + SHOW_WELCOME_PAGE);
            smartSessionManager.save(new SystemConfig(SHOW_WELCOME_PAGE, "false"));
        }
        if (!hasKey(allSystemConfigs, DICTIONARY_URL)) {
            log.debug("创建系统配置：" + DICTIONARY_URL);
            smartSessionManager.save(new SystemConfig(DICTIONARY_URL, "http://dict.youdao.com/w/eng/"));
        }
        if (!hasKey(allSystemConfigs, WINDOWS_BASE_DRIVE)) {
            log.debug("创建系统配置：" + WINDOWS_BASE_DRIVE);
            smartSessionManager.save(new SystemConfig(WINDOWS_BASE_DRIVE, "c:"));
        }
        if (!hasKey(allSystemConfigs, VIEW_APP_BAR)) {
            log.debug("创建系统配置：" + VIEW_APP_BAR);
            smartSessionManager.save(new SystemConfig(VIEW_APP_BAR, "true"));
        }
        if (!hasKey(allSystemConfigs, USERNAME)) {
            log.debug("创建系统配置：" + USERNAME);
            smartSessionManager.save(new SystemConfig(USERNAME, ""));
        }
    }

    @Override
    public List<SystemConfig> loadAllConfig() {
        return smartSessionManager.getAllSystemConfigs();
    }

    public boolean hasKey(List<SystemConfig> list, String key) {
        for (SystemConfig config : list) {
            if (config.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }

    public String getSystemValue(String key) {
        SystemConfig conf = smartSessionManager.getAllSystemConfigs().stream().filter(e -> e.getKey().equals(key)).findAny().orElse(null);
        return conf.getValue();
    }

    public Boolean getSystemValueBoolean(String key) {
        String val = getSystemValue(key);
        return Boolean.valueOf(val);
    }

    /**
     * Utilities bar must be visible?
     *
     * @return
     */
    @Override
    public Boolean isViewAppBar() {
        return getSystemValueBoolean(VIEW_APP_BAR);
    }

    /**
     * Connection bar must be visible?
     *
     * @return
     */
    @Override
    public Boolean isViewConnectionBar() {
        return getSystemValueBoolean(VIEW_CONNECTION_BAR);
    }

    /**
     * Bottom Quick Bar be Visible  ?
     *
     * @return
     */
    @Override
    public Boolean isViewBottomQuickBar() {
        return getSystemValueBoolean(VIEW_BOTTOM_QUICK_BAR);
    }

    /**
     * Get Putty/KiTTY executable path.
     *
     * @return
     */
    @Override
    public String getPutty() {
        return getSystemValue(PUTTY_EXECUTABLE);
        //		return StringUtils.isEmpty(value) ? Program.DEFAULT_APP_PUTTY.getPath() : value;
    }

    /**
     * Get Plink/Klink executable path.
     *
     * @return
     */
    @Override
    public String getPlink() {
        return getSystemValue(PLINK_EXECUTABLE);
        //		return StringUtils.isEmpty(value) ? Program.DEFAULT_APP_PLINK.getPath() : value;
    }

    /**
     * Get key generator executable path.
     *
     * @return
     */
    @Override
    public String getKeyGenerator() {
        return getSystemValue(KEY_GENERATOR_EXECUTABLE);
        //		return StringUtils.isEmpty(value) ? Program.DEFAULT_APP_KEYGEN.getPath() : value;
    }

    /**
     * Get dictionary baseUrl, I put dict.youdao.com as a chines-english dictionary. User can customize it as to his own dict url
     *
     * @return
     */
    @Override
    public String getDictionaryBaseUrl() {
        return getSystemValue(DICTIONARY_URL);
    }

    @Override
    public Integer getWaitForInitTime() {
        return Integer.parseInt(getSystemValue(WAIT_FOR_INIT_TIME));
    }

    /**
     * user can customize his username, in most case user may using his own username to login multiple linux, so provide a centralized username entry for user
     *
     * @return
     */
    @Override
    public String getUsername() {
        return getSystemValue(USERNAME);
    }

    /**
     * customize win path base prefix when converting path from linux and windows
     *
     * @return
     */
    @Override
    public String getWinPathBaseDrive() {
        return getSystemValue(WINDOWS_BASE_DRIVE);
    }

    /**
     * get welcome visible config
     *
     * @return
     */
    @Override
    public Boolean isShowWelcomePage() {
        return getSystemValueBoolean(SHOW_WELCOME_PAGE);
    }

    @Override
    public String getPin() {
        return "";
    }

    @Override
    public String getOtp() {
        return "";
    }

    @Override
    public String getPassphrase() {
        return "";
    }

    /**
     * Get main mindow position and size.
     *
     * @return
     */
    @Override
    public Rectangle getWindowPositionSize() {
        // Split comma-separated values by x, y, width, height:
        String[] array = getSystemValue(WINDOW_POSITION_SIZE).split(",");
        //		String[] array = ((String) prop.get("windowPositionSize")).split(",");

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
