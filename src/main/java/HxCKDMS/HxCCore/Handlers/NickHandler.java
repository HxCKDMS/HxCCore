package HxCKDMS.HxCCore.Handlers;

import HxCKDMS.HxCCore.HxCCore;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;
import java.util.UUID;

import static HxCKDMS.HxCCore.lib.References.*;
import static HxCKDMS.HxCCore.Configs.Configurations.*;

public class NickHandler {
    public static String getMessageHeader(EntityPlayerMP player){
        UUID UUID = player.getUniqueID();
        String colouredNick = getColouredNick(player);

        File PermissionsData = new File(HxCCore.HxCCoreDir, "HxC-Permissions.dat");
        NBTTagCompound Permissions = NBTFileIO.getNbtTagCompound(PermissionsData, "Permissions");
        int SenderPermLevel = Permissions.getInteger(player.getDisplayNameString());

        String rawGroup, formattedString = colouredNick;
        switch(SenderPermLevel){
            case 1:
                rawGroup = CC + PERM_COLOURS[1] + PERM_NAMES[1];
                break;
            case 2:
                rawGroup = CC + PERM_COLOURS[2] + PERM_NAMES[2];
                break;
            case 3:
                rawGroup = CC + PERM_COLOURS[3] + PERM_NAMES[3];
                break;
            case 4:
                rawGroup = CC + PERM_COLOURS[4] + PERM_NAMES[4];
                break;
            case 5:
                rawGroup = CC + PERM_COLOURS[5] + PERM_NAMES[5];
                break;
            default:
                rawGroup = CC + PERM_COLOURS[0] + PERM_NAMES[0];
                break;
        }

        if (EnableGroupTagInChat)
            formattedString = String.format(formats.get("GroupTag"), rawGroup) + formattedString;

        String tag = formats.get("HxCTag");
        if(HxCCore.coders.contains(UUID))
            tag = String.format(tag, CC + "bHxC");
        else if(HxCCore.supporters.contains(UUID))
            tag = String.format(tag, CC + "4HxC Supporter");
        else if(HxCCore.helpers.contains(UUID))
            tag = String.format(tag, CC + "aHxC Helper");
        else if(HxCCore.artists.contains(UUID))
            tag = String.format(tag, CC + "cHxC Artist");
        else
            tag = "";


        if(EnableHxCTagInChat && !tag.equalsIgnoreCase(""))
            formattedString = tag + formattedString;

        return formattedString.replaceAll("&",CC);
    }

    public static String getColouredNick(EntityPlayerMP player) {
        UUID UUID = player.getUniqueID();
        File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID.toString() + ".dat");
        String nick = NBTFileIO.getString(CustomPlayerData, "nickname");
        boolean isOpped = player.mcServer.getConfigurationManager().canSendCommands(player.getGameProfile());

        String tmp = nick.isEmpty() ? player.getDisplayNameString() : nick;

        if (nick.isEmpty() && isOpped) tmp = CC + '4' + tmp;
        else if (nick.isEmpty()) tmp = CC + 'f' + tmp;

        return tmp + CC + 'f';
    }
}
