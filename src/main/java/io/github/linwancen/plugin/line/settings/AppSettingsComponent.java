package io.github.linwancen.plugin.line.settings;

import com.intellij.ui.components.JBCheckBox;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class AppSettingsComponent {

    private final JPanel myMainPanel;
    private final JBCheckBox childNum = new JBCheckBox(I18n.message("child.num"));
    private final JBCheckBox fileLine = new JBCheckBox(I18n.message("file.line"));
    private final JBCheckBox methodLine = new JBCheckBox(I18n.message("method.line"));

    public AppSettingsComponent() {
        myMainPanel = FormBuilder.createFormBuilder()
                .addComponent(childNum, 1)
                .addComponent(fileLine, 1)
                .addComponent(methodLine, 1)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    public JPanel getPanel() {
        return myMainPanel;
    }

    @NotNull
    public JComponent getPreferredFocusedComponent() {
        return childNum;
    }

    public boolean getChildNum() {
        return childNum.isSelected();
    }

    public void setChildNum(boolean newStatus) {
        childNum.setSelected(newStatus);
    }

    public boolean getFileLine() {
        return fileLine.isSelected();
    }

    public void setFileLine(boolean newStatus) {
        fileLine.setSelected(newStatus);
    }

    public boolean getMethodLine() {
        return methodLine.isSelected();
    }

    public void setMethodLine(boolean newStatus) {
        methodLine.setSelected(newStatus);
    }
}
