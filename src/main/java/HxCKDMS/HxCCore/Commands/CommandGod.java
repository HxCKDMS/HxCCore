package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import HxCKDMS.HxCCore.lib.References;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.io.File;
import java.util.List;

@HxCCommand(defaultPermission = 3, mainCommand = CommandsHandler.class, isEnabled = true)
public class CommandGod implements ISubCommand {
    public static CommandGod instance = new CommandGod();

    @Override
    public String getCommandName() {
        return "God";
    }

    @Override
    public int[] getCommandRequiredParams() {
        return new int[]{0, 1, -1};
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args, boolean isPlayer) {
        switch(args.length) {
            case 1:
                if (sender instanceof EntityPlayer) {
                    EntityPlayerMP player = (EntityPlayerMP) sender;
                    boolean CanSend = PermissionsHandler.canUseCommand(CommandsConfig.CommandPermissions.get("God"), player);
                    if (CanSend) {
                        String UUID = player.getUniqueID().toString();
                        File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");

                        NBTFileIO.setBoolean(CustomPlayerData, "god", !NBTFileIO.getBoolean(CustomPlayerData, "god"));
                        player.addChatComponentMessage(new ChatComponentText((NBTFileIO.getBoolean(CustomPlayerData, "god") ? "\u00A76Enabled" : "\u00A76Disabled") + " god mode."));
                    } else throw new WrongUsageException(HxCCore.util.readLangOnServer(References.MOD_ID, "commands.exception.permission"));
                } else throw new WrongUsageException(HxCCore.util.readLangOnServer(References.MOD_ID, "commands.exception.playersonly"));
            break;
            case 2:
                EntityPlayerMP player = (EntityPlayerMP) sender;
                EntityPlayerMP player2 = net.minecraft.command.CommandBase.getPlayer(sender, args[1]);
                String UUID = player2.getUniqueID().toString();
                File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");

                NBTFileIO.setBoolean(CustomPlayerData, "god", !NBTFileIO.getBoolean(CustomPlayerData, "god"));
                player2.addChatMessage(new ChatComponentText(NBTFileIO.getBoolean(CustomPlayerData, "god") ? "\u00A76You suddenly feel immortal." : "\u00A76You suddenly feel mortal."));
                player.addChatComponentMessage(new ChatComponentText((NBTFileIO.getBoolean(CustomPlayerData, "god") ? "\u00A76Enabled" : "\u00A76Disabled") + " god mode for " + player2.getDisplayName()));
            break;
            default: throw new WrongUsageException(HxCCore.util.readLangOnServer(References.MOD_ID, "commands." + getCommandName().toLowerCase() + ".usage"));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if(args.length == 2){
            return CommandBase.getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
        }
        return null;
    }
}
