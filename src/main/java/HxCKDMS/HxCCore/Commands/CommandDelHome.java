package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@HxCCommand(defaultPermission = 4, mainCommand = CommandsHandler.class, isEnabled = true)
public class CommandDelHome implements ISubCommand {
    public static CommandDelHome instance = new CommandDelHome();

    @Override
    public String getCommandName() {
        return "DelHome";
    }

    @Override
    public int[] getCommandRequiredParams() {
        return new int[]{0, -1, -1};
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args, boolean isPlayer) throws WrongUsageException {
        if (isPlayer) {
            EntityPlayerMP player = (EntityPlayerMP)sender;
            boolean CanSend = PermissionsHandler.canUseCommand(CommandsConfig.CommandPermissions.get("DelHome"), player);
            if (CanSend) {
                String UUID = player.getUniqueID().toString();
                File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");

                NBTTagCompound home = NBTFileIO.getNbtTagCompound(CustomPlayerData, "home");

                String hName = args.length == 1 ? "home" : args[1];
                if (home.hasKey(hName)) {
                    home.removeTag(hName);

                    ChatComponentText msg = new ChatComponentText("Home (" + hName + ") has been deleted.");
                    msg.getChatStyle().setColor(EnumChatFormatting.DARK_RED);
                    player.addChatMessage(msg);

                    NBTFileIO.setNbtTagCompound(HxCCore.CustomWorldData, "home", home);
                } else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.home"));
            } else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.permission"));
        } else throw new WrongUsageException(StatCollector.translateToLocal("commands.exception.playersonly"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 2) {
            EntityPlayerMP player = (EntityPlayerMP) sender;
            String UUID = player.getUniqueID().toString();
            File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");
            NBTTagCompound home = NBTFileIO.getNbtTagCompound(CustomPlayerData, "home");
            return new LinkedList<>((Set<String>) home.getKeySet());
        }
        return null;
    }
}