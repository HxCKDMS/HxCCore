package hxckdms.hxccore.api.command;

public class SubCommandConfigHandler {
    public int permissionLevel;
    public boolean enable;

    public SubCommandConfigHandler(int permissionLevel, boolean enable) {
        this.permissionLevel = permissionLevel;
        this.enable = enable;
    }

    public SubCommandConfigHandler() {}

    @Override
    public String toString() {
        return "SubCommandConfigHandler{" +
                "permissionLevel=" + permissionLevel +
                ", enable=" + enable +
                '}';
    }
}
