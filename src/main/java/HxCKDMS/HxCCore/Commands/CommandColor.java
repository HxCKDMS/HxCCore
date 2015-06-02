package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.Config;
import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.ISubCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class CommandColor implements ISubCommand {
    public static CommandColor instance = new CommandColor();
    public static final List<String> validChars = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f");
    @Override
    public String getCommandName() {
        return "color";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) {
        if(sender instanceof EntityPlayerMP){
            EntityPlayerMP player = (EntityPlayerMP) sender;
            String UUID = player.getUniqueID().toString();
            File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");
            boolean CanSend = PermissionsHandler.canUseCommand(Config.ColorPL, player);
            if (CanSend) {
                char color = 'f';
                if (args.length >= 2) color = args[1].charAt(0);
                if (color >= 'a' || color <= 'f' || color <= '0' || color >= '9') NBTFileIO.setString(CustomPlayerData, "Color", String.valueOf(color));
                else sender.addChatMessage(new ChatComponentText("\u00A74Invalid usage. /HxC color [Color] Valid Colors are: \u00A700 \u00A711 \u00A722 \u00A733 \u00A744 \u00A755 \u00A766 \u00A777 \u00A788 \u00A799 \u00A7aa \u00A7bb \u00A7cc \u00A7dd \u00A7ee \u00A7ff"));
            } else {
                sender.addChatMessage(new ChatComponentText("\u00A74You do not have permission to use this command."));
            }
        } else {
            sender.addChatMessage(new ChatComponentText("\u00A74This command can only be executed by a player."));
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return validChars;
    }

}
