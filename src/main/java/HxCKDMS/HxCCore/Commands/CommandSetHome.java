package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.ISubCommand;
import HxCKDMS.HxCCore.lib.References;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.io.File;
import java.util.List;

public class CommandSetHome implements ISubCommand {
    public static CommandSetHome instance = new CommandSetHome();

    @Override
    public String getCommandName() {
        return "setHome";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) throws WrongUsageException {
        if (sender instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP)sender;
            boolean CanSend = PermissionsHandler.canUseCommand(Configurations.commands.get("SetHome"), player);
            int pl = PermissionsHandler.permLevel(player);
            if (CanSend) {
                String UUID = player.getUniqueID().toString();
                File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");

                NBTTagCompound home = NBTFileIO.getNbtTagCompound(CustomPlayerData, "home");
                NBTTagCompound homeDir = new NBTTagCompound();

                String oldhomes = home.getString("homesList");

                String hName = args.length == 1 ? "default" : args[1];


                if (oldhomes.isEmpty())
                    oldhomes = hName;
                if (!oldhomes.contains(hName))
                    oldhomes = oldhomes + ", " + hName;

                if (References.HOMES[pl] != -1 && oldhomes.split(", ").length > References.HOMES[pl]) {
                    return;
                }

                int x = (int)player.posX;
                int y = (int)player.posY;
                int z = (int)player.posZ;
                int dim = player.dimension;

                ChatComponentText msg = new ChatComponentText("Home (" + hName + ") has been set to coordinates: X(" + x + ") Y(" + y + ") Z(" + z + ") Dimension(" + dim + ").");
                msg.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
                player.addChatMessage(msg);

                homeDir.setInteger("x", x);
                homeDir.setInteger("y", y);
                homeDir.setInteger("z", z);
                homeDir.setInteger("dim", dim);

                home.setTag(hName, homeDir);

                home.setString("homesList", oldhomes);
                NBTFileIO.setNbtTagCompound(CustomPlayerData, "home", home);
            } else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.permission"));
        } else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.playersonly"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}
