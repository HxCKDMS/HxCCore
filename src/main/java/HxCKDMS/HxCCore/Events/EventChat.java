package HxCKDMS.HxCCore.Events;

import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.HxCCore;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.event.ServerChatEvent;

import java.io.File;
import java.util.EventListener;
import java.util.UUID;

import static HxCKDMS.HxCCore.lib.References.CC;
import static HxCKDMS.HxCCore.Handlers.NickHandler.getMessageHeader;

@SuppressWarnings("unused")
public class EventChat implements EventListener {

    @SubscribeEvent
    public void onServerChatEvent(ServerChatEvent event) {
        UUID UUID = event.player.getUniqueID();
        File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID.toString() + ".dat");
        if(!CustomPlayerData.exists()) return;

        String playerColor = NBTFileIO.getString(CustomPlayerData, "Color");
        String message = event.message;


        String ChatColor;
        if (playerColor.equalsIgnoreCase("") || playerColor.equalsIgnoreCase("f"))
            ChatColor = CC + "f";
        else
            ChatColor = CC + playerColor;

        event.setComponent(new ChatComponentTranslation(String.format(Configurations.formats.get("ChatFormat"), getMessageHeader(event.player), ChatColor + message)));
    }
}