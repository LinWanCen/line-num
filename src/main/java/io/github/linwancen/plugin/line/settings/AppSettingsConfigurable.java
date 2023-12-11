package io.github.linwancen.plugin.line.settings;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class AppSettingsConfigurable implements Configurable {

    @SuppressWarnings("NotNullFieldNotInitialized")
    @NotNull
    private AppSettingsComponent mySettingsComponent;

    @NotNull
    @Override
    public String getDisplayName() {
        return "Line Num";
    }

    @NotNull
    @Override
    public JComponent getPreferredFocusedComponent() {
        return mySettingsComponent.getPreferredFocusedComponent();
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        mySettingsComponent = new AppSettingsComponent();
        return mySettingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        @NotNull AppSettingsState settings = AppSettingsState.getInstance();
        boolean modified = mySettingsComponent.getChildNum() != settings.childNum;
        modified |= mySettingsComponent.getFileLine() != settings.fileLine;
        modified |= mySettingsComponent.getMethodLine() != settings.methodLine;

        return modified;
    }

    @Override
    public void apply() {
        @NotNull AppSettingsState settings = AppSettingsState.getInstance();
        settings.childNum = mySettingsComponent.getChildNum();
        settings.fileLine = mySettingsComponent.getFileLine();
        settings.methodLine = mySettingsComponent.getMethodLine();
    }

    @Override
    public void reset() {
        @NotNull AppSettingsState settings = AppSettingsState.getInstance();
        reset(settings, mySettingsComponent);
    }

    static void reset(@NotNull AppSettingsState settings, @NotNull AppSettingsComponent mySettingsComponent) {
        mySettingsComponent.setChildNum(settings.childNum);
        mySettingsComponent.setFileLine(settings.fileLine);
        mySettingsComponent.setMethodLine(settings.methodLine);
    }

    @Override
    public void disposeUIResources() {
        //noinspection ConstantConditions
        mySettingsComponent = null;
    }

}
