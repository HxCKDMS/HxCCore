package HxCKDMS.HxCCore.Events;

import HxCKDMS.HxCCore.Configs.Config;
import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.HxCCore;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.event.ServerChatEvent;

import java.io.File;
import java.util.EventListener;
import java.util.UUID;

@SuppressWarnings("unused")
public class EventChat implements EventListener {
    
    public final static String CC = "\u00A7";

    @SubscribeEvent
    public void onServerChatEvent(ServerChatEvent event){
        UUID UUID = event.player.getUniqueID();
        File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID.toString() + ".dat");
        String nick = NBTFileIO.getString(CustomPlayerData, "nickname");
        String playerColor = NBTFileIO.getString(CustomPlayerData, "Color");
        String message = event.message;
        File PermissionsData = new File(HxCCore.HxCCoreDir, "HxC-Permissions.dat");
        NBTTagCompound Permissions = NBTFileIO.getNbtTagCompound(PermissionsData, "Permissions");
        int SenderPermLevel = Permissions.getInteger(event.player.getDisplayName());

        //Color Code

        String rawGroup;
        switch(SenderPermLevel){
            case 1:
                rawGroup = CC + Config.PermLevel1Color + Config.PermLevel1Name;
                break;
            case 2:
                rawGroup = CC + Config.PermLevel2Color + Config.PermLevel2Name;
                break;
            case 3:
                rawGroup = CC + Config.PermLevel3Color + Config.PermLevel3Name;
                break;
            case 4:
                rawGroup = CC + Config.PermLevel4Color + Config.PermLevel4Name;
                break;
            case 5:
                rawGroup = CC + Config.PermLevel5Color + Config.PermLevel5Name;
                break;
            default:
                rawGroup = CC + Config.PermLevel0Color + Config.PermLevel0Name;
                break;
        }

        String tag = "";
        if(HxCCore.coders.contains(UUID))
            tag = String.format(Config.TagFormat, "&bHxC");
        else if(HxCCore.supporters.contains(UUID))
            tag = String.format(Config.TagFormat, "&4HxC Supporter");
        else if(HxCCore.helpers.contains(UUID))
            tag = String.format(Config.TagFormat, "&aHxC Helper");
        else if(HxCCore.artists.contains(UUID))
            tag = String.format(Config.TagFormat, "&cHxC Artist");

        boolean isOpped = event.player.mcServer.getConfigurationManager().func_152596_g(event.player.getGameProfile());

        String opped = CC + "4" + event.player.getDisplayName() + CC + "f";
        String name = event.player.getDisplayName();
        String nicked = nick;

        if (Config.GroupInChat) {
            opped = String.format(Config.GroupFormat, rawGroup) + opped;
            name = String.format(Config.GroupFormat, rawGroup) + name;
            nicked = String.format(Config.GroupFormat, rawGroup) + nicked;
        }

        if(Config.TagInChat && !tag.equalsIgnoreCase("")){
            opped = tag + opped;
            name = tag + name;
            nicked = tag + nicked;
        }

        String ChatColor;
        if (playerColor.equalsIgnoreCase("") || playerColor.equalsIgnoreCase("f")) {
            ChatColor = CC + "f";
        } else {
            ChatColor = CC + playerColor;
        }

        nicked = nicked.replace("&", CC) + CC + "f";
        message = message.replace("&", CC);

        if(isOpped && nick.equals(CC + "f"))
            event.component = new ChatComponentTranslation(String.format(Config.ChatFormat, opped, ChatColor + message));
        else if(nick.equals(CC + "f"))
            event.component = new ChatComponentTranslation(String.format(Config.ChatFormat, name, ChatColor + message));
        else
            event.component = new ChatComponentTranslation(String.format(Config.ChatFormat, nicked, ChatColor + message));
    }
}
