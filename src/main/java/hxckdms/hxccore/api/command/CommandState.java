package hxckdms.hxccore.api.command;

import net.minecraft.util.text.TextComponentTranslation;

public enum CommandState {
    ENABLED(null, true),
    DISABLED(new TextComponentTranslation("commands.sub.disabled"), false);

    private final TextComponentTranslation errorText;
    private final boolean usageAllowed;

    CommandState(TextComponentTranslation errorText, boolean usageAllowed) {
        this.errorText = errorText;
        this.usageAllowed = usageAllowed;
    }

    public TextComponentTranslation getErrorText() {
        return errorText;
    }

    public boolean isUsageAllowed() {
        return usageAllowed;
    }
}
