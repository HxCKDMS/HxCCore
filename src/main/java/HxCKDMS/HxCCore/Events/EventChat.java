package HxCKDMS.HxCCore.Events;

import HxCKDMS.HxCCore.Configs.Config;
import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.HxCCore;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.event.ServerChatEvent;

import java.io.File;
import java.util.EventListener;

public class EventChat implements EventListener {

    @SubscribeEvent
    public void onServerChatEvent(ServerChatEvent event){
        String UUID = event.player.getUniqueID().toString();
        File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");
        String nick = NBTFileIO.getString(CustomPlayerData, "nickname");
        String message = event.message;

        boolean isOpped = event.player.mcServer.getConfigurationManager().func_152596_g(event.player.getGameProfile());

        nick = nick.replace("&", "\u00A7") + "\u00A7f";
        message = message.replace("&", "\u00A7");

        String nameOP = "\u00A74" + event.player.getDisplayName() + "\u00A7f";

        if(isOpped && nick.equals("\u00A7f"))
            event.component = new ChatComponentTranslation(String.format(Config.ChatFormat, nameOP, message));
        else if(nick.equals("\u00A7f"))
            event.component = new ChatComponentTranslation(String.format(Config.ChatFormat, event.player.getDisplayName(), message));
        else
            event.component = new ChatComponentTranslation(String.format(Config.ChatFormat, nick, message));
    }
}
