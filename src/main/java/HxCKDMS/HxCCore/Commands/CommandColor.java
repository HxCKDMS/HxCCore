package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.network.MessageColor;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class CommandColor implements ISubCommand {
    public static CommandColor instance = new CommandColor();
    
    @Override
    public String getName() {
        return "color";
    }

    @Override
    public void execute(ICommandSender sender, String[] args) {
        if(sender instanceof EntityPlayerMP){
            EntityPlayerMP player = (EntityPlayerMP) sender;
            String UUID = player.getUniqueID().toString();
            File PermissionsData = new File(HxCCore.HxCCoreDir, "HxC-Permissions.dat");
            NBTTagCompound Permissions = NBTFileIO.getNbtTagCompound(PermissionsData, "Permissions");
            int SenderPermLevel = Permissions.getInteger(player.getName());
            boolean isopped = HxCCore.server.getConfigurationManager().canSendCommands(player.getGameProfile());
            if (SenderPermLevel >= 1 || isopped) {
                char color = args.length == 1 ? 'f' : args[1].toCharArray()[0];
                if ((color < 'a' || color > 'f') && (color < '0' || color > '9')) color = 'f';
                // Will be buggy if LAN network has people logging on/off (names will lose their colors)
                HxCCore.packetPipeLine.sendToServer(new MessageColor(UUID, color));
            } else {
                sender.addChatMessage(new ChatComponentText("\u00A74You do not have permission to use this command."));
            }
        } else {
            sender.addChatMessage(new ChatComponentText("\u00A74This command can only be executed by a player."));
        }
    }
    
    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return tabCompleteOptions; 
    }
    
    private static final List<String> tabCompleteOptions = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f");
}
