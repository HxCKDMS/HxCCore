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

public class EventChat implements EventListener {
    
    public final static String CC = "\u00A7";

    @SubscribeEvent
    public void onServerChatEvent(ServerChatEvent event){
        String UUID = event.player.getUniqueID().toString();
        File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");
        String nick = NBTFileIO.getString(CustomPlayerData, "nickname");
        String playerColor = NBTFileIO.getString(CustomPlayerData, "Color");
        String message = event.message;
        File PermissionsData = new File(HxCCore.HxCCoreDir, "HxC-Permissions.dat");
        NBTTagCompound Permissions = NBTFileIO.getNbtTagCompound(PermissionsData, "Permissions");
        int SenderPermLevel = Permissions.getInteger(event.player.getDisplayName());

        //Color Code

        String rawgroup;
        switch(SenderPermLevel){
            case 1:
                rawgroup = CC + Config.PermLevel1Color + Config.PermLevel1Name;
                break;
            case 2:
                rawgroup = CC + Config.PermLevel2Color + Config.PermLevel2Name;
                break;
            case 3:
                rawgroup = CC + Config.PermLevel3Color + Config.PermLevel3Name;
                break;
            case 4:
                rawgroup = CC + Config.PermLevel4Color + Config.PermLevel4Name;
                break;
            case 5:
                rawgroup = CC + Config.PermLevel5Color + Config.PermLevel5Name;
                break;
            default:
                rawgroup = CC + Config.PermLevel0Color + Config.PermLevel0Name;
                break;
        }

        String GroupFormat = Config.GroupFormat;

        boolean isOpped = event.player.mcServer.getConfigurationManager().func_152596_g(event.player.getGameProfile());

        nick = nick.replace("&", CC) + CC + "f";
        message = message.replace("&", CC);
        String boxedgroup = GroupFormat.replace("%g", rawgroup);
        String formattedgroup = boxedgroup.replace("&", CC);

        String nameOP = CC + "4" + event.player.getDisplayName() + CC + "f";

        String opped;
        String name;
        String nicked;

        if (Config.GroupInChat) {
            opped = formattedgroup + nameOP;
            name = formattedgroup + event.player.getDisplayName();
            nicked = formattedgroup + nick;
        } else {
            opped = nameOP;
            name = event.player.getDisplayName();
            nicked = nick;
        }
        String ChatColor;
        if (playerColor.equalsIgnoreCase("") || playerColor.equalsIgnoreCase("f")) {
            ChatColor = CC + "f";
        } else {
            ChatColor = CC + playerColor;
        }

        if(isOpped && nick.equals(CC + "f"))
            event.component = new ChatComponentTranslation(String.format(Config.ChatFormat, opped, ChatColor + message));
        else if(nick.equals(CC + "f"))
            event.component = new ChatComponentTranslation(String.format(Config.ChatFormat, name, ChatColor + message));
        else
            event.component = new ChatComponentTranslation(String.format(Config.ChatFormat, nicked, ChatColor + message));
    }
}
