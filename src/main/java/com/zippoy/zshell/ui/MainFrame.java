package com.zippoy.zshell.ui;

import com.zippoy.zshell.control.InvokeProgram;
import com.sp.dao.SmartSessionManager;
import com.zippoy.zshell.ui.widgets.BorderData;
import com.zippoy.zshell.ui.widgets.BorderLayout;
import com.zippoy.zshell.model.ConstantValue;
import com.zippoy.zshell.config.ConfigService;
import com.zippoy.zshell.config.ConfigServiceFactory;
import com.zippoy.zshell.ui.component.AppToolbar;
import com.zippoy.zshell.ui.component.BottomToolbar;
import com.zippoy.zshell.ui.component.ConnectionToolbar;
import com.zippoy.zshell.ui.component.MImage;
import com.zippoy.zshell.ui.component.MainMenu;
import com.zippoy.zshell.ui.component.TabManager;
import com.sp.utils.RegistryUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class MainFrame {
    // 移除非静态字段的static修饰符
    public static final Display display = new Display();
    public static final Shell shell = new Shell(display);
    public static SmartSessionManager smartSessionManager;
    private ConfigService configService;

    private MainMenu mainMenu;
    private ConnectionToolbar connectionToolbar;
    private AppToolbar appToolbar;
    private BottomToolbar bottomToolbar;
    private TabManager tabManager;

    public MainFrame() {
        RegistryUtils.createPuttyKeys();
        loadConfiguration();

        initializeShell();
        initializeComponents();
        layoutComponents();
        setVisibleComponents();

        Context.set(Context.builder()
                .mainMenu(mainMenu)
                .connectionToolbar(connectionToolbar)
                .appToolbar(appToolbar)
                .bottomToolbar(bottomToolbar)
                .tabManager(tabManager)
                .config(configService)
                .build());

        if (!shell.isDisposed() && configService.isShowWelcomePage()) {
            tabManager.showWelcomeTab(ConstantValue.HOME_URL);
        }
    }

    private void initializeShell() {
        shell.setLayout(new BorderLayout());
        shell.setImage(MImage.mainImage);
        shell.setText(ConstantValue.MAIN_WINDOW_TITLE + " [" + ConstantValue.MAIN_WINDOW_VERSION + "]");
        shell.setBounds(configService.getWindowPositionSize());
    }

    private void initializeComponents() {
        //smartSessionManager = new SmartSessionManager();
        mainMenu = new MainMenu(shell, configService);
        connectionToolbar = new ConnectionToolbar(shell, configService);
        appToolbar = new AppToolbar(shell);
        bottomToolbar = new BottomToolbar(shell, configService);
        tabManager = new TabManager(shell, configService, display);
    }

    private void layoutComponents() {
        connectionToolbar.setLayoutData(new BorderData(SWT.TOP));
        bottomToolbar.setLayoutData(new BorderData(SWT.BOTTOM));
        appToolbar.setLayoutData(new BorderData(SWT.LEFT));
        tabManager.setLayoutData(new BorderData(SWT.FILL));
    }

    private void loadConfiguration() {
        InvokeProgram.killPuttyWarningsAndErrs();
        configService = ConfigServiceFactory.getConfigService();
    }

    private void setVisibleComponents() {
        connectionToolbar.setVisible(configService.isViewConnectionBar());
        appToolbar.setVisible(configService.isViewAppBar());
        bottomToolbar.setVisible(configService.isViewBottomQuickBar());
    }

    public void open() {
        shell.open();
    }

}