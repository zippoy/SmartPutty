package com.zippoy.zshell.ui.dialog;

import com.sp.dao.SmartSessionManager;
import com.zippoy.zshell.entity.SystemConfig;
import com.zippoy.zshell.ui.MainFrame;
import com.zippoy.zshell.ui.component.MImage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统配置对话框<br>
 *
 * @author zippoy
 * @date 2025-06-06
 */
@Slf4j
public class SystemConfigDialog implements SelectionListener, MouseListener {

    private final SmartSessionManager smartSessionManager;
    final private Shell dialog;
    Table table;
    TableEditor editor;
    int EDITABLECOLUMN = 2;
    Button saveButton;

    List<SystemConfig> cachedSystemConfigList = new ArrayList<>();  // TO be updated to DB cache

    // Constructor:
    public SystemConfigDialog(Shell parent) {
        this.smartSessionManager = new SmartSessionManager();
        this.dialog = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);

        init();

        dialog.pack();
        dialog.open();
        dialog.setLocation(MainFrame.shell.getLocation());
    }

    private void init() {
        Text text = new Text(dialog, SWT.LEFT);
        text.setBounds(0, 0, 550, 27);
        text.setText("通过单击表中的值内容来修改值，按 保存 存储配置");

        saveButton = new Button(dialog, SWT.LEFT);
        saveButton.setBounds(0, 30, 80, 27);
        saveButton.setText("保存");
        saveButton.setImage(MImage.saveImage);
        saveButton.addSelectionListener(this);

        table = new Table(dialog, SWT.BORDER);
        dialog.setText("系统配置");
        dialog.setSize(350, 300);

        table = new Table(dialog, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
        table.setBounds(0, 60, 550, 300);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        table.addMouseListener(this);
        table.addSelectionListener(this);

        TableColumn tableIdColumn = new TableColumn(table, SWT.NONE);
        tableIdColumn.setWidth(50);
        tableIdColumn.setText("Id");

        TableColumn tableKeyColumn = new TableColumn(table, SWT.NONE);
        tableKeyColumn.setWidth(200);
        tableKeyColumn.setText("键");

        TableColumn tableValueColumn = new TableColumn(table, SWT.NONE);
        tableValueColumn.setWidth(300);
        tableValueColumn.setText("值");

        editor = new TableEditor(table);
        // The editor must have the same size as the cell and must
        // not be any smaller than 50 pixels.
        editor.horizontalAlignment = SWT.LEFT;
        editor.grabHorizontal = true;
        editor.minimumWidth = 50;
        // editing the second column
        final int EDITABLECOLUMN = 1;

        loadTableData();
    }

    private void loadTableData() {
        table.removeAll();
        List<SystemConfig> configList = smartSessionManager.getAllSystemConfigs();
        for (SystemConfig config : configList) {
            TableItem tableItem = new TableItem(table, SWT.NONE);
            tableItem.setData("config", config);
            tableItem.setText(new String[] { config.getId().toString(), config.getKey(), config.getValue() });
        }
    }

    private boolean isChangedConfig(SystemConfig old, String newVal) {
        return !StringUtils.equals(old.getValue(), newVal);
    }

    @Override
    public void mouseDoubleClick(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseDown(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseUp(MouseEvent mouseEvent) {

    }

    @Override
    public void widgetSelected(SelectionEvent e) {
        if (e.getSource() == table) {
            // Clean up any previous editor control
            Control oldEditor = editor.getEditor();

            if (oldEditor != null) {
                oldEditor.dispose();
            }

            // Identify the selected row
            TableItem item = (TableItem) e.item;
            if (item == null) {
                return;
            }

            // The control that will be the editor must be a child of the
            // Table
            Text newEditor = new Text(table, SWT.NONE);
            newEditor.setText(item.getText(EDITABLECOLUMN));
            newEditor.addModifyListener(me -> {
                String newText = ((Text) editor.getEditor()).getText();
                //                smartSessionManager.update(new SystemConfig(oldConfig.getId(), oldConfig.getKey(), newText));
                editor.getItem().setText(EDITABLECOLUMN, newText);
            });
            newEditor.selectAll();
            newEditor.setFocus();
            editor.setEditor(newEditor, item, EDITABLECOLUMN);
        } else if (e.getSource() == saveButton) {
            String diffMsg = "";
            for (TableItem item : table.getItems()) {
                SystemConfig oldConfig = (SystemConfig) item.getData("config");
                if (isChangedConfig(oldConfig, item.getText(2))) {
                    cachedSystemConfigList.add(new SystemConfig(oldConfig.getId(), oldConfig.getKey(), item.getText(2)));
                    diffMsg += String.format("%s %s->%s", oldConfig.getKey(), oldConfig.getValue(), item.getText(2)) + "\n";
                    log.debug("changed id:{},key:{},value:{}", item.getText(0), item.getText(1), item.getText(2));
                }
            }
            if (!cachedSystemConfigList.isEmpty()) {
                MessageBox messageBox = new MessageBox(this.dialog, SWT.ICON_QUESTION | SWT.WRAP | SWT.YES | SWT.NO);
                messageBox.setMessage("是否确定要保存更改?\n" + diffMsg);
                messageBox.setText("配置更新");
                int response = messageBox.open();
                if (response == SWT.YES) {
                    for (SystemConfig config : cachedSystemConfigList) {
                        smartSessionManager.update(config);
                    }
                }
            } else {
                MessageBox messagebox = new MessageBox(this.dialog, SWT.ICON_INFORMATION | SWT.OK);
                messagebox.setMessage("没有任何变化，请在表格内容中编辑值，然后点保存");
                messagebox.open();
            }
        }
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent selectionEvent) {

    }
}
