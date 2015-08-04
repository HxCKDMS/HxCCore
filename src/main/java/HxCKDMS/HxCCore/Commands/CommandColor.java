package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.ISubCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.StatCollector;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class CommandColor implements ISubCommand {
    public static CommandColor instance = new CommandColor();
    public static final List<String> validChars = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "k", "m", "n", "l");
    @Override
    public String getCommandName() {
        return "color";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) throws WrongUsageException {
        if(sender instanceof EntityPlayerMP){
            EntityPlayerMP player = (EntityPlayerMP) sender;
            String UUID = player.getUniqueID().toString();
            File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");
            boolean CanSend = PermissionsHandler.canUseCommand(Configurations.commands.get("Color"), player);
            if (CanSend) {
                char color = 'f';
                if (args.length >= 2) color = args[1].charAt(0);
                if (validChars.contains(color)) NBTFileIO.setString(CustomPlayerData, "Color", String.valueOf(color));
                else throw new WrongUsageException("commands." + getCommandName() + ".usage");
            } else throw new WrongUsageException(StatCollector.translateToLocal("command.exception.permission"));
        } else throw new WrongUsageException(StatCollector.translateToLocal("command.exception.playersonly"));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return validChars;
    }

}
