package hxckdms.hxccore.api.command;

import java.lang.reflect.ParameterizedType;

public abstract class AbstractSubCommand<T extends AbstractMultiCommand> implements ISubCommand<T> {
    protected int permissionLevel = 1;
    protected CommandState state = CommandState.ENABLED;
    @SuppressWarnings("unchecked")
    protected Class<T> parentCommand = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    @Override
    public int getPermissionLevel() {
        return permissionLevel;
    }

    @Override
    public CommandState getState() {
        return state;
    }

    @Override
    public void setPermissionLevel(int level) {
        permissionLevel = level;
    }

    @Override
    public void setState(CommandState state) {
        this.state = state;
    }

    @Override
    public Class<T> getParent() {
        return parentCommand;
    }
}
