package com.zippoy.zshell;

import com.zippoy.zshell.ui.MainFrame;

import static com.zippoy.zshell.ui.MainFrame.display;
import static com.zippoy.zshell.ui.MainFrame.shell;

/**
 * <br>
 *
 * @author zippoy
 * @date 2025-06-10
 */
public class MainApp {

    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();
        mainFrame.open();

        // 3. 主事件循环
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }

        // 4. 释放资源
        display.dispose();
    }

}
