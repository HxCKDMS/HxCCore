package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import net.minecraft.block.Block;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.StatCollector;

import java.io.File;
import java.util.List;

@HxCCommand(defaultPermission = 4, mainCommand = CommandMain.class)
public class CommandPath implements ISubCommand {
    public static CommandPath instance = new CommandPath();

    @Override
    public String getCommandName() {
        return "Path";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) throws WrongUsageException {
        if (sender instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP)sender;
            boolean CanSend = PermissionsHandler.canUseCommand(CommandsConfig.commands.get("Path"), player);
            String UUID = player.getUniqueID().toString();
            File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");
            if (CanSend) {
                if ((args.length >= 2 && Block.getBlockFromName(args[1]) != null) || args.length == 1) {
                    NBTFileIO.setBoolean(CustomPlayerData, "Pathing", !NBTFileIO.getBoolean(CustomPlayerData, "Pathing"));
                    NBTFileIO.setString(CustomPlayerData, "PathMat", args.length >= 2 ? args[1] : "minecraft:cobblestone");
                }
            } else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.permission"));
        } else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.playersonly"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}