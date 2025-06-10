package com.zippoy.zshell.ui;

import com.zippoy.zshell.config.ConfigService;
import com.zippoy.zshell.ui.component.AppToolbar;
import com.zippoy.zshell.ui.component.BottomToolbar;
import com.zippoy.zshell.ui.component.ConnectionToolbar;
import com.zippoy.zshell.ui.component.MainMenu;
import com.zippoy.zshell.ui.component.TabManager;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <br>
 *
 * @author zippoy
 * @date 2025-06-09
 */
@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Context {

    private static final ThreadLocal<Context> CONTEXT_THREAD_LOCAL = new ThreadLocal<>();

    public static Context get() {
        return CONTEXT_THREAD_LOCAL.get();
    }

    public static void set(final Context context) {
        CONTEXT_THREAD_LOCAL.set(context);
    }

    private MainMenu mainMenu;
    private ConnectionToolbar connectionToolbar;
    private AppToolbar appToolbar;
    private BottomToolbar bottomToolbar;
    private TabManager tabManager;

    /**
     * config.properties系统配置
     */
    private ConfigService config;

}
