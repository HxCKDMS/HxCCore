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

        String group;
        switch(SenderPermLevel){
            case 1:
                group = CC + Config.PermLevel1Color + Config.PermLevel1Name;
                break;
            case 2:
                group = CC + Config.PermLevel2Color + Config.PermLevel2Name;
                break;
            case 3:
                group = CC + Config.PermLevel3Color + Config.PermLevel3Name;
                break;
            case 4:
                group = CC + Config.PermLevel4Color + Config.PermLevel4Name;
                break;
            case 5:
                group = CC + Config.PermLevel5Color + Config.PermLevel5Name;
                break;
            default:
                group = CC + Config.PermLevel0Color + Config.PermLevel0Name;
                break;
        }

        String GroupFormat = Config.GroupFormat;

        boolean isOpped = event.player.mcServer.getConfigurationManager().canSendCommands(event.player.getGameProfile());

        nick = nick.replace("&", CC) + CC + "f";
        message = message.replace("&", CC);
        String chatgroup = GroupFormat.replace("%g", group);
        String G = chatgroup.replace("&", CC);

        String nameOP = CC + "4" + event.player.getName() + CC + "f";

        if(isOpped && nick.equals(CC + "f"))
            event.component = new ChatComponentTranslation(String.format(Config.ChatFormat, G + nameOP, message));
        else if(nick.equals(CC + "f"))
            event.component = new ChatComponentTranslation(String.format(Config.ChatFormat, G + event.player.getName(), message));
        else
            event.component = new ChatComponentTranslation(String.format(Config.ChatFormat, G + nick, message));
    }
}
