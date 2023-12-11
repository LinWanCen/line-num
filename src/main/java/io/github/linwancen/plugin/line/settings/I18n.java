package io.github.linwancen.plugin.line.settings;

import com.intellij.DynamicBundle;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

import java.util.function.Supplier;

public class I18n extends DynamicBundle {
    @NonNls
    public static final String BUNDLE = "messages.I18n";
    private static final I18n INSTANCE = new I18n();

    private I18n() {
        super(BUNDLE);
    }

    @NotNull
    public static String message(@NotNull @PropertyKey(resourceBundle = BUNDLE) String key,
                                 @NotNull Object... params) {
        return INSTANCE.getMessage(key, params);
    }

    @NotNull
    public static Supplier<String> messagePointer(@NotNull @PropertyKey(resourceBundle = BUNDLE) String key,
                                                  @NotNull Object... params) {
        return INSTANCE.getLazyMessage(key, params);
    }
}
