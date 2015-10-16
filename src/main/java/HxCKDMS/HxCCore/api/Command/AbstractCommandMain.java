package HxCKDMS.HxCCore.api.Command;

import net.minecraft.command.CommandBase;

public abstract class AbstractCommandMain extends CommandBase {
    public abstract boolean registerSubCommand(ISubCommand subCommand);
    public abstract AbstractCommandMain getInstance();
}
