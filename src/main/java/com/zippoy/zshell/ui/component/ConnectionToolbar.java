package com.zippoy.zshell.ui.component;

import com.zippoy.zshell.entity.ConfigSession;
import com.zippoy.zshell.model.JumpHost;
import com.zippoy.zshell.model.Protocol;
import com.zippoy.zshell.config.ConfigService;
import com.zippoy.zshell.config.ConfigServiceFactory;
import com.zippoy.zshell.ui.Context;
import com.sp.utils.RegistryUtils;
import com.zippoy.zshell.util.TotpUtil;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.util.Arrays;

/**
 * 连接工具栏<br>
 *
 * @author zippoy
 * @date 2025-06-06
 */
@Slf4j
public class ConnectionToolbar implements SelectionListener {

    private final Group connectGroup;

    private final Composite quickConnectGroup;
    private final Combo connectTypeCombo;
    private final Combo protocolCombo;
    private final Text hostItem;
    private final Text portItem;
    private final Text usernameItem;
    private final Text passwordItem;
    private final Combo sessionCombo;
    private final Button quickConnectButton;

    private final Composite jumpHostConnectGroup;
    private final Combo jumpHostCombo;
    private final Button jumpHostConnectButton;

    private final Composite container;
    private final StackLayout stackLayout;

    public ConnectionToolbar(Shell shell, ConfigService configService) {
        connectGroup = createGroup(shell, SWT.BAR, 4);

        //
        connectTypeCombo = createCombo(connectGroup, SWT.LEFT | SWT.READ_ONLY, "连接方式:", 80, 20, "");
        connectTypeCombo.add("跳板机连接");
        connectTypeCombo.add("主机连接");
        connectTypeCombo.select(0);
        // 添加选择事件监听
        connectTypeCombo.addSelectionListener(this);

        // 添加ToolBar分隔符
        createSeparator(connectGroup, SWT.SEPARATOR | SWT.VERTICAL, 30);

        // 创建堆叠容器
        container = new Composite(connectGroup, SWT.NONE);
        stackLayout = new StackLayout();
        container.setLayout(stackLayout);

        // jumpHostConnectGroup的控件
        jumpHostConnectGroup = createSubGroup(container, SWT.NONE, 3);
        jumpHostCombo = createCombo(jumpHostConnectGroup, SWT.LEFT | SWT.READ_ONLY, " 跳板机: ", 150, 20, "");
        Arrays.stream(JumpHost.values()).forEach(p -> jumpHostCombo.add(p.getHost()));
        jumpHostCombo.select(0);

        jumpHostConnectButton = createButton(jumpHostConnectGroup, SWT.PUSH, "连接", MImage.puttyImage, "连接到跳板机");

        // quickConnectGroup的控件
        quickConnectGroup = createSubGroup(container, SWT.NONE, 13);

        protocolCombo = createCombo(quickConnectGroup, SWT.LEFT | SWT.READ_ONLY, " 协议:", 40, 20, "连接需要的协议");
        Arrays.stream(Protocol.values()).forEach(p -> protocolCombo.add(p.getName()));
        protocolCombo.select(0);

        hostItem = createMsgText(quickConnectGroup, SWT.BORDER, " 主机名:", 160, 20, "hostname/session", "必须设置 Hostname 或 Session，Session 优先于 hostname");
        portItem = createValueText(quickConnectGroup, SWT.BORDER, " 端口:", 30, 20, "22", "");
        usernameItem = createValueText(quickConnectGroup, SWT.BORDER, " 帐号:", 100, 20, configService.getUsername(), "");
        passwordItem = createMsgText(quickConnectGroup, SWT.BORDER | SWT.PASSWORD, "密码", 160, 20, "任选", "根据您的身份验证方法查看是否需要密码");

        sessionCombo = createCombo(quickConnectGroup, SWT.READ_ONLY, "会话", 40, 20, "要使用的会话");
        sessionCombo.add("");
        RegistryUtils.getAllPuttySessions().forEach(sessionCombo::add);
        sessionCombo.select(0);

        quickConnectButton = createButton(quickConnectGroup, SWT.PUSH, "连接", MImage.puttyImage, "连接到主机");

        // 初始显示跳板机组
        stackLayout.topControl = jumpHostConnectGroup;
        // pack
        container.layout();
        connectGroup.pack();
    }

    @Override
    public void widgetSelected(SelectionEvent e) {
        if (e.getSource() == connectTypeCombo) {
            toggleConnectionMode();
        } else if (e.getSource() == quickConnectButton) {
            quickConnect();
        } else if (e.getSource() == jumpHostConnectButton) {
            jumpHostConnect();
        }
    }

    /**
     * 切换连接方式事件
     */
    private void toggleConnectionMode() {
        // 切换显示的组
        stackLayout.topControl = connectTypeCombo.getSelectionIndex() == 1 ? quickConnectGroup : jumpHostConnectGroup;
        // 刷新布局
        container.layout();
        connectGroup.layout();
    }

    /**
     * 快速连接
     */
    private void quickConnect() {
        // Putty wants lower case!
        Protocol protocol = Protocol.values()[protocolCombo.getSelectionIndex()];
        String host = hostItem.getText();
        String port = portItem.getText();
        String user = usernameItem.getText();
        String password = passwordItem.getText();
        String session = sessionCombo.getText();
        log.info("protocol: {}, host: {}, port: {}, user: {}, password: {}, session: {}", protocol, host, port, user, password, session);
        ConfigSession configSession = null;
        if (session.trim().isEmpty()) {
            configSession = new ConfigSession(host, port, user, password, protocol, "");
        } else {
            configSession = new ConfigSession(user, password, port, session);
        }
        Context.get().getTabManager().addSession(configSession);
    }

    /**
     * 跳板机连接
     */
    private void jumpHostConnect() {
        ConfigService configService = ConfigServiceFactory.getConfigService();
        // Putty wants lower case!
        Protocol protocol = Protocol.SSH2;
        String host = jumpHostCombo.getText();
        String port = "22";
        String user = configService.getUsername();
        String password = configService.getPin() + TotpUtil.getCurrentOtp();
        String session = "";
        log.info("protocol: {}, host: {}, port: {}, user: {}, password: {}, session: {}", protocol, host, port, user, password, session);
        ConfigSession configSession = new ConfigSession(host, port, user, password, protocol, "");
        Context.get().getTabManager().addSession(configSession);
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e) {
        // doNothing
    }

    private Label createSeparator(Composite parent, int style, int heightHint) {
        Label separator = new Label(parent, style);
        GridData gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
        gridData.heightHint = heightHint;
        separator.setLayoutData(gridData);
        return separator;
    }

    private Group createGroup(Composite parent, int style, int columnNum) {
        Group group = new Group(parent, style);
        GridLayout layout = new GridLayout(columnNum, false);
        layout.marginHeight = 5;
        layout.marginWidth = 5;
        group.setLayout(layout);
        return group;
    }

    private Composite createSubGroup(Composite parent, int style, int columnNum) {
        Composite group = new Composite(parent, style);
        GridLayout layout = new GridLayout(columnNum, false);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.verticalSpacing = 0;
        group.setLayout(layout);
        return group;
    }

    private Combo createCombo(Composite parent, int style, String labelText, int width, int height, String tipText) {
        new Label(parent, SWT.RIGHT).setText(labelText);
        Combo combo = new Combo(parent, style);
        combo.setToolTipText(tipText);
        GridData gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
        gridData.widthHint = width;
        combo.setLayoutData(gridData);
        return combo;
    }

    /**
     * 创建含提示的Text
     */
    private Text createMsgText(Composite parent, int style, String label, int width, int height, String msg, String tipText) {
        new Label(parent, SWT.CENTER).setText(label);
        Text item = new Text(parent, style);
        GridData gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
        gridData.widthHint = width;
        item.setLayoutData(gridData);
        item.setMessage(msg);
        item.setToolTipText(tipText);
        return item;
    }

    /**
     * 创建有默认值的Text
     */
    private Text createValueText(Composite parent, int style, String label, int width, int height, String defaultText, String tipText) {
        new Label(parent, SWT.CENTER).setText(label);
        Text item = new Text(parent, style);
        GridData gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
        gridData.widthHint = width;
        item.setLayoutData(gridData);
        item.setText(defaultText);
        item.setToolTipText(tipText);
        return item;
    }

    /**
     * 创建按钮
     */
    private Button createButton(Composite parent, int style, String text, Image image, String tipText) {
        return createButton(parent, style, text, image, SWT.DEFAULT, SWT.DEFAULT, tipText);
    }

    /**
     * 创建按钮
     */
    private Button createButton(Composite parent, int style, String text, Image image, int width, int height, String tipText) {
        Button button = new Button(parent, style);
        button.setText(text);
        button.setImage(image);
        GridData gridData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
        gridData.widthHint = width;
        button.setLayoutData(gridData);
        button.setToolTipText(tipText);
        button.addSelectionListener(this);
        return button;
    }

    public void setVisible(boolean visible) {
        connectGroup.setVisible(visible);
    }


    public void setLayoutData(Object layoutData) {
        connectGroup.setLayoutData(layoutData);
    }

}