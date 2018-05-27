package hxckdms.hxccore.api.command;

public enum CommandState {
    ENABLED("", true),
    DISABLED("commands.sub.disabled", false);

    private final String errorText;
    private final boolean usageAllowed;

    CommandState(String errorText, boolean usageAllowed) {
        this.errorText = errorText;
        this.usageAllowed = usageAllowed;
    }

    public String getErrorText() {
        return errorText;
    }

    public boolean isUsageAllowed() {
        return usageAllowed;
    }
}
