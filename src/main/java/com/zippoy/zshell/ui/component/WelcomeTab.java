package com.zippoy.zshell.ui.component;

import com.zippoy.zshell.config.ConfigService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;

/**
 * 欢迎页<br>
 *
 * @author zippoy
 * @date 2025-06-06
 */
public class WelcomeTab {
    private final CTabItem welcomeItem;
    private final Browser browser;

    public WelcomeTab(CTabFolder folder, ConfigService configService) {
        welcomeItem = new CTabItem(folder, SWT.CLOSE);
        browser = new Browser(folder, SWT.NONE);
        welcomeItem.setControl(browser);
        welcomeItem.setText("欢迎页");
    }

    public void show(String url) {
        browser.setUrl(url);
        welcomeItem.getParent().setSelection(welcomeItem);
    }
}