package com.zippoy.zshell.ui.component;

import com.zippoy.zshell.control.InvokeProgram;
import com.zippoy.zshell.entity.ConfigSession;
import com.zippoy.zshell.config.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Listener;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * 标签页管理<br>
 *
 * @author zippoy
 * @date 2025-06-06
 */
@Slf4j
public class TabManager implements CTabFolder2Listener, MouseListener, ShellListener, SelectionListener {
    private final Shell shell;
    private final ConfigService configService;
    private final Display display;

    private final CTabFolder folder;

    private final WelcomeTab welcomeTab;

    public TabManager(Shell shell, ConfigService configService, Display display) {
        this.shell = shell;
        this.configService = configService;
        this.display = display;

        folder = new CTabFolder(shell, SWT.BORDER);
        folder.setSimple(false);
        folder.setUnselectedCloseVisible(false);
        folder.addCTabFolder2Listener(this);
        folder.addMouseListener(this);
        folder.addSelectionListener(this);

        welcomeTab = new WelcomeTab(folder, configService);
    }


    public void setLayoutData(Object layoutData) {
        folder.setLayoutData(layoutData);
    }

    public void showWelcomeTab(String url) {
        if (Display.getCurrent() != null) {
            // 当前是UI线程，直接执行
            doShowWelcomeTab(url);
        } else {
            // 切换到UI线程
            display.asyncExec(() -> doShowWelcomeTab(url));
        }
    }

    private void doShowWelcomeTab(String url) {
        if (folder.isDisposed()) {
            return;
        }
        welcomeTab.show(url);
        // 实际的UI操作...
    }

    public void reloadSession(CTabItem item, ConfigSession session) {
        if (item == null) {
            item = new CTabItem(folder, SWT.CLOSE);
        }

        Composite composite = new Composite(folder, SWT.EMBEDDED);
        composite.setBackground(new Color(display, 0, 0, 0));
        item.setControl(composite);
        item.setData("TYPE", "session");
        folder.setSelection(item);
        item.setText("connecting");
        item.setImage(MImage.puttyImage);
        Thread t = new InvokeProgram(composite, display, item, session, configService);
        t.start();
    }

    public void addSession(ConfigSession session) {
        reloadSession(null, session);
    }

    @Override
    public void widgetSelected(SelectionEvent e) {
        if (e.getSource() == folder) {
            if (folder.getSelection().getData("hwnd") != null) {
                int hwnd = (Integer) folder.getSelection().getData("hwnd");
                InvokeProgram.setWindowFocus(hwnd);
            }
        }
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e) {

    }

    @Override
    public void close(CTabFolderEvent event) {

    }

    @Override
    public void minimize(CTabFolderEvent event) {

    }

    @Override
    public void maximize(CTabFolderEvent event) {

    }

    @Override
    public void restore(CTabFolderEvent event) {

    }

    @Override
    public void showList(CTabFolderEvent event) {

    }

    @Override
    public void shellActivated(ShellEvent e) {

    }

    @Override
    public void shellClosed(ShellEvent e) {

    }

    @Override
    public void shellDeactivated(ShellEvent e) {

    }

    @Override
    public void shellDeiconified(ShellEvent e) {

    }

    @Override
    public void shellIconified(ShellEvent e) {

    }

    @Override
    public void mouseDoubleClick(MouseEvent e) {

    }

    @Override
    public void mouseDown(MouseEvent e) {

    }

    @Override
    public void mouseUp(MouseEvent e) {

    }


}