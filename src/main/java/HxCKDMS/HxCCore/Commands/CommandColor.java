package HxCKDMS.HxCCore.Commands;

import java.io.File;
import java.util.List;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.Utils.LogHelper;
import HxCKDMS.HxCCore.network.MessageColor;
import HxCKDMS.HxCCore.renderers.RenderHxCPlayer;

public class CommandColor implements ISubCommand {
    public static CommandColor instance = new CommandColor();
    
    @Override
    public String getCommandName() {
        return "color";
    }
    
    @Override
    public void handleCommand(ICommandSender sender, String[] args) {
        EntityPlayerMP player = (EntityPlayerMP) sender;
        String UUID = player.getUniqueID().toString();
        
        //File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");
        char color = args.length == 1 ? 'f' : args[1].toCharArray()[0];
        if ((color < 'a' || color > 'f') && (color < '0' || color > '9')) color = 'f';
        LogHelper.info(FMLCommonHandler.instance().getSide().toString(), "HxCCore");
        if (MinecraftServer.getServer().isDedicatedServer()) {
            HxCCore.network.sendToServer(new MessageColor.Message(UUID, color));
        } else {
            // Will be buggy if LAN network has people logging on/off (names will lose their colors on reboot)
            HxCCore.network.sendToAll(new MessageColor.Message(UUID, color));
        }
    }
    
    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}
