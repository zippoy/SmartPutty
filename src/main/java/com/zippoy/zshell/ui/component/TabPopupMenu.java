package com.zippoy.zshell.ui.component;

import com.zippoy.zshell.config.ConfigService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

/**
 * 选项卡弹出菜单<br>
 *
 * @author zippoy
 * @date 2025-06-06
 */
public class TabPopupMenu implements SelectionListener {

    private final Shell shell;
    private final ConfigService configService;
    private final Menu popupmenu;

    private final MenuItem reloadPopItem;
    private final MenuItem clonePopItem;
    private final MenuItem vncPopItem;

    private final MenuItem transferPopItem;
    private final MenuItem ftpMenuItem, scpMenuItem, sftpMenuItem;

    public TabPopupMenu(Shell shell, ConfigService configService) {
        this.shell = shell;
        this.configService = configService;

        popupmenu = new Menu(shell, SWT.POP_UP);

        reloadPopItem = createMenuItem(popupmenu, SWT.PUSH, "重新加载会话", MImage.reloadImage, SWT.NONE);
        // openPuttyItem = createMenuItem(popupmenu, "在putty打开", MImage.puttyImage, SWT.NONE);
        //		 openPuttyItem = new MenuItem(popupmenu, SWT.PUSH);
        //		 openPuttyItem.setText("open in putty");
        //		 openPuttyItem.setImage(MImage.puttyImage);
        //		 // openPuttyItem.setToolTipText("Opens connection on a single window");
        //		 openPuttyItem.addSelectionListener(this);

        clonePopItem = createMenuItem(popupmenu, SWT.PUSH, "克隆会话", MImage.cloneImage, SWT.NONE);
        vncPopItem = createMenuItem(popupmenu, SWT.PUSH, "VNC", MImage.vncImage, SWT.NONE);


        transferPopItem = createMenuItem(popupmenu, SWT.CASCADE, "传输文件", MImage.transferImage, SWT.NONE);
        Menu subMenu = new Menu(popupmenu);
        ftpMenuItem = createMenuItem(subMenu, SWT.PUSH, "FTP", null, SWT.NONE);
        scpMenuItem = createMenuItem(subMenu, SWT.PUSH, "SCP", null, SWT.NONE);
        sftpMenuItem = createMenuItem(subMenu, SWT.PUSH, "SFTP", null, SWT.NONE);
        transferPopItem.setMenu(subMenu);
    }


    @Override
    public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
        // 处理菜单选择事件
    }

    @Override
    public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
        // do nothing
    }

    private MenuItem createMenuItem(Menu parent, int style, String text, Image image, int accelerator) {
        return createMenuItem(parent, style, text, image, accelerator, "");
    }

    private MenuItem createMenuItem(Menu parent, int style, String text, Image image, int accelerator, String tipText) {
        MenuItem item = new MenuItem(parent, style);
        item.setText(text);
        item.setImage(image);
        item.setAccelerator(accelerator);
        if((style | SWT.CASCADE) != SWT.CASCADE) {
            item.addSelectionListener(this);
        }
        item.setToolTipText(tipText);
        return item;
    }

}
