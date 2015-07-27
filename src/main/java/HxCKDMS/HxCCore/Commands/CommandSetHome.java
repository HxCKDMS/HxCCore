package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.ISubCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.io.File;
import java.util.List;

public class CommandSetHome implements ISubCommand {
    public static CommandSetHome instance = new CommandSetHome();

    @Override
    public String getCommandName() {
        return "setHome";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) {
        if(sender instanceof EntityPlayerMP){
            EntityPlayerMP player = (EntityPlayerMP)sender;
            boolean CanSend = PermissionsHandler.canUseCommand(Configurations.commands.get("SetHome"), player);
            if (CanSend) {
                String UUID = player.getUniqueID().toString();

                File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");

                NBTTagCompound home = NBTFileIO.getNbtTagCompound(CustomPlayerData, "home");
                NBTTagCompound homeDir = new NBTTagCompound();

                String hName = args.length == 1 ? "default" : args[1];

                int x = (int)player.posX;
                int y = (int)player.posY;
                int z = (int)player.posZ;
                int dim = player.dimension;

                player.addChatMessage(new ChatComponentText("\u00A72Home (" + hName + ") has been set to coordinates: X(" + x + ") Y(" + y + ") Z(" + z + ") Dimension(" + dim + ")."));

                homeDir.setInteger("x", x);
                homeDir.setInteger("y", y);
                homeDir.setInteger("z", z);
                homeDir.setInteger("dim", dim);

                home.setTag(hName, homeDir);

                NBTFileIO.setNbtTagCompound(CustomPlayerData, "home", home);
            } else  sender.addChatMessage(new ChatComponentText("\u00A74You do not have permission to use this command."));
        } else sender.addChatMessage(new ChatComponentText("\u00A74This command can only be executed by a player."));
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
