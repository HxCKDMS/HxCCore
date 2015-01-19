package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.HxCCore;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.io.File;
import java.util.List;

public class CommandSetWarp implements ISubCommand {
    public static CommandSetWarp instance = new CommandSetWarp();

    @Override
    public String getCommandName() {
        return "setWarp";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) {
        if(sender instanceof EntityPlayerMP){
            EntityPlayerMP player = (EntityPlayerMP)sender;
            File PermissionsData = new File(HxCCore.HxCCoreDir, "HxC-Permissions.dat");
            NBTTagCompound Permissions = NBTFileIO.getNbtTagCompound(PermissionsData, "Permissions");
            int SenderPermLevel = Permissions.getInteger(player.getDisplayName());
            boolean isopped = HxCCore.server.getConfigurationManager().func_152596_g(player.getGameProfile());
            if (SenderPermLevel >= 4 || isopped) {
                File HxCWorldData = new File(HxCCore.HxCCoreDir, "HxCWorld.dat");

                NBTTagCompound warp = NBTFileIO.getNbtTagCompound(HxCWorldData, "warp");
                NBTTagCompound warpDir = new NBTTagCompound();

                String wName = args.length == 1 ? "default" : args[1];

                int x = (int)player.posX;
                int y = (int)player.posY;
                int z = (int)player.posZ;
                int dim = player.dimension;

                player.addChatMessage(new ChatComponentText("\u00A72Warp point (" + wName + ") has been set to coordinates: X(" + x + ") Y(" + y + ") Z(" + z + ") Dimension(" + dim + ")."));

                warpDir.setInteger("x", x);
                warpDir.setInteger("y", y);
                warpDir.setInteger("z", z);
                warpDir.setInteger("dim", dim);

                warp.setTag(wName, warpDir);

                NBTFileIO.setNbtTagCompound(HxCWorldData, "warp", warp);
            } else {
                sender.addChatMessage(new ChatComponentText("\u00A74You do not have permission to use this command."));
            }
        }else{
            sender.addChatMessage(new ChatComponentText("\u00A74This command can only be executed by a player."));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if(args.length == 2){
            return net.minecraft.command.CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
        }
        return null;
    }
}
