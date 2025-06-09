package com.sp.ui;

import com.sp.control.InvokeProgram;
import com.sp.dao.SmartSessionManager;
import com.sp.entity.ConfigSession;
import com.sp.model.BorderData;
import com.sp.model.BorderLayout;
import com.sp.model.ConstantValue;
import com.sp.model.Program;
import com.sp.model.Protocol;
import com.sp.service.ConfigService;
import com.sp.service.DBConfigService;
import com.sp.utils.RegistryUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Listener;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import java.awt.Graphics2D;
import java.awt.SplashScreen;
import java.util.List;

import static com.sp.ui.FrameHelper.setCompositeVisible;

@Slf4j
public class MainFrame implements SelectionListener, CTabFolder2Listener, MouseListener, ShellListener {

    public static Display display = new Display();
    public static SmartSessionManager smartSessionManager;
    private ConfigService configService;
    public static final Shell shell = new Shell(display);

    //private JumpServerConnectBar jumpServerConnectBar;

    private MenuItem openItem, newItem, captureItem, remoteDesktopItem, calculatorItem, vncItem, notePadItem, keyGenItem, exitItem, updateItem, webcomeMenuItem, reloadPopItem, clonePopItem, transferPopItem, scpMenuItem, ftpMenuItem, sftpMenuItem, vncPopItem, configProgramsLocationsItem, utilitiesBarMenuItem, connectionBarMenuItem, bottomQuickBarMenuItem;
    private Menu popupmenu;
    private ToolItem itemNew, itemOpen, itemRemoteDesk, itemCapture, itemCalculator, itemVNC, itemNotePad, itemKenGen, itemHelp;
    private CTabFolder folder;
    private CTabItem welcomeItem, dictItem;
    private ToolBar utilitiesToolbar;
    private Group connectGroup, quickBottomGroup;

    // 连接栏组件
    private Button connectButton;
    private Text hostItem;
    private Text portItem;
    private Text usernameItem;
    private Text passwordItem;
    private Combo protocolCombo;
    private Combo sessionCombo;

    // bottom util bar components
    private Text pathItem, dictText;
    private Button win2UnixButton, unix2WinButton, openPathButton, dictButton;

    // Empty constructor to no use splash screen.
    public MainFrame() {
        RegistryUtils.createPuttyKeys();

        loadConfiguration();

        shell.setLayout(new BorderLayout());
        shell.setImage(MImage.mainImage);
        shell.setText(ConstantValue.MAIN_WINDOW_TITLE + " [" + ConstantValue.MAIN_WINDOW_VERSION + "]");
        shell.setBounds(configService.getWindowPositionSize());
        shell.addShellListener(this);

        // Get dbmanager instance:
        smartSessionManager = new SmartSessionManager();

        //		bar.setSelection(3);

        // Main menu:
        createMainMenu(shell);
        //		bar.setSelection(4);

        // Create upper connection toolbar:
        createConnectionBar(shell);

        //customToolBar = new CustomToolBar(shell, SWT.TOP);
        //customToolBar.pack();
        //customToolBar.setVisible(true);

        // Create upper utilities toolbar:
        createUtilitiesToolbar(shell);

        // Create bottom utilities toolbar:
        createBottomUtilitiesToolBar(shell);

        // Create lower tabs zone:
        createTabs(shell);
        createTabPopupMenu(shell); // Right button menu

        // Show/Hide toolbars based on configuration file values:
        setVisibleComponents();
        if (configService.isShowWelcomePage()) {
            showWelcomeTab(ConstantValue.HOME_URL);
        }
        applyFeatureToggle();
    }

    public MainFrame(SplashScreen splash, Graphics2D g) {
        Splash.renderSplashFrame(g, "创建注册");
        splash.update();
        RegistryUtils.createPuttyKeys();

        Splash.renderSplashFrame(g, "加载配置");
        splash.update();
        loadConfiguration();

        shell.setLayout(new BorderLayout());
        shell.setImage(MImage.mainImage);
        shell.setText(ConstantValue.MAIN_WINDOW_TITLE + " [" + ConstantValue.MAIN_WINDOW_VERSION + "]");
        shell.setBounds(configService.getWindowPositionSize());
        shell.addShellListener(this);

        Splash.renderSplashFrame(g, "正在加载数据库");
        splash.update();
        // Get dbmanager instance:
        smartSessionManager = new SmartSessionManager();
        //		bar.setSelection(3);
        // Main menu:
        Splash.renderSplashFrame(g, "创建菜单");
        splash.update();
        createMainMenu(shell);
        //		bar.setSelection(4);

        Splash.renderSplashFrame(g, "创建工具栏");
        splash.update();
        // Create upper connection toolbar:
        createConnectionBar(shell);

        // Create upper utilities toolbar:
        createUtilitiesToolbar(shell);

        Splash.renderSplashFrame(g, "创建实用程序栏");
        splash.update();
        // Create bottom utilities toolbar:
        createBottomUtilitiesToolBar(shell);

        Splash.renderSplashFrame(g, "创建标签页");
        splash.update();
        // Create lower tabs zone:
        createTabs(shell);
        // Right button menu
        createTabPopupMenu(shell);

        Splash.renderSplashFrame(g, "初始化功能");
        splash.update();
        // Show/Hide toolbars based on configuration file values:
        setVisibleComponents();
        if (configService.isShowWelcomePage()) {
            showWelcomeTab(ConstantValue.HOME_URL);
        }
        applyFeatureToggle();
        Splash.renderSplashFrame(g, "初始化完成");
        splash.update();
    }

    public void open() {
        shell.open();
    }
    //	public void setVisible(boolean b){
    //		shell.setVisible(b);
    //	}

    /**
     * Main menu.
     */
    private void createMainMenu(Shell shell) {
        Menu menu = new Menu(shell, SWT.BAR);
        shell.setMenuBar(menu);

        // Menu: File
        MenuItem file = new MenuItem(menu, SWT.CASCADE);
        file.setText("文件");
        Menu filemenu = new Menu(shell, SWT.DROP_DOWN);
        file.setMenu(filemenu);

        newItem = new MenuItem(filemenu, SWT.PUSH);
        newItem.setText("新建\tCtrl+N");
        newItem.setImage(MImage.newImage);
        newItem.setAccelerator(SWT.CTRL + 'N');
        newItem.addSelectionListener(this);

        openItem = new MenuItem(filemenu, SWT.PUSH);
        openItem.setText("打开\tCtrl+O");
        openItem.setImage(MImage.openImage);
        openItem.setAccelerator(SWT.CTRL + 'O');
        openItem.addSelectionListener(this);


        // Separator:
        new MenuItem(filemenu, SWT.SEPARATOR);

        exitItem = new MenuItem(filemenu, SWT.PUSH);
        exitItem.setText("退出\tCtrl+X");
        exitItem.setImage(null);
        exitItem.setAccelerator(SWT.CTRL + 'X');
        exitItem.addSelectionListener(this);

        // Menu: View
        MenuItem view = new MenuItem(menu, SWT.CASCADE);
        view.setText("视图");
        Menu viewMenu = new Menu(shell, SWT.DROP_DOWN);
        view.setMenu(viewMenu);

        utilitiesBarMenuItem = new MenuItem(viewMenu, SWT.CHECK);
        utilitiesBarMenuItem.setText("实用程序栏");
        utilitiesBarMenuItem.setSelection(true);
        utilitiesBarMenuItem.addSelectionListener(this);

        connectionBarMenuItem = new MenuItem(viewMenu, SWT.CHECK);
        connectionBarMenuItem.setText("连接栏");
        connectionBarMenuItem.setSelection(true);
        connectionBarMenuItem.addSelectionListener(this);

        bottomQuickBarMenuItem = new MenuItem(viewMenu, SWT.CHECK);
        bottomQuickBarMenuItem.setText("底部快捷栏");
        bottomQuickBarMenuItem.setSelection(true);
        bottomQuickBarMenuItem.addSelectionListener(this);

        // Menu: Options
        MenuItem configurationMenuItem = new MenuItem(menu, SWT.CASCADE);
        configurationMenuItem.setText("配置");
        Menu optionsMenu = new Menu(shell, SWT.DROP_DOWN);
        configurationMenuItem.setMenu(optionsMenu);

        configProgramsLocationsItem = new MenuItem(optionsMenu, SWT.PUSH);
        configProgramsLocationsItem.setText("程序配置");
        configProgramsLocationsItem.setImage(MImage.configImage);
        // configProgramsItem.setAccelerator(SWT.CTRL + 'R'); // TODO: setup a
        // key and enable!
        configProgramsLocationsItem.addSelectionListener(this);

        // Menu: Application
        MenuItem application = new MenuItem(menu, SWT.CASCADE);
        application.setText("应用");
        Menu applicationMenu = new Menu(shell, SWT.DROP_DOWN);
        application.setMenu(applicationMenu);

        remoteDesktopItem = new MenuItem(applicationMenu, SWT.PUSH);
        remoteDesktopItem.setText("远程桌面\tCtrl+R");
        remoteDesktopItem.setImage(MImage.RemoteDeskImage);
        remoteDesktopItem.setAccelerator(SWT.CTRL + 'R');
        remoteDesktopItem.addSelectionListener(this);

        captureItem = new MenuItem(applicationMenu, SWT.PUSH);
        captureItem.setText("截屏\tCtrl+C");
        captureItem.setImage(MImage.captureImage);
        captureItem.addSelectionListener(this);

        calculatorItem = new MenuItem(applicationMenu, SWT.PUSH);
        calculatorItem.setText("计算器");
        calculatorItem.setImage(MImage.calculator);
        calculatorItem.addSelectionListener(this);

        vncItem = new MenuItem(applicationMenu, SWT.PUSH);
        vncItem.setText("VNC");
        vncItem.setImage(MImage.vncImage);
        vncItem.addSelectionListener(this);

        notePadItem = new MenuItem(applicationMenu, SWT.PUSH);
        notePadItem.setText("记事本");
        notePadItem.setImage(MImage.notepad);
        notePadItem.addSelectionListener(this);

        keyGenItem = new MenuItem(applicationMenu, SWT.PUSH);
        keyGenItem.setText("KenGen");
        keyGenItem.setImage(MImage.key);
        keyGenItem.addSelectionListener(this);


        //		List<HashMap<String, String>> listMenuItems = configuration.getBatchConfig();
        //		for (HashMap<String, String> menuHashMap : listMenuItems) {
        //			String type = menuHashMap.get("type");
        //			if (type == null || type.equals("seperator")) {
        //				new MenuItem(applicationMenu, SWT.SEPARATOR);
        //				continue;
        //			}
        //			String path = menuHashMap.get("path") == null ? "N/A" : menuHashMap.get("path");
        //			String argument = menuHashMap.get("argument") == null ? "N/A" : menuHashMap.get("argument");
        //			String description = menuHashMap.get("description") == null ? "N/A" : menuHashMap.get("description");
        //			MenuItem menuItem = new MenuItem(applicationMenu, SWT.PUSH);
        //			menuItem.setText(description);
        //			// menuItem.setToolTipText(path + " " + argument);
        //			menuItem.setData("path", path);
        //			menuItem.setData("argument", argument);
        //			menuItem.setData("description", description);
        //			menuItem.setData("type", "dynamicApplication");
        //			menuItem.addSelectionListener(this);
        //		}

        // Menu: About
        MenuItem about = new MenuItem(menu, SWT.CASCADE);
        about.setText("关于");
        Menu aboutMenu = new Menu(shell, SWT.DROP_DOWN);
        about.setMenu(aboutMenu);

        webcomeMenuItem = new MenuItem(aboutMenu, SWT.PUSH);
        webcomeMenuItem.setText("欢迎页");
        webcomeMenuItem.addSelectionListener(this);

        updateItem = new MenuItem(aboutMenu, SWT.PUSH);
        updateItem.setText("更新");
        updateItem.addSelectionListener(this);
    }

    /**
     * Create bottom connection bar.
     */
    private void createConnectionBar(Shell shell) {
        connectGroup = new Group(shell, SWT.NONE);

        RowLayout layout = new RowLayout();
        layout.marginTop = 3;
        layout.marginBottom = 3;
        layout.marginLeft = 1;
        layout.marginRight = 1;
        layout.spacing = 5;
        layout.wrap = false;
        layout.center = true;

        connectGroup.setLayout(layout);
        connectGroup.setLayoutData(new BorderData(SWT.TOP));
        connectGroup.setText("快速连接");

        // Protocol:
        new Label(connectGroup, SWT.RIGHT).setText("协议");
        protocolCombo = new Combo(connectGroup, SWT.LEFT | SWT.READ_ONLY);
        // Get all protocols and add:
        for (Protocol protocol : Protocol.values()) {
            protocolCombo.add(protocol.getName());
        }
        // Set default value.
        protocolCombo.select(0);
        protocolCombo.setToolTipText("要使用的协议");
        protocolCombo.setLayoutData(new RowData(40, 20));

        // Hostname:
        new Label(connectGroup, SWT.RIGHT).setText("主机名");
        hostItem = new Text(connectGroup, SWT.BORDER);
        hostItem.setLayoutData(new RowData(160, 14));
        hostItem.setMessage("hostname/session");
        hostItem.setToolTipText("必须设置 Hostname 或 Session，Session 优先于 hostname");

        // Port:
        new Label(connectGroup, SWT.RIGHT).setText("Port");
        portItem = new Text(connectGroup, SWT.BORDER);
        portItem.setText("22");
        portItem.setLayoutData(new RowData(30, 14));

        // Username:
        new Label(connectGroup, SWT.RIGHT).setText("帐号");
        usernameItem = new Text(connectGroup, SWT.BORDER);
        usernameItem.setText(configService.getUsername());
        usernameItem.setLayoutData(new RowData(100, 14));

        // Password
        new Label(connectGroup, SWT.RIGHT).setText("密码");
        passwordItem = new Text(connectGroup, SWT.PASSWORD | SWT.BORDER);
        passwordItem.setLayoutData(new RowData(160, 14));
        passwordItem.setMessage("任选");
        passwordItem.setToolTipText("根据您的身份验证方法查看是否需要密码");

        // Session:
        new Label(connectGroup, SWT.RIGHT).setText("会话");
        sessionCombo = new Combo(connectGroup, SWT.READ_ONLY);
        sessionCombo.setLayoutData(new RowData());
        sessionCombo.setToolTipText("要使用的会话");
        // Empty entry to use none.
        sessionCombo.add("");
        // Get all "Putty" sessions:
        List<String> sessions = RegistryUtils.getAllPuttySessions();
        for (String session : sessions) {
            sessionCombo.add(session);
        }

        // Connect button:
        connectButton = new Button(connectGroup, SWT.PUSH);
        connectButton.setText("连接");
        connectButton.setImage(MImage.puttyImage);
        connectButton.setLayoutData(new RowData());
        connectButton.setToolTipText("连接到主机");
        connectButton.addSelectionListener(this);
        connectGroup.pack();
    }

    private void createBottomUtilitiesToolBar(Shell shell) {
        quickBottomGroup = new Group(shell, SWT.BAR);
        RowLayout layout1 = new RowLayout();
        layout1.marginTop = 3;
        layout1.marginBottom = 3;
        layout1.marginLeft = 1;
        layout1.marginRight = 1;
        layout1.spacing = 5;
        layout1.wrap = false;
        layout1.center = true;
        quickBottomGroup.setLayout(layout1);
        quickBottomGroup.setLayoutData(new BorderData(SWT.BOTTOM));
        // Path:
        new Label(quickBottomGroup, SWT.RIGHT).setText("路径");
        pathItem = new Text(quickBottomGroup, SWT.BORDER);
        pathItem.setText("");
        pathItem.setLayoutData(new RowData(250, 20));

        win2UnixButton = new Button(quickBottomGroup, SWT.PUSH);
        win2UnixButton.setText("->Linux");
        win2UnixButton.setImage(MImage.linux);
        win2UnixButton.setToolTipText("将 Windows 路径转换为 Linux");
        win2UnixButton.setLayoutData(new RowData());
        win2UnixButton.addSelectionListener(this);

        unix2WinButton = new Button(quickBottomGroup, SWT.PUSH);
        unix2WinButton.setText("->Windows");
        unix2WinButton.setToolTipText("将 Linux 路径转换为 Windows");
        unix2WinButton.setImage(MImage.windows);
        unix2WinButton.setLayoutData(new RowData());
        unix2WinButton.addSelectionListener(this);

        openPathButton = new Button(quickBottomGroup, SWT.PUSH);
        openPathButton.setText("打开");
        openPathButton.setImage(MImage.folder);
        openPathButton.setToolTipText("打开目录/文件");
        openPathButton.addSelectionListener(this);

        // dictionary
        new Label(quickBottomGroup, SWT.RIGHT).setText("词典");
        dictText = new Text(quickBottomGroup, SWT.BORDER);
        dictText.setLayoutData(new RowData(100, 20));
        dictText.addListener(SWT.Traverse, (event) -> {
            if (event.detail == SWT.TRAVERSE_RETURN) {
                String keyword = dictText.getText().trim();
                OpenDictTab(keyword);
            }
        });

        dictButton = new Button(quickBottomGroup, SWT.PUSH);
        dictButton.setText("搜索");
        dictButton.setToolTipText("在词典中搜索关键词");
        dictButton.setImage(MImage.dictImage);
        dictButton.addSelectionListener(this);
        quickBottomGroup.pack();
    }

    /**
     * Create utilities toolbar.
     */
    private void createUtilitiesToolbar(Shell shell) {
        utilitiesToolbar = new ToolBar(shell, SWT.VERTICAL);
        utilitiesToolbar.setLayoutData(new BorderData(SWT.LEFT));

        itemNew = new ToolItem(utilitiesToolbar, SWT.PUSH);
        itemNew.setText("新建  ");
        itemNew.setToolTipText("创建新会话");
        itemNew.setImage(MImage.newImage);
        itemNew.addSelectionListener(this);

        itemOpen = new ToolItem(utilitiesToolbar, SWT.PUSH);
        itemOpen.setText("打开  ");
        itemOpen.setToolTipText("打开现有会话");
        itemOpen.setImage(MImage.openImage);
        itemOpen.addSelectionListener(this);

        itemRemoteDesk = new ToolItem(utilitiesToolbar, SWT.PUSH);
        itemRemoteDesk.setText("远程桌面");
        itemRemoteDesk.setToolTipText("打开系统远程桌面工具");
        itemRemoteDesk.setImage(MImage.RemoteDeskImage);
        itemRemoteDesk.addSelectionListener(this);

        itemCapture = new ToolItem(utilitiesToolbar, SWT.PUSH);
        itemCapture.setText("截屏");
        itemCapture.setToolTipText("打开 FastStone Capture");
        itemCapture.setImage(MImage.captureImage);
        itemCapture.addSelectionListener(this);

        itemCalculator = new ToolItem(utilitiesToolbar, SWT.PUSH);
        itemCalculator.setText("计算器");
        itemCalculator.setToolTipText("打开系统计算器");
        itemCalculator.setImage(MImage.calculator);
        itemCalculator.addSelectionListener(this);

        itemVNC = new ToolItem(utilitiesToolbar, SWT.PUSH);
        itemVNC.setText("VNC");
        itemVNC.setToolTipText("打开 VNC");
        itemVNC.setImage(MImage.vncImage);
        itemVNC.addSelectionListener(this);

        itemNotePad = new ToolItem(utilitiesToolbar, SWT.PUSH);
        itemNotePad.setText("记事本");
        itemNotePad.setToolTipText("打开记事本");
        itemNotePad.setImage(MImage.notepad);
        itemNotePad.addSelectionListener(this);

        itemKenGen = new ToolItem(utilitiesToolbar, SWT.PUSH);
        itemKenGen.setText("KenGen");
        itemKenGen.setToolTipText("转换 SSH Key");
        itemKenGen.setImage(MImage.key);
        itemKenGen.addSelectionListener(this);

        itemHelp = new ToolItem(utilitiesToolbar, SWT.PUSH);
        itemHelp.setText("帮助");
        itemHelp.setToolTipText("帮助文档");
        itemHelp.setImage(MImage.helpImage);
        itemHelp.addSelectionListener(this);

        utilitiesToolbar.pack();
    }

    /**
     * Create tabs zone.
     */
    private void createTabs(Shell shell) {
        folder = new CTabFolder(shell, SWT.BORDER);

        folder.setLayoutData(new BorderData());
        folder.setSimple(false);
        folder.setUnselectedCloseVisible(false);
        folder.addCTabFolder2Listener(this);
        folder.addMouseListener(this);
        folder.addSelectionListener(this);
    }

    /**
     * Tab popup menu.
     */
    private void createTabPopupMenu(Shell shell) {
        popupmenu = new Menu(shell, SWT.POP_UP);
        reloadPopItem = new MenuItem(popupmenu, SWT.PUSH);
        reloadPopItem.setText("重新加载会话");
        reloadPopItem.setImage(MImage.reloadImage);
        reloadPopItem.addSelectionListener(this);

        //		 openPuttyItem = new MenuItem(popupmenu, SWT.PUSH);
        //		 openPuttyItem.setText("open in putty");
        //		 openPuttyItem.setImage(MImage.puttyImage);
        //		 // openPuttyItem.setToolTipText("Opens connection on a single
        //		 window");
        //		 openPuttyItem.addSelectionListener(this);

        clonePopItem = new MenuItem(popupmenu, SWT.PUSH);
        clonePopItem.setText("克隆会话");
        clonePopItem.setImage(MImage.cloneImage);
        clonePopItem.addSelectionListener(this);

        vncPopItem = new MenuItem(popupmenu, SWT.PUSH);
        vncPopItem.setText("VNC");
        vncPopItem.setImage(MImage.vncImage);
        vncPopItem.addSelectionListener(this);

        transferPopItem = new MenuItem(popupmenu, SWT.CASCADE);
        transferPopItem.setText("传输文件");
        transferPopItem.setImage(MImage.transferImage);

        Menu subMenu = new Menu(popupmenu);
        ftpMenuItem = new MenuItem(subMenu, SWT.PUSH);
        ftpMenuItem.setText("FTP");
        // ftpMenuItem.setToolTipText("Simple FTP");
        ftpMenuItem.addSelectionListener(this);

        scpMenuItem = new MenuItem(subMenu, SWT.PUSH);
        scpMenuItem.setText("SCP");
        // scpMenuItem.setToolTipText("FTP over SSH");
        scpMenuItem.addSelectionListener(this);

        sftpMenuItem = new MenuItem(subMenu, SWT.PUSH);
        sftpMenuItem.setText("SFTP");
        // sftpMenuItem.setToolTipText("Secure FTP");
        sftpMenuItem.addSelectionListener(this);

        transferPopItem.setMenu(subMenu);
    }

    private void loadConfiguration() {
        InvokeProgram.killPuttyWarningsAndErrs();
        configService = new DBConfigService();
    }

    private void showWelcomeTab(String url) {
        if (welcomeItem == null || welcomeItem.isDisposed()) {
            welcomeItem = new CTabItem(folder, SWT.CLOSE);
            Browser browser = new Browser(folder, SWT.NONE);
            browser.setUrl(url);
            welcomeItem.setControl(browser);
            folder.setSelection(welcomeItem);
            welcomeItem.setText("欢迎页");
        } else {
            folder.setSelection(welcomeItem);
        }
    }

    private void OpenDictTab(String keyword) {
        if (dictItem == null || dictItem.isDisposed()) {
            dictItem = new CTabItem(folder, SWT.CLOSE);
            dictItem.setImage(MImage.dictImage);
            Browser browser = new Browser(folder, SWT.NONE);
            browser.setUrl(configService.getDictionaryBaseUrl() + keyword);
            dictItem.setControl(browser);
            folder.setSelection(dictItem);
            dictItem.setText("词典");
        } else {
            folder.setSelection(dictItem);
            ((Browser) dictItem.getControl()).setUrl(configService.getDictionaryBaseUrl() + keyword);
        }

    }

    /**
     * Show/Hide toolbars based on configuration file values.
     */
    private void setVisibleComponents() {
        Event event = new Event();

        utilitiesBarMenuItem.setSelection(configService.isViewAppBar());
        utilitiesBarMenuItem.notifyListeners(SWT.Selection, event);
        connectionBarMenuItem.setSelection(configService.isViewConnectionBar());
        connectionBarMenuItem.notifyListeners(SWT.Selection, event);
        bottomQuickBarMenuItem.setSelection(configService.isViewBottomQuickBar());
        bottomQuickBarMenuItem.notifyListeners(SWT.Selection, event);

    }

    /**
     * Open a new session in a new tab.
     *
     * @param item
     * @param session
     */
    public void addSession(CTabItem item, ConfigSession session) {
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

    private void reloadSession() {
        CTabItem tabItem = folder.getSelection();
        if (tabItem.getData("hwnd") == null) {
            return;
        }
        int hwnd = Integer.parseInt(String.valueOf(tabItem.getData("hwnd")));
        InvokeProgram.killProcess(hwnd);
        addSession(tabItem, (ConfigSession) tabItem.getData("session"));
    }

    private void cloneSession() {
        CTabItem tabItem = folder.getSelection();
        if (tabItem.getData("session") == null) {
            return;
        }
        ConfigSession session = (ConfigSession) tabItem.getData("session");
        addSession(null, session);
    }

    private void openWinscp(String protocol) {
        if (folder.getSelection().getData("session") == null) {
            return;
        }
        ConfigSession session = (ConfigSession) folder.getSelection().getData("session");
        String arg = protocol + "://" + session.getUsername() + ":" + session.getPassword() + "@" + session.getHost() + ":" + session.getPort();

        InvokeProgram.runProgram(Program.APP_WINSCP, arg);
    }

    private void OpenPutty() {
        //		CTabItem tabItem = folder.getSelection();
        //		if (tabItem.getData("session") == null)
        //			return;
        //		ConfigSession session = (ConfigSession) tabItem.getData("session");
        //		InvokeProgram.invokeSinglePutty(session);
    }

    private void openVNCSession() {
        CTabItem item = folder.getSelection();
        if (item.getData("session") == null) {
            return;
        }
        ConfigSession session = (ConfigSession) item.getData("session");
        if (session != null) {
            String host = session.getHost();
            InputDialog inputDialog = new InputDialog(shell, "Input VNC Server Host", "Example:    xx.swg.usma.ibm.com:1", host + ":1", null);
            if (InputDialog.OK == inputDialog.open()) {
                InvokeProgram.runProgram(Program.APP_VNC, inputDialog.getValue());
            }
        }
    }

    public void disposeApp() {
        CTabItem[] items = folder.getItems();
        for (CTabItem item : items) {
            if (item.getData("hwnd") != null) {
                int hwnd = Integer.parseInt(String.valueOf(item.getData("hwnd")));
                InvokeProgram.killProcess(hwnd);
            }
        }

        // Close in-memory database:
        smartSessionManager.shutdown();

    }



    /**
     * check the feature toggle, dispose the features who equals to "false"
     */
    private void applyFeatureToggle() {
        //		Properties props = configuration.getFeatureToggleProps();
        //		boolean bVnc = "true".equalsIgnoreCase(props.getProperty("vnc", "true"));
        //		if (!bVnc) {
        //			this.vncPopItem.dispose();
        //			this.itemVNC.dispose();
        //		}
        //
        //		boolean bTransfer = "true".equalsIgnoreCase(props.getProperty("transfer", "true"));
        //		if (!bTransfer) {
        //			this.transferPopItem.dispose();
        //		}
    }

    /**
     * 为其他启动函数准备全局变量。
     * 与此 VM 选项一起使用：-splash：icon/splash.jpg
     */
    private static void splashInit() {
        final MainFrame main;
        final SplashScreen splash = SplashScreen.getSplashScreen();

        // If no splash image has been defined in VM options...
        if (splash == null) {
            log.info("未定义启动画面！");
            main = new MainFrame();
        } else {
            Graphics2D g = splash.createGraphics();
            if (g == null) {
                log.info("无法创建启动画面的图形！");
                main = new MainFrame();
            } else {
                main = new MainFrame(splash, g);
                splash.close();
            }
        }
        main.open();
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void widgetSelected(SelectionEvent e) {
        if (e.getSource() == newItem || e.getSource() == itemNew) {
            new NewSessionDialog(this, null, "add");
        } else if (e.getSource() == itemOpen || e.getSource() == openItem) {
            new OpenSessionDialog(this, shell);
        } else if (e.getSource() == itemRemoteDesk || e.getSource() == remoteDesktopItem) {
            InvokeProgram.runProgram(Program.APP_REMOTE_DESK, null);
        } else if (e.getSource() == exitItem) {
            disposeApp();
            System.exit(0);
        } else if (e.getSource() == itemCapture || e.getSource() == captureItem) {
            InvokeProgram.runProgram(Program.APP_CAPTURE, null);
            shell.setMinimized(true);
        } else if (e.getSource() == itemCalculator || e.getSource() == calculatorItem) {
            InvokeProgram.runProgram(Program.APP_CALCULATOR, null);
        } else if (e.getSource() == itemVNC || e.getSource() == vncItem) {
            InvokeProgram.runProgram(Program.APP_VNC, null);
        } else if (e.getSource() == itemNotePad || e.getSource() == notePadItem) {
            InvokeProgram.runProgram(Program.APP_NOTEPAD, null);
        } else if (e.getSource() == itemKenGen || e.getSource() == keyGenItem) {
            InvokeProgram.runCMD(configService.getKeyGenerator(), null);
        } else if (e.getSource() == itemHelp || e.getSource() == webcomeMenuItem) {
            showWelcomeTab(ConstantValue.HOME_URL);
        } else if (e.getSource() == updateItem) {
            // This doesn't work, but I don't know why!
            // MessageDialog.openInformation(shell, "Update tool", "remaining function");
            MessageBox messagebox = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
            messagebox.setText("更新工具");
            messagebox.setMessage("剩余功能。请手动检查是否有更新的版本。");
            if (messagebox.open() == SWT.OK) {
                shell.setFocus();
            }
        } else if (e.getSource() == utilitiesBarMenuItem) {
            Boolean visible = utilitiesBarMenuItem.getSelection();
            setCompositeVisible(utilitiesToolbar, shell, visible);
            smartSessionManager.updateSystemConfig(ConfigService.VIEW_APP_BAR, String.valueOf(visible));
        } else if (e.getSource() == connectionBarMenuItem) {
            Boolean visible = connectionBarMenuItem.getSelection();
            setCompositeVisible(connectGroup, shell, visible);
            smartSessionManager.updateSystemConfig(ConfigService.VIEW_CONNECTION_BAR, String.valueOf(visible));
        } else if (e.getSource() == bottomQuickBarMenuItem) {
            Boolean visible = bottomQuickBarMenuItem.getSelection();
            setCompositeVisible(quickBottomGroup, shell, visible);
            smartSessionManager.updateSystemConfig(ConfigService.VIEW_BOTTOM_QUICK_BAR, String.valueOf(visible));
        } else if (e.getSource() == configProgramsLocationsItem) {
            new SystemConfigDialog(shell);
            //			new ProgramsLocationsDialog(shell);
            // menuItem
        } else if (e.getSource() == reloadPopItem) {
            reloadSession();
        } else if (e.getSource() == clonePopItem) {
            cloneSession();
        } else if (e.getSource() == ftpMenuItem) {
            openWinscp("ftp");
        } else if (e.getSource() == scpMenuItem) {
            openWinscp("scp");
        } else if (e.getSource() == sftpMenuItem) {
            openWinscp("sftp");
        } else if (e.getSource() == vncPopItem) {
            openVNCSession();
            // folder
        } else if (e.getSource() == folder) {
            if (folder.getSelection().getData("hwnd") != null) {
                int hwnd = (Integer) folder.getSelection().getData("hwnd");
                InvokeProgram.setWindowFocus(hwnd);
            }
        } else if (StringUtils.endsWith(e.getSource().getClass().toString(), "MenuItem") && "dynamicApplication".equals(
                ((MenuItem) e.getSource()).getData("type"))) {
            String path = ((MenuItem) e.getSource()).getData("path").toString();
            String argument = ((MenuItem) e.getSource()).getData("argument").toString();
            InvokeProgram.runCMD(path, argument);
        } else if (e.getSource() == connectButton) {
            // String protocol = protocolCombo.getText().toLowerCase(); //
            // Putty wants lower case!
            Protocol protocol = Protocol.values()[protocolCombo.getSelectionIndex()];
            String host = hostItem.getText();
            String port = portItem.getText();
            String user = usernameItem.getText();
            String password = passwordItem.getText();
            String session = sessionCombo.getText();
            System.out.println("protocol: " + protocol + ", host: " + host + ", port: " + port + ", user: " + user + ", password: " + password + ", session: "
                    + session); //DEBUG
            ConfigSession configSession = null;
            if (session.trim().isEmpty()) {
                configSession = new ConfigSession(host, port, user, password, protocol, "");
            } else {
                configSession = new ConfigSession(user, password, port, session);
            }
            addSession(null, configSession);
        } else if (e.getSource() == win2UnixButton) {
            String path = pathItem.getText().trim();
            if (StringUtils.isBlank(path)) {
                MessageDialog.openInformation(shell, "Info", "请输入正确的路径！");
                return;
            }
            path = StringUtils.stripStart(path, "/\\" + configService.getWinPathBaseDrive());
            pathItem.setText("/" + FilenameUtils.separatorsToUnix(path));
        } else if (e.getSource() == unix2WinButton) {
            String path = pathItem.getText().trim();
            if (StringUtils.isBlank(path)) {
                MessageDialog.openInformation(shell, "Info", "请输入正确的路径！");
                return;
            }
            path = StringUtils.stripStart(path, "/\\" + configService.getWinPathBaseDrive());
            pathItem.setText(configService.getWinPathBaseDrive() + "\\" + FilenameUtils.separatorsToWindows(path));
        } else if (e.getSource() == openPathButton) {
            String path = pathItem.getText().trim();
            if (StringUtils.isBlank(path)) {
                MessageDialog.openInformation(shell, "Info", "请输入正确的路径！");
                return;
            }
            path = StringUtils.stripStart(path, "/\\" + configService.getWinPathBaseDrive());
            path = configService.getWinPathBaseDrive() + "\\" + FilenameUtils.separatorsToWindows(path);
            pathItem.setText(path);
            if (!InvokeProgram.openFolder(path)) {
                MessageDialog.openError(shell, "Error", "路径不存在！");
            }
        } else if (e.getSource() == dictButton) {
            String keyword = dictText.getText().trim();
            OpenDictTab(keyword);
        }
    }

    @Override
    public void close(CTabFolderEvent e) {
        if (e.item == folder.getSelection()) {
            if (e.item.getData("session") != null) {
                MessageBox messagebox = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
                messagebox.setText("确认退出");
                messagebox.setMessage("是否确定退出会话: " + ((ConfigSession) e.item.getData("session")).getHost());
                if (messagebox.open() == SWT.YES) {
                    int hwnd = Integer.parseInt(String.valueOf(e.item.getData("hwnd")));
                    InvokeProgram.killProcess(hwnd);

                    e.item.dispose();
                    e.doit = true;
                } else {
                    e.doit = false;
                }
            }

        } else {
            e.item.dispose();
            e.doit = true;
            shell.setFocus();
        }
    }

    @Override
    public void maximize(CTabFolderEvent ctabfolderevent) {
        // TODO Auto-generated method stub
    }

    @Override
    public void minimize(CTabFolderEvent ctabfolderevent) {
        // TODO Auto-generated method stub
    }

    @Override
    public void restore(CTabFolderEvent ctabfolderevent) {
        // TODO Auto-generated method stub
    }

    @Override
    public void showList(CTabFolderEvent ctabfolderevent) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseDoubleClick(MouseEvent mouseevent) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseDown(MouseEvent e) {
        // TODO Auto-generated method stub
        if (e.button == 3) {
            CTabItem selectItem = folder.getItem(new Point(e.x, e.y));
            if (selectItem != null && StringUtils.equalsIgnoreCase(String.valueOf(folder.getSelection().getData("TYPE")), "session")) {
                folder.setSelection(selectItem);
                popupmenu.setVisible(true);
            } else {
                popupmenu.setVisible(false);
            }
        }
    }

    @Override
    public void mouseUp(MouseEvent mouseevent) {
        // TODO Auto-generated method stub
    }

    @Override
    public void shellActivated(ShellEvent e) {
        // TODO Auto-generated method stub
        if (folder.getSelection() != null) {
            Object objhwnd = folder.getSelection().getData("hwnd");
            if (objhwnd != null) {
                InvokeProgram.setWindowFocus(Integer.parseInt(objhwnd.toString()));
            }
        }
    }

    @Override
    public void shellClosed(ShellEvent e) {
        // TODO Auto-generated method stub
        // disableProxy();
        disposeApp();
    }

    @Override
    public void shellDeactivated(ShellEvent arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void shellDeiconified(ShellEvent arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void shellIconified(ShellEvent arg0) {
        // TODO Auto-generated method stub
    }

    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
    }

    public void keyReleased(KeyEvent arg0) {
        // TODO Auto-generated method stub
    }

    public static void main(String[] args) {
        // Initialize splash image:
        splashInit();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }

}
