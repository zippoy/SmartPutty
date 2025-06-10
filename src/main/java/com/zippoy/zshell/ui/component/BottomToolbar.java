package com.zippoy.zshell.ui.component;

import com.zippoy.zshell.config.ConfigService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * 底部工具栏<br>
 *
 * @author zippoy
 * @date 2025-06-06
 */
public class BottomToolbar {

    private final Group quickBottomGroup;
    private final Text pathItem;
    private final Text dictText;
    private final Button win2UnixButton;
    private final Button unix2WinButton;
    private final Button dictButton;

    public BottomToolbar(Shell shell, ConfigService configService) {
        quickBottomGroup = new Group(shell, SWT.BAR);

        RowLayout layout = new RowLayout();
        layout.marginTop = 3;
        layout.marginBottom = 3;
        layout.marginLeft = 1;
        layout.marginRight = 1;
        layout.spacing = 5;
        layout.wrap = false;
        layout.center = true;
        quickBottomGroup.setLayout(layout);

        pathItem = createValueText("路径", SWT.BORDER, 250, 20, "", "");
        win2UnixButton = createButton("->Linux", MImage.linux, "将 Windows 路径转换为 Linux");
        unix2WinButton = createButton("->Windows", MImage.windows, "将 Linux 路径转换为 Windows");

        dictText = createValueText("目录", SWT.BORDER, 100, 20, "", "");
        dictText.addListener(SWT.Traverse, (event) -> {
            if (event.detail == SWT.TRAVERSE_RETURN) {
                String keyword = dictText.getText().trim();
                OpenDictTab(keyword);
            }
        });

        dictButton = createButton("搜索", MImage.dictImage, "在词典中搜索关键词");

        quickBottomGroup.pack();
    }

    private void OpenDictTab(String keyword) {
        throw new UnsupportedOperationException("BottomToolbar");
    }

    public void setVisible(Boolean visible) {
        quickBottomGroup.setVisible(visible);
    }

    /**
     * 创建含提示的Text
     */
    private Text createMsgText(String label, int style, int width, int height, String msg, String tipText) {
        new Label(quickBottomGroup, SWT.RIGHT).setText(label);
        Text item = new Text(quickBottomGroup, style);
        item.setLayoutData(new RowData(width, height));
        item.setMessage(msg);
        item.setToolTipText(tipText);
        return item;
    }

    /**
     * 创建有默认值的Text
     */
    private Text createValueText(String label, int style, int width, int height, String defaultText, String tipText) {
        new Label(quickBottomGroup, SWT.RIGHT).setText(label);
        Text item = new Text(quickBottomGroup, style);
        item.setLayoutData(new RowData(width, height));
        item.setText(defaultText);
        item.setToolTipText(tipText);
        return item;
    }

    /**
     * 创建按钮
     */
    private Button createButton(String text, Image image, String tipText) {
        return createButton(text, image, SWT.DEFAULT, SWT.DEFAULT, tipText);
    }

    /**
     * 创建按钮
     */
    private Button createButton(String text, Image image, int width, int height, String tipText) {
        Button button = new Button(quickBottomGroup, SWT.PUSH);
        button.setText(text);
        button.setImage(image);
        button.setLayoutData(new RowData(width, height));
        button.setToolTipText(tipText);
        return button;
    }


    public void setLayoutData(Object layoutData) {
        quickBottomGroup.setLayoutData(layoutData);
    }

}
