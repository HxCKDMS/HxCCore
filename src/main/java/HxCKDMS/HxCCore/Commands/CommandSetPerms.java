package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import HxCKDMS.HxCCore.api.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.api.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.api.Handlers.PermissionsHandler;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.io.File;
import java.util.List;

import static HxCKDMS.HxCCore.lib.References.*;

@HxCCommand(defaultPermission = 5, mainCommand = CommandsHandler.class, isEnabled = true)
public class CommandSetPerms implements ISubCommand {
    public static CommandSetPerms instance = new CommandSetPerms();
    @Override
    public String getCommandName() {
        return "SetPerms";
    }

    @Override
    public int[] getCommandRequiredParams() {
        return new int[]{2, 2, -1};
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args, boolean isPlayer) throws WrongUsageException {
        if (sender instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) sender;
            NBTTagCompound Permissions = NBTFileIO.getNbtTagCompound(HxCCore.PermissionsData, "Permissions");
            boolean CanUse = PermissionsHandler.canUseCommand(CommandsConfig.CommandPermissions.get(getCommandName()), player);
            if (CanUse) {
                if (args.length == 3) {
                    String playerName = args[1];
                    int permLevel = Integer.parseInt(args[2]);
                    if (permLevel > Configurations.Permissions.size() || permLevel < 0)
                        throw new WrongUsageException(HxCCore.util.getTranslation((isPlayer ? ((EntityPlayerMP) sender).getUniqueID() : java.util.UUID.randomUUID()), "commands." + getCommandName().toLowerCase() + ".usage"));

                    Permissions.setInteger(playerName, permLevel);
                    player.addChatMessage(new ChatComponentText(CC + "6" + playerName + "'s" + CC + "6 Permissions Level was set to " + CC + PERM_COLOURS[permLevel] + PERM_NAMES[permLevel] + CC + "6."));

                    NBTFileIO.setNbtTagCompound(HxCCore.PermissionsData, "Permissions", Permissions);
                } else throw new WrongUsageException("commands.exception.permission");
            } else throw new WrongUsageException("commands.exception.permission");
        } else {
            File PermissionsData = new File(HxCCore.HxCCoreDir, "HxC-Permissions.dat");
            NBTTagCompound Permissions = NBTFileIO.getNbtTagCompound(PermissionsData, "Permissions");
            if (args.length == 3) {
                String playerName = args[1];
                int permLevel = Integer.parseInt(args[2]);
                if (permLevel > Configurations.Permissions.size() || permLevel < 0)
                    throw new WrongUsageException(HxCCore.util.getTranslation((isPlayer ? ((EntityPlayerMP) sender).getUniqueID() : java.util.UUID.randomUUID()), "commands." + getCommandName().toLowerCase() + ".usage"));

                Permissions.setInteger(playerName, permLevel);
                sender.addChatMessage(new ChatComponentText(playerName + "'s Permissions Level was set to " + PERM_NAMES[permLevel] + "."));

                NBTFileIO.setNbtTagCompound(PermissionsData, "Permissions", Permissions);
            } else throw new WrongUsageException("commands." + getCommandName().toLowerCase() + ".usage");
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
