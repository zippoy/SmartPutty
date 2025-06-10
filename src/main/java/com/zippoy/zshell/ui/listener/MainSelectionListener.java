package com.zippoy.zshell.ui.listener;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolItem;

/**
 * 主选择监听器<br>
 *
 * @author zippoy
 * @date 2025-06-06
 */
public class MainSelectionListener extends SelectionAdapter {
    @Override
    public void widgetSelected(SelectionEvent e) {
        Event event = new Event();
        event.widget = e.widget;
        event.data = e.data;

        if (e.getSource() instanceof MenuItem) {
            handleMenuItem(event);
        } else if (e.getSource() instanceof ToolItem) {
            handleToolItem(event);
        } else if (e.getSource() instanceof Button) {
            handleButton(event);
        }
    }

    private void handleMenuItem(Event event) {
        // 处理菜单项点击
    }

    private void handleToolItem(Event event) {
        // 处理工具栏项点击
    }

    private void handleButton(Event event) {
        // 处理按钮点击
    }
}
