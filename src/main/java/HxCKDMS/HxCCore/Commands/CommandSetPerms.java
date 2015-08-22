package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.ISubCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

import java.io.File;
import java.util.List;

import static HxCKDMS.HxCCore.lib.References.*;

public class CommandSetPerms implements ISubCommand {
    public static CommandSetPerms instance = new CommandSetPerms();
    @Override
    public String getCommandName() {
        return "setPerms";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) throws WrongUsageException {
        if (sender instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) sender;
            File PermissionsData = new File(HxCCore.HxCCoreDir, "HxC-Permissions.dat");
            NBTTagCompound Permissions = NBTFileIO.getNbtTagCompound(PermissionsData, "Permissions");
            boolean CanUse = PermissionsHandler.canUseCommand(Configurations.commands.get("SetPerms"), player);
            if (CanUse) {
                if (args.length == 3) {
                    String playerName = args[1];
                    int permLevel = Integer.parseInt(args[2]);
                    if (permLevel > Configurations.Permissions.size() || permLevel < 0)
                        throw new WrongUsageException(StatCollector.translateToLocal("commands." + getCommandName() + ".usage"));

                    Permissions.setInteger(playerName, permLevel);
                    player.addChatMessage(new ChatComponentText(CC + "6" + playerName + "'s" + CC + "6 Permissions Level was set to " + CC + PERM_COLOURS[permLevel] + PERM_NAMES[permLevel] + CC + "6."));

                    NBTFileIO.setNbtTagCompound(PermissionsData, "Permissions", Permissions);
                } else throw new WrongUsageException("commands.exception.permission");
            } else throw new WrongUsageException("commands.exception.permission");
        } else {
            File PermissionsData = new File(HxCCore.HxCCoreDir, "HxC-Permissions.dat");
            NBTTagCompound Permissions = NBTFileIO.getNbtTagCompound(PermissionsData, "Permissions");
            if (args.length == 3) {
                String playerName = args[1];
                int permLevel = Integer.parseInt(args[2]);
                if (permLevel > Configurations.Permissions.size() || permLevel < 0)
                    throw new WrongUsageException(StatCollector.translateToLocal("commands." + getCommandName() + ".usage"));

                Permissions.setInteger(playerName, permLevel);
                sender.addChatMessage(new ChatComponentText(playerName + "'s Permissions Level was set to " + PERM_NAMES[permLevel] + "."));

                NBTFileIO.setNbtTagCompound(PermissionsData, "Permissions", Permissions);
            } else throw new WrongUsageException("command."+getCommandName()+".usage");
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
