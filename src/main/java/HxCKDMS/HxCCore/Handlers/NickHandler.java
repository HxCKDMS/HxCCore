package HxCKDMS.HxCCore.Handlers;

import HxCKDMS.HxCCore.Configs.Config;
import HxCKDMS.HxCCore.HxCCore;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;
import java.util.UUID;

public class NickHandler {
    public final static String CC = "\u00A7";

    public static String getPlayerNickName(EntityPlayerMP player){
        UUID UUID = player.getUniqueID();
        File CustomPlayerData = new File(HxCCore.HxCCoreDir, "HxC-" + UUID.toString() + ".dat");
        String nick = NBTFileIO.getString(CustomPlayerData, "nickname");

        File PermissionsData = new File(HxCCore.HxCCoreDir, "HxC-Permissions.dat");
        NBTTagCompound Permissions = NBTFileIO.getNbtTagCompound(PermissionsData, "Permissions");
        int SenderPermLevel = Permissions.getInteger(player.getDisplayName());

        //Color Code

        String rawGroup;
        switch(SenderPermLevel){
            case 1:
                rawGroup = CC + Config.PermLevelColor[0] + Config.PermLevelName[0];
                break;
            case 2:
                rawGroup = CC + Config.PermLevelColor[1] + Config.PermLevelName[1];
                break;
            case 3:
                rawGroup = CC + Config.PermLevelColor[2] + Config.PermLevelName[2];
                break;
            case 4:
                rawGroup = CC + Config.PermLevelColor[3] + Config.PermLevelName[3];
                break;
            case 5:
                rawGroup = CC + Config.PermLevelColor[4] + Config.PermLevelName[4];
                break;
            default:
                rawGroup = CC + Config.PermLevelColor[5] + Config.PermLevelName[5];
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

        boolean isOpped = player.mcServer.getConfigurationManager().func_152596_g(player.getGameProfile());

        nick = CC + "f" + nick;

        String opped = CC + "4" + player.getDisplayName() + CC + "f";
        String name = player.getDisplayName();
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

        nicked = nicked.replace("&", CC) + CC + "f";
        opped = opped.replace("&", CC) + CC + "f";
        name = name.replace("&", CC) + CC + "f";

        if(isOpped && nick.equals(CC + "f"))
            return opped;
        else if(nick.equals(CC + "f"))
            return name;
        else
            return nicked;
    }
}
