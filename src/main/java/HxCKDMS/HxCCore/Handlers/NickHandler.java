package HxCKDMS.HxCCore.Handlers;

import HxCKDMS.HxCCore.Configs.Configurations;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.lib.References;
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
                rawGroup = CC + References.permColours[1] + References.permNames[1];
                break;
            case 2:
                rawGroup = CC + References.permColours[2] + References.permNames[2];
                break;
            case 3:
                rawGroup = CC + References.permColours[3] + References.permNames[3];
                break;
            case 4:
                rawGroup = CC + References.permColours[4] + References.permNames[4];
                break;
            case 5:
                rawGroup = CC + References.permColours[5] + References.permNames[5];
                break;
            default:
                rawGroup = CC + References.permColours[0] + References.permNames[0];
                break;
        }

        String tag = Configurations.formats.get("HxCTag");
        if(HxCCore.coders.contains(UUID))
            tag = String.format(tag, "&bHxC");
        else if(HxCCore.supporters.contains(UUID))
            tag = String.format(tag, "&4HxC Supporter");
        else if(HxCCore.helpers.contains(UUID))
            tag = String.format(tag, "&aHxC Helper");
        else if(HxCCore.artists.contains(UUID))
            tag = String.format(tag, "&cHxC Artist");
        else
            tag = "";

        boolean isOpped = player.mcServer.getConfigurationManager().func_152596_g(player.getGameProfile());

        nick = CC + "f" + nick;

        String opped = CC + "4" + player.getDisplayName() + CC + "f";
        String name = player.getDisplayName();
        String nicked = nick;

        if (Configurations.EnableGroupTagInChat) {
            opped = String.format(Configurations.formats.get("GroupTag"), rawGroup) + opped;
            name = String.format(Configurations.formats.get("GroupTag"), rawGroup) + name;
            nicked = String.format(Configurations.formats.get("GroupTag"), rawGroup) + nicked;
        }

        if(Configurations.EnableHxCTagInChat && !tag.equalsIgnoreCase("")){
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
