package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import HxCKDMS.HxCCore.api.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.api.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.api.Handlers.PermissionsHandler;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@HxCCommand(defaultPermission = 4, mainCommand = CommandsHandler.class, isEnabled = true)
public class CommandDelWarp implements ISubCommand {
    public static CommandDelWarp instance = new CommandDelWarp();

    @Override
    public String getCommandName() {
        return "DelWarp";
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
            if (CanSend) {
                NBTTagCompound warp = NBTFileIO.getNbtTagCompound(HxCCore.CustomWorldData, "warp");

                String wName = args.length == 1 ? "default" : args[1];
                if (warp.hasKey(wName)) {
                    warp.removeTag(wName);

                    ChatComponentText msg = new ChatComponentText("Warp (" + wName + ") has been deleted.");
                    msg.getChatStyle().setColor(EnumChatFormatting.DARK_RED);
                    player.addChatMessage(msg);

                    NBTFileIO.setNbtTagCompound(HxCCore.CustomWorldData, "warp", warp);
                } else throw new WrongUsageException(HxCCore.util.getTranslation((isPlayer ? ((EntityPlayerMP) sender).getUniqueID() : java.util.UUID.randomUUID()), "commands.exception.warp"));
            } else throw new WrongUsageException(HxCCore.util.getTranslation((isPlayer ? ((EntityPlayerMP) sender).getUniqueID() : java.util.UUID.randomUUID()), "commands.exception.permission"));
        } else throw new WrongUsageException(HxCCore.util.getTranslation((isPlayer ? ((EntityPlayerMP) sender).getUniqueID() : java.util.UUID.randomUUID()), "commands.exception.playersonly"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 2) {
            NBTTagCompound warp = NBTFileIO.getNbtTagCompound(HxCCore.CustomWorldData, "warp");
            return new LinkedList<>((Set<String>) warp.func_150296_c());
        }
        return null;
    }
}
