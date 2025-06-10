package com.zippoy.zshell.control;

import com.zippoy.zshell.entity.ConfigSession;
import com.zippoy.zshell.model.ConstantValue;
import com.zippoy.zshell.model.Program;
import com.zippoy.zshell.config.ConfigService;
import com.zippoy.zshell.config.ConfigServiceFactory;
import com.zippoy.zshell.ui.MainFrame;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.internal.win32.SHELLEXECUTEINFO;
import org.eclipse.swt.internal.win32.TCHAR;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

@Slf4j
public class InvokeProgram extends Thread {

    private final Display display;
    private final Composite composite;
    private final ConfigSession session;
    private final CTabItem tabItem;
    private final ConfigService configService;

    // Constructor:
    public InvokeProgram(Composite composite, Display display, CTabItem tabItem, ConfigSession session, ConfigService configService) {
        this.composite = composite;
        this.display = display;
        this.tabItem = tabItem;
        this.session = session;
        this.configService = configService;

    }

    @Override
    public void run() {
        this.display.syncExec(() -> invokePutty(session));
    }

    public static void setWindowFocus(int hwnd) {
        // OS.SendMessage(hwnd, OS.WM_SETFOCUS, 0, 0);
        // OS.SetCapture(hwnd);
        //        OS.SetForegroundWindow(hwnd);
        //        OS.SetCursor(hwnd);
    }

    public static void setMainWindowFocus() {
        int hwndMainWindow = isMainWindowRunning();
        setWindowFocus(hwndMainWindow);
    }

    public static int isMainWindowRunning() {
        return (int) OS.FindWindow(null, new TCHAR(0, ConstantValue.MAIN_WINDOW_TITLE, true));
    }

    /**
     * Helper to mount Putty command-line parameters.
     *
     * @return
     */
    private static String setPuttyParameters(ConfigSession session) {
        String args = "";

        String host = session.getHost();
        String port = session.getPort();
        String user = session.getUsername();
        String password = session.getPassword();
        String file = session.getFile();
        String protocol = session.getProtocol() == null ? "-ssh -2" : session.getProtocol().getParameter();
        String puttySession = session.getPuttySession();

        if (session.getSessionType() == ConstantValue.PURE_PUTTY_SESSION) {
            // Putty session must the very first parameter to work well.
            args = " -load \"" + puttySession + "\"";
            if (!user.isEmpty()) {
                args += String.format(" -l \"%s\"", user);
            }
            if (!password.isEmpty()) {
                args += String.format(" -pw \"%s\"", password);
            }
            if (!port.isEmpty()) {
                args += String.format(" -P %s ", port);
            }
        } else {
            args = String.format(" %s %s -l %s ", protocol, host, user);

            if (!password.isEmpty()) {
                args += String.format(" -pw \"%s\"", password);
            }
            // private key
            if (!file.isEmpty()) {
                args += String.format(" -i \"%s\"", file);
            }
            if (!port.isEmpty()) {
                args += String.format(" -P %s ", port);
            }

        }
        log.debug("Putty parameters: {}", args);

        return args;
    }

    /**
     * Start Putty in a tab.
     *
     * @param session
     */
    public void invokePutty(ConfigSession session) {
        // Mount command-line Putty parameters:
        String args = setPuttyParameters(session);

        String puttyPath = ConfigServiceFactory.getConfigService().getPutty();

        int hHeap = (int) OS.GetProcessHeap();
        TCHAR buffer = new TCHAR(0, puttyPath, true);
        int byteCount = buffer.length() * TCHAR.sizeof;
        int lpFile = (int) OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
        TCHAR buffer1 = new TCHAR(0, args, true);
        int byteCount1 = buffer1.length() * TCHAR.sizeof;
        int lpParameters = (int) OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, byteCount1);
        log.debug("Putty:{}, args:{}", puttyPath, args);
        OS.MoveMemory(lpFile, buffer, byteCount);
        OS.MoveMemory(lpParameters, buffer1, byteCount1);

        SHELLEXECUTEINFO info = new SHELLEXECUTEINFO();
        info.cbSize = SHELLEXECUTEINFO.sizeof;
        info.lpFile = lpFile;
        info.lpParameters = lpParameters;

        // info.nShow = OS.SW_HIDE;

        boolean result = OS.ShellExecuteEx(info);

        if (lpFile != 0) {
            OS.HeapFree(hHeap, 0, lpFile);
        }

        if (lpParameters != 0) {
            OS.HeapFree(hHeap, 0, lpParameters);
        }

        if (result == false) {
            MessageDialog.openInformation(MainFrame.shell, "OPEN PUTTY ERROR", String.format("Failed cmd: %s %s", puttyPath, args));
            return;
        }

        int hwndalert = (int) OS.FindWindow(null, new TCHAR(0, "PuTTY Security Alert", true));
        if (hwndalert != 0) {
            int waitingForOperation = 10000;
            while (waitingForOperation > 0) {
                try {
                    if (OS.FindWindow(null, new TCHAR(0, "PuTTY Security Alert", true)) == 0) {
                        break;
                    }

                    Thread.sleep(500);
                    waitingForOperation -= 500;
                } catch (InterruptedException e) {
                    log.error(ExceptionUtils.getStackTrace(e));
                }
            }
        }

        int count = 15;
        int hwnd = 0;
        while (count > 0 && (hwnd = (int) OS.FindWindow(new TCHAR(0, "PuTTY", true), null)) == 0) {
            int waitingTime = configService.getWaitForInitTime();
            try {
                Thread.sleep(waitingTime);
            } catch (InterruptedException e) {
                log.error(ExceptionUtils.getStackTrace(e));
            }
            count--;
        }
        if (count == 0) {
            // This doesn't work, but I don't know why!
            // MessageDialog.openError(MainFrame.shell, "OPEN PUTTY ERROR",
            //	String.format("Failed cmd: %s %s", MainFrame.configuration.getPuttyExecutable(), args));
            MessageBox messagebox = new MessageBox(MainFrame.shell, SWT.ICON_INFORMATION | SWT.OK);
            messagebox.setText("打开 PUTTY 错误");
            messagebox.setMessage(configService.getPutty() + args);
            if (messagebox.open() == SWT.OK) {
                MainFrame.shell.setFocus();
            }
        }
        int oldStyle = OS.GetWindowLong(hwnd, OS.GWL_STYLE);
        OS.SetWindowLong(hwnd, OS.GWL_STYLE, oldStyle & ~OS.WS_BORDER);

        OS.SetParent(hwnd, composite.handle);
        OS.SendMessage(hwnd, OS.WM_SYSCOMMAND, OS.SC_MAXIMIZE, 0);

        String tabDisplayName = session.getSessionType() == ConstantValue.PURE_PUTTY_SESSION ? session.getPuttySession() : session.getHost();
        if (hwnd != 0) {
            tabItem.setText(tabDisplayName);
            tabItem.setData("hwnd", hwnd);
            tabItem.setData("session", session);
            setWindowFocus(hwnd);
        } else {
            tabItem.dispose();
        }
    }

    public static void killProcess(int hwnd) {
        OS.SendMessage(hwnd, OS.WM_CLOSE, null, 0);
    }

    public static void killPuttyWarningsAndErrs() {
        int hwndalert = (int) OS.FindWindow(null, new TCHAR(0, "PuTTY Security Alert", true));
        if (hwndalert != 0) {
            killProcess(hwndalert);
        }

        int hwndError = (int) OS.FindWindow(null, new TCHAR(0, "PuTTY Error", true));
        if (hwndError != 0) {
            killProcess(hwndError);
        }

        hwndError = (int) OS.FindWindow(null, new TCHAR(0, "PuTTY Fatal Error", true));
        if (hwndError != 0) {
            killProcess(hwndError);
        }
    }

    /**
     * Execute an utility from left bar.
     *
     * @param program
     * @param arg
     */
    public static void runProgram(Program program, String arg) {
        try {
            String cmd;

            if (arg != null) {
                cmd = program.getPath() + " " + arg;
            } else {
                cmd = program.getPath();
            }

            log.debug("Command line: {}", cmd);

            Runtime.getRuntime().exec(cmd);
        } catch (IOException ex) {
            log.error(ExceptionUtils.getStackTrace(ex));
        }
    }

    /**
     * Execute an utility from left bar.
     *
     * @param program
     * @param arg
     */
    public static void runCMD(String program, String arg) {
        String cmd;

        if (arg != null) {
            cmd = program + " " + arg;
        } else {
            cmd = program;
        }

        log.debug("Command line: " + cmd);

        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException ex) {
            log.error(ExceptionUtils.getStackTrace(ex));
        }
    }

    /**
     * open path in windows explorer
     *
     * @param path
     * @return
     */
    public static boolean openFolder(String path) {
        Desktop desktop = Desktop.getDesktop();
        File dirToOpen;

        try {
            dirToOpen = new File(path);
            desktop.open(dirToOpen);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            return false;
        }
        return true;
    }

}
