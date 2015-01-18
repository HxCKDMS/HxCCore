package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.HxCCore;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
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

            File PermissionsData = new File(HxCCore.HxCCoreDir, "HxC-Permissions.dat");
            NBTTagCompound Permissions = NBTFileIO.getNbtTagCompound(PermissionsData, "Permissions");
            int SenderPermLevel = Permissions.getInteger(player.getDisplayName());
            boolean isopped = HxCCore.server.getConfigurationManager().func_152596_g(player.getGameProfile());
            if (SenderPermLevel >= 0 || isopped) {
                String UUID = player.getUniqueID().toString();

                File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");

                NBTTagCompound home = NBTFileIO.getNbtTagCompound(CustomPlayerData, "home");
                NBTTagCompound homeDir = new NBTTagCompound();

                String hName = args.length == 1 ? "default" : args[1];

                int x = (int)player.posX;
                int y = (int)player.posY;
                int z = (int)player.posZ;
                int dim = player.dimension;

                homeDir.setInteger("x", x);
                homeDir.setInteger("y", y);
                homeDir.setInteger("z", z);
                homeDir.setInteger("dim", dim);

                home.setTag(hName, homeDir);

                NBTFileIO.setNbtTagCompound(CustomPlayerData, "home", home);
            } else {
                sender.addChatMessage(new ChatComponentText("\u00A74You do not have permission to use this command."));
            }
        } else {
            sender.addChatMessage(new ChatComponentText("\u00A74This command can only be executed by a player."));
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}
