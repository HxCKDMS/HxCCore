package HxCKDMS.HxCCore.Events;

import HxCKDMS.HxCCore.Configs.Config;
import HxCKDMS.HxCCore.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.HxCCore;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.util.EventListener;

public class EventChat implements EventListener {

    @SubscribeEvent
    public void onServerChatEvent(ServerChatEvent event){
        String UUID = event.player.getUniqueID().toString();
        File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID + ".dat");
        String nick = NBTFileIO.getString(CustomPlayerData, "nickname");
        String message = event.message;
        File PermissionsData = new File(HxCCore.HxCCoreDir, "HxC-Permissions.dat");
        NBTTagCompound Permissions = NBTFileIO.getNbtTagCompound(PermissionsData, "Permissions");
        int SenderPermLevel = Permissions.getInteger(event.player.getName());
        
        //Color Code
        String CC = "\u00A7";

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

        boolean isOpped = event.player.mcServer.getConfigurationManager().canSendCommands(event.player.getGameProfile());

        nick = nick.replace("&", CC) + CC + "f";
        message = message.replace("&", CC);
        String boxedgroup = GroupFormat.replace("%g", rawgroup);
        String formattedgroup = boxedgroup.replace("&", CC);

        String nameOP = CC + "4" + event.player.getName() + CC + "f";

        String opped;
        String name;
        String nicked;

        if (Config.GroupInChat) {
            opped = formattedgroup + nameOP;
            name = formattedgroup + event.player.getName();
            nicked = formattedgroup + nick;
        } else {
            opped = nameOP;
            name = event.player.getName();
            nicked = nick;
        }

        if(isOpped && nick.equals(CC + "f"))
            event.component = new ChatComponentTranslation(String.format(Config.ChatFormat, opped, message));
        else if(nick.equals(CC + "f"))
            event.component = new ChatComponentTranslation(String.format(Config.ChatFormat, name, message));
        else
            event.component = new ChatComponentTranslation(String.format(Config.ChatFormat, nicked, message));
    }
}
