package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.ISubCommand;
import net.minecraft.block.Block;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

import java.io.File;
import java.util.List;

public class CommandPath implements ISubCommand {
    public static CommandPath instance = new CommandPath();

    @Override
    public String getName() {
        return "path";
    }

    @Override
    public void execute(ICommandSender sender, String[] args) {
        if(sender instanceof EntityPlayerMP){
            EntityPlayerMP player = (EntityPlayerMP)sender;
            boolean CanSend = PermissionsHandler.canUseCommand(Configurations.commands.get("Path"), player);
            String UUID = player.getUniqueID().toString();
            File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");
            if (CanSend) {
                if ((args.length >= 2 && Block.getBlockFromName(args[1]) != null) || args.length == 1) {
                    NBTFileIO.setBoolean(CustomPlayerData, "Pathing", !NBTFileIO.getBoolean(CustomPlayerData, "Pathing"));
                    NBTFileIO.setString(CustomPlayerData, "PathMat", args.length >= 2 ? args[1] : "minecraft:cobblestone");
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}