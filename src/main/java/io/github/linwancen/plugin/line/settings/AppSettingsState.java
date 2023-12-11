package io.github.linwancen.plugin.line.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
        name = "io.github.linwancen.plugin.line.settings.AppSettingsState",
        storages = @Storage("LineNumGlobal.xml")
)
public class AppSettingsState implements PersistentStateComponent<AppSettingsState> {

    public static final AppSettingsState DEFAULT_SETTING = new AppSettingsState();

    public boolean childNum = true;
    public boolean fileLine = true;
    public boolean methodLine = true;

    @NotNull
    public static AppSettingsState getInstance() {
        AppSettingsState service = ApplicationManager.getApplication().getService(AppSettingsState.class);
        if (service == null) {
            return new AppSettingsState();
        }
        return service;
    }

    @Nullable
    @Override
    public AppSettingsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull AppSettingsState state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
