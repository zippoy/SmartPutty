package com.zippoy.zshell.config;

import com.zippoy.zshell.entity.SystemConfig;
import com.sp.ui.MainFrame;
import org.eclipse.swt.graphics.Rectangle;

import java.util.List;

/**
 * <br>
 *
 * @author zippoy
 * @date 2025-06-09
 */
public interface ConfigService {

    String KEY_GENERATOR_EXECUTABLE = "keyGenerator";
    String PLINK_EXECUTABLE = "plink";
    String PUTTY_EXECUTABLE = "putty";
    String WAIT_FOR_INIT_TIME = "waitForInitTime";
    String WINDOW_POSITION_SIZE = "windowPositionSize";
    String VIEW_CONNECTION_BAR = "viewConnectionBar";
    String VIEW_BOTTOM_QUICK_BAR = "viewBottomQuickBar";
    String VIEW_APP_BAR = "viewAppBar";
    String SHOW_WELCOME_PAGE = "showWelcomePage";
    String DICTIONARY_URL = "dictionaryUrl";
    String WINDOWS_BASE_DRIVE = "windowsBaseDrive";
    String USERNAME = "username";
    String PIN = "pin";
    String QOTP = "qotp";
    String PASSPHRASE = "passphrase";

    List<SystemConfig> loadAllConfig();

    Boolean isShowWelcomePage();

    Boolean isViewAppBar();

    Boolean isViewConnectionBar();

    Boolean isViewBottomQuickBar();

    String getPutty();

    String getPlink();

    String getKeyGenerator();

    String getDictionaryBaseUrl();

    Integer getWaitForInitTime();

    String getUsername();

    String getWinPathBaseDrive();

    String getPin();

    String getQotp();

    String getPassphrase();

    Rectangle getWindowPositionSize();

    /**
     * Get main mindow position and size in String format.
     * @return
     */
    default String getWindowPositionSizeString() {
        String x = String.valueOf(MainFrame.shell.getBounds().x);
        String y = String.valueOf(MainFrame.shell.getBounds().y);
        String width = String.valueOf(MainFrame.shell.getBounds().width);
        String height = String.valueOf(MainFrame.shell.getBounds().height);

        return x + "," + y + "," + width + "," + height;
    }
}
