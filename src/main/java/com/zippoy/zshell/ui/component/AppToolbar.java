package com.zippoy.zshell.ui.component;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * 实用工具工具栏<br>
 *
 * @author zippoy
 * @date 2025-06-06
 */
public class AppToolbar {

    private final ToolBar appToolbar;
    private final ToolItem newItem;
    private final ToolItem openItem;
    private final ToolItem remoteDeskItem;
    private final ToolItem captureItem;
    private final ToolItem calculatorItem;
    private final ToolItem vncItem;
    private final ToolItem notePadItem;
    private final ToolItem keyGenItem;
    private final ToolItem helpItem;

    public AppToolbar(Shell shell) {
        appToolbar = new ToolBar(shell, SWT.VERTICAL);

        newItem = createToolItem(appToolbar, SWT.PUSH, MImage.newImage, "新建", "创建新会话");
        openItem = createToolItem(appToolbar, SWT.PUSH, MImage.openImage, "打开", "打开现有会话");
        remoteDeskItem = createToolItem(appToolbar, SWT.PUSH, MImage.RemoteDeskImage, "远程桌面", "打开系统远程桌面工具");
        captureItem = createToolItem(appToolbar, SWT.PUSH, MImage.captureImage, "截屏", "打开 FastStone Capture");
        calculatorItem = createToolItem(appToolbar, SWT.PUSH, MImage.calculator, "计算器", "打开系统计算器");
        vncItem = createToolItem(appToolbar, SWT.PUSH, MImage.vncImage, "VNC", "打开VNC");
        notePadItem = createToolItem(appToolbar, SWT.PUSH, MImage.notepad, "记事本", "打开记事本");
        keyGenItem = createToolItem(appToolbar, SWT.PUSH, MImage.key, "KenGen", "转换 SSH Key");
        helpItem = createToolItem(appToolbar, SWT.PUSH, MImage.helpImage, "帮助", "帮助文档");

        appToolbar.pack();
    }

    /**
     * 创建ToolItem
     */
    private ToolItem createToolItem(ToolBar parent, int style, Image image, String text, String tipText) {
        ToolItem item = new ToolItem(parent, style);
        item.setText(text);
        item.setToolTipText(tipText);
        item.setImage(image);
        return item;
    }

    public void setVisible(boolean visible) {
        appToolbar.setVisible(visible);
    }

    public void setLayoutData(Object layoutData) {
        appToolbar.setLayoutData(layoutData);
    }

}