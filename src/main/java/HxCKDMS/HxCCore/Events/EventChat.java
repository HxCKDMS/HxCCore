package HxCKDMS.HxCCore.Events;

import HxCKDMS.HxCCore.Configs.Config;
import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.HxCCore;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.util.EventListener;
import java.util.UUID;

import static HxCKDMS.HxCCore.Handlers.NickHandler.CC;
import static HxCKDMS.HxCCore.Handlers.NickHandler.getPlayerNickName;

@SuppressWarnings("unused")
public class EventChat implements EventListener {


    @SubscribeEvent
    public void onServerChatEvent(ServerChatEvent event){
        UUID UUID = event.player.getUniqueID();
        File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID.toString() + ".dat");
        String nick = NBTFileIO.getString(CustomPlayerData, "nickname");
        String playerColor = NBTFileIO.getString(CustomPlayerData, "Color");
        String message = event.message;


        String ChatColor;
        if (playerColor.equalsIgnoreCase("") || playerColor.equalsIgnoreCase("f")) {
            ChatColor = CC + "f";
        } else {
            ChatColor = CC + playerColor;
        }
        message = message.replace("&", CC).replace("%", "%%");

        event.setComponent(new ChatComponentTranslation(String.format(Config.ChatFormat, getPlayerNickName(event.player), ChatColor + message)));
    }
}
