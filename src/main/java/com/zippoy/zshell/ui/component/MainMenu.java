package com.zippoy.zshell.ui.component;

import com.zippoy.zshell.config.ConfigService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

/**
 * 主菜单<br>
 *
 * @author zippoy
 * @date 2025-06-06
 */
public class MainMenu implements SelectionListener {

    private final Shell shell;
    private final ConfigService configService;
    private final Menu menuBar;

    private final MenuItem fileMenu;
    private final MenuItem newItem, openItem, exitItem;

    private final MenuItem viewMenu;
    private final MenuItem utilitiesBarItem, connectionBarItem, bottomQuickBarItem;

    private final MenuItem configMenu;
    private final MenuItem programConfigItem;

    private final MenuItem appMenu;
    private final MenuItem remoteDesktopItem, captureItem, calculatorItem, vncItem, notePadItem, keyGenItem;

    private final MenuItem aboutMenu;
    private final MenuItem welcomeItem, updateItem;

    public MainMenu(Shell shell, ConfigService configService) {
        this.shell = shell;
        this.configService = configService;

        menuBar = new Menu(shell, SWT.BAR);
        shell.setMenuBar(menuBar);

        // 文件菜单
        fileMenu = createMenuItem("文件", SWT.CASCADE);
        Menu fileSubMenu = createSubMenu(fileMenu);
        newItem = createMenuItem(fileSubMenu, "新建\tCtrl+N", MImage.newImage, SWT.CTRL + 'N');
        openItem = createMenuItem(fileSubMenu, "打开\tCtrl+O", MImage.openImage, SWT.CTRL + 'O');
        new MenuItem(fileSubMenu, SWT.SEPARATOR);
        exitItem = createMenuItem(fileSubMenu, "退出\tCtrl+X", null, SWT.CTRL + 'X');

        // 视图菜单
        viewMenu = createMenuItem("视图", SWT.CASCADE);
        Menu viewSubMenu = createSubMenu(viewMenu);
        utilitiesBarItem = createCheckItem(viewSubMenu, "实用程序栏", configService.isViewAppBar());
        connectionBarItem = createCheckItem(viewSubMenu, "连接栏", configService.isViewConnectionBar());
        bottomQuickBarItem = createCheckItem(viewSubMenu, "底部快捷栏", configService.isViewBottomQuickBar());


        // 设置菜单
        configMenu = createMenuItem("设置", SWT.CASCADE);
        Menu configSubMenu = createSubMenu(configMenu);
        programConfigItem = createMenuItem(configSubMenu, "程序配置\tCtrl+P", MImage.configImage, SWT.CTRL + 'P');

        // 应用菜单
        appMenu = createMenuItem("应用", SWT.CASCADE);
        Menu appSubMenu = createSubMenu(appMenu);
        remoteDesktopItem = createMenuItem(appSubMenu, "远程桌面\tCtrl+R", MImage.RemoteDeskImage, SWT.CTRL + 'R');
        captureItem = createMenuItem(appSubMenu, "截屏\tCtrl+C", MImage.captureImage, SWT.CTRL + 'C');
        calculatorItem = createMenuItem(appSubMenu, "计算器", MImage.calculator, SWT.NONE);
        vncItem = createMenuItem(appSubMenu, "VNC", MImage.vncImage, SWT.NONE);
        notePadItem = createMenuItem(appSubMenu, "记事本", MImage.notepad, SWT.NONE);
        keyGenItem = createMenuItem(appSubMenu, "KenGen", MImage.key, SWT.NONE);

        // 关于菜单
        aboutMenu = createMenuItem("关于", SWT.CASCADE);
        Menu aboutSubMenu = createSubMenu(aboutMenu);
        welcomeItem = createMenuItem(aboutSubMenu, "欢迎页", null, SWT.NONE);
        updateItem = createMenuItem(aboutSubMenu, "更新", null, SWT.NONE);
    }

    @Override
    public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
        // 处理菜单选择事件
    }

    @Override
    public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
        // do nothing
    }

    private MenuItem createMenuItem(String text, int style) {
        MenuItem item = new MenuItem(menuBar, style);
        item.setText(text);
        return item;
    }

    private Menu createSubMenu(MenuItem parent) {
        Menu menu = new Menu(shell, SWT.DROP_DOWN);
        parent.setMenu(menu);
        return menu;
    }

    private MenuItem createMenuItem(Menu parent, String text, Image image, int accelerator) {
        MenuItem item = new MenuItem(parent, SWT.PUSH);
        item.setText(text);
        item.setImage(image);
        item.setAccelerator(accelerator);
        item.addSelectionListener(this);
        return item;
    }

    private MenuItem createCheckItem(Menu parent, String text, boolean checked) {
        MenuItem item = new MenuItem(parent, SWT.CHECK);
        item.setText(text);
        item.setSelection(checked);
        item.addSelectionListener(this);
        return item;
    }

}
