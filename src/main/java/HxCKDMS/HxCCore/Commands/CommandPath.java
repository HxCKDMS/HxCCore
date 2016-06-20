package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.api.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.api.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.api.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import HxCKDMS.HxCCore.lib.References;
import net.minecraft.block.Block;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;

import java.io.File;
import java.util.List;

@HxCCommand(defaultPermission = 4, mainCommand = CommandsHandler.class, isEnabled = true)
public class CommandPath implements ISubCommand {
    public static CommandPath instance = new CommandPath();

    @Override
    public String getCommandName() {
        return "Path";
    }

    @Override
    public int[] getCommandRequiredParams() {
        return new int[]{0, -1, -1};
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args, boolean isPlayer) throws WrongUsageException {
        if (isPlayer) {
            EntityPlayerMP player = (EntityPlayerMP)sender;
            boolean CanSend = PermissionsHandler.canUseCommand(CommandsConfig.CommandPermissions.get(getCommandName()), player);
            String UUID = player.getUniqueID().toString();
            File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");
            if (CanSend) {
                if ((args.length >= 2 && Block.getBlockFromName(args[1]) != null) || args.length == 1) {
                    NBTFileIO.setBoolean(CustomPlayerData, "Pathing", !NBTFileIO.getBoolean(CustomPlayerData, "Pathing"));
                    NBTFileIO.setString(CustomPlayerData, "PathMat", args.length >= 2 ? args[1] : "minecraft:cobblestone");
                    NBTFileIO.setInteger(CustomPlayerData, "PathMeta", args.length >= 3 ? Integer.valueOf(args[2]) : 0);
                    NBTFileIO.setInteger(CustomPlayerData, "PathSize", args.length >= 4 ? Integer.valueOf(args[3]) : 2);
                }
            } else throw new WrongUsageException(HxCCore.util.readLangOnServer(References.MOD_ID, "commands.exception.permission"));
        } else throw new WrongUsageException(HxCCore.util.readLangOnServer(References.MOD_ID, "commands.exception.playersonly"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}