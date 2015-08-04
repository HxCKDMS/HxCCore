package HxCKDMS.HxCCore.Events;

import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.lib.References;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.util.EventListener;
import java.util.UUID;

import static HxCKDMS.HxCCore.Handlers.NickHandler.getMessageHeader;
import static HxCKDMS.HxCCore.lib.References.CC;

@SuppressWarnings("unused")
public class EventChat implements EventListener {

    @SubscribeEvent
    public void onServerChatEvent(ServerChatEvent event) {
        UUID UUID = event.player.getUniqueID();
        File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID.toString() + ".dat");
        if(!CustomPlayerData.exists()) return;

        String playerColor = NBTFileIO.getString(CustomPlayerData, "Color");

        String ChatColor;
        if (playerColor.equalsIgnoreCase("") || playerColor.equalsIgnoreCase("f"))
            ChatColor = CC + "f";
        else
            ChatColor = CC + playerColor;

        String[] tmp = event.message.split(" ");
        String tmp2 = "";
        for (String str : tmp) {
            if (str.startsWith("&")) {
                String[] mg = str.split("&");
                for (String str2 : mg) {
                    if (str2.length() >= 2) {
                        ChatColor = ChatColor + CC + str2.charAt(0);
                        str2 = str2.substring(1);
                        tmp2 = tmp2 + ChatColor + str2;
                    } else {
                        if (!str2.trim().isEmpty())
                            ChatColor = ChatColor + CC + str2.charAt(0);
                    }
                }
            }
            else tmp2 = tmp2 + " " + ChatColor + str;
        }
        if (!tmp2.replaceAll(References.CC, "").trim().isEmpty())
            event.setComponent(new ChatComponentTranslation(String.format(Configurations.formats.get("ChatFormat"), getMessageHeader(event.player), tmp2.trim().replaceAll("%", "%%"))));
    }
}