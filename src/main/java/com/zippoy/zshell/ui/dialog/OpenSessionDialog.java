package com.zippoy.zshell.ui.dialog;

import com.sp.dao.SmartSessionManager;
import com.zippoy.zshell.entity.ConfigSession;
import com.sp.ui.MainFrame;
import com.zippoy.zshell.ui.component.MImage;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import java.util.ArrayList;

/**
 * 打开会话对话框<br>
 *
 * @author zippoy
 * @date 2025-06-06
 */
public class OpenSessionDialog implements SelectionListener, MouseListener {
    private MainFrame mainFrame = null;
    private Shell dialog = null;
    protected Object result;
    private Table table;
    private final SmartSessionManager smartSessionManager;
    private Button addButton, editButton, deleteButton, connectButton, puttyWindow;
    // Helper to deal with positions until a new layout can be made:
    private static final int X_POS = 404;

    public OpenSessionDialog(MainFrame mainFrame, Shell parent) {
        this.dialog = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        this.mainFrame = mainFrame;
        this.smartSessionManager = new SmartSessionManager();

        init();
    }

    /**
     * Initialize window in a safer way.
     * Usefull to avoid "Leaking This In Constructor" warnings.
     */
    private void init() {
        dialog.setImage(MImage.openImage);
        dialog.setText("打开会话");
        dialog.setSize(350, 300);

        table = new Table(dialog, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
        table.setBounds(0, 0, 396, 257);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        table.addMouseListener(this);

        TableColumn tableHostColumn = new TableColumn(table, SWT.NONE);
        tableHostColumn.setWidth(166);
        tableHostColumn.setText("主机");

        TableColumn tablePortColumn = new TableColumn(table, SWT.NONE);
        tablePortColumn.setWidth(50);
        tablePortColumn.setText("端口");

        TableColumn tableUserColumn = new TableColumn(table, SWT.NONE);
        tableUserColumn.setWidth(74);
        tableUserColumn.setText("账号");

        TableColumn tableTimeColumn = new TableColumn(table, SWT.NONE);
        tableTimeColumn.setWidth(102);
        tableTimeColumn.setText("协议");

        loadTable();

        Menu popupmenu = new Menu(dialog, SWT.POP_UP);
        MenuItem addPopItem = new MenuItem(popupmenu, SWT.PUSH);
        addPopItem.setText("新增会话");
        addPopItem.setImage(MImage.addImage);
        addPopItem.addSelectionListener(this);
        MenuItem editPopItem = new MenuItem(popupmenu, SWT.PUSH);
        editPopItem.setText("编辑会话");
        editPopItem.setImage(MImage.editImage);
        editPopItem.addSelectionListener(this);
        MenuItem deletePopItem = new MenuItem(popupmenu, SWT.PUSH);
        deletePopItem.setText("删除会话");
        deletePopItem.setImage(MImage.deleteImage);
        deletePopItem.addSelectionListener(this);
        table.setMenu(popupmenu);

        //button
        addButton = new Button(dialog, SWT.LEFT);
        addButton.setBounds(X_POS, 5, 80, 27);
        addButton.setText("新增   ");
        addButton.setImage(MImage.addImage);
        addButton.setToolTipText("新增连接");
        addButton.addSelectionListener(this);

        editButton = new Button(dialog, SWT.LEFT);
        editButton.setBounds(X_POS, 38, 80, 27);
        editButton.setText("编辑 ");
        editButton.setImage(MImage.editImage);
        editButton.setToolTipText("编辑所选连接");
        editButton.addSelectionListener(this);

        deleteButton = new Button(dialog, SWT.LEFT);
        deleteButton.setBounds(X_POS, 70, 80, 27);
        deleteButton.setText("删除");
        deleteButton.setImage(MImage.deleteImage);
        deleteButton.setToolTipText("删除选定的连接");
        deleteButton.addSelectionListener(this);

//		puttyWindow = new Button(dialog, SWT.LEFT);
//		puttyWindow.setBounds(X_POS, 103, 80, 27);
//		puttyWindow.setText("Putty");
//		puttyWindow.setImage(MImage.puttyImage);
//		puttyWindow.setToolTipText("Open selected connection in a single window");
//		puttyWindow.addSelectionListener(this);

        connectButton = new Button(dialog, SWT.NONE);
        connectButton.setBounds(X_POS, 235, 80, 27);
        connectButton.setText("连接");
        connectButton.setImage(MImage.connectImage);
        connectButton.setToolTipText("在选项卡中打开选定的连接");
        connectButton.addSelectionListener(this);

        dialog.pack();
        dialog.open();
    }

    public ConfigSession getCurrentSelectSession() {
        if (table.getSelection().length > 0) {
            return (ConfigSession) (table.getSelection()[0].getData("session"));
        } else {
            return null;
        }
    }

    public void loadTable() {
        table.removeAll();
        ArrayList<ConfigSession> sessions = (ArrayList<ConfigSession>) smartSessionManager.getAllCSessions();
        for (ConfigSession session : sessions) {
            TableItem tableItem = new TableItem(table, SWT.NONE);
            tableItem.setData("session", session);
            tableItem.setText(new String[] { session.getHost(), session.getPort(), session.getUsername(), session.getProtocol().getName() });
        }
    }

    /**
     * Open all selected sessions in tabs.
     */
    private void OpenSelectedSessions() {
        TableItem[] tableItems = table.getSelection();
        ArrayList<ConfigSession> sessions = new ArrayList<ConfigSession>();
        for (TableItem tableItem : tableItems) {
            ConfigSession csession = smartSessionManager.queryCSessionBySession((ConfigSession) tableItem.getData("session"));
            sessions.add(csession);
            // System.out.println("OpenSelectedSessions() " + csession); //DEBUG
        }
        dialog.dispose();

        for (ConfigSession session : sessions) {
            this.mainFrame.addSession(null, session);
        }
    }

    /**
     * Open a Putty session in a window outside program.
     */
//	private void OpenPutty(){
//		TableItem[] tableItems = table.getSelection();
//		if(tableItems!=null){
//			ConfigSession csession = smartSessionManager.queryCSessionBySession((ConfigSession) tableItems[0].getData("session"));
//			InvokeProgram.invokeSinglePutty(csession);
//			dialog.dispose();
//		}
//	}

    @Override
    public void widgetDefaultSelected(SelectionEvent arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void widgetSelected(SelectionEvent e) {
        // TODO Auto-generated method stub
        //	System.out.println(e.getSource().toString());
        if (e.getSource() == addButton) {
            new NewSessionDialog(null, this, "add");
        } else if (e.getSource() == editButton) {
            if (table.getSelection().length == 1) {
                // Only one record must be editable.
                new NewSessionDialog(null, this, "edit");
            } else {
                MessageDialog.openInformation(dialog, "Warning", "Please select one record!");
            }
        } else if (e.getSource() == deleteButton) {
            if (this.table.getSelection().length == 0) {
                MessageDialog.openInformation(dialog, "Warning", "Please select at least one record!");
                return;
            }
            TableItem[] tableItems = table.getSelection();
            for (TableItem item : tableItems) {
                ConfigSession session = (ConfigSession) item.getData("session");
                smartSessionManager.delete(session);
            }
            loadTable();
        } else if (e.getSource() == puttyWindow) {
            if (this.table.getSelection().length == 0) {
                MessageDialog.openInformation(dialog, "Warning", "Please select one record!");
                return;
            }
            //			OpenPutty();
        } else if (e.getSource() == connectButton) {
            if (this.table.getSelection().length == 0) {
                MessageDialog.openInformation(dialog, "Warning", "Please select at least one record!");
                return;
            }
            OpenSelectedSessions();
        }
    }

    @Override
    public void mouseDoubleClick(MouseEvent e) {
        // TODO Auto-generated method stub
        if (e.getSource().equals(table)) {
            OpenSelectedSessions();
        }
    }

    @Override
    public void mouseDown(MouseEvent mouseevent) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseUp(MouseEvent mouseevent) {
        // TODO Auto-generated method stub
    }
}
