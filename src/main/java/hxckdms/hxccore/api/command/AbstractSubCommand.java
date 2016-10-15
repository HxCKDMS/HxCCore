package hxckdms.hxccore.api.command;

public abstract class AbstractSubCommand implements ISubCommand {
    protected int permissionLevel = 1;
    protected CommandState state = CommandState.ENABLED;

    @Override
    public int getPermissionLevel() {
        return permissionLevel;
    }

    @Override
    public CommandState getCommandState() {
        return state;
    }

    @Override
    public void setPermissionLevel(int level) {
        permissionLevel = level;
    }

    @Override
    public void setCommandState(CommandState state) {
        this.state = state;
    }
}
