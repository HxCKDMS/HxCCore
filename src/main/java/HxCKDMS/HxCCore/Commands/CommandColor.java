package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import HxCKDMS.HxCCore.lib.References;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.StatCollector;

import java.io.File;
import java.util.List;

@HxCCommand(defaultPermission = 1, mainCommand = CommandMain.class)
public class CommandColor implements ISubCommand {
    public static CommandColor instance = new CommandColor();

    @Override
    public String getCommandName() {
        return "Color";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) throws WrongUsageException {
        if(sender instanceof EntityPlayerMP){
            EntityPlayerMP player = (EntityPlayerMP) sender;
            String UUID = player.getUniqueID().toString();
            File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");
            boolean CanSend = PermissionsHandler.canUseCommand(CommandsConfig.commands.get("Color"), player);
            if (CanSend) {
                char color = 'f';
                if (args.length >= 2) color = args[1].charAt(0);
                if (References.COLOR_CHARS.contains(color) && !Configurations.bannedColorCharacters.contains(color)) NBTFileIO.setString(CustomPlayerData, "Color", String.valueOf(color));
                else throw new WrongUsageException("commands." + getCommandName().toLowerCase() + ".usage");
            } else throw new WrongUsageException(StatCollector.translateToLocal("command.exception.permission"));
        } else throw new WrongUsageException(StatCollector.translateToLocal("command.exception.playersonly"));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return References.COLOR_CHARS_STRING;
    }
}
