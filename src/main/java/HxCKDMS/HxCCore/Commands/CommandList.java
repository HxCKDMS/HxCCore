package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import HxCKDMS.HxCCore.api.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.api.Handlers.NBTFileIO;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"unchecked", "unused"})
@HxCCommand(defaultPermission = 0, mainCommand = CommandsHandler.class, isEnabled = true)
public class CommandList implements ISubCommand {
    public static CommandList instance = new CommandList();

    @Override
    public String getCommandName() {
        return "List";
    }

    @Override
    public int[] getCommandRequiredParams() {
        return new int[]{0, 1, -1};
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args, boolean isPlayer) throws WrongUsageException, PlayerNotFoundException {
        if(args.length >= 2) {
            if (args[1].toLowerCase().contains("h")) {
                if(!(sender instanceof EntityPlayerMP)) throw new WrongUsageException(HxCCore.util.getTranslation((isPlayer ? ((EntityPlayerMP) sender).getUniqueID() : java.util.UUID.randomUUID()), "commands.exception.playersonly"));

                EntityPlayerMP player = (EntityPlayerMP) sender;
                String UUID = player.getUniqueID().toString();
                File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");
                NBTTagCompound home = NBTFileIO.getNbtTagCompound(CustomPlayerData, "home");

                if(home.func_150296_c().isEmpty()) throw new WrongUsageException(HxCCore.util.getTranslation((isPlayer ? ((EntityPlayerMP) sender).getUniqueID() : java.util.UUID.randomUUID()), "commands.exception.noHomes"));

                ChatComponentText homes = new ChatComponentText(home.func_150296_c().toString().replace("[", "").replace("]", ""));
                homes.getChatStyle().setColor(EnumChatFormatting.GOLD);
                player.addChatMessage(homes);
            } else if (args[1].toLowerCase().contains("w")) {
                File CustomWorldData = new File(HxCCore.HxCCoreDir, "HxCWorld.dat");
                NBTTagCompound warp = NBTFileIO.getNbtTagCompound(CustomWorldData, "warp");

                if(warp.func_150296_c().isEmpty()) throw new WrongUsageException(HxCCore.util.getTranslation((isPlayer ? ((EntityPlayerMP) sender).getUniqueID() : java.util.UUID.randomUUID()), "commands.exception.noWarps"));

                ChatComponentText warps = new ChatComponentText(warp.func_150296_c().toString().replace("[", "").replace("]", ""));
                warps.getChatStyle().setColor(EnumChatFormatting.LIGHT_PURPLE);
                sender.addChatMessage(warps);
            } else throw new WrongUsageException(HxCCore.util.getTranslation((isPlayer ? ((EntityPlayerMP) sender).getUniqueID() : java.util.UUID.randomUUID()), "commands." + getCommandName().toLowerCase() + ".usage"));
        }else throw new WrongUsageException(HxCCore.util.getTranslation((isPlayer ? ((EntityPlayerMP) sender).getUniqueID() : java.util.UUID.randomUUID()), "commands." + getCommandName().toLowerCase() + ".usage"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 2)
            return Arrays.asList("warps", "homes");
        return null;
    }
}
