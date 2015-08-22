package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.ISubCommand;
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

public class CommandList implements ISubCommand {
    public static CommandList instance = new CommandList();

    @Override
    public String getCommandName() {
        return "list";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) throws WrongUsageException, PlayerNotFoundException {
        if (sender instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) sender;
            String UUID = player.getUniqueID().toString();
            File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");
            File CustomWorldData = new File(HxCCore.HxCCoreDir, "HxCWorld.dat");

            NBTTagCompound home = NBTFileIO.getNbtTagCompound(CustomPlayerData, "home");
            ChatComponentText homes = new ChatComponentText(home.getString("homesList"));
            NBTTagCompound warp = NBTFileIO.getNbtTagCompound(CustomWorldData, "warp");
            ChatComponentText warps = new ChatComponentText(warp.getString("warpsList"));
            homes.getChatStyle().setColor(EnumChatFormatting.GOLD);
            warps.getChatStyle().setColor(EnumChatFormatting.LIGHT_PURPLE);
            player.addChatMessage(args[2].equals("warps") ? warps : homes);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if(args.length == 2){
            return Arrays.asList("warps", "homes");
        }
        return null;
    }
}
